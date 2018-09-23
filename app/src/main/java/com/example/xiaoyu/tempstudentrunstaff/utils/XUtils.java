package com.example.xiaoyu.tempstudentrunstaff.utils;

import android.widget.Toast;

import com.example.xiaoyu.tempstudentrunstaff.App;

public class XUtils {
    public static void ShowToast(String content){
        Toast.makeText(App.app,content,Toast.LENGTH_SHORT).show();
    }
}
