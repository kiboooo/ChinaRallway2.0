package com.atguigu.chinarallway.RequstServer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.atguigu.chinarallway.Activity.LoginActivity;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.StoreData;
import com.atguigu.chinarallway.Chart.StoreActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kiboooo on 2017/9/14.
 *
 */

public class ManagerRequst {

    /*获取所有梁板的信息，该功能组合查询也可以实现*/
    public static void SaveBeamStatisticsRequest(final String uid, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                RequestBody body = new FormBody.Builder()
                        .add("uid", uid)
                        .add("type", "store")
                        .add("searchAll", "1")
                        .build();

                Request request = new Request.Builder()
                        .header("Cookie", AllStaticBean.mRespenseCookie)
                        .url(AllStaticBean.URL + AllStaticBean.GroupURL)
                        .post(body)
                        .build();

                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.e("store ", data);
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getInt("code")!=0) {
                            JSONArray array = jsonObject.getJSONArray("data");
                            if (array.length() > 0) {
                                StoreData[] storeDatas = new Gson().fromJson(array.toString(), StoreData[].class);

                                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                        .parse(storeDatas[0].getOutTime());

                                java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                        .parse(storeDatas[0].getInTime());

                                Log.e("store", storeDatas[0].getOutTime() + "      " + storeDatas[0].getInTime()
                                        + "   " + ((date.getTime() - date2.getTime()) / (3600 * 24 * 1000)));
                                Message message = new Message();
                                message.what = StoreActivity.STORE_SUCCESS;
                                message.obj = storeDatas;
                                handler.sendMessage(message);
                            } else {
                                handler.sendEmptyMessage(StoreActivity.CODE_0_FALL);
                            }
                        }else
                            handler.sendEmptyMessage(StoreActivity.CODE_0_FALL);
                        response.close();
                    } else {
                        handler.sendEmptyMessage(StoreActivity.STORE_FALL);
                    }
                } catch (IOException | JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*适配组合查询的查询方法*/
    public static void AllRequest(final String Type, final String searchType, final String searchParam,
                                  final String searchAll, final Handler handler,
                                  final int MessageSuccessWhat, final int MessageFallWhat) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("AllRequest", Type + "  " + searchType + "  " + searchParam);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(30, TimeUnit.SECONDS)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .build();

                    final RequestBody requestBody = new FormBody.Builder()
                            .add("uid", LoginActivity.userData.getUid())
                            .add("type", Type)
                            .add("searchType", searchType)
                            .add("searchParam", URLEncoder.encode(searchParam, "UTF-8"))
                            .add("searchAll", searchAll)
                            .build();

                    Request request = new Request.Builder()
                            .header("Cookie", AllStaticBean.mRespenseCookie)
                            .url(AllStaticBean.URL + AllStaticBean.GroupURL)
                            .post(requestBody)
                            .build();

                    Call call = client.newCall(request);
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.e("ALlRequast", body);
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.getInt("code") > 0) {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Message message = new Message();
                            message.what = MessageSuccessWhat;
                            message.obj = jsonArray;
                            handler.sendMessage(message);
                        } else {
                            handler.sendEmptyMessage(MessageFallWhat);
                        }
                        response.close();
                    } else {
                        Log.e("AllRequest", "失败了");
                        handler.sendEmptyMessage(MessageFallWhat);
                    }
                } catch (SocketTimeoutException exception) {
                    exception.printStackTrace();
                    handler.sendEmptyMessage(MessageFallWhat);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*生产计划的查询*/
    public static void ProductiongPlanRequest(final String Type, final String searchType,
                                              final String searchParam, final String searchAll,
                                              final String HowWeek, final Handler handler,
                                              final int MessageSuccessWhat, final int MessageFallWhat) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("AllRequest", Type + "  " + searchType + "  " + searchParam);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(30, TimeUnit.SECONDS)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .build();

                    final RequestBody requestBody = new FormBody.Builder()
                            .add("uid", LoginActivity.userData.getUid())
                            .add("type", Type)
                            .add("searchType", searchType)
                            .add("searchParam", URLEncoder.encode(searchParam, "UTF-8"))
                            .add("searchAll", searchAll)
                            .add("week", HowWeek)
                            .build();

                    Request request = new Request.Builder()
                            .header("Cookie", AllStaticBean.mRespenseCookie)
                            .url(AllStaticBean.URL + AllStaticBean.GroupURL)
                            .post(requestBody)
                            .build();

                    Call call = client.newCall(request);
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.e("ALlRequast1", body);
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.getInt("code") > 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Message message = new Message();
                            message.what = MessageSuccessWhat;
                            message.obj = jsonArray;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = MessageFallWhat;
                            message.arg1 = 0;
                            handler.sendMessage(message);
                        }
                        response.close();
                    } else {
                        Log.e("AllRequest", response.body().string());
                        Message message = new Message();
                        message.what = MessageFallWhat;
                        message.arg1 = 1;
                        handler.sendMessage(message);
                    }
                } catch (SocketTimeoutException exception) {
                    exception.printStackTrace();
                    Message message = new Message();
                    message.what = MessageFallWhat;
                    message.arg1 = 1;
                    handler.sendMessage(message);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = MessageFallWhat;
                    message.arg1 = 1;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}

