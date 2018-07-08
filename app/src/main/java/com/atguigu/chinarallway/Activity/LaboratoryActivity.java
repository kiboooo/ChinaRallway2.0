package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.CheckRecData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.Scanner.CustomCaptureActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kiboooo on 2017/9/14.
 * Function : 实验员
 */

public class LaboratoryActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.iv_scaner)
    ImageView ivScaner;
    @Bind(R.id.add_info)
    CardView addInfo;
    @Bind(R.id.add_info_search)
    CardView addInfoSearch;
    @Bind(R.id.add_info_change)
    CardView addInfoChange;
    @Bind(R.id.bnv_bottom)
    BottomNavigationView bnvBottom;


    private final int LAB_SEARCH_SUCCESS = 80;
    private final int LAB_CHANGE_SUCCESS = 81;
    private final int LAB_PUT_SUCCESS = 85;
    private final int LAB_PUT2_SUCCESS = 84;
    private final int LAB_FALL_SUCCESS = 82;
    private final int LAB_FALL2_SUCCESS = 8282;

    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler labHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LAB_SEARCH_SUCCESS:
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.checkRecDatas = new Gson().fromJson(jsonArray.toString(), CheckRecData[].class);
                    loadingDialog.close();
                    startActivity(new Intent(LaboratoryActivity.this, LabDataSearchActivity.class));
                    break;
                case LAB_CHANGE_SUCCESS:
                    JSONArray json = (JSONArray) msg.obj;
                    AllStaticBean.checkRecDatas = new Gson().fromJson(json.toString(), CheckRecData[].class);
                    loadingDialog.close();
                    startActivity(new Intent(LaboratoryActivity.this, LabDataChange.class));
                    break;
                case LAB_PUT_SUCCESS:
                    JSONArray jsonPut = (JSONArray) msg.obj;
                    AllStaticBean.checkRecDatas = new Gson().fromJson(jsonPut.toString(), CheckRecData[].class);
                    ManagerRequst.AllRequest(
                                "beam","","","1",
                                labHandler,
                                LAB_PUT2_SUCCESS,
                                LAB_FALL_SUCCESS
                        );
                    break;
                case LAB_PUT2_SUCCESS:
                    JSONArray jsonPut2 = (JSONArray) msg.obj;
                    AllStaticBean.MaxBeamData = new Gson().fromJson(jsonPut2.toString(), BeamData[].class);
                    loadingDialog.close();
                    startActivity(new Intent(LaboratoryActivity.this, LabDataActivity.class));
                    break;
                case LAB_FALL_SUCCESS:
                    loadingDialog.close();
                    Toast.makeText(LaboratoryActivity.this, "检测管理内容为空", Toast.LENGTH_SHORT).show();
                    break;
                case LAB_FALL2_SUCCESS:
                    ManagerRequst.AllRequest(
                            "beam","","","1",
                            labHandler,
                            LAB_PUT2_SUCCESS,
                            LAB_FALL_SUCCESS
                    );
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory);
        ButterKnife.bind(this);
        addInfo.setOnClickListener(this);
        addInfoSearch.setOnClickListener(this);
        addInfoChange.setOnClickListener(this);
        ivScaner.setOnClickListener(this);
        tvTitleName.setText("实验员:" + LoginActivity.userData.getUname());
        bnvBottom.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        loadingDialog = new LoadingDialog(this, "数据查询中...");
        switch (v.getId()) {
            case R.id.add_info:
                /*录入的信息必须是梁板中没有接受检测的信息*/
                if (AllStaticBean.checkRecDatas == null) {
                    loadingDialog.show();
                    ManagerRequst.AllRequest(
                            "checkRec", "", "", "1",
                            labHandler,
                            LAB_PUT_SUCCESS,
                            LAB_FALL2_SUCCESS
                    );
                } else if (AllStaticBean.MaxBeamData == null) {
                    ManagerRequst.AllRequest(
                            "beam","","","1",
                            labHandler,
                            LAB_PUT2_SUCCESS,
                            LAB_FALL_SUCCESS
                    );
                }else
                startActivity(new Intent(this, LabDataActivity.class));
                break;
                /*检测记录查询内容*/
            case R.id.add_info_search:
                    loadingDialog.show();
                    ManagerRequst.AllRequest(
                            "checkRec", "", "", "1",
                            labHandler,
                            LAB_SEARCH_SUCCESS,
                            LAB_FALL_SUCCESS
                    );
                break;
                /*检测数据修改*/
            case R.id.add_info_change:
                    loadingDialog.show();
                    ManagerRequst.AllRequest(
                            "checkRec", "", "", "1",
                            labHandler,
                            LAB_CHANGE_SUCCESS,
                            LAB_FALL_SUCCESS
                    );
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
        }
        return false;
    }
}
