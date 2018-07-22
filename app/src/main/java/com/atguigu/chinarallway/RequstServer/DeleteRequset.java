package com.atguigu.chinarallway.RequstServer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.TaskData;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteRequset {
    public static void DeleteProductionPlan(
            final TaskData data, final int success, final int fall,
            final Handler mHandler,final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String RequsetData =
                            AllStaticBean.GsonToDate.toJson(data);

                    Log.e("DeleteRequset  : ", RequsetData);
                    Log.e("DeleteRequset  : ",  URLEncoder.encode(
                            RequsetData, "UTF-8"));

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();

                    RequestBody body = new FormBody.Builder()
                            .add("data", URLEncoder.encode(
                                    RequsetData, "UTF-8"))
                            .build();


                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.removePlan)
                            .post(body)
                            .build();

                    Response mResponse = okHttpClient.newCall(request).execute();

                    if (mResponse.isSuccessful()) {
                        String content = mResponse.body().string();
                        JSONObject jsonObject = new JSONObject(content);

                        Log.e("DeleteRequset  ", content);
                        if ( jsonObject.getInt("code") > 0) {
                            Message message = new Message();
                            message.what = success;
                            message.arg1 = position;
                            mHandler.sendMessage(message);
                        }else{
                            Message message = new Message();
                            message.what = fall;
                            message.obj = jsonObject.getString("msg");
                            mHandler.sendMessage(message);
                        }
                    }else{
                        Message message = new Message();
                        message.what = fall;
                        message.obj = "请求出错";
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
