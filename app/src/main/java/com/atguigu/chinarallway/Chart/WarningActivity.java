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
import com.atguigu.chinarallway.Bean.FactoryData;
import com.atguigu.chinarallway.Bean.MakePosition;
import com.atguigu.chinarallway.Bean.StorePositionData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 陈雨田 on 2017/9/13.
 * Titles：查看预警
 */

public class WarningActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind((R.id.barchart_warning))
    BarChart barchart;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.pedestal_normo)
    TextView normo;
    @Bind(R.id.pedestal_warning1)
    TextView warning1;
    @Bind(R.id.pedestal_warning2)
    TextView warning2;
    @Bind(R.id.warning_linearLayout)
    View WarningLinearLayout;
    @Bind(R.id.X_textView)
    TextView X_textView;


    private final int WARNING_GET_FACTORY_SUCCESS = 14789;

    private final int WARNING_GET_FACTORY_FALL = 12369;

    private final int WARNING_GET_MAKEPOSITION_SUCCESS = 14785;
    private final int WARNING_GET_MAKEPOSITION_FALL = 25874;

    private final int WARNING_GET_STOREPOSITION_SUCCESS = 147852;
    private final int WARNING_GET_STOREPOSITION_FALL = 258741;

    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    public Handler warnHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WARNING_GET_FACTORY_SUCCESS:
                    JSONArray jsonArray = (JSONArray) msg.obj;

                    AllStaticBean.factoryDatas = AllStaticBean.GsonToDate.fromJson(jsonArray.toString(), FactoryData[].class);
                    initBarChartData(1);
                    break;
                case WARNING_GET_FACTORY_FALL:
                    Toast.makeText(WarningActivity.this, "请求梁场表出错", Toast.LENGTH_SHORT).show();
                    break;
                case WARNING_GET_MAKEPOSITION_SUCCESS:
                    JSONArray makePosition = (JSONArray) msg.obj;
                    AllStaticBean.makePositions = AllStaticBean.GsonToDate.fromJson(makePosition.toString(), MakePosition[].class);
                    initBarChartData(2);
                    break;
                case WARNING_GET_MAKEPOSITION_FALL:
                    Toast.makeText(WarningActivity.this, "请求制梁台座表出错", Toast.LENGTH_SHORT).show();
                    break;
                case WARNING_GET_STOREPOSITION_SUCCESS:
                    JSONArray storePosition = (JSONArray) msg.obj;
                    AllStaticBean.storePositionDatas = AllStaticBean.GsonToDate.fromJson(storePosition.toString(), StorePositionData[].class);
                    initBarChart(
                            AllStaticBean.factoryDatas[0].getHighWarn().floatValue(),
                            AllStaticBean.factoryDatas[0].getLowWarn().floatValue(),
                            GetMakeVacancyRate(),
                            GetStoreVacancyRate());
                    break;
                case WARNING_GET_STOREPOSITION_FALL:
                    Toast.makeText(WarningActivity.this, "请求存梁台座表出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart_warning);
        ButterKnife.bind(this);
        tvTitleTable.setText("查看预警");
        ivBack.setOnClickListener(this);
        loadingDialog = new LoadingDialog(this, "数据加载中..");
        WarningLinearLayout.setVisibility(View.INVISIBLE);
        X_textView.setVisibility(View.INVISIBLE);
        loadingDialog.show();
        if (AllStaticBean.factoryDatas != null) {
            if (AllStaticBean.makePositions!= null &&
                    AllStaticBean.storePositionDatas!= null) {
                initBarChart(
                        AllStaticBean.factoryDatas[0].getHighWarn().floatValue(),
                        AllStaticBean.factoryDatas[0].getLowWarn().floatValue(),
                        GetMakeVacancyRate(),
                        GetStoreVacancyRate());
            } else
                initBarChartData(1);
        } else {
            ManagerRequst.AllRequest(
                    "factory", "", "", "1",
                    warnHandler,
                    WARNING_GET_FACTORY_SUCCESS,
                    WARNING_GET_FACTORY_FALL
            );
        }
    }

    private void initBarChartData(int i) {
        if (i == 1) {
            ManagerRequst.AllRequest(
                    "makePosition","","","1",
                    warnHandler,
                    WARNING_GET_MAKEPOSITION_SUCCESS,
                    WARNING_GET_MAKEPOSITION_FALL
            );
        }else
        {
            ManagerRequst.AllRequest(
                    "storePosition","","","1",
                    warnHandler,
                    WARNING_GET_STOREPOSITION_SUCCESS,
                    WARNING_GET_STOREPOSITION_FALL
            );
        }
    }

    private float GetMakeVacancyRate() {
        int flag = 0;
        for (MakePosition position : AllStaticBean.makePositions) {
            if (position.isIdle()) {
                flag++;
            }
        }
        Log.e("makeVacancyRate", flag+"  "+AllStaticBean.makePositions.length+"  "+ (flag*1.0 / AllStaticBean.makePositions.length) * 100);
        return (float)(1.0*flag/AllStaticBean.makePositions.length)*100;
    }
    private float GetStoreVacancyRate() {
        int flag = 0;
        for (StorePositionData position : AllStaticBean.storePositionDatas) {
            if (position.getStatus().equals("空闲")) {
                flag++;
            }
        }
        Log.e("storeVacancyRate", flag+"  "+AllStaticBean.storePositionDatas.length+" "+ (flag*1.0 / AllStaticBean.storePositionDatas.length) * 100);
        return (float) (1.0*flag/AllStaticBean.storePositionDatas.length)*100;
    }

    private void initBarChart(float top,float low,float data1,float data2) {
        loadingDialog.close();
        WarningLinearLayout.setVisibility(View.VISIBLE);
        X_textView.setVisibility(View.VISIBLE);
        ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组
        ArrayList<String> xVals = new ArrayList<>();//X轴数据

        xVals.add("制梁台座");
        xVals.add("存梁台座");
        yVals.add(new BarEntry(data1, 0));
        yVals.add(new BarEntry(data2, 1));
        BarDataSet barDataSet = new BarDataSet(yVals, "空置率");
        barDataSet.setColor(Color.rgb(38, 135, 133));//设置第一组数据颜色


        ArrayList<BarDataSet> threebardata = new ArrayList<>();
        LimitLine yLimitLine1 = new LimitLine(low,"空置率下限");
        yLimitLine1.setLineColor(Color.RED);
        yLimitLine1.setTextColor(Color.RED);
        yLimitLine1.setTextSize(10);
        LimitLine yLimitLine2 = new LimitLine(top,"空置率上限");
        yLimitLine2.setTextSize(10);
        yLimitLine2.setLineColor(Color.RED);
        yLimitLine2.setTextColor(Color.RED);
        // 获得左侧侧坐标轴
        YAxis leftAxis = barchart.getAxisLeft();
        leftAxis.addLimitLine(yLimitLine1);
        leftAxis.addLimitLine(yLimitLine2);
        leftAxis.setAxisMaxValue(100);
        /*BarDataSet 接口很关键，是添加多组数据的关键结构，
        LineChart也是可以采用对应的接口类，也可以添加多组数据*/
        threebardata.add(barDataSet);
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
        barchart.animateXY(1000, 2000);//设置动画
        if (data1 > top || data1 < low) {
            warning1.setVisibility(View.VISIBLE);
            if (normo.getVisibility() == View.VISIBLE) {
                normo.setVisibility(View.GONE);
            }
        }
        if (data2 > top || data2 < low) {
            warning2.setVisibility(View.VISIBLE);
            if (normo.getVisibility() == View.VISIBLE) {
                normo.setVisibility(View.GONE);
            }
        }
        if (data1 < top && data1 > low && data2 < top && data2 > low)
            normo.setVisibility(View.VISIBLE);

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
