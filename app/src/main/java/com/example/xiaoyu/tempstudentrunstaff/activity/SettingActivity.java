package com.example.xiaoyu.tempstudentrunstaff.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xiaoyu.tempstudentrunstaff.App;
import com.example.xiaoyu.tempstudentrunstaff.R;
import com.example.xiaoyu.tempstudentrunstaff.entity.ResponseTemplate;
import com.example.xiaoyu.tempstudentrunstaff.utils.XUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zcy.acache.ACache;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdName;
    private EditText mEdPhone;
    private EditText mEdMiyao;
    private Button mBtSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if(ACache.get(App.app).getAsObject("run")!=null && (Boolean) ACache.get(App.app).getAsObject("run")==true){
            startActivity(new Intent(SettingActivity.this,MainActivity.class));
            finish();
        }
        initView();
        mBtSave.setOnClickListener(this);
    }

    private void initView() {
        mEdName = findViewById(R.id.ed_name);
        mEdPhone = findViewById(R.id.ed_phone);
        mEdMiyao = findViewById(R.id.ed_miyao);
        mBtSave = findViewById(R.id.bt_save);
    }

    @Override
    public void onClick(View view) {
        if(TextUtils.isEmpty(mEdName.getText().toString())||TextUtils.isEmpty(mEdPhone.getText().toString())||TextUtils.isEmpty(mEdMiyao.getText().toString())){
            XUtils.ShowToast("请输入完整信息！");
        }else{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url("http://studentrun.club:8080/xiaoyu/Api/Miyao").get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("网络请求错误:",e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String temp = response.body().string();
                    Log.e("查看返回的数据",temp);
                    try{
                        final ResponseTemplate<String> message = new Gson().fromJson(temp,new TypeToken<ResponseTemplate<String>>(){}.getType());
                        final String miyao = message.getData();
                        if(miyao.equals(mEdMiyao.getText().toString())){
                            ACache.get(App.app).put("miyao",message.getData());
                            ACache.get(App.app).put("name",mEdName.getText().toString());
                            ACache.get(App.app).put("phone",mEdPhone.getText().toString());
                            ACache.get(App.app).put("miyao",miyao);
                            ACache.get(App.app).put("run",true,60*60*24);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    XUtils.ShowToast("验证成功！");
                                    startActivity(new Intent(SettingActivity.this,MainActivity.class));
                                    finish();
                                }
                            });
                            Log.e("秘钥是否缓存!",ACache.get(App.app).getAsString("miyao"));
                            Log.e("姓名是否缓存!",ACache.get(App.app).getAsString("name"));
                            Log.e("手机号否缓存!",ACache.get(App.app).getAsString("phone"));
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    XUtils.ShowToast("密钥验证错误！");
                                }
                            });
                        }
                    }catch (Exception e){
                        Log.e("验证出错",e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XUtils.ShowToast("验证出错！");
                            }
                        });
                    }

                }
            });
        }
    }
}