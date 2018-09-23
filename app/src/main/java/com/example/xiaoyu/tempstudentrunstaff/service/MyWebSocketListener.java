package com.example.xiaoyu.tempstudentrunstaff.service;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.xiaoyu.tempstudentrunstaff.App;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketListener extends WebSocketListener{
    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    public WebSocket mwebSocket;
    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.e("WebSocket:","连接成功。。。");
        mwebSocket = webSocket;
    }

    /** Invoked when a text (type {@code 0x1}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.e("WebSocket:","服务器发来消息:"+text);
        Intent intent = new Intent();
        intent.setAction("liar.xiaoyu.www.shuaxindingdan");
        intent.putExtra("action","msg");
        intent.putExtra("msg",text);
        App.app.sendBroadcast(intent);
    }

    /** Invoked when a binary (type {@code 0x2}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.e("WebSocket:","服务器发来字节:"+bytes);
    }

    /**
     * Invoked when the remote peer has indicated that no more incoming messages will be
     * transmitted.
     */
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.e("WebSocket:","服务器表示不再有消息传入"+code);
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.e("WebSocket:","双方确认不再有消息");
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        Log.e("WebSocket:","当关闭时有错误的读写的网络消息丢失");
    }

    public void senServer(String message){
        mwebSocket.send(message);
    }
}