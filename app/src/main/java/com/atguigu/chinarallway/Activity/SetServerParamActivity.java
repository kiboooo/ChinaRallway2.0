package com.atguigu.chinarallway.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LZL on 2017/10/12.
 */

public class SetServerParamActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.set_server_button)
    Button setButton;
    @Bind(R.id.server_ip)
    EditText ip;
    @Bind(R.id.server_port)
    EditText port;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_server);
        ButterKnife.bind(this);
        setListener();
        getSupportActionBar().setTitle("设置服务器地址");
    }

    private void setListener(){
        setButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_server_button:{
                setServerParam();
                break;
            }
        }
    }

    private void setServerParam(){
        boolean flag = ip.getText().toString().matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");
        boolean portFlag = port.getText().toString().matches("\\d{1,5}");
        String portData = port.getText().toString();
        if(port.getText().toString().equals(""))
        {
            portData = "8080";
            portFlag = true;
        }
        if(flag&&portFlag){
            AllStaticBean.URL = "http://"+ip.getText().toString()+":"+portData+"/ChinaRailWay";
            Snackbar.make(port,"设置成功",Snackbar.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }).start();
        }else{
            Snackbar.make(port,"格式错误",Snackbar.LENGTH_SHORT).show();
        }
    }
}
