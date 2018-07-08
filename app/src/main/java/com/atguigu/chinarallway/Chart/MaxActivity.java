package com.atguigu.chinarallway.Chart;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.CheckRecData;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/12.
 */

public class MaxActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.barchart_max)
    BarChart barchart;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.bid_side_spinner)
    Spinner BidSideSpinner;
    @Bind(R.id.bid_num_spinner)
    Spinner BidNumSpinner;
    @Bind(R.id.bridge_spinner)
    Spinner BridgeSpinner;
    @Bind(R.id.max_search)
    Button MaxSearch;

    private String BeamRequestContent = "";
    private String[] MaxSpinnerData = null;
    private int MaxDate = 0;
    private float MaxTheory = 0;
    private final int MAXBEAM_SUCCESS = 123;
    private final int MAXCHECK_SUCCESS = 1234;
    private final int MAXBEAM_FALL = 456;
    private final int MAXCHECK_FALL = 4567;

    private List<String> side = new ArrayList<>();
    private List<String> bridgename = new ArrayList<>();
    private List<String> bridgelist = new ArrayList<>();
    private List<String> numlist = new ArrayList<>();
    private List<String> number = new ArrayList<>();

    private String sideChoose ="";

    private ArrayList<String> BeamName = null;
    private ArrayList<String> BeamLorR = null;
    private ArrayList<String> BeamID = null;

    ArrayAdapter<String> Id_Adapter = null;
    ArrayAdapter<String> LorR_Adapter = null;

    private Handler max_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MAXBEAM_SUCCESS:
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.MaxBeamData = new Gson().fromJson(jsonArray.toString(), BeamData[].class);
                    BeamName = new ArrayList<>();
                    BeamName.add("");
                    for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
                        String name = AllStaticBean.MaxBeamData[i - 1].getbName();

                        if (BeamName.size() == 1 || BeamName.indexOf(AllStaticBean.MaxBeamData[i - 1].getbName()) < 0) {
                            BeamName.add(name);
                        }
                    }
                    onBind_BeamName_Spinner();
                    break;
                   /* JSONArray jsonArray = (JSONArray) msg.obj;
                    AllStaticBean.MaxBeamData = new Gson().fromJson(jsonArray.toString(), BeamData[].class);
                    MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length+1];
                    MaxSpinnerData[0] = "";
                    for (int i = 1; i < MaxSpinnerData.length ; i++) {
                        MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i-1].getbName()
                                + "_" + AllStaticBean.MaxBeamData[i-1].getbID();
                        bridgename.add(AllStaticBean.MaxBeamData[i-1].getbName());
                    }
                    onBind_BeamName_Spinner();
                    //BoundSpinner();
                    break;*/
                case MAXBEAM_FALL:
                    //出错显示: 错误原因；
                    Toast.makeText(MaxActivity.this, "获取信息出错，请刷新", Toast.LENGTH_SHORT).show();
                    break;
                case MAXCHECK_SUCCESS:
                    JSONArray Ja = (JSONArray) msg.obj;
                    CheckRecData[] maxCheckRecData = new Gson().fromJson(Ja.toString(), CheckRecData[].class);
                    AllStaticBean.MaxBeamData = new Gson().fromJson(Ja.toString(), BeamData[].class);
                    MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length+1];
                    MaxSpinnerData[0] = "";
                    for (int i = 1; i < MaxSpinnerData.length ; i++) {
                        MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i-1].getbName()
                                + "_" + AllStaticBean.MaxBeamData[i-1].getbID();
                    }
                    for (int i = 1; i < MaxSpinnerData.length ; i++) {
                        if (BeamRequestContent.equals(MaxSpinnerData[i])){
                            MaxDate = AllStaticBean.MaxBeamData[i - 1].getKeep();
                            MaxTheory = AllStaticBean.MaxBeamData[i - 1].getUpWarp().floatValue();
                        }
                    }
                    float maxReality = FindRealData(maxCheckRecData);
                    if (maxReality > 0)
                    initBarChart(MaxDate, maxReality, MaxTheory);
                    else
                        Toast.makeText(MaxActivity.this, "获取的检测数据未统计，请核对检测数据1", Toast.LENGTH_SHORT).show();
                    break;
                case MAXCHECK_FALL:
                    Toast.makeText(MaxActivity.this, "获取的检测数据未统计，请核对检测数据2", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart_max);
        ButterKnife.bind(this);
        initSpinner();
       /* if (MaxSpinnerData!= null) {
            BoundSpinner();
        }*/
        ivBack.setOnClickListener(this);
        MaxSearch.setOnClickListener(this);
        tvTitleTable.setText("上拱值统计");


    }

    private float FindRealData(CheckRecData[] checkRecDatas) {
        for (CheckRecData checkRecData : checkRecDatas) {
            if (checkRecData.getbID().equals(BeamRequestContent.substring(BeamRequestContent.indexOf("_") + 1))) {
                return checkRecData.getUpWard().floatValue();
            }
        }
        return 0;
    }

    private void onBind_BeamName_Spinner() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, BeamName);
        //绑定 Adapter到控件
        BridgeSpinner.setAdapter(mAdapter);
        BridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (BeamLorR == null) {
                    BeamLorR = new ArrayList<>();
                }else {
                    BeamLorR.clear();
                }
                BeamLorR.add("");
                if (BeamID==null){
                    BeamID = new ArrayList<>();
                }else {
                    BeamID.clear();
                }
                BeamID.add("");
                if (Id_Adapter != null) {
                    Id_Adapter.notifyDataSetChanged();
                }
                onBind_LorR_Sprinner(BeamName.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                BeamRequestContent = "";
            }
        });
    }

    //绑定梁板的左右；
    private void onBind_LorR_Sprinner(final String beamName) {
        BeamID.clear();
        BeamID.add("");

        //筛选左右线
        for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName)
                    && BeamLorR.indexOf(temp.getbID().substring(0, 2)) < 0) {
                BeamLorR.add(temp.getbID().substring(0, 2));
            }
        }
        LorR_Adapter = new ArrayAdapter<>(this,R.layout.spinner_item,BeamLorR );
        BidSideSpinner.setAdapter(LorR_Adapter);
        BidSideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!beamName.equals("") && !BeamLorR.get(position).equals("")) {
                    onBind_ID_Sprinner(beamName,BeamLorR.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BeamRequestContent = "";
            }
        });
    }

    //绑定梁板的Id
    private void onBind_ID_Sprinner(final String beamName, final String lr) {
        BeamID.clear();
        BeamID.add("");

        for (int i = 1; i < AllStaticBean.MaxBeamData.length+1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName) && temp.getbID().substring(0, 2).equals(lr)) {
                BeamID.add(temp.getbID().substring(2));
            }
        }

        Id_Adapter = new ArrayAdapter<>(this, R.layout.spinner_item, BeamID);
        BidNumSpinner.setAdapter(Id_Adapter);
        BidNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BeamRequestContent = beamName + "_" + lr + BeamID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BeamRequestContent = "";
            }
        });
    }

/*
    private void BoundSpinner() {
       ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, MaxSpinnerData);
        //绑定 Adapter到控件
        MaxSpinner.setAdapter(mAdapter);
        MaxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BeamRequestContent = MaxSpinnerData[i];
                if (i != 0) {
                    MaxDate = AllStaticBean.MaxBeamData[i - 1].getKeep();
                    MaxTheory = AllStaticBean.MaxBeamData[i - 1].getUpWarp().floatValue();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                BeamRequestContent = "";
            }
        })
        for (String i:bridgename) {
            if (!bridgelist.contains(i)){
                bridgelist.add(i);
            }
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,bridgelist);
        BridgeSpinner.setAdapter(mAdapter);
        BridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = bridgelist.get(position);
                for (int i = 1; i < MaxSpinnerData.length; i++) {
                    String[] param = MaxSpinnerData[i].split("_");
                    if (name.equals(param[0])){
                        numlist.add(param[1]);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> nAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,side);
        BidSideSpinner.setAdapter(nAdapter);
        BidSideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sideChoose = side.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> kAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,numlist);
        BidNumSpinner.setAdapter(kAdapter);
        BidNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
*/

    private void initSpinner() {
        if (AllStaticBean.MaxBeamData == null) {
            ManagerRequst.AllRequest(
                    "beam", "", "", "1",
                    max_handler,
                    MAXBEAM_SUCCESS,
                    MAXBEAM_FALL
            );
        } else {
         /*   MaxSpinnerData = new String[AllStaticBean.MaxBeamData.length+1];
            MaxSpinnerData[0] = "";
            for (int i = 1; i < MaxSpinnerData.length ; i++) {
                MaxSpinnerData[i] = AllStaticBean.MaxBeamData[i-1].getbName()
                        + "_" + AllStaticBean.MaxBeamData[i-1].getbID();
            }*/
            BeamName = new ArrayList<>();
            BeamName.add("");
            for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
                String name = AllStaticBean.MaxBeamData[i - 1].getbName();

                if (BeamName.size() == 1 || BeamName.indexOf(AllStaticBean.MaxBeamData[i - 1].getbName()) < 0) {
                    BeamName.add(name);
                }
            }
            onBind_BeamName_Spinner();
        }
    }

    private void initBarChart(final int Date,final float reality,final float theory ) {
        ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组
        ArrayList<BarEntry> yVals2 = new ArrayList<>();//Y轴方向第二组数组
        ArrayList<String> xVals = new ArrayList<>();//X轴数据
        xVals.add(Date + "天");
        yVals.add(new BarEntry(theory,0));
        yVals2.add(new BarEntry(reality,0));

        BarDataSet barDataSet = new BarDataSet(yVals, "理论起供值");
        barDataSet.setColor(Color.rgb(255, 123, 124));//设置第一组数据颜色

        BarDataSet barDataSet2 = new BarDataSet(yVals2, "实际起供值");
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
            case R.id.max_search:
                if (!BeamRequestContent.equals("")) {
                   String SearchParam = BeamRequestContent.substring(0,BeamRequestContent.indexOf("_"));
                    Log.e("All",BeamRequestContent+"  "+SearchParam);
                    ManagerRequst.AllRequest(
                            "checkRec",
                            "bName",
                            SearchParam,
                            "0",
                            max_handler,
                            MAXCHECK_SUCCESS,
                            MAXCHECK_FALL
                    );
                }
                break;
        }
    }
}
