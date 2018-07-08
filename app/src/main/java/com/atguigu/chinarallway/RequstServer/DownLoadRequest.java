package com.atguigu.chinarallway.RequstServer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.atguigu.chinarallway.Bean.AllStaticBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kiboooo on 2017/10/13.
 */

public class DownLoadRequest {

    /*文件查询*/
    public static void FileCheckRequest(final String bID, final String bName,
                                        final int success, final int fall,
                                        final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                    RequestBody body = new FormBody.Builder()
                            .add("bID", URLEncoder.encode(bID, "UTF-8"))
                            .add("bName", URLEncoder.encode(bName, "UTF-8"))
                            .build();

                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.FileSearchURL)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        int base = -2;
                        if (content.length() > 0)
                        {
                            JSONObject jsonObject = new JSONObject(content);
                            base = jsonObject.getInt("code");
                            Message MSG = new Message();
                            if (base >= 1) {
                                MSG.what = success;
                                MSG.obj = jsonObject.getJSONObject("data");
                                mHandler.sendMessage(MSG);
                            } else
                                mHandler.sendEmptyMessage(fall);
                        }else
                            mHandler.sendEmptyMessage(fall);
                    }
                    else mHandler.sendEmptyMessage(fall);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*利用文件查询返回的信息，进行文件下载*/
    public static void FileDownLoadRequest(final String Type, final String FileName,
                                           final int success, final int fall,
                                           final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                Log.e("FileDownLoadRequest", Type + "  " + FileName);

                OkHttpClient client = new OkHttpClient.Builder()
                        .build();

                    RequestBody body = new FormBody.Builder()
                            .add("type", Type)
                            .add("fileName", FileName)
                            .build();


                    Request request = new Request.Builder()
                            .url(AllStaticBean.URL + AllStaticBean.FileDownLoadURL )
                            .post(body)
                            .build();

                Response response1 = client.newCall(request).execute();
                    if (response1.isSuccessful()) {
                        Log.e("FileDownLoadRequest", Type);
                        InputStream fileInputStream = response1.body().byteStream();
                        OutputStream fileOutputStream;

                        if (Type.equals("image") || Type.equals("factory")) {
                            File imageDir = new File(AllStaticBean.SaveImagePath);
                            if (!imageDir.exists()) {
                                imageDir.mkdirs();
                            }
                            Log.e("FileDownLoadRequest", AllStaticBean.SaveImagePath);
                            String FileNameDecode = URLDecoder.decode(FileName, "UTF-8");
                            Log.e("FileNameDecode", AllStaticBean.SaveImagePath + File.separator + FileNameDecode);
                            fileOutputStream = new FileOutputStream(new
                                    File(AllStaticBean.SaveImagePath + File.separator + FileNameDecode));
                            AllStaticBean.BeamPlanName = AllStaticBean.SaveImagePath +
                                    File.separator + FileNameDecode;//返回图片存放地址
                        } else {
                            File VideoDir = new File(AllStaticBean.SaveVideoPath);
                            if (!VideoDir.exists()) {
                                VideoDir.mkdirs();
                            }
                            String VideoFileNameDecode = URLDecoder.decode(FileName, "UTF-8");
                            fileOutputStream = new FileOutputStream(new
                                    File(AllStaticBean.SaveVideoPath + File.separator + VideoFileNameDecode));
                            AllStaticBean.BeamVideoName = AllStaticBean.SaveVideoPath
                                    + File.separator + VideoFileNameDecode;//返回视频存放地址
                        }
                        Log.e("FileDownLoadRequest", AllStaticBean.BeamPlanName + "   " + AllStaticBean.BeamVideoName);
                        byte[] buffer = new byte[2024];
                        int len;
                        while ((len = fileInputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.close();
                        fileInputStream.close();

                    }  mHandler.sendEmptyMessage(success);
//                    else {
//                        Log.e("FileDownLoadRequest", "失败了，或者梁场照片不在");
//                        mHandler.sendEmptyMessage(success);
////                        mHandler.sendEmptyMessage(fall);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
