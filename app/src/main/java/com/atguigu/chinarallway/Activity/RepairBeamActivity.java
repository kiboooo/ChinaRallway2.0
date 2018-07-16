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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Bean.ModifyData;
import com.atguigu.chinarallway.Bean.StoreData;
import com.atguigu.chinarallway.Bean.StorePositionData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/26.
 * Function:维护梁板状态
 */

public class RepairBeamActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.beam_search_spinner)
    Spinner beamSearchSpinner;
    @Bind(R.id.btn_beam_search)
    Button btnBeamSearch;
    @Bind(R.id.beam_search)
    LinearLayout beamSearch;
    @Bind(R.id.beam_buff)
    TextView beamBuff;
    @Bind(R.id.beam_enter)
    Button beamEnter;
    @Bind(R.id.beam_leave)
    Button beamLeave;
    @Bind(R.id.beam_search_spinner_LorR)
    Spinner beamSearchSpinnerLorR;
    @Bind(R.id.beam_search_spinner_ID)
    Spinner beamSearchSpinnerID;
    @Bind(R.id.beam_state_spinner)
    Spinner beamStateSpinner;
    @Bind(R.id.state_submit)
    Button btnStateSubmit;
    @Bind(R.id.storeBeam_spinner)
    Spinner beamStoreBeam;
    @Bind(R.id.storeBeamPosition_spinner)
    Spinner beamStoreBeamPosition;
    @Bind(R.id.store_submit)
    Button btnStoreSubmit;

    private List<String> beamStateChange = new ArrayList<>();


    private String BeamRequestContent = "";
    private String BeamStateChange="";
    private short PedId;
    private String StorePosition = "";

    private ArrayList<String> BeamName = null;
    private ArrayList<String> BeamLorR = null;
    private ArrayList<String> BeamID = null;
    private ArrayList<Short> PedIdList = null;
    private ArrayList<String> PosList = null;

    ArrayAdapter<String> Id_Adapter = null;
    ArrayAdapter<String> LorR_Adapter = null;
    ArrayAdapter<String> State_Adapter = null;
    ArrayAdapter<Short>  PedId_Adapter = null;
    ArrayAdapter<String> Pos_Adapter = null;

    private final int BEAM_SUCCESS = 12345;
    private final int BEAM_FALL = 45678;
    private final int changeBeamStatus_SUCCESS = 7001;
    private final int changeBeamStatus_FALL = 7077;
    private final int STORE_POSITION_SUCCESS = 1001;
    private final int STORE_POSITION_FALL = 10002;


    private LoadingDialog loadingDialog;


    @SuppressLint("HandlerLeak")
    private Handler max_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BEAM_SUCCESS:
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
                case BEAM_FALL:
                    Toast.makeText(RepairBeamActivity.this, "梁板拉取失败，请退出重试", Toast.LENGTH_SHORT).show();
                    break;

                case changeBeamStatus_SUCCESS:
                    loadingDialog.close();
                    Toast.makeText(RepairBeamActivity.this, "梁板状态修改成功，再次进入维护梁板时生效", Toast.LENGTH_SHORT).show();
                    break;

                case changeBeamStatus_FALL:
                    loadingDialog.close();
                    Toast.makeText(RepairBeamActivity.this, "梁板状态修改失败，请核对重试", Toast.LENGTH_SHORT).show();
                    break;

                case STORE_POSITION_SUCCESS:
                    JSONArray jsonArrayStorePosition = (JSONArray) msg.obj;
                    AllStaticBean.storePositionDatas = new Gson().fromJson(jsonArrayStorePosition.toString(), StorePositionData[].class);
                    PedIdList = new ArrayList<>();
                    PosList = new ArrayList<>();
                    //PedIdList.add(Short.valueOf(""));
                    PosList.add("");
                    for (int i = 1; i < AllStaticBean.storePositionDatas.length + 1 ; i++) {
                        short pedId = AllStaticBean.storePositionDatas[i-1].getPedID();
                        String pos = AllStaticBean.storePositionDatas[i-1].getPos();

                        if (PedIdList.size() == 1 || PedIdList.indexOf(AllStaticBean.storePositionDatas[i - 1].getPedID()) < 0) {
                            PedIdList.add(pedId);
                        }
                        if (PosList.size() == 1 || PosList.indexOf(AllStaticBean.storePositionDatas[i - 1].getPos()) < 0) {
                            PosList.add(pos);
                        }

                    }
                    onBind_PedID_Spinner();
                    onBind_Pos_Spinner();
                    break;
                case STORE_POSITION_FALL:
                    Toast.makeText(RepairBeamActivity.this, "存梁台座信息拉取失败，请退出重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam_repair);
        ButterKnife.bind(this);
        initSpinner();
        getStorePosition();
        ivBack.setOnClickListener(this);
        tvTitleTable.setText("维护梁板状态");
        beamEnter.setOnClickListener(this);
        beamLeave.setOnClickListener(this);
        btnBeamSearch.setOnClickListener(this);
        btnStateSubmit.setOnClickListener(this);
        btnStoreSubmit.setOnClickListener(this);
        beamStateChange.add("");
        beamStateChange.add("未预制");
        beamStateChange.add("已预制");
        beamStateChange.add("已入库");
        beamStateChange.add("已出库");
        onBind_BeamState_Change_Spinner();
    }

    private void initSpinner() {
        ManagerRequst.AllRequest(
                "beam", "", "", "1",
                max_handler,
                BEAM_SUCCESS,
                BEAM_FALL
        );
    }

    private void getStorePosition(){
        ManagerRequst.AllRequest("storePosition","","","1",
                max_handler,
                STORE_POSITION_SUCCESS,
                STORE_POSITION_FALL);
    }

    @Override
    public void onClick(View v) {
        String Buff = beamBuff.getText().toString();
        loadingDialog = new LoadingDialog(RepairBeamActivity.this, "数据上传中...");
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_beam_search:
                if (!BeamRequestContent.equals("")) {
                    SetBeamContent();
                }
                break;

            case R.id.beam_enter:
                if (Buff.equals("制作中")) {
                    /*提交梁板入库状态*/
                    loadingDialog.show();
                    BeamData beamData = SearchNeedBeam();
                    if (beamData != null) {
                        UpDataRequest.ChangeBeamStatusRequest("已入库",
                                beamData.getbID(), beamData.getbName(),
                                changeBeamStatus_SUCCESS, changeBeamStatus_FALL,
                                max_handler);
                    }
                } else {
                    Toast.makeText(RepairBeamActivity.this,
                            "当前梁板状态不允许进行此操作！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.beam_leave:
                if (Buff.equals("已入场")) {
                    /*提交梁板出库状态*/
                    loadingDialog.show();
                    BeamData beamData = SearchNeedBeam();
                    if (beamData != null) {
                        UpDataRequest.ChangeBeamStatusRequest("已出库",
                                beamData.getbID(), beamData.getbName(),
                                changeBeamStatus_SUCCESS, changeBeamStatus_FALL,
                                max_handler);
                    }
                } else {
                    Toast.makeText(RepairBeamActivity.this,
                            "当前梁板状态不允许进行此操作！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.state_submit:
                BeamData beamData = SearchNeedBeam();
                if (beamData != null){
                    /*UpDataRequest.ChangeBeamStatusRequest(BeamStateChange,beamData.getbID(),beamData.getbName(),
                            changeBeamStatus_SUCCESS,changeBeamStatus_FALL,max_handler);*/
                    try {
                        ModifyData[] pk = new ModifyData[]{
                                new ModifyData("bName", URLEncoder.encode(beamData.getbName(), "UTF-8")),
                                new ModifyData("bID", URLEncoder.encode(beamData.getbID(), "UTF-8"))
                        };

                        ModifyData[]  modifyData = new ModifyData[]{
                                new ModifyData("status",URLEncoder.encode(BeamStateChange,"UTF-8"))
                        };

                        UpDataRequest.ModifyDataRequest("beam",pk,modifyData,changeBeamStatus_SUCCESS,changeBeamStatus_FALL,max_handler);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    beamBuff.setText(BeamStateChange);
                }else {
                    Toast.makeText(RepairBeamActivity.this,
                            "当前梁板状态不允许进行此操作！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.store_submit:
                BeamData beamData1 = SearchNeedBeam();
                if (beamData1 != null){
                    try {
                        ModifyData[] pk = new ModifyData[]{
                                new ModifyData("bName", URLEncoder.encode(beamData1.getbName(), "UTF-8")),
                                new ModifyData("bID", URLEncoder.encode(beamData1.getbID(), "UTF-8"))
                        };

                        ModifyData[]  modifyData = new ModifyData[]{
                                new ModifyData("pedID",URLEncoder.encode(PedId+"","UTF-8")),
                                new ModifyData("pos",URLEncoder.encode(StorePosition,"UTF-8"))
                        };

                        UpDataRequest.ModifyDataRequest("store",pk,modifyData,changeBeamStatus_SUCCESS,changeBeamStatus_FALL,max_handler);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(RepairBeamActivity.this,
                            "当前梁板状态不允许进行此操作！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void SetBeamContent() {
        if (AllStaticBean.MaxBeamData != null) {
            BeamData BD = SearchNeedBeam();
            if (BD != null) {
                beamBuff.setText(BD.getStatus());
            } else
                Log.e("SetBeamContent", "BD为null");
        }
    }

    private BeamData SearchNeedBeam() {
        BeamData data = null;
        String[] param = BeamRequestContent.split("_");
        if (param.length < 2)
            return null;
        for (BeamData beamData : AllStaticBean.MaxBeamData) {
            if (beamData.getbName().equals(param[0]) && beamData.getbID().equals(param[1])) {
                data = beamData;
                break;
            }
        }
        return data;
    }

    //桥名的下拉框
    private void onBind_BeamName_Spinner() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, BeamName);
        //绑定 Adapter到控件
        beamSearchSpinner.setAdapter(mAdapter);
        beamSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    //通知更新IdAdapert
                    Id_Adapter.notifyDataSetChanged();
                }
                onBind_LorR_Spinner(BeamName.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                BeamRequestContent = "";
            }
        });
    }

    //绑定梁板的左右；
    private void onBind_LorR_Spinner(final String beamName) {

        //筛选左右线
        for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName)
                    && BeamLorR.indexOf(temp.getbID().substring(0, 2)) < 0) {
                BeamLorR.add(temp.getbID().substring(0, 2));
            }
        }
        LorR_Adapter = new ArrayAdapter<>(this,R.layout.spinner_item,BeamLorR );
        beamSearchSpinnerLorR.setAdapter(LorR_Adapter);
        beamSearchSpinnerLorR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!beamName.equals("") && !BeamLorR.get(position).equals("")) {
                    onBind_ID_Spinner(beamName,BeamLorR.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BeamRequestContent = "";
            }
        });
    }

    //绑定梁板的Id
    private void onBind_ID_Spinner(final String beamName, final String lr) {
        BeamID.clear();
        BeamID.add("");

        for (int i = 1; i < AllStaticBean.MaxBeamData.length+1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName) && temp.getbID().substring(0, 2).equals(lr)) {
                BeamID.add(temp.getbID().substring(2));
            }
        }
        Id_Adapter = new ArrayAdapter<>(this, R.layout.spinner_item, BeamID);
        beamSearchSpinnerID.setAdapter(Id_Adapter);
        beamSearchSpinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void onBind_BeamState_Change_Spinner(){
        State_Adapter = new ArrayAdapter<>(this,R.layout.spinner_item,beamStateChange);
        beamStateSpinner.setAdapter(State_Adapter);
        beamStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newState = beamStateChange.get(position);
                Log.e("beamStateChange",newState);
                if (!newState.equals("")){
                    BeamStateChange = newState;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onBind_PedID_Spinner(){
        PedId_Adapter = new ArrayAdapter<>(this,R.layout.spinner_item,PedIdList);
        beamStoreBeam.setAdapter(PedId_Adapter);
        beamStoreBeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                short newPesId = PedIdList.get(position);
                if (newPesId != 0){
                    PedId = newPesId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onBind_Pos_Spinner(){
        Pos_Adapter = new ArrayAdapter<>(this,R.layout.spinner_item,PosList);
        beamStoreBeamPosition.setAdapter(Pos_Adapter);
        beamStoreBeamPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newPosition = PosList.get(position);
                if (!newPosition.equals("")){
                    StorePosition = newPosition;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
