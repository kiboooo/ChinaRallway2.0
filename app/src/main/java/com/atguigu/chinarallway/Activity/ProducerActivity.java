package com.atguigu.chinarallway.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.Scanner.CustomCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/11.
 */

public class ProducerActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.iv_scaner)
    ImageView ivScaner;
    @Bind(R.id.bnv_bottom)
    BottomNavigationView bnvBottom;
    @Bind(R.id.add_info_producer)
    CardView addInfoProducer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer);
        ButterKnife.bind(this);
        String Content = "生产员:" + LoginActivity.userData.getUname();
        tvTitleName.setText(Content);
        ivScaner.setOnClickListener(this);
        addInfoProducer.setOnClickListener(this);
        bnvBottom.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_info_producer:
                /*需要在生产计划表生成后，才打开显示UI*/
                startActivity(new Intent(ProducerActivity.this, ProducerPlanActivity.class));
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

