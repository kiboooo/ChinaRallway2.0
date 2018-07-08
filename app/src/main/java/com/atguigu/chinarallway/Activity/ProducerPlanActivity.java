package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Adapter.ProducerAdapter;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.TaskWithBeam;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;

import org.json.JSONArray;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.atguigu.chinarallway.R.id.iv_back;

/**
 * Created by Kiboooo on 2017/10/9.
 */

public class ProducerPlanActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.MenuTitle)
    TabLayout MenuTitle;
    @Bind(R.id.MenuContent)
    ViewPager MenuContent;

    private final int TASK_SUCCESS = 3003;
    private final int TASK_FALL = 3001;
    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TASK_SUCCESS:
                    loadingDialog.close();
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.taskWithBeams = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(), TaskWithBeam[].class);
                    ProducerAdapter adapter = new ProducerAdapter(getSupportFragmentManager(), ProducerPlanActivity.this);
                    MenuContent.setAdapter(adapter);
                    MenuTitle.setupWithViewPager(MenuContent);
                    break;
                case TASK_FALL:
                    loadingDialog.close();
                    Toast.makeText(ProducerPlanActivity.this, "生产任务未定制或当天无生产任务",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_plan);
        ButterKnife.bind(this);
        String mTitle = AllStaticBean.formatter.format(new Date(System.currentTimeMillis()))+":生产任务单";
        tvTitleTable.setText(mTitle);
        ivBack.setOnClickListener(this);
        loadingDialog = new LoadingDialog(this, "数据请求中...");
        /*searchAll = 0 为当天数据，1 为全部数据*/
        ManagerRequst.AllRequest("TaskwithBeam", "", "", "0", mHandler, TASK_SUCCESS, TASK_FALL);
        loadingDialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case iv_back:
                finish();
                break;
        }
    }
}
