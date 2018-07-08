package com.atguigu.chinarallway.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.atguigu.chinarallway.Scanner.ScanDisplay;
import com.atguigu.chinarallway.application.MyApplication;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by 陈雨田 on 2017/9/11.
 *
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getinstance().addActivity(this);
    }

    @Override
    public boolean onKeyDown(final int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog exitDialog = new AlertDialog.Builder(this).create();
            exitDialog.setTitle("温馨提示：");
            exitDialog.setMessage("你确定要退出吗");
            exitDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyApplication.getinstance().closeAllActiivty();
                }
            });
            exitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            exitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                //不允许点击back时，导致Dialog消失。
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return true;
                }
            });
            exitDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getinstance().removeActivity(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "取消扫描", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, ScanDisplay.class);
                intent.putExtra("result", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {

    }
}