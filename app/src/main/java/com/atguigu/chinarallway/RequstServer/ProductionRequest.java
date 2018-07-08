package com.atguigu.chinarallway.RequstServer;

import android.os.Handler;
import android.util.Log;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.TaskData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kiboooo on 2017/10/15.
 */

public class ProductionRequest {


    /*生成生产计划*/
    public static void StartProductionRequest(final String ids, final int success , final int fall,
                                              final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient okHttpClientk = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("ids", ids)
                            .build();

                    Log.e("StartProductionRequest", AllStaticBean.StartProductonURL + AllStaticBean.StartURL);
                    Request request = new Request.Builder()
                            .url(AllStaticBean.StartProductonURL + AllStaticBean.StartURL)
                            .post(body)
                            .build();

                    Response response = okHttpClientk.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        Log.e("Start", content);
                        if (new JSONObject(content).getBoolean("success")) {
                            handler.sendEmptyMessage(success);
                        } else
                        {
                            Log.e("StartProductionRequest", "boolean识别失败");
                            handler.sendEmptyMessage(fall);
                        }

                    } else
                        handler.sendEmptyMessage(fall);
                } catch (IOException | JSONException e) {
                    handler.sendEmptyMessage(fall);
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /* web端的更新生产计划的接口 */
    public static void UpdatePlanState(final TaskData taskData,
                                       final int success, final int fall, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("TaskDate", AllStaticBean.formatter.format(taskData.getTaskDate().getTime()))
                        .add("BName", URLEncoder.encode(taskData.getbName(), "UTF-8"))
                        .add("BID", URLEncoder.encode(taskData.getbID(), "UTF-8"))
                        .add("MakeOrder", URLEncoder.encode(taskData.getMakeOrder(), "UTF-8"))
                        .add("MakePosID", URLEncoder.encode(taskData.getMakePosId(), "UTF-8"))
                        .add("PedID", String.valueOf(taskData.getPedID()))
                        .add("Pos", URLEncoder.encode(taskData.getPos(), "UTF-8"))
                        .add("Permit", taskData.isPermit() ? "1" : "0")
                        .build();

                Request request = new Request.Builder()
                        .url(AllStaticBean.StartProductonURL + AllStaticBean.UpdateURL)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String content = response.body().string();
                    if (new JSONObject(content).getBoolean("success")) {
                        handler.sendEmptyMessage(success);
                    }else
                    {
                        Log.e("UpdatePlanState", "更新失败");
                        handler.sendEmptyMessage(fall);
                    }
                }else {
                    Log.e("UpdatePlanState", "500页");
                    handler.sendEmptyMessage(fall);
                }
                } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            }
        }).start();
    }
}
