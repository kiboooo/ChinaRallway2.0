package com.atguigu.chinarallway.Chart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.MonthData;
import com.atguigu.chinarallway.Bean.StoreData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/12.
 * Title：钢筋量统计
 */

public class RebarAmountActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.barchart_rebar)
    BarChart barchart;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.rebar_layout)
    View rebar_layout;
    @Bind(R.id.rebar_content)
    TextView rebar_content;

    private final int REBAR_THEORY_SUCCESS = 94;
    private final int REBAR_THEORY_FALL = 93;
    private final int REBAR_BEAM_SUCCESS = 90;
    private final int REBAR_BEAM_FALL = 95;
    private final int REBAR_REALITY_SUCCESS = 92;
    private final int REBAR_REALITY_FALL = 91;

    private float[] Theory = new float[13];
    private float[] Reality = new float[13];

    private Date BaseDate = null;
    private StoreData[] storeDatas;
    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler RA_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REBAR_THEORY_SUCCESS:
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    MonthData[] monthDatas = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(), MonthData[].class);
                    Reality[0] = 0;
                    for (int i = 0; i < monthDatas.length; i++) {
                        if (i == 0) {
                          BaseDate = monthDatas[0].getsDate();
                            Log.e("REBAR_THEORY_SUCCESS", BaseDate.toString());
                        }
                        Reality[i + 1] = monthDatas[i].getConcrete().floatValue();
                    }
                    ManagerRequst.AllRequest(
                            "store", "", "", "1",
                            RA_Handler,
                            REBAR_REALITY_SUCCESS,
                            REBAR_REALITY_FALL
                    );
                    break;
                case REBAR_THEORY_FALL:
                    Toast.makeText(RebarAmountActivity.this, "获取信息出错，请确认查询请求", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;

                case REBAR_REALITY_SUCCESS:
                    JSONArray store_jsonArray = (JSONArray) msg.obj;
                    storeDatas = AllStaticBean.GsonToDate.fromJson(store_jsonArray.toString(), StoreData[].class);
                    ManagerRequst.AllRequest(
                            "beam", "", "", "1",
                            RA_Handler,
                            REBAR_BEAM_SUCCESS,
                            REBAR_BEAM_FALL
                    );
                    break;
                case REBAR_REALITY_FALL:
                    Toast.makeText(RebarAmountActivity.this, "获取信息出错，请确认查询请求", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;
                case REBAR_BEAM_SUCCESS:
                    JSONArray store_JA = (JSONArray) msg.obj;
                    BeamData[] beamDatas = AllStaticBean.GsonToDate.fromJson(store_JA.toString(), BeamData[].class);
                    initRebarAmount(storeDatas, beamDatas);
                    break;

                case REBAR_BEAM_FALL:
                    Toast.makeText(RebarAmountActivity.this, "获取信息出错，请确认查询请求", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart_rebar);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        tvTitleTable.setText("钢筋量统计");
        loadingDialog = new LoadingDialog(this, "数据加载中...");
        rebar_layout.setVisibility(View.INVISIBLE);
        rebar_content.setVisibility(View.INVISIBLE);
        loadingDialog.show();
        ManagerRequst.AllRequest(
                "monthData","","","1",
                RA_Handler,
                REBAR_THEORY_SUCCESS,
                REBAR_THEORY_FALL
        );
    }

    private void initRebarAmount(StoreData[] SD, BeamData[] BD) {
        SimpleDateFormat sdf  =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Log.e("initRebarAmount", "进入了" + SD.length + "   *");
        for (StoreData aSD : SD) {
            try {
                int flag = sdf.parse(aSD.getInTime()).getMonth() - BaseDate.getMonth();
                Theory[flag] += SearchBD(aSD.getbName(), aSD.getbID(), BD);
            } catch (ParseException e) {
                e.printStackTrace();
                loadingDialog.close();
            }
        }
        initBarChart(Theory, Reality);
    }

    private float SearchBD(String BName,String Bid ,BeamData[] BD) {
        for (BeamData beamData : BD) {
            if (beamData.getbName().equals(BName)
                    && beamData.getbID().equals(Bid)) {
                if (beamData.getSteel() != null) {
                    return beamData.getSteel().floatValue();
                }else{
                    return 0.0f;
                }

            }
        }
        return 0;
    }


    private void initBarChart(final float[] theory,final float[] reality) {
        rebar_layout.setVisibility(View.VISIBLE);
        rebar_content.setVisibility(View.VISIBLE);
        loadingDialog.close();
        ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组
        ArrayList<BarEntry> yVals2 = new ArrayList<>();//Y轴方向第二组数组
        ArrayList<String> xVals = new ArrayList<>();//X轴数据

        for (int i = 1; i < 13; i++) {//添加数据源
            xVals.add(i + "月");
            yVals.add(new BarEntry(theory[i], i));
            yVals2.add(new BarEntry(reality[i], i));

        }

        BarDataSet barDataSet = new BarDataSet(yVals, "理论消耗值");
        barDataSet.setColor(Color.rgb(255, 123, 124));//设置第一组数据颜色

        BarDataSet barDataSet2 = new BarDataSet(yVals2, "实际消耗值");
        barDataSet2.setColor(Color.rgb(114, 188, 223));//设置第二组数据颜色

        ArrayList<BarDataSet> threebardata = new ArrayList<>();//BarDataSet 接口很关键，是添加多组数据的关键结构，LineChart也是可以采用对应的接口类，也可以添加多组数据
        threebardata.add(barDataSet);
        threebardata.add(barDataSet2);

        BarData bardata = new BarData(xVals, threebardata);
        barchart.setData(bardata);
        barchart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);//设置注解的位置在左上方
        barchart.getLegend().setForm(Legend.LegendForm.CIRCLE);//这是左边显示小图标的形状

        barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
        barchart.getXAxis().setDrawGridLines(false);//不显示网格

        barchart.getAxisRight().setEnabled(false);//右侧不显示Y轴
        barchart.getAxisLeft().setAxisMinValue(0.0f);//设置Y轴显示最小值，不然0下面会有空隙
        barchart.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格

        barchart.setDescription("");//设置描述
        barchart.setDescriptionTextSize(20.f);//设置描述字体
        barchart.animateXY(1000, 2000);//设置动画
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
