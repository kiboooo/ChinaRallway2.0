package com.atguigu.chinarallway.Scanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.chinarallway.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kiboooo on 2017/9/21.
 */

public class ScanDisplay extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.ScanDisplay)
    WebView display;
    @Bind(R.id.iv_back)
    ImageView back;
    @Bind(R.id.tv_title_table)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_resout_display);
        ButterKnife.bind(this);
        title.setText("扫描结果");
        back.setOnClickListener(this);
        display.getSettings().setJavaScriptEnabled(true);
        display.setDownloadListener(new CustomWebViewDownLoadListener());
        display.setWebViewClient(new WebViewClient());
        display.loadUrl(getIntent().getStringExtra("result"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private class CustomWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            //此处为文件下载的实现函数，简单处理就是交给系统默认下载器去处理文件，
            // 也可以自己定义下载，比如在状态栏下载，弹出dialog提示下载进度等等
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    /*当前webView 可以返回前面网页的内容的时候，返回之前网页内容*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && display.canGoBack()) {
            display.goBack();
            return true;
        }
        return false;
    }

}
