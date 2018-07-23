package com.atguigu.chinarallway.RequstServer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.FactoryData;
import com.atguigu.chinarallway.Bean.ModifyData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kiboooo on 2017/10/6.
 *
 */

public class UpDataRequest {

    /*修改数据库各个表的通用请求接口*/
    public static void ModifyDataRequest(final String type, final ModifyData[] pk, final ModifyData[] modifyData,
                                         final int success, final int fall, final Handler mHandler,final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();
                    RequestBody body = new FormBody.Builder()
                            .add("type", type)
                            .add("pk", new Gson().toJson(pk))
                            .add("modifyData", new Gson().toJson(modifyData))
                            .build();
                    Request request = new Request.Builder()
                        .url(AllStaticBean.URL + AllStaticBean.ModifyURL)
                        .post(body)
                        .build();
                    Log.e("Body", new Gson().toJson(pk) + "  " + new Gson().toJson(modifyData));
                    Response mResponse = okHttpClient.newCall(request).execute();
                    if (mResponse.isSuccessful()) {
                        String content = mResponse.body().string();
                        Log.e("RequestFall", content);
                        if (new JSONObject(content).getInt("code")>=1) {
                            Message message = new Message();
                            message.what = success;
                            message.arg1 = position;
                            mHandler.sendMessage(message);
                        }else
                        {
                            mHandler.sendEmptyMessage(fall);
                        }
                    } else {
                        Log.e("RequestFall", mResponse.body().string());
                        mHandler.sendEmptyMessage(fall);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /*修改梁场信息的特定接口，附带文件上传功能*/
    public static void ModifyFactoryDataRequest(final FactoryData factoryData, final File file,
                                                final int success, final int fall, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//                    String URL_FactoryData = "?data="+ URLEncoder.encode(AllStaticBean.GsonToDate.toJson(factoryData),"UTF-8");
//                    String URL_FactoryData = "?data="+ URLEncoder.encode(AllStaticBean.GsonToDate.toJson(factoryData),"ISO8859-1");


//                        Log.e("URL_FactoryData", URL_FactoryData);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();


                    Request request = new Request.Builder()
//                            .url(AllStaticBean.URL + AllStaticBean.FactoryModifyURL + URL_FactoryData )
                            .url(AllStaticBean.URL + AllStaticBean.FactoryModifyURL)
                            .addHeader("data",URLEncoder.encode(AllStaticBean.GsonToDate.toJson(factoryData),"UTF-8"))
                            .post(RequestBody.create(MediaType.parse("image/*"), file))
                            .build();


                    Log.e("url", AllStaticBean.URL + AllStaticBean.FactoryModifyURL + factoryData.getName());
//                    Log.e("realurl", AllStaticBean.URL + AllStaticBean.FactoryModifyURL + URL_FactoryData);

                    Response mResponse = okHttpClient.newCall(request).execute();

                    if (mResponse.isSuccessful()) {
                        String content = mResponse.body().string();
                        Log.e("RequestFall", content);
                        if (new JSONObject(content).getInt("code")>=1) {
                            mHandler.sendEmptyMessage(success);
                        }else
                        {
                            mHandler.sendEmptyMessage(fall);
                        }
                    } else {
                        Log.e("RequestFall", mResponse.body().string());
                        mHandler.sendEmptyMessage(fall);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /*增加数据库相应新表单的请求*/
    public static void AddInfoRequest(final String type, final String data,
                                      final int success, final int fall, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();
                RequestBody body = new FormBody.Builder()
                        .add("type", type)
                        .add("data", data)
                        .build();
                Request request = new Request.Builder()
                        .url(AllStaticBean.URL + AllStaticBean.AddInfoURL)
                        .post(body)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String Body = response.body().string();
                    if (response.isSuccessful()) {
                        Log.e("success",Body);
                        if (Body.length()>0 && new JSONObject(Body).getInt("code") >= 1) {
                            Log.e("bodyss", "录入成功");
                            mHandler.sendEmptyMessage(success);
                        } else {
                            Log.e("bodyss", "" + response.code() + Body);
                            mHandler.sendEmptyMessage(fall);
                        }
                    }else {
                        Log.e("bodyss", "  "+Body);
                        mHandler.sendEmptyMessage(fall);
                    }
                } catch (IOException | JSONException e) {
                    Log.e("bodyss", "chuyichang");
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*文件上传接口，主要用于上传工艺视频*/
    public static void FileUpLoadRequest(final String bID, final String bName,
                                         final String FileType, final String FileName,
                                         final String FileSuffix, final File mFile,
                                         final int success, final int fall,
                                         final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();

                    RequestBody requestBody = RequestBody.create(MediaType.parse(FileType), mFile);

                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.FileUploadURL+"?bID="+URLEncoder.encode(bID,"UTF-8")+"&bName="+URLEncoder.encode(bName,"UTF-8"))
                            .addHeader("FileType", FileType)
                            .addHeader("FileName",URLEncoder.encode(FileName,"UTF-8"))
                            .addHeader("FileSuffix",FileSuffix)
                            .addHeader("bName",URLEncoder.encode(bName,"UTF-8"))
                            .addHeader("bID",URLEncoder.encode(bID,"UTF-8"))
                            .post(requestBody)
                            .build();

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String BodyContent = response.body().string();
                        System.out.println(BodyContent);
                        if (BodyContent.length() > 0 && new JSONObject(BodyContent).getInt("code") >= 0) {

                            mHandler.sendEmptyMessage(success);
                        }else
                            mHandler.sendEmptyMessage(fall);
                    }else
                    {
                        int code = response.code();
                        String body = response.body().string();
                        System.out.println("code is "+code+"\n"+body);
                        mHandler.sendEmptyMessage(fall);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*梁板信息更改接口，上传出场和入场*/
    public static void ChangeBeamStatusRequest(final String Type,
                                               final String BeamID,
                                               final String bName,
                                               final int success, final int fall,
                                               final Handler mHandler
    ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("ChangeBeamStatusRequest", Type + "  " + BeamID + "   " + bName);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();

                    RequestBody body = new FormBody.Builder()
                            .add("type", URLEncoder.encode(Type, "UTF-8"))
                            .add("beamId", URLEncoder.encode(BeamID, "UTF-8"))
                            .add("bName", URLEncoder.encode(bName, "UTF-8"))
                            .build();

                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.ChangeBeamStatusURL)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        if (content.length() > 0 && new JSONObject(content).getInt("code") >= 1) {
                            Log.e("ChangeBeamStatusRequest", content);
                            mHandler.sendEmptyMessage(success);
                        }else {
                            Log.e("ChangeBeamStatusRequest", content);
                            mHandler.sendEmptyMessage(fall);
                        }
                    } else {
                        Log.e("ChangeBeamStatusRequest", "not-isSuccessful");
                        mHandler.sendEmptyMessage(fall);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /*转json数据修改task表的数据*/
    public static void ModifyTask(final String data,final String Commite, final int success, final int fall, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.e("ModifyTask", data);

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("data", data)
                        .add("commite", Commite)
                        .build();

                Request request = new Request.Builder()
                        .url(AllStaticBean.URL + AllStaticBean.ProductionURL)
                        .post(body)
                        .build();

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        if (new JSONObject(content).getInt("code") >= 1) {
                            Log.e("ModifyTask", content);
                            mHandler.sendEmptyMessage(success);
                        }else {
                            Log.e("ModifyTask", content);
                            mHandler.sendEmptyMessage(fall);
                        }
                    }else {
                        Log.e("ModifyTask", response.body().string());
                        mHandler.sendEmptyMessage(fall);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*修改架梁计划表*/
    public static void ModifyBuildPlan(final String data, final int success, final int fall, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.e("ModifyTask", data);

                    OkHttpClient okHttpClient = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("data", data)
                            .build();

                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.BuildPlanURL)
                            .post(body)
                            .build();

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        if (new JSONObject(content).getInt("code") >= 1) {
                            Log.e("ModifyBuildPlan", content);
                            mHandler.sendEmptyMessage(success);
                        }else {
                            Log.e("ModifyBuildPlan", content);
                            mHandler.sendEmptyMessage(fall);
                        }
                    }else {
                        Log.e("ModifyBuildPlan", response.body().string());
                        mHandler.sendEmptyMessage(fall);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*更新定位信息*/
    public static void ChangeBeamFind(final String bName, final String bID,
                                      final Handler mHandler, final int success, final int fall) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();

                    RequestBody requestBody = new FormBody.Builder()
                            .add("bName", URLEncoder.encode(bName, "UTF-8"))
                            .add("bID", URLEncoder.encode(bID, "UTF-8"))
                            .build();

                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.changeBeamFind)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String content = response.body().string();
                    Log.e("ChangeBeamFind", content);
                    if (response.isSuccessful()) {
                        if (new JSONObject(content).getInt("code") >= 1) {
                            mHandler.sendEmptyMessage(success);
                        }else {
                            mHandler.sendEmptyMessage(fall);
                        }
                    }
                    else {

                        mHandler.sendEmptyMessage(fall);
                    }

                } catch (UnsupportedEncodingException e) {
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                } catch (IOException e) {
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(fall);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
