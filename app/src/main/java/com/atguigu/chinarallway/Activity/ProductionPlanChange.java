package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Adapter.ProductionPlanChangeAdapter;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.GetWeekStartAndEnd;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ProductionPlanChange extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.ProducerPlanWeeksChange)
    RecyclerView ProducerPlanWeeksChange;
    @Bind(R.id.Up)
    Button Up;

    private final int ChangeUpdateFinish = 4005;
    private final int GOAuditSUCCUSS = 4003;
    private final int ChangeUpdateFALL = 4002;
    private final int GOAuditFALL = 4004;
//    @Bind(R.id.PageUp2)
//    Button PageUp2;
//    @Bind(R.id.PPPage2)
//    TextView PPPage2;
//    @Bind(R.id.PageDown2)
//    Button PageDown2;

    private LoadingDialog loadingDialog;
    private ProductionPlanChangeAdapter adapter;
    private int PAGE = 1;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case ChangeUpdateFALL:
                    loadingDialog.close();
                    Toast.makeText(ProductionPlanChange.this, "生产任务修改失败，请审核重试", Toast.LENGTH_SHORT).show();
                    break;


                case ChangeUpdateFinish:
                    loadingDialog.close();
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductionPlanChange.this)
                            .setTitle("修改成功：")
                            .setMessage("生产计划（" + new GetWeekStartAndEnd().getWeekStartAndEnd() + "）未审核，是否执行审核")
                            .setPositiveButton("执行", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingDialog.show();
                                    ManagerRequst.ProductiongPlanRequest("Task", "", "", "3", "1",
                                            mHandler, GOAuditSUCCUSS, GOAuditFALL);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .create();
                    alertDialog.show();
                    break;

                case GOAuditSUCCUSS:
                    loadingDialog.close();
                    startActivity(new Intent(ProductionPlanChange.this, ProductionPlanCheckOrAudit.class));
                    finish();
                    break;

                case GOAuditFALL:
                    loadingDialog.close();
                    Toast.makeText(ProductionPlanChange.this, "生产任务请求失败，请审核重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        loadingDialog = new LoadingDialog(ProductionPlanChange.this, "数据上传中....");
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.Up:
                if (ALLPage() == PAGE) {
                    /*用数据更新接口提交更改数据*/
                    loadingDialog.show();
                    try {
                        SaveData(PAGE);

                        //修改数据库数据，Commite：1 修改
                        UpDataRequest.ModifyTask(URLEncoder.encode(AllStaticBean.GsonToDate.toJson(AllStaticBean.TaskData), "UTF-8"),
                                "1", ChangeUpdateFinish, ChangeUpdateFALL, mHandler);

                    } catch (ParseException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else
                    Toast.makeText(ProductionPlanChange.this,
                            "温馨提示：下一页还有数据尚未审核，不允许提交未审核数据",
                            Toast.LENGTH_SHORT).show();

                break;

//            case R.id.PageDown2:
//                PAGE++;
//                if (!(PAGE > ALLPage())) {
//                    Log.e("Page", PAGE + "");
//                    try {
//                        SaveData(PAGE-1);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    initRecyclerView(PAGE);
//                }else
//                {
//                    PAGE--;
//                    Toast.makeText(ProductionPlanChange.this,"没有下一页",Toast.LENGTH_SHORT).show();
//                    Log.e("Page", PAGE + "");
//                }
//                break;
//
//            case R.id.PageUp2:
//                PAGE--;
//                if (!(PAGE <= 0)) {
//                    Log.e("Page", PAGE + "");
//                    try {
//                        SaveData(PAGE+1);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    initRecyclerView(PAGE);
//                }else
//                {
//                    PAGE++;
//                    Toast.makeText(ProductionPlanChange.this,"没有上一页",Toast.LENGTH_SHORT).show();
//                    Log.e("Page", PAGE + "");
//                }
//                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_production_plan_change);
        ButterKnife.bind(this);
        String content = new GetWeekStartAndEnd().getWeekStartAndEnd() + ": 修改生产任务";
        tvTitleTable.setText(content);
        ivBack.setOnClickListener(this);
        Up.setOnClickListener(this);
//        PageDown2.setOnClickListener(this);
//        PageUp2.setOnClickListener(this);
        initRecyclerView(PAGE);
    }

    private void initRecyclerView(int NowPage) {
        List<TaskData> taskDatas = new ArrayList<>();
        int MaxPage = NowPage * 5 > AllStaticBean.TaskData.length ? AllStaticBean.TaskData.length : NowPage * 5;
//        for (int i = (NowPage - 1) * 5; i < MaxPage ; i++) {
//            taskDatas.add(AllStaticBean.TaskData[i]);
//        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductionPlanChange.this);
        adapter = new ProductionPlanChangeAdapter(AllStaticBean.TaskData,ProductionPlanChange.this,getFragmentManager());
        ProducerPlanWeeksChange.setLayoutManager(linearLayoutManager);
        ProducerPlanWeeksChange.setAdapter(adapter);
//        PPPage2.setText(GetNowPage(PAGE));
    }

    private String GetNowPage(int NowPage) {
        String page = "0/1";
        if (AllStaticBean.TaskData!=null)
            page = NowPage + "/" + ALLPage();
        return page;
    }

    private int ALLPage() {
        int i = AllStaticBean.TaskData.length / 5;
        double j = AllStaticBean.TaskData.length * 1.0 / 5;
        return (j > i ? i + 1 : i);
    }

    private void SaveData(int nowPage) throws ParseException {
        int MAX = adapter.getItemCount() < 5 ? adapter.getItemCount() : 5;
        for (int i = (nowPage - 1) * 5, j = 0; j < MAX; i++, j++) {
            AllStaticBean.TaskData[i] = adapter.GetTaskData(j);
            Log.e("SaveData", AllStaticBean.TaskData[i].getPos());

        }
    }
}

