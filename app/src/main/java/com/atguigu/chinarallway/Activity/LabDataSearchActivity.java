package com.atguigu.chinarallway.Activity;

import android.os.Bundle;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/20.
 */

public class LabDataSearchActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.bridge_spinner)
    Spinner BridgeSpinner;
    @Bind(R.id.bid_side_spinner)
    Spinner BidSideSpinner;
    @Bind(R.id.bid_num_spinner)
    Spinner BidNumSpinner;
    @Bind(R.id.lab_search)
    Button LabSearch;
    @Bind(R.id.tv_produce_name)
    TextView tvProduceName;
    @Bind(R.id.tv_produce_id)
    TextView tvProduceId;
    @Bind(R.id.tv_produce_down)
    TextView tvProduceDown;
    @Bind(R.id.tv_produce_bottom)
    TextView tvProduceBottom;
    @Bind(R.id.tv_produce_top)
    TextView tvProduceTop;
    @Bind(R.id.tv_produce_pro)
    TextView tvProducePro;
    private String BeamRequestContent = "";
    private String[] LabSpinner;

    private ArrayList<String> checkBeamName = null;
    private ArrayList<String> checkBeamLorR = null;
    private ArrayList<String> checkBeamID = null;

    ArrayAdapter<String> Id_Adapter = null;
    ArrayAdapter<String> LorR_Adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_search);
        ButterKnife.bind(this);
       /* LabSpinner = new String[AllStaticBean.checkRecDatas.length + 1];
        LabSpinner[0] = "";
        for (int i = 1; i < LabSpinner.length; i++) {
            LabSpinner[i] = AllStaticBean.checkRecDatas[i - 1].getbName()
                    + "_" + AllStaticBean.checkRecDatas[i - 1].getbID();
        }*/
        checkBeamName = new ArrayList<>();
        checkBeamName.add("");
        for (int i = 1; i < AllStaticBean.checkRecDatas.length + 1; i++) {
            String name = AllStaticBean.checkRecDatas[i - 1].getbName();

            if (checkBeamName.size() == 1 || checkBeamName.indexOf(AllStaticBean.checkRecDatas[i - 1].getbName()) < 0) {
                checkBeamName.add(name);
            }
        }
        onBind_BeamName_Spinner();
        //BoundSpinner();
        ivBack.setOnClickListener(this);
        LabSearch.setOnClickListener(this);
        tvTitleTable.setText("实验任务数据");
    }

    private void onBind_BeamName_Spinner() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, checkBeamName);
        //绑定 Adapter到控件
        BridgeSpinner.setAdapter(mAdapter);
        BridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkBeamLorR == null) {
                    checkBeamLorR = new ArrayList<>();
                } else {
                    checkBeamLorR.clear();
                }
                checkBeamLorR.add("");
                if (checkBeamID == null) {
                    checkBeamID = new ArrayList<>();
                } else {
                    checkBeamID.clear();
                }
                checkBeamID.add("");
                if (Id_Adapter != null) {
                    Id_Adapter.notifyDataSetChanged();
                }
                onBind_LorR_Sprinner(checkBeamName.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                BeamRequestContent = "";
            }
        });
    }

    //绑定梁板的左右；
    private void onBind_LorR_Sprinner(final String beamName) {
        checkBeamID.clear();
        checkBeamID.add("");

        //筛选左右线
        for (int i = 1; i < AllStaticBean.checkRecDatas.length + 1; i++) {
            CheckRecData temp = AllStaticBean.checkRecDatas[i - 1];
            if (temp.getbName().equals(beamName)
                    && checkBeamLorR.indexOf(temp.getbID().substring(0, 2)) < 0) {
                checkBeamLorR.add(temp.getbID().substring(0, 2));
            }
        }
        LorR_Adapter = new ArrayAdapter<>(this,R.layout.spinner_item,checkBeamLorR );
        BidSideSpinner.setAdapter(LorR_Adapter);
        BidSideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!beamName.equals("") && !checkBeamLorR.get(position).equals("")) {
                    onBind_ID_Sprinner(beamName,checkBeamLorR.get(position));
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
        checkBeamID.clear();
        checkBeamID.add("");

        for (int i = 1; i < AllStaticBean.checkRecDatas.length+1; i++) {
            CheckRecData temp = AllStaticBean.checkRecDatas[i - 1];
            if (temp.getbName().equals(beamName) && temp.getbID().substring(0, 2).equals(lr)) {
                checkBeamID.add(temp.getbID().substring(2));
            }
        }

        Id_Adapter = new ArrayAdapter<>(this, R.layout.spinner_item, checkBeamID);
        BidNumSpinner.setAdapter(Id_Adapter);
        BidNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BeamRequestContent = beamName + "_" + lr + checkBeamID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BeamRequestContent = "";
            }
        });
    }

   /* private void BoundSpinner() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, LabSpinner);
        //绑定 Adapter到控件
        labSearchSpinner.setAdapter(mAdapter);
        labSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BeamRequestContent = LabSpinner[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                BeamRequestContent = "";
            }
        });
    }*/

    private CheckRecData SearchCRD() {
        String[] flags = BeamRequestContent.split("_");
        Log.e("SearchCRD", flags[0] + "   " + flags[1]);
        if (flags.length == 2) {
            for (CheckRecData recData : AllStaticBean.checkRecDatas) {
                if (recData.getbName().equals(flags[0]) && recData.getbID().equals(flags[1])) {
                    return recData;
                }
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.lab_search:
                if (BeamRequestContent.equals("")) {
                    Toast.makeText(LabDataSearchActivity.this, "查询梁板不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    CheckRecData checkRecData = SearchCRD();
                    if (checkRecData != null) {
                        tvProduceName.setText(checkRecData.getbName());
                        tvProduceId.setText(checkRecData.getbID());
                        tvProduceBottom.setText(String.valueOf(checkRecData.getProBottom()));
                        tvProduceDown.setText(String.valueOf(checkRecData.getProDown()));
                        tvProduceTop.setText(String.valueOf(checkRecData.getProTop()));
                        tvProducePro.setText(String.valueOf(checkRecData.getProScale()));
                    } else
                        Toast.makeText(LabDataSearchActivity.this, "查询出错！", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }
}
