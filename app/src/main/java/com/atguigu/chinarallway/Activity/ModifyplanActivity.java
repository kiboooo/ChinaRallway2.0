package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Adapter.ModifyplanAdapter;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BuildPlan;
import com.atguigu.chinarallway.Bean.GetWeekStartAndEnd;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.RequstServer.ProductionRequest;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/23.
 */

public class ModifyplanActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.et_date)
    TextView etDate;
    @Bind(R.id.btn_upload_plan)
    Button btnUploadPlan;
    @Bind(R.id.recyclerview_modify_plandata)
    RecyclerView recyclerviewModifyPlandata;
    @Bind(R.id.btn_Action_producer_plan)
    Button btnActionProducerPlan;


    private ModifyplanAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*判断是不是梁场技术员加载*/
    private boolean isSetuptech;

    private ArrayList<String> mTitle = new ArrayList<>();
    public String[] bNameList;
    public String[] side;
    public String[] bID;
    private String[] MaxSpinnerData = null;
//    private String[] BridgeSpinnerData = null;

    private LoadingDialog loadingDialog;

    private final int CHANGE_SUCCESS = 1256;
    private final int CHANGE_FALL = 1278;

    private final int TASKSUCCESS = 2003;
    private final int TASKFALL = 2001;
    private final int STARTSUCCESS = 2002;

    private final int GETBUILDPLANSUCCESS = 2004;//拉取架梁计划信号量
    private final int STARTFALL = 2000;


    public String[] getbNameList() {
        return bNameList;
    }

    public String[] getSide() {
        return side;
    }

    public String[] getbID() {
        return bID;
    }

    public String[] getMaxSpinnerData() {
        return MaxSpinnerData;
    }

//    public String[] getBridgeSpinnerData() {
//        return BridgeSpinnerData;
//    }


    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_SUCCESS:
                    Toast.makeText(ModifyplanActivity.this, "录入成功", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;
                case CHANGE_FALL:
                    Toast.makeText(ModifyplanActivity.this, "录入失败", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;

                case TASKFALL:
                    loadingDialog.close();
                    Toast.makeText(ModifyplanActivity.this,"本周的生产计划未生成",Toast.LENGTH_SHORT).show();
                    break;

                    /*架梁计划拉取成功*/
                case GETBUILDPLANSUCCESS:
                    JSONArray jsonArray2 = (JSONArray) msg.obj;
                    AllStaticBean.buildPlens = AllStaticBean.GsonToDate.fromJson(jsonArray2.toString(), BuildPlan[].class);
                    String RequestBody_IDS = "";
                    for (BuildPlan buildPlen : AllStaticBean.buildPlens) {
                        RequestBody_IDS += buildPlen.getBuildID() + ",";
                    }
                    if (!RequestBody_IDS.equals("")) {
                        RequestBody_IDS = RequestBody_IDS.substring(0, RequestBody_IDS.lastIndexOf(","));
                        Log.e("RequestBody_IDS", RequestBody_IDS);
                        //利用架梁计划的ID 生成生产计划
                        ProductionRequest.StartProductionRequest(RequestBody_IDS, STARTSUCCESS, STARTFALL, mhandler);
                    }else
                        mhandler.sendEmptyMessage(STARTFALL);
                    break;


                    /*拉取生产计划成功*/
                case TASKSUCCESS:
                    loadingDialog.close();
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.TaskData = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(), TaskData[].class);
                    /*直接跳转审核*/
                    startActivity(new Intent(ModifyplanActivity.this, ProductionPlanCheckOrAudit.class));
                    finish();
                    break;

                    /*生产计划生成成功*/
                case STARTSUCCESS:
                    loadingDialog.close();
                    ManagerRequst.ProductiongPlanRequest("task", "", "", "3", "1",
                            mhandler, TASKSUCCESS, TASKFALL);
                    break;

                case STARTFALL:
                    loadingDialog.close();
                    Toast.makeText(ModifyplanActivity.this,
                            "生成生产任务失败 或 生产任务已存在",Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyplan);
        ButterKnife.bind(this);
        isSetuptech = getIntent().getStringExtra("Status").equals("true");
        tvTitleTable.setText("修改架梁计划表");
        ivBack.setOnClickListener(this);
        btnUploadPlan.setOnClickListener(this);
        if (isSetuptech) {
            btnActionProducerPlan.setVisibility(View.VISIBLE);
            btnActionProducerPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*启动生产任务，UI展示转移到查询界面,先拉取架梁计划*/
                    loadingDialog = new LoadingDialog(ModifyplanActivity.this, "数据加载中...");
                    loadingDialog.show();
                    /*再拉取一次提交的架梁计划*/
                    ManagerRequst.ProductiongPlanRequest("buildPlan",
                            "", "", "3", "1",
                            mhandler, GETBUILDPLANSUCCESS, STARTFALL);
//                    ProductionRequest.StartProductionRequest( STARTSUCCESS, STARTFALL, mhandler);
                }
            });
        }

        etDate.setText(new GetWeekStartAndEnd().getWeekStartAndEnd());
        initData();
        initSpinner();
        //开始设置RecyclerView
        recyclerviewModifyPlandata = (RecyclerView) this.findViewById(R.id.recyclerview_modify_plandata);
        //设置固定大小
        recyclerviewModifyPlandata.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);

        //给RecyclerView设置布局管理器
        recyclerviewModifyPlandata.setLayoutManager(mLayoutManager);
        mAdapter = new ModifyplanAdapter(this, mTitle);
        recyclerviewModifyPlandata.setAdapter(mAdapter);
    }

    public void initData() {
//        this.bNameList = new String[AllStaticBean.BridgeName.length + 1];
        this.bNameList = new String[AllStaticBean.MaxBeamData.length + 1];
        bNameList[0] = "";
        for (int i = 1; i < bNameList.length; i++) {
//            bNameList[i] = AllStaticBean.BridgeName[i - 1].getbName();
            bNameList[i] = AllStaticBean.MaxBeamData[i - 1].getbName()
                    + " " + AllStaticBean.MaxBeamData[i - 1].getStatus();
        }
        this.side = new String[3];
        this.side[0] = "";
        this.side[1] = "左";
        this.side[2] = "右";

        this.bID = new String[AllStaticBean.MaxBeamData.length + 1];
        this.bID[0] = "";
        for (int i = 1; i < bID.length; i++) {
            this.bID[i] = AllStaticBean.MaxBeamData[i - 1].getbID();
        }

    }

    private void initSpinner() {
        MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length];
//        BridgeSpinnerData = new String[AllStaticBean.BridgeName.length];
        MaxSpinnerData[0] = "";
//        BridgeSpinnerData[0] = "";
        for (int i = 0; i < MaxSpinnerData.length; i++) {
            MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i].getbName()
                    + "_" + AllStaticBean.MaxBeamData[i].getbID();
//            BridgeSpinnerData[i] = AllStaticBean.BridgeName[i].getbName();

        }

    }

    @Override
    public void onClick(View v) {
        loadingDialog = new LoadingDialog(ModifyplanActivity.this, "数据加载中...");
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_upload_plan:
                loadingDialog.show();
                BuildPlan[] buildPlan = new BuildPlan[AllStaticBean.buildPlens.length];
                for (int i = 0; i < buildPlan.length; i++) {
                    BuildPlan b = new BuildPlan();
                    b.setbID(AllStaticBean.buildPlens[i].getbID());
                    b.setbName(AllStaticBean.buildPlens[i].getbName());
                    b.setSide(AllStaticBean.buildPlens[i].getSide());
                    b.setSeq(Short.parseShort(String.valueOf(AllStaticBean.buildPlens[i].getSeq())));
                    b.setbFromDate(AllStaticBean.buildPlens[i].getbFromDate());
                    b.setbToDate(AllStaticBean.buildPlens[i].getbToDate());
                    b.setBuildID(AllStaticBean.buildPlens[i].getBuildID());
                    b.setDir(AllStaticBean.buildPlens[i].getDir());
                    System.out.println(AllStaticBean.buildPlens[i].getDir());
                    buildPlan[i] = b;
                }
                Gson Gsons = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String content = Gsons.toJson(buildPlan);
                Log.e("content", content);
                try {
                    content = URLEncoder.encode(content, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                UpDataRequest.ModifyBuildPlan(content, CHANGE_SUCCESS, CHANGE_FALL, mhandler);
                break;
        }
    }
}
