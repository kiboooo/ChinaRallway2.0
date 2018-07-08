package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
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

import com.atguigu.chinarallway.Adapter.PlanDataAdapter;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BuildPlan;
import com.atguigu.chinarallway.Bean.FactoryData;
import com.atguigu.chinarallway.Bean.GetWeekStartAndEnd;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/23.
 *
 */

public class MakePlanActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.btn_upload)
    Button btnUpload;
    @Bind(R.id.et_date)
    TextView etDate;
    @Bind(R.id.recyclerview_plandata)
    RecyclerView recyclerviewPlandata;
    @Bind(R.id.btn_add_beam)
    Button btnAddBeam;

    private ArrayList<String> mTitle=new ArrayList<>();

    private PlanDataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public List<String> bridgename = new ArrayList<>();
    public List<String> getBridgename(){return bridgename;}


    private String[] MaxSpinnerData = null;
    private String[] BridgeSpinnerData = null;
    private int FactoryAbilityData;

    private final int ADDINFO_SUCCESS = 74111;
    private final int ADDINFO_FALL = 74121;
    private final int FACTORY_SUCCESS = 741112;
    private final int FACTORY_FALL = 741212;

    private LoadingDialog loadingDialog;

    private String fromDate;
    private String toDate;


    public String[] getMaxSpinnerData() {
        return MaxSpinnerData;
    }

    public String[] getBridgeSpinnerData() { return BridgeSpinnerData; }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADDINFO_SUCCESS:
                    loadingDialog.close();
                    Toast.makeText(MakePlanActivity.this, "录入成功", Toast.LENGTH_SHORT).show();
                    break;
                case ADDINFO_FALL:
                    Toast.makeText(MakePlanActivity.this, "录入失败", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;
                case FACTORY_SUCCESS:
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.factoryDatas = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(),FactoryData[].class);
                    FactoryAbilityData = AllStaticBean.factoryDatas[0].getAbility();
                    break;
                case FACTORY_FALL:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeplan);
        ButterKnife.bind(this);

        tvTitleTable.setText("制定架梁计划");
        ivBack.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnAddBeam.setOnClickListener(this);

        initSpinner();
       if (MaxSpinnerData != null) {
            BoundSpinner();
        }


        String date = new GetWeekStartAndEnd().getWeekStartAndEnd();

        String[] pas = date.split("_");
        fromDate = pas[0];
        toDate = pas[1];
        etDate.setText(date);

        //开始设置RecyclerView
        recyclerviewPlandata = (RecyclerView) this.findViewById(R.id.recyclerview_plandata);
        //设置固定大小
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
        recyclerviewPlandata.setLayoutManager(mLayoutManager);


    }


    private void initSpinner() {
            bridgename.add(" ");
            MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length ];
//            BridgeSpinnerData = new String[AllStaticBean.BridgeName.length];
            MaxSpinnerData[0] = "";
//            BridgeSpinnerData[0] = "";
            for (int i = 0; i < MaxSpinnerData.length; i++) {
                MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i ].getbName()
                        + "_" + AllStaticBean.MaxBeamData[i].getbID()
                        + "_" + AllStaticBean.MaxBeamData[i].getStatus();
//                BridgeSpinnerData[i] = AllStaticBean.BridgeName[i].getbName();
                if(!bridgename.contains(AllStaticBean.MaxBeamData[i].getbName()))
                    bridgename.add(AllStaticBean.MaxBeamData[i].getbName());

            }

//            for (int i=1; i<BridgeSpinnerData.length;i++){
//            if(!bridgename.contains(BridgeSpinnerData[i]))
//                bridgename.add(BridgeSpinnerData[i]);
//            }

        ManagerRequst.AllRequest(
                "factory", "", "", "1",
                mhandler,
                FACTORY_SUCCESS,
                FACTORY_FALL
        );
    }

    private void BoundSpinner() {
        mAdapter = new PlanDataAdapter(this,mTitle);
        recyclerviewPlandata.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {
        loadingDialog = new LoadingDialog(MakePlanActivity.this, "数据上传中...");
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_upload:
                loadingDialog.show();
                BuildPlan[] buildPlan = new BuildPlan[mAdapter.getmSeq().length];
                if (mAdapter.isbIDlist()
                        && mAdapter.isbNamelist()
                        && mAdapter.isSidechooselist()) {

                    for (int i = 0; i < buildPlan.length; i++) {
                        BuildPlan b = new BuildPlan();
                        try {
                            //默认让Dir设置成“从小里程到大里程”
                            b.setDir(URLEncoder.encode("小里程到大里程", "UTF-8"));
                            b.setbID(URLEncoder.encode(mAdapter.getbIDlist().get(i), "UTF-8"));
                            b.setbName(URLEncoder.encode(mAdapter.getbNamelist().get(i), "UTF-8"));
                            b.setSide(URLEncoder.encode(mAdapter.getSidechooselist().get(i), "UTF-8"));
                            b.setSeq(mAdapter.getmSeq()[i]);
                            b.setbFromDate(Date.valueOf(fromDate));
                            b.setbToDate(Date.valueOf(toDate));
                            buildPlan[i] = b;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    Gson Gsons = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    String content = Gsons.toJson(buildPlan);

                    UpDataRequest.AddInfoRequest("buildPlan", content,
                            ADDINFO_SUCCESS, ADDINFO_FALL, mhandler);

                    Log.e("upload", content);
                } else {
                    loadingDialog.close();
                    Toast.makeText(MakePlanActivity.this, "请勿提交空数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_beam:
                Log.e("btn_add_beam", "" + mAdapter.Max());
                if(mAdapter.Max()< FactoryAbilityData*7 ){
                    mAdapter.add("",0);
                }

                break;



        }
    }

}
