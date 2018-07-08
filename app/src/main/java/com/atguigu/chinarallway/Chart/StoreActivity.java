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

import com.atguigu.chinarallway.Activity.LoginActivity;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.StoreChartData;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/12.
 * Title：存梁统计
 */

public class StoreActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.barchart_store)
    BarChart barchart;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.store_content)
    TextView store_content;
    @Bind(R.id.store_layout)
    View store_layout;

    public static final int STORE_SUCCESS = 111;
    public static final int STORE_FALL = 999;
    public static final int CODE_0_FALL = 989;
    private LoadingDialog loadingDialog;

    private List<StoreChartData> SCD = new ArrayList<>();//库存记录


    @SuppressLint("HandlerLeak")
    private Handler smhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STORE_SUCCESS:
                    AllStaticBean.mStoreData = (StoreData[]) msg.obj;
                    try {
                        GetStoreDataChart(AllStaticBean.mStoreData);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    initBarChart();
                    break;
                case STORE_FALL:
                    //暂时无数据
                    Toast.makeText(StoreActivity.this, "查询出错，请核对", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;
                case CODE_0_FALL:
                    Toast.makeText(StoreActivity.this, "当前库存为空", Toast.LENGTH_SHORT).show();
                    loadingDialog.close();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart_store);
        ButterKnife.bind(this);
        store_content.setVisibility(View.INVISIBLE);
        store_layout.setVisibility(View.INVISIBLE);
        loadingDialog = new LoadingDialog(this, "数据加载中...");
        ivBack.setOnClickListener(this);
        tvTitleTable.setText("存梁统计");

        if (AllStaticBean.mStoreData == null) {
            loadingDialog.show();
            ManagerRequst.SaveBeamStatisticsRequest(LoginActivity.userData.getUid(), smhandler);
        }else{
            if (!SCD.isEmpty())  initBarChart();
            else {
                try {
                    GetStoreDataChart(AllStaticBean.mStoreData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                initBarChart();
            }
        }
    }

    public void initBarChart() {
        loadingDialog.close();
        store_content.setVisibility(View.VISIBLE);
        store_layout.setVisibility(View.VISIBLE);
        ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组
        ArrayList<String> xVals = new ArrayList<>();//X轴数据

        Collections.sort(SCD);
        for (int i = 0; i < SCD.size(); i++) {//添加数据源
            xVals.add(SCD.get(i).getXDay() + "天");
            yVals.add(new BarEntry(SCD.get(i).getYAmount(), i));
        }

        BarDataSet barDataSet = new BarDataSet(yVals, "存梁数量");
        barDataSet.setColor(Color.rgb(255, 123, 124));//设置第一组数据颜色

        ArrayList<BarDataSet> threebardata = new ArrayList<>();
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
        barchart.setDescriptionTextSize(20.f);//设置描述字体
        barchart.animateXY(1000, 2000);//设置动画
    }

    private long ChangeStringToDate(String InTime,String OutTime) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(OutTime);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).parse(InTime);
            return (date.getTime() - date2.getTime()) / (3600 * 24 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void GetStoreDataChart(StoreData[] sd) throws ParseException {
        int j;
        int MeanError = 60000;
        if (SCD.isEmpty()) {
            Log.e("Chart", "SCD为空");
            for (int i = 0; i < sd.length; i++) {
                /*判断当前库存是否已经出场*/
                Date SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(sd[i].getOutTime());
                Log.e("Chart",SDF.getTime()-System.currentTimeMillis()+"");

                if ( System.currentTimeMillis()-SDF.getTime() <= MeanError) {

                    //遍历添加新的库存梁板，有相同天数的就叠加，没有就添加;
                    for (j = 0; j < SCD.size(); j++) {
                        if (ChangeStringToDate(sd[i].getInTime(), sd[i].getOutTime())
                                == SCD.get(j).getXDay()
                                ) {
                            SCD.get(j).setYAmount(SCD.get(j).getYAmount() + 1);
                            break;
                        }

                    }
                    if (j == SCD.size()) {
                        StoreChartData mScd = new StoreChartData();
                        mScd.setXDay((int) ChangeStringToDate(sd[i].getInTime(), sd[i].getOutTime()));
                        mScd.setYAmount(1);
                        SCD.add(mScd);
                    }
                }else
                Log.e("Chart", "为通过");
            }
        }
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
