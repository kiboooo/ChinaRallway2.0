package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.FactoryData;
import com.atguigu.chinarallway.Chart.MaxActivity;
import com.atguigu.chinarallway.Chart.ProtectActivity;
import com.atguigu.chinarallway.Chart.RebarAmountActivity;
import com.atguigu.chinarallway.Chart.StorPositionActivity;
import com.atguigu.chinarallway.Chart.StoreActivity;
import com.atguigu.chinarallway.Chart.WarningActivity;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.DownLoadRequest;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.Scanner.CustomCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/11.
 */

public class ManagerActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.iv_scaner)
    ImageView ivScaner;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.image2)
    ImageView image2;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.linear1)
    LinearLayout linear1;
    @Bind(R.id.image3)
    ImageView image3;
    @Bind(R.id.layout3)
    LinearLayout layout3;
    @Bind(R.id.image4)
    ImageView image4;
    @Bind(R.id.layout4)
    LinearLayout layout4;
    @Bind(R.id.linear2)
    LinearLayout linear2;
    @Bind(R.id.image5)
    ImageView image5;
    @Bind(R.id.layout5)
    LinearLayout layout5;
    @Bind(R.id.image6)
    ImageView image6;
    @Bind(R.id.layout6)
    LinearLayout layout6;
    @Bind(R.id.linear3)
    LinearLayout linear3;
    @Bind(R.id.bnv_bottom)
    BottomNavigationView bnvBottom;
    private LoadingDialog loadingDialog;
    private final int SUCCESSFLU = 5555;
    private final int LOAD_SUCCESS = 1024;//请求梁场图片成功回调
    private final int FDAFALL = 1111;//请求梁场除图片数据外失败回调信号量
    private final int BEAM_PICTURE_FALL = 1221;//请求梁场图片失败回调

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESSFLU:
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.factoryDatas = AllStaticBean.GsonToDate.fromJson(
                            jsonArray.toString(), FactoryData[].class);
                    Log.e("FileDownLoadRequest", "开始请求图片   " + AllStaticBean.factoryDatas[0].getName() + ".jpg;");
                    try {
                        DownLoadRequest.FileDownLoadRequest("factory",
                                URLEncoder.encode(AllStaticBean.factoryDatas[0].getName()+".jpg", "UTF-8"),
                                LOAD_SUCCESS, BEAM_PICTURE_FALL, handler);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
                case LOAD_SUCCESS:
                    loadingDialog.close();
                    Log.e("LOADIMAGE_SUCCESS", "图片加载完毕");
                    startActivity(new Intent(ManagerActivity.this, FactorySearchActivity.class));
                    break;

                case FDAFALL:
                    loadingDialog.close();
                    Toast.makeText(ManagerActivity.this, "查询梁表出错，请确认重试", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
        String content = "管理员:" + LoginActivity.userData.getUname();
        tvTitleName.setText(content);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        bnvBottom.setOnNavigationItemSelectedListener(this);
        ivScaner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                startActivity(new Intent(this, WarningActivity.class));
                break;
            case R.id.layout2:
                startActivity(new Intent(this, StoreActivity.class));
                break;
            case R.id.layout3:
                startActivity(new Intent(this, StorPositionActivity.class));
                break;
            case R.id.layout4:
                startActivity(new Intent(this, MaxActivity.class));
                break;
            case R.id.layout5:
                startActivity(new Intent(this, ProtectActivity.class));
                break;
            case R.id.layout6:
                startActivity(new Intent(this, RebarAmountActivity.class));
                break;
            case R.id.iv_scaner:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);//设置识别的码，QR_CODE_TYPE：为二维码
                intentIntegrator.setTimeout(10 * 1000);//设置扫描超时时间
                intentIntegrator.initiateScan();
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                startActivity(new Intent(this, MyUserActivity.class));
                break;
            case R.id.factory_search:
                    loadingDialog = new LoadingDialog(ManagerActivity.this, "数据加载中...");
                    loadingDialog.show();
                    ManagerRequst.AllRequest(
                            "factory", "", "", "1",
                            handler,
                            SUCCESSFLU, FDAFALL);
                break;
        }
        return false;
    }
}
