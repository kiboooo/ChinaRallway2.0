package com.atguigu.chinarallway.RequstServer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.atguigu.chinarallway.Activity.LoginActivity;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.SavePassWordData;
import com.atguigu.chinarallway.Bean.UserData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kiboooo on 2017/9/12.
 * 登陆请求接口
 */

public class LoginRequst {


    public static void RLogin(final String UserName , final String PasssWord,final String codeFinish, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build();

                RequestBody requestBody = new FormBody.Builder()
                        .add("uid", UserName)
                        .add("pwd", codeFinish)
                        .build();

                final Request request = new Request.Builder()
                        .url(AllStaticBean.URL+AllStaticBean.LoginURL)
                        .post(requestBody)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = new Message();
                        msg.what = LoginActivity.FALL;
                        mHandler.sendMessage(msg);
                        Log.e("text", "请求出错了");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String Data = response.body().string();
                            Log.e("text", Data);
                            try {
                                JSONObject jsonObject = new JSONObject(Data);
                                if (jsonObject.getInt("code") == 1) {
                                    JSONObject object = jsonObject.getJSONObject("data");
                                    AllStaticBean.mRespenseCookie = response.header("Set-Cookie");
                                    UserData userData = new Gson().fromJson(object.toString(), UserData.class);
                                    SavePassWordData savePassWordData = new SavePassWordData();
                                    savePassWordData.setPassword(PasssWord);
                                    savePassWordData.setUserData(userData);
                                    Log.e("text", object.toString());
                                    Message message = new Message();
                                    message.obj = savePassWordData;
                                    message.what = LoginActivity.SUCCESS;
                                    mHandler.sendMessage(message);
                                    Log.e("text ttc", userData.getUid() + userData.getPriv() + userData.getUname());
                                } else {
                                    Message msg = new Message();
                                    msg.what = LoginActivity.FALL;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch ( Exception ex) {
                                Message msg = new Message();
                                msg.what = LoginActivity.FALL;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    //检查版本更新，比对当前Version 和 线上Version 的差别
    public static void CheckVersion(final Context context, final Handler mHandler, final int success, final int fall) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(AllStaticBean.URL + AllStaticBean.CheckVersionURL)
                        .build();


                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        Log.e("content", content);
                        JSONObject jsonObject = new JSONObject(content);
                        String Version = jsonObject.getString("version");
                        //这里应该使用versionCod
                        String NowVersion = context.getPackageManager().
                                getPackageInfo(context.getPackageName(), 0).versionName;
                        Log.e("CheckVersion", NowVersion);
                        if (Version.compareTo(NowVersion) > 0) {
                            Message message = Message.obtain();
                            message.what = success;
                            mHandler.sendMessage(message);
                        }
                        else
                            mHandler.sendEmptyMessage(fall);
                    }else
                        mHandler.sendEmptyMessage(fall);
                } catch (IOException e) {
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
