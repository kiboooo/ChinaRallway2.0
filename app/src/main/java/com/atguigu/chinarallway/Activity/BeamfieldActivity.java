package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.BuildPlan;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.Scanner.CustomCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/11.
 * Function：梁场技术员
 */

public class BeamfieldActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.iv_scaner)
    ImageView ivScaner;
    @Bind(R.id.ll_repair_beam)
    LinearLayout llRepairBeam;
    @Bind(R.id.ll_search_beam)
    LinearLayout llSearchBeam;
    @Bind(R.id.ll_upload_radio)
    LinearLayout llUploadRadio;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.ll_read_radio)
    LinearLayout llReadRadio;
    @Bind(R.id.ll_search_misson)
    LinearLayout llSearchMisson;
    @Bind(R.id.bnv_bottom)
    BottomNavigationView bnvBottom;
    @Bind(R.id.ll_make_misson)
    LinearLayout llMakeMisson;
    @Bind(R.id.ll_change_misson)
    LinearLayout llChangeMisson;

    private final int TASKSUCCESS = 2003;
    private final int TASKCHANGESUCCESS = 2005;
    private final int TASKFALL = 2001;

    private final int BEAM_SUCCESS= 913;
    private final int BEAM_FALL = 914;

    /*获取所有梁板数据的*/
    private final int BUILD_PLAN_SUCCESS2 = 5644;
    private final int BUILD_PLAN_FAILED2 = 5666;


    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BEAM_SUCCESS:
                    loadingDialog.close();
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.MaxBeamData = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(), BeamData[].class);
                    break;

                case BEAM_FALL:
                    loadingDialog.close();
                    Snackbar.make(llMakeMisson,"获取梁板失败",2000).show();
                    break;

                case TASKSUCCESS:
                    loadingDialog.close();
                    JSONArray ja = (JSONArray) msg.obj;
                    AllStaticBean.TaskData = AllStaticBean.GsonToDate.fromJson(ja.toString(), TaskData[].class);
                    startActivity(new Intent(BeamfieldActivity.this, ProductionPlanCheckOrAudit.class));
                    break;

                case TASKCHANGESUCCESS:
                    loadingDialog.close();
                    JSONArray jsonArray3 = (JSONArray) msg.obj;
                    AllStaticBean.TaskData = AllStaticBean.GsonToDate.fromJson(jsonArray3.toString(), TaskData[].class);
                    startActivity(new Intent(BeamfieldActivity.this, ProductionPlanChange.class));
                    break;

                case TASKFALL:
                    loadingDialog.close();
                    Toast.makeText(BeamfieldActivity.this,"本周的生产计划未生成",Toast.LENGTH_SHORT).show();
                    break;

                case BUILD_PLAN_SUCCESS2:{
                    JSONArray array = (JSONArray) msg.obj;
                    BuildPlan[] buildPlen = AllStaticBean.GsonToDate.fromJson(array.toString(),BuildPlan[].class);
                    AllStaticBean.buildPlens = buildPlen;
                    dissmissDialog();

                  /*跳转到修改架梁计划*/
                    Intent intent = new Intent(getApplicationContext(), ModifyplanActivity.class);
                    intent.putExtra("Status", "true");
                    startActivity(intent);
                    break;
                }

                case BUILD_PLAN_FAILED2:{
                    dissmissDialog();
                    Toast.makeText(BeamfieldActivity.this,
                            "架梁计划未生成",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beamfield);
        ButterKnife.bind(this);
        ivScaner.setOnClickListener(this);
        String content = "梁场技术员:" + LoginActivity.userData.getUname();
        tvTitleName.setText(content);
        loadingDialog = new LoadingDialog(BeamfieldActivity.this, "请稍后...");
        loadingDialog.show();
        ManagerRequst.AllRequest("beam","","","1",
                mHandler,BEAM_SUCCESS,BEAM_FALL);
        //设置按钮的监听
        bnvBottom.setOnNavigationItemSelectedListener(this);
        llSearchBeam.setOnClickListener(this);
        llUploadRadio.setOnClickListener(this);
        llRepairBeam.setOnClickListener(this);
        llSearchMisson.setOnClickListener(this);
        llReadRadio.setOnClickListener(this);
        llChangeMisson.setOnClickListener(this);
        llMakeMisson.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        loadingDialog = new LoadingDialog(BeamfieldActivity.this, "请稍后...");
        switch (view.getId()) {
            case R.id.iv_scaner:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);//设置识别的码，QR_CODE_TYPE：为二维码
                intentIntegrator.setTimeout(10 * 1000);//设置扫描超时时间
                intentIntegrator.initiateScan();
                break;

            case R.id.ll_search_beam:
                /*查询梁板信息*/
                startActivity(new Intent(this, BeamDataSearchActivity.class));
                break;

            case R.id.ll_upload_radio:
                /*上传工艺视频*/
                startActivity(new Intent(this, UploadRadioActivity.class));
                break;

            case R.id.ll_repair_beam:
                /*维护梁板状态*/
                startActivity(new Intent(this, RepairBeamActivity.class));
                break;

            case R.id.ll_read_radio:
                /*查看工艺视频*/
                startActivity(new Intent(this, BrowseVideoActivity.class));
                break;

            case R.id.ll_make_misson:
                /*启动生产任务，UI展示转移到查询界面,先拉取架梁计划*/
                loadingDialog.show();
                ManagerRequst.ProductiongPlanRequest(
                        "buildPlan", "", "", "3","1",
                        mHandler,
                        BUILD_PLAN_SUCCESS2,
                        BUILD_PLAN_FAILED2
                );
                break;

            case R.id.ll_search_misson:
                /*显示生产任务表  0 为当前周 1 为全部数据 3 实现week数据 ,week : -1 上周，0 当周，1 下周*/
                loadingDialog.show();
                ManagerRequst.ProductiongPlanRequest("task", "", "", "3","1",
                        mHandler, TASKSUCCESS, TASKFALL);
                break;

            case R.id.ll_change_misson:
                /*修改生产任务 0 为当前周 1 为全部数据 3 实现week数据 ,week : -1 上周，0 当周，1 下周*/
                loadingDialog.show();
                ManagerRequst.ProductiongPlanRequest("task", "", "", "3","1",
                        mHandler, TASKCHANGESUCCESS, TASKFALL);
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

    public void dissmissDialog(){
        if(loadingDialog != null)
            loadingDialog.close();
    }
}
