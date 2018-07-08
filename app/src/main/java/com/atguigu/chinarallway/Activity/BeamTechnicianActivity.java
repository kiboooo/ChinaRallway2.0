package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.BuildPlan;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.Scanner.CustomCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/11.
 * 角色：架梁技术员
 */

public class BeamTechnicianActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.iv_scaner)
    ImageView iv_scaner;
    @Bind(R.id.ll_makeplan)
    LinearLayout llMakeplan;
    @Bind(R.id.makeplan)
    CardView makeplan;
    @Bind(R.id.ll_modify)
    LinearLayout llModify;
    @Bind(R.id.bnv_bottom)
    BottomNavigationView bnvBottom;

    private LoadingDialog loadingDialog;

    private String[] MaxSpinnerData = null;
//    private String[] BridgeSpinnerData = null;

    private int FactoryAbilityData =0 ;
    private final int MAXBEAM_SUCCESS = 12342;
    private final int MAXCHECK_SUCCESS = 123452;
    private final int MAXBEAM_FALL = 45672;
    private final int MAXCHECK_FALL = 456782;

    private final int BUILD_PLAN_SUCCESS = 10;
    private final int BUILD_PLAN_FAILED = 11;
    private final int BUILD_PLAN_SUCCESS1 = 101;
    private final int BUILD_PLAN_FAILED1 = 112;

    public static final int INIT = 20;
    public static final int INIT_SUCCESS = 21;
    public static final int INIT_FAILED = 22;

    private List<String> bNamelistput = new ArrayList<>();
    private List<String> bIDlistput = new ArrayList<>();
    private List<String> sidelistput = new ArrayList<>();
    private Short[] mSeqput = null;

   /* public List<String> bridgename = new ArrayList<>();
    public List<String> getBridgename(){return bridgename;}*/

    public List<String> getbNamelistput(){return bNamelistput;}
    public List<String> getbIDlistput(){return bIDlistput;}
    public List<String> getSidelistput(){return sidelistput;}
    public Short[] getmSeqput(){return mSeqput;}

  @SuppressLint("HandlerLeak")
  private Handler max_handler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {
              case MAXBEAM_SUCCESS:
                  JSONArray jsonArray = (JSONArray) msg.obj;
                  AllStaticBean.MaxBeamData = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(), BeamData[].class);


                  MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length + 1];
//                  BridgeSpinnerData = new String[AllStaticBean.BridgeName.length + 1];
                  MaxSpinnerData[0] = "";
//                  BridgeSpinnerData[0] = "";
                  for (int i = 1; i < MaxSpinnerData.length; i++) {
                      MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i-1].getbName()
                              + "_" + AllStaticBean.MaxBeamData[i-1].getbID();
//                      BridgeSpinnerData[i] = AllStaticBean.BridgeName[i-1].getbName();
                  }
                 /* for (int i=1; i<BridgeSpinnerData.length;i++){
                      if(!bridgename.contains(BridgeSpinnerData[i]))
                          bridgename.add(BridgeSpinnerData[i]);
                  }*/
                     ManagerRequst.ProductiongPlanRequest(
                             "buildPlan", "", "", "3","1",
                             max_handler,
                             BUILD_PLAN_SUCCESS1,
                             BUILD_PLAN_FAILED1
                     );
                  break;

              case BUILD_PLAN_SUCCESS1:{
                  JSONArray array = (JSONArray) msg.obj;
                  BuildPlan[] buildPlen = AllStaticBean.GsonToDate.fromJson(array.toString(),BuildPlan[].class);
                  AllStaticBean.buildPlens = buildPlen;
                  loadingDialog.close();

                  /*跳转到制定架梁计划*/
                  Intent intent = new Intent(getApplicationContext(), MakePlanActivity.class);
                  intent.putExtra("Status", "true");
                  startActivity(intent);
                  break;
              }

              case BUILD_PLAN_SUCCESS:{
                  JSONArray array = (JSONArray) msg.obj;
                  BuildPlan[] buildPlen = AllStaticBean.GsonToDate.fromJson(array.toString(),BuildPlan[].class);
                  AllStaticBean.buildPlens = buildPlen;
                  dissmissDialog();

                  /*跳转到修改架梁计划*/
                  Intent intent = new Intent(getApplicationContext(), ModifyplanActivity.class);
                  intent.putExtra("Status", "false");
                  startActivity(intent);
                  break;
              }
              case BUILD_PLAN_FAILED1:{
                  dissmissDialog();
                  if (msg.arg1 == 0) {
                      Intent intent = new Intent(getApplicationContext(), MakePlanActivity.class);
                      intent.putExtra("Status", "false");
                      startActivity(intent);
                  }else
                      Snackbar.make(llModify,"请求失败2",2000).show();
                  break;
              }
              case BUILD_PLAN_FAILED:{
                  dissmissDialog();
                  Snackbar.make(llModify,"下周的架梁计划未生成",2000).show();
                  break;
              }
              case MAXBEAM_FALL:
                  //出错显示: 错误原因；
                  Toast.makeText(BeamTechnicianActivity.this, "获取信息出错，请刷新", Toast.LENGTH_SHORT).show();
                  break;

              case MAXCHECK_SUCCESS:

                  break;
              case MAXCHECK_FALL:
                  Toast.makeText(BeamTechnicianActivity.this, "获取信息出错，请确认查询请求", Toast.LENGTH_SHORT).show();
                  break;

              case INIT_SUCCESS:{
                  dissmissDialog();
                  JSONArray jsonArray1 = (JSONArray)msg.obj;
                  AllStaticBean.MaxBeamData = AllStaticBean.GsonToDate.fromJson(jsonArray1.toString(), BeamData[].class);
//                  AllStaticBean.BridgeName = AllStaticBean.GsonToDate.fromJson(jsonArray1.toString(), BridgeData[].class);
                  MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length + 1];
//                  BridgeSpinnerData = new String[AllStaticBean.BridgeName.length + 1];
                  MaxSpinnerData[0] = "";
//                  BridgeSpinnerData[0] = "";
                  for (int i = 1; i < MaxSpinnerData.length; i++) {
                      MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i-1].getbName()
                              + "_" + AllStaticBean.MaxBeamData[i-1].getbID();
//                      BridgeSpinnerData[i] = AllStaticBean.BridgeName[i-1].getbName();

                  }
                  break;
              }
              case INIT_FAILED:{
                  dissmissDialog();
                  Snackbar.make(llModify,"请求失败",2000).show();
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Thread.sleep(2000);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                          finish();
                      }
                  }).start();
                  break;
              }
          }
      }
  };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam_technician);
        ButterKnife.bind(this);
        iv_scaner.setOnClickListener(this);
        String content = "架梁技术员:" + LoginActivity.userData.getUname();
        tvTitleName.setText(content);
        llMakeplan.setOnClickListener(this);
        llModify.setOnClickListener(this);
        bnvBottom.setOnNavigationItemSelectedListener(this);
        showLoading("请稍后，正在加载数据");
        ManagerRequst.AllRequest(
                "beam", "", "", "1",
                max_handler,
                INIT_SUCCESS,
                INIT_FAILED
        );
    }

    public void showLoading(String msg){
        loadingDialog = new LoadingDialog(this,msg);
        loadingDialog.show();
    }

    public void dissmissDialog(){
        if(loadingDialog != null)
            loadingDialog.close();
    }

    @Override
    public void onClick(View view) {
        loadingDialog = new LoadingDialog(this, "数据加载中...");
        switch (view.getId()) {
            case R.id.iv_scaner:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);//设置识别的码，QR_CODE_TYPE：为二维码
                intentIntegrator.setTimeout(10 * 1000);//设置扫描超时时间
                intentIntegrator.initiateScan();
                break;

            case R.id.ll_makeplan:
                loadingDialog.show();
                ManagerRequst.AllRequest(
                        "beam", "", "", "1",
                        max_handler,
                        MAXBEAM_SUCCESS,
                        MAXBEAM_FALL
                );

                break;
            case R.id.ll_modify:
                loadingDialog.show();
                ManagerRequst.ProductiongPlanRequest(
                        "buildPlan", "", "", "3","1",
                        max_handler,
                        BUILD_PLAN_SUCCESS,
                        BUILD_PLAN_FAILED
                );
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
