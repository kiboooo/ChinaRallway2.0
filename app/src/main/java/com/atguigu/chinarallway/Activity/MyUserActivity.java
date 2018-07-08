package com.atguigu.chinarallway.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.application.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/13.
 */

public class MyUserActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.iv_user_back)
    ImageView ivUserBack;
    @Bind(R.id.user_icon)
    ImageView userIcon;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_position)
    TextView tvPosition;
    @Bind(R.id.tv_canel)
    TextView tvCanel;
    @Bind(R.id.JurisdictionContent)
    TextView tvJContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        MyApplication.getinstance().addActivity(this);
        ButterKnife.bind(this);
        tvUsername.setText("姓名："+LoginActivity.userData.getUname());
        tvPosition.setText("职称："+LoginActivity.userData.getRoleName());
        tvJContent.setText(LoginActivity.userData.getPriv());
        ivUserBack.setOnClickListener(this);
        tvCanel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_back:
                finish();
                break;
            case R.id.tv_canel:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getinstance().removeActivity(this);
    }
}
