package com.example.xiaoyu.tempstudentrunstaff.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.xiaoyu.tempstudentrunstaff.activity.MainActivity;
import com.example.xiaoyu.tempstudentrunstaff.App;
import com.example.xiaoyu.tempstudentrunstaff.R;
import com.example.xiaoyu.tempstudentrunstaff.entity.Order;
import com.example.xiaoyu.tempstudentrunstaff.entity.RequestTemplate;
import com.example.xiaoyu.tempstudentrunstaff.entity.ResponseTemplate;
import com.example.xiaoyu.tempstudentrunstaff.utils.XUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zcy.acache.ACache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DingdanAdapter extends RecyclerView.Adapter<DingdanAdapter.DingdanViewHolder>{
    List<Order> datas = new ArrayList<>();
    LayoutInflater mLayoutInflater;
    OkHttpClient mOkHttpClient;
    Gson mGson;

    public DingdanAdapter(LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
        mOkHttpClient = new OkHttpClient();
        mGson = new Gson();
    }

    public void setDatas(List<Order> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DingdanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DingdanViewHolder viewHolder = new DingdanViewHolder(mLayoutInflater.inflate(R.layout.holder_layout,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DingdanViewHolder holder, int position) {
        final Integer location = position;
        holder.dingdanhao.setText("订单编号："+ datas.get(position).getId());
        holder.dingdanshijian.setText("订单时间："+ datas.get(position).getDatetime());
        holder.dingdanneirong.setText(datas.get(position).getInfo());
        holder.dingdanzhuangtai.setText(datas.get(position).getStaff()!=null?"配送人员:"+datas.get(position).getStaff()+"\t联系方式:"+datas.get(position).getPhone():"配送人员：暂无");
        holder.dizhi.setText("配送地址:"+datas.get(position).getRidgepole()+"栋"+datas.get(position).getDorm()+"寝室");
        //Log.e("查看数据真假值","订单号:"+datas.get(position).getId()+"订单按钮真假:"+datas.get(position).getBt_flag()+"订单状态:"+datas.get(position).getFlag());
        holder.lianxidianhua.setText("联系电话："+datas.get(position).getContact()+"");
        if(datas.get(position).getFlag()>0){
            holder.accept.setVisibility(View.INVISIBLE);
        }else{
            holder.accept.setVisibility(View.VISIBLE);
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RequestTemplate<Order> data = new RequestTemplate<>();
                    Order order = datas.get(location);
                    order.setStaff(MainActivity.staff.getName());
                    order.setPhone(MainActivity.staff.getPhone());
                    order.setFlag(1);
                    data.setKey(ACache.get(App.app).getAsString("miyao"));
                    data.setData(order);
                    final String datajson = mGson.toJson(data);
                    Log.e("大酒店",datajson);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),datajson);
                    Request request = new Request.Builder().url("http://192.168.0.105:8080/Api/Order").put(requestBody).build();
                    mOkHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("网络请求错误:", e.toString());
                            MainActivity.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    XUtils.ShowToast("请检查你的网络环境！");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String body = response.body().string();
                            Log.e("查看数据",body);
                            MainActivity.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ResponseTemplate<Boolean> message = mGson.fromJson(body,new TypeToken<ResponseTemplate<Boolean>>(){}.getType());
                                    if(message.getSucces()){
                                        XUtils.ShowToast("接受成功！");
                                        holder.accept.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent();
                                        intent.setAction("liar.xiaoyu.www.shuaxindingdan");
                                        intent.putExtra("action","shuaxin");
                                        App.app.sendBroadcast(intent);
                                    }else{
                                        XUtils.ShowToast("或许已经被别人接了！");
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class DingdanViewHolder extends RecyclerView.ViewHolder {
        public TextView dingdanhao;
        public TextView dingdanshijian;
        public TextView dingdanneirong;
        public TextView dingdanzhuangtai;
        public TextView dizhi;
        public TextView lianxidianhua;
        public Button accept;
        public DingdanViewHolder(View itemView) {
            super(itemView);
            dingdanhao = itemView.findViewById(R.id.holder_dingdanhao);
            dingdanneirong = itemView.findViewById(R.id.holder_dingdanneirong);
            dingdanshijian = itemView.findViewById(R.id.holder_dingdanshijian);
            dingdanzhuangtai = itemView.findViewById(R.id.holder_dingdanzhuangtai);
            dizhi = itemView.findViewById(R.id.holder_dizhi);
            accept = itemView.findViewById(R.id.holder_accept);
            lianxidianhua = itemView.findViewById(R.id.holder_lianxidianhua);
        }
    }
}
