package com.example.xiaoyu.tempstudentrunstaff.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.xiaoyu.tempstudentrunstaff.App;
import com.example.xiaoyu.tempstudentrunstaff.R;
import com.example.xiaoyu.tempstudentrunstaff.adapter.DingdanAdapter;
import com.example.xiaoyu.tempstudentrunstaff.entity.Order;
import com.example.xiaoyu.tempstudentrunstaff.entity.ResponseTemplate;
import com.example.xiaoyu.tempstudentrunstaff.entity.Staff;
import com.example.xiaoyu.tempstudentrunstaff.service.MyWebSocketListener;
import com.example.xiaoyu.tempstudentrunstaff.utils.XUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zcy.acache.ACache;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mMainRecycler;
    private DingdanAdapter mDingdanAdapter;
    private Spinner mSpSushelou;
    private Button mSearch;
    private OkHttpClient okHttpClient;
    private Dialog mDialog;
    private Button mBtRefresh;
    public static Staff staff;
    public static Activity activity;
    public static WebSocket webSocket;
    private MyWebSocketListener mWebSocketListener;
    private boolean flag = false;
    IntentFilter intentFilter;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("asdasd","asdasdasd");
            if(intent.getStringExtra("action")!=null&&intent.getStringExtra("action").equals("shuaxin")){
                initData();
            }else if (intent.getStringExtra("action")!=null&&intent.getStringExtra("action").equals("msg")){
                XUtils.ShowToast(intent.getStringExtra("msg")+",请刷新！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        mSearch.setOnClickListener(this);
        mBtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }

    public void initData() {

        String url = "http://studentrun.club:8080/xiaoyu/Api/Order?key=" + ACache.get(App.app).getAsString("miyao");
        Request request = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求错误:", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("查看返回的数据", temp);
                ResponseTemplate<List<Order>> message = new Gson().fromJson(temp, new TypeToken<ResponseTemplate<List<Order>>>() {
                }.getType());
                final List<Order> data = message.getData();
                if (message.getSucces()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDingdanAdapter.setDatas(data);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            XUtils.ShowToast("数据请求失败！");
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        mMainRecycler = findViewById(R.id.main_recycler);
        mDingdanAdapter = new DingdanAdapter(getLayoutInflater());
        mMainRecycler.setAdapter(mDingdanAdapter);
        mMainRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        mSpSushelou = findViewById(R.id.sp_sushelou);
        mSearch = findViewById(R.id.search);
        mDialog = new Dialog(this);
        mDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_load, null));
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mBtRefresh = findViewById(R.id.bt_refresh);
        staff = new Staff();
        staff.setName(ACache.get(App.app).getAsString("name"));
        staff.setPhone(ACache.get(App.app).getAsString("phone"));
        activity = this;
        intentFilter = new IntentFilter();
        intentFilter.addAction("liar.xiaoyu.www.shuaxindingdan");
        registerReceiver(mBroadcastReceiver,intentFilter);
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//允许失败重试
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(5, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        String url = "ws://studentrun.club:8080/xiaoyu/WebSocketConnection/staff";
        mWebSocketListener = new MyWebSocketListener();
        Request request = new Request.Builder().url(url).build();
        webSocket = okHttpClient.newWebSocket(request,mWebSocketListener);
        flag = true;
    }

    @Override
    public void onClick(View view) {
        mDialog.show();
        String s = mSpSushelou.getSelectedItem().toString();

        String url = "http://studentrun.club:8080/xiaoyu/Api/OrderByRidgepole?key=" + ACache.get(App.app).getAsString("miyao") + "&ridgepole=" + s;
        Log.e("筛选查询地址:", url);
        Request request = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求错误:", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("查看返回的数据", temp);
                ResponseTemplate<List<Order>> message = new Gson().fromJson(temp, new TypeToken<ResponseTemplate<List<Order>>>() {
                }.getType());
                final List<Order> data = message.getData();
                if (message.getSucces()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDingdanAdapter.setDatas(data);
                            mDialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            XUtils.ShowToast("数据请求失败！");
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onStop() {
        flag = false;
        webSocket.close(1000,null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        webSocket.close(1000,null);
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        registerReceiver(mBroadcastReceiver,intentFilter);
        if(!flag){
            Request request = webSocket.request();
            webSocket = okHttpClient.newWebSocket(request,mWebSocketListener);
        }
        super.onResume();
    }
}
