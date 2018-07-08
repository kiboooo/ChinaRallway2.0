package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.CheckRecData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/20.
 */

public class LabDataActivity extends AppCompatActivity implements View.OnClickListener {


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
    @Bind(R.id.et_produce_name)
    TextView etProduceName;
    @Bind(R.id.et_produce_id)
    TextView etProduceId;
    @Bind(R.id.et_produce_down)
    EditText etProduceDown;
    @Bind(R.id.et_produce_bottom)
    EditText etProduceBottom;
    @Bind(R.id.et_produce_top)
    EditText etProduceTop;
    @Bind(R.id.et_produce_pro)
    EditText etProducePro;
    @Bind(R.id.tv_load_button)
    TextView tvLoadButton;
    //    private String[] LabSpinner;
    private List<String> labItem = new ArrayList<>();
    private String LabSetData = "";
    private List<BeamData> beamDatas = new ArrayList<>();
    private LoadingDialog loadingDialog;
    private ArrayAdapter<String> mAdapter;

    private String BeamRequestContent = "";

    private final int ADDINFO_SUCCESS = 7411;
    private final int ADDINFO_FALL = 7412;

    private ArrayList<String> BeamName = null;
    private ArrayList<String> BeamLorR = null;
    private ArrayList<String> BeamID = null;

    ArrayAdapter<String> Id_Adapter = null;
    ArrayAdapter<String> LorR_Adapter = null;

    private String LorRandID = "";

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADDINFO_SUCCESS:
                    loadingDialog.close();
//                    for (int i = 1; i < LabSpinner.length; i++) {
//                        if (LabSpinner[i].compareTo(etProduceName + "_" + etProduceId) == 0) {
//                            LabSpinner[i] = "";
//                            break;
//                        }
//                    }
                   /* if (!labSearchSpinner.getSelectedItem().toString().equals(""))
                        mAdapter.remove(labSearchSpinner.getSelectedItem().toString());*/
                    Toast.makeText(LabDataActivity.this, "录入成功", Toast.LENGTH_SHORT).show();
                    break;
                case ADDINFO_FALL:
                    loadingDialog.close();
                    Toast.makeText(LabDataActivity.this, "录入失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_data);
        ButterKnife.bind(this);
        tvTitleTable.setText("录入实验数据");
        ivBack.setOnClickListener(this);
        tvLoadButton.setOnClickListener(this);
        SetNotCheckData();
        //BoundSpinner();
    }

    private void SetNotCheckData() {
       /* int i;
        for (BeamData data : AllStaticBean.MaxBeamData) {
            if (AllStaticBean.checkRecDatas != null) {
                for (i = 0; i < AllStaticBean.checkRecDatas.length; i++) {
                    if (data.getbID().equals(AllStaticBean.checkRecDatas[i].getbID())
                            && data.getbName().equals(AllStaticBean.checkRecDatas[i].getbName())) {
                        break;
                    }
                }
                if (i == AllStaticBean.checkRecDatas.length) {
                    Log.e("SetNotCheckData", data.getbName() + "  " + data.getbID());
                    beamDatas.add(data);
                }
            } else
                beamDatas.add(data);
        }
        labItem.add("");
        for (int j = 1; j < beamDatas.size() + 1; j++) {
            labItem.add(beamDatas.get(j - 1).getbName()
                    + "_" + beamDatas.get(j - 1).getbID());
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
                etProduceName.setText(BeamName.get(i));
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
                    LorRandID = BeamLorR.get(position);
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
                etProduceId.setText(LorRandID+BeamID.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BeamRequestContent = "";
            }
        });
    }


    /*private void BoundSpinner() {
        List<String> Itemlist = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, labItem);
        //绑定 Adapter到控件
        labSearchSpinner.setAdapter(mAdapter);
        labSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                LabSetData = LabSpinner[i];
                LabSetData = adapterView.getItemAtPosition(i).toString();
                String[] flags = LabSetData.split("_");
                if (flags.length == 2) {
                    etProduceName.setText(flags[0]);
                    etProduceId.setText(flags[1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                LabSetData = "";
            }
        });
    }*/

    @Override
    public void onClick(View v) {
        loadingDialog = new LoadingDialog(LabDataActivity.this, "数据录入中...");
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_load_button:
                /*将数据插入表*/
                if (etProduceName.length() > 1) {
                    try {
                        loadingDialog.show();
                        CheckRecData checkRecData = new CheckRecData();
                        CheckRecData[] checkRecDatas = new CheckRecData[1];
                        checkRecData.setbID(URLEncoder.encode(etProduceId.getText().toString(), "UTF-8"));
                        checkRecData.setbName(URLEncoder.encode(etProduceName.getText().toString(), "UTF-8"));
                        checkRecData.setProBottom(BigDecimal.valueOf(Double.parseDouble(String.valueOf(etProduceBottom.getText()))));
                        checkRecData.setProTop(BigDecimal.valueOf(Double.parseDouble(String.valueOf(etProduceTop.getText()))));
                        checkRecData.setProDown(BigDecimal.valueOf(Double.parseDouble(String.valueOf(etProduceDown.getText()))));
                        checkRecData.setProScale(BigDecimal.valueOf(Double.parseDouble(String.valueOf(etProducePro.getText()))));
                        checkRecDatas[0] = checkRecData;
                        String content = new Gson().toJson(checkRecDatas);
                        Log.e("luru", content);

                        UpDataRequest.AddInfoRequest("checkRec", content,
                                ADDINFO_SUCCESS, ADDINFO_FALL, mhandler);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        loadingDialog.close();
                        Toast.makeText(LabDataActivity.this, "不允许提交空数据，请核对再提交", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(LabDataActivity.this, "请选择录入数据", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
