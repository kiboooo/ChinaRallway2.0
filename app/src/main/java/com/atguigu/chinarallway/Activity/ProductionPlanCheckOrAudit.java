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

import com.atguigu.chinarallway.Adapter.ProductionPlanCOAAdapter;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.GetWeekStartAndEnd;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kiboooo on 2017/10/14.
 */

public class ProductionPlanCheckOrAudit extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.ProducerPlanWeeks)
    RecyclerView ProducerPlanWeeks;
    @Bind(R.id.Audit)
    Button Audit;
    @Bind(R.id.UNAudit)
    Button UNAudit;
    @Bind(R.id.PageUp1)
    Button PageUp1;
    @Bind(R.id.PPPage1)
    TextView PPPage1;
    @Bind(R.id.PageDown1)
    Button PageDown1;

    private final int AuditPass_SUCCESS = 6000;
    private final int AuditPass_FALL = 6001;

    private ProductionPlanCOAAdapter adapter;
    private LoadingDialog loadingDialog;
    private int PAGE = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AuditPass_SUCCESS:
                    loadingDialog.close();
                    Toast.makeText(ProductionPlanCheckOrAudit.this, "生产计划提交成功", Toast.LENGTH_SHORT).show();
                    break;
                case AuditPass_FALL:
                    loadingDialog.close();
                    Toast.makeText(ProductionPlanCheckOrAudit.this, "生产计划提交失败，请核对重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        loadingDialog = new LoadingDialog(ProductionPlanCheckOrAudit.this, "数据上传中...");
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.Audit:
                /*上传已审核的状态*/

                boolean base = true;
                if (AllStaticBean.TaskData != null) {
                    if (ALLPage()==PAGE) {
                        for (int i = 0; i < AllStaticBean.TaskData.length; i++) {
                            if (!AllStaticBean.TaskData[i].isPermit()) {
                                base = false;
                                break;
                            }
                        }
                        if (base) {
                            Toast.makeText(ProductionPlanCheckOrAudit.this,
                                    "生产计划已审核通过，请勿重复提交", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                loadingDialog.show();
                                SaveData(PAGE);
                                Log.e("上传已审核的状态", AllStaticBean.GsonToDate.toJson(AllStaticBean.TaskData));
                                //修改数据库状态,Commite:0 审核
                                UpDataRequest.ModifyTask(
                                        URLEncoder.encode(AllStaticBean.GsonToDate.toJson(AllStaticBean.TaskData), "UTF-8"),
                                        "0",AuditPass_SUCCESS, AuditPass_FALL, mHandler);
                            } catch (ParseException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                }else
                    Toast.makeText(ProductionPlanCheckOrAudit.this,
                            "温馨提示：下一页还有数据尚未审核，不允许提交未审核数据",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ProductionPlanCheckOrAudit.this, "生产计划为空，不允许提交", Toast.LENGTH_SHORT).show();
                break;

            case R.id.UNAudit:
                /*询问是否要修改，跳转相应界面*/
                if (AllStaticBean.TaskData != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductionPlanCheckOrAudit.this)
                            .setTitle("温馨提示：")
                            .setMessage("生产计划（" + new GetWeekStartAndEnd().getWeekStartAndEnd() + "）审核未通过，是否执行修改")
                            .setPositiveButton("执行", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(ProductionPlanCheckOrAudit.this, ProductionPlanChange.class));
                                    finish();
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
                } else
                    Toast.makeText(ProductionPlanCheckOrAudit.this, "生产计划为空，不允许操作", Toast.LENGTH_SHORT).show();
                break;

            case R.id.PageDown1:
                PAGE++;
                Log.e("PageDown1", " " + ALLPage());
                if (!(PAGE > ALLPage())) {
                    Log.e("Page", PAGE + "");
                    try {
                        SaveData(PAGE-1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    initRecyclerView(PAGE);
                }else
                {
                    PAGE--;
                    Toast.makeText(ProductionPlanCheckOrAudit.this,"没有下一页",Toast.LENGTH_SHORT).show();
                    Log.e("Page", PAGE + "");
                }
                break;

            case R.id.PageUp1:
                PAGE--;
                if (!(PAGE <= 0)) {
                    Log.e("Page", PAGE + "");
                    try {
                        SaveData(PAGE+1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    initRecyclerView(PAGE);
                }else
                {
                    PAGE++;
                    Toast.makeText(ProductionPlanCheckOrAudit.this,"没有上一页",Toast.LENGTH_SHORT).show();
                    Log.e("Page", PAGE + "");
                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_production_plan);
        ButterKnife.bind(this);
        String content = new GetWeekStartAndEnd().getWeekStartAndEnd() + ": 生产任务";
        tvTitleTable.setText(content);
        ivBack.setOnClickListener(this);
        Audit.setOnClickListener(this);
        UNAudit.setOnClickListener(this);
        PageUp1.setOnClickListener(this);
        PageDown1.setOnClickListener(this);
        initRecyclerView(PAGE);
    }


    private void initRecyclerView(int NowPage) {

        List<TaskData> taskDatas = new ArrayList<>();
//        do {
            int MaxPage = (NowPage * 5 > AllStaticBean.TaskData.length)?AllStaticBean.TaskData.length:NowPage * 5;
                for (int i = (NowPage - 1) * 5; i < MaxPage; i++) {
                    taskDatas.add(AllStaticBean.TaskData[i]);
                }
                Log.e("taskDatasList", "Size is :" + taskDatas.size() + "   " + MaxPage + "  " + NowPage);
//            NowPage++;
//        } while (Math.abs(NowPage * 5 - AllStaticBean.TaskData.length)<=5 );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductionPlanCheckOrAudit.this);
        adapter = new ProductionPlanCOAAdapter(taskDatas);
        ProducerPlanWeeks.setLayoutManager(linearLayoutManager);
        ProducerPlanWeeks.setAdapter(adapter);
        PPPage1.setText(GetNowPage(PAGE));
    }

    private String GetNowPage(int NowPage) {
        String page = "0/1";
        if (AllStaticBean.TaskData!=null)
            page = NowPage + "/" + ALLPage();
        return page;
    }

    private int ALLPage() {
        int i = AllStaticBean.TaskData.length / 5;
        double j = (AllStaticBean.TaskData.length * 1.0)/ 5;
        Log.e("ALLPage", " " + i + " " + j);
        return (j > i ? i + 1 : i);
    }

    private void SaveData(int nowPage) throws ParseException {
        int MAX = adapter.getItemCount() < 5 ? adapter.getItemCount() : 5;
        for (int i = (nowPage - 1) * 5, j = 0; j < MAX; i++, j++) {
            AllStaticBean.TaskData[i] = adapter.GetTaskData(j);
        }
    }

}
