package com.atguigu.chinarallway.Chart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.MakePosition;
import com.atguigu.chinarallway.Bean.StorePositionData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/12.
 * Titles：台位占用统计
 */

public class StorPositionActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.piechart)
    PieChart mChart;
    @Bind(R.id.piechart2)
    PieChart nChart;
    @Bind(R.id.store1)
    View Stroe1;
    @Bind(R.id.store2)
    View Stroe2;


    private final int SP_MADE_SUCCESS = 33;
    private final int SP_MADE_FALL = 22;
    private final int SP_STORE_SUCCESS = 44;
    private final int SP_STORE_FALL = 55;

    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler spHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SP_MADE_SUCCESS:
                    JSONArray makePosition = (JSONArray) msg.obj;
                    AllStaticBean.makePositions = new Gson().fromJson(makePosition.toString(), MakePosition[].class);
                    initBarChartData(2);
                    break;
                case SP_MADE_FALL:
                    loadingDialog.close();
                    Toast.makeText(StorPositionActivity.this, "请求制梁台座表出错", Toast.LENGTH_SHORT).show();
                    break;
                case SP_STORE_SUCCESS:
                    JSONArray storePosition = (JSONArray) msg.obj;
                    AllStaticBean.storePositionDatas = new Gson().fromJson(storePosition.toString(), StorePositionData[].class);
                    int flag1[] = FindScale(1);
                    PieData mPieData = getPieData(flag1[0],flag1[1],flag1[2]);
                    int flag2[] = FindScale(2);
                    PieData nPieData = getPieData(flag2[0],flag2[1],flag2[2]);
                    loadingDialog.close();
                    Stroe1.setVisibility(View.VISIBLE);
                    Stroe2.setVisibility(View.VISIBLE);
                    showChart2(nChart,nPieData);
                    showChart1(mChart,mPieData);
                    break;
                case SP_STORE_FALL:
                    loadingDialog.close();
                    Toast.makeText(StorPositionActivity.this, "请求存梁台座表出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart_storepositon);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this, "数据加载中..");
        Stroe1.setVisibility(View.INVISIBLE);
        Stroe2.setVisibility(View.INVISIBLE);
        if (AllStaticBean.makePositions != null
                && AllStaticBean.storePositionDatas != null) {
            int flag1[] = FindScale(1);
            PieData mPieData = getPieData(flag1[0],flag1[1],flag1[2]);
            int flag2[] = FindScale(2);
            PieData nPieData = getPieData(flag2[0],flag2[1],flag2[2]);
            loadingDialog.close();
            Stroe1.setVisibility(View.VISIBLE);
            Stroe2.setVisibility(View.VISIBLE);
            showChart2(nChart,nPieData);
            showChart1(mChart,mPieData);
        }else
            initBarChartData(1);
        nChart.setCenterText("存梁台位统计");
        mChart.setCenterText("制梁台位统计");
        tvTitleTable.setText("台位占用统计");
        ivBack.setOnClickListener(this);
    }

    private int[] FindScale(int i) {
        int ChartData[] = new int[3];/* { 使用中，空闲，废弃 }*/
        if (i == 1) {
            for (MakePosition position : AllStaticBean.makePositions) {
                if (position.isIdle()) {
                    ChartData[1]++;
                }else
                    ChartData[0]++;
            }
            ChartData[2] = AllStaticBean.makePositions.length
                    - ChartData[0] - ChartData[1];
        } else if (i == 2) {
            for (StorePositionData positionData : AllStaticBean.storePositionDatas) {
                if (positionData.getStatus().equals("空闲")) {
                    ChartData[1]++;
                } else if (positionData.getStatus().equals("废弃")) {
                    ChartData[2]++;
                }
                ChartData[0] = AllStaticBean.storePositionDatas.length
                        - ChartData[2] - ChartData[1];
            }
        }
        return ChartData;
    }

    private void initBarChartData(int i) {
        loadingDialog.show();
        if (i == 1) {
            ManagerRequst.AllRequest(
                    "makePosition","","","1",
                    spHandler,
                    SP_MADE_SUCCESS,
                    SP_MADE_FALL
            );
        }else
        {
            ManagerRequst.AllRequest(
                    "storePosition","","","1",
                    spHandler,
                    SP_STORE_SUCCESS,
                    SP_STORE_FALL
            );
        }
    }

    private void showChart1(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(68f); // 半透明圈
        pieChart.setDescription("");
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setTransparentCircleRadius(10f);
        pieData.setValueTextSize(15);


        //设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

//        pieChart.animateY(8000, Easing.EasingOption.EaseOutBounce);//设置动画
        pieChart.animateXY(1000, 1000);  //设置动画
    }

    private void showChart2(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription("");

        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setTransparentCircleRadius(0f);
        pieData.setValueTextSize(15);

        //设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

//        pieChart.animateY(8000, Easing.EasingOption.EaseOutBounce);  //设置动画
        pieChart.animateXY(1000, 1000);  //设置动画
        pieChart.invalidate();
        // mChart.spin(2000, 0, 360);
    }

    private PieData getPieData(int machine_using,int machine_free,int machine_abandon) {

        ArrayList<String> xValues = new ArrayList<>();  //xVals用来表示每个饼块上的内容


        xValues.add("空闲" + "(" + machine_free + ")");  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        xValues.add("使用中" + "(" + machine_using + ")");
        xValues.add("废弃" + "(" + machine_abandon + ")");


        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        float quarterly1 = (float) ((float) machine_free*1.0/(machine_abandon+machine_free+machine_using)*100);
        float quarterly2 = (float) ((float) machine_using*1.0/(machine_abandon+machine_free+machine_using)*100);
        float quarterly3 = (float) ((float) machine_abandon*1.0/(machine_abandon+machine_free+machine_using)*100);

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(2f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 3 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        return new PieData(xValues, pieDataSet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
