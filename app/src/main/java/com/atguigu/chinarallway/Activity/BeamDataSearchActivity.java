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
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/25.
 * Function: 查询梁板信息
 */

public class BeamDataSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BEAM";
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.beam_search_spinner)
    Spinner beamSearchSpinner;
    @Bind(R.id.beam_search_spinner_LorR)
    Spinner beamSearchSpinnerLorR;
    @Bind(R.id.beam_search_spinner_ID)
    Spinner beamSearchSpinnerID;
    @Bind(R.id.btn_beam_search)
    Button btnBeamSearch;
    @Bind(R.id.beam_search)
    LinearLayout beamSearch;
    @Bind(R.id.find_text)
    TextView findText;
    @Bind(R.id.find_button)
    Button findButton;
    @Bind(R.id.find)
    LinearLayout find;
    @Bind(R.id.tv_beam_name)
    TextView tvBeamName;
    @Bind(R.id.tv_beam_id)
    TextView tvBeamId;
    @Bind(R.id.tv_beam_size)
    TextView tvBeamSize;
    @Bind(R.id.tv_beam_slope)
    TextView tvBeamSlope;
    @Bind(R.id.tv_beam_concrete)
    TextView tvBeamConcrete;
    @Bind(R.id.tv_beam_steel)
    TextView tvBeamSteel;
    @Bind(R.id.tv_beam_intensity)
    TextView tvBeamIntensity;
    @Bind(R.id.tv_beam_len)
    TextView tvBeamLen;
    @Bind(R.id.tv_beam_height)
    TextView tvBeamHeight;
    @Bind(R.id.tv_beam_lsl)
    TextView tvBeamLsl;
    @Bind(R.id.tv_beam_lsr)
    TextView tvBeamLsr;
    @Bind(R.id.tv_beam_lbl)
    TextView tvBeamLbl;
    @Bind(R.id.tv_beam_lbr)
    TextView tvBeamLbr;
    @Bind(R.id.tv_beam_rsl)
    TextView tvBeamRsl;
    @Bind(R.id.tv_beam_rsr)
    TextView tvBeamRsr;
    @Bind(R.id.tv_beam_rbl)
    TextView tvBeamRbl;
    @Bind(R.id.tv_beam_rbr)
    TextView tvBeamRbr;
    @Bind(R.id.tv_beam_lls)
    TextView tvBeamLls;
    @Bind(R.id.tv_beam_llb)
    TextView tvBeamLlb;
    @Bind(R.id.tv_beam_lld)
    TextView tvBeamLld;
    @Bind(R.id.tv_beam_rls)
    TextView tvBeamRls;
    @Bind(R.id.tv_beam_rlb)
    TextView tvBeamRlb;
    @Bind(R.id.tv_beam_rld)
    TextView tvBeamRld;
    @Bind(R.id.tv_beam_endSize)
    TextView tvBeamEndSize;
    @Bind(R.id.tv_beam_down)
    TextView tvBeamDown;
    @Bind(R.id.tv_beam_top)
    TextView tvBeamTop;
    @Bind(R.id.tv_beam_bottom)
    TextView tvBeamBottom;
    @Bind(R.id.tv_beam_proDown)
    TextView tvBeamProDown;
    @Bind(R.id.tv_beam_proTop)
    TextView tvBeamProTop;
    @Bind(R.id.tv_beam_proBottom)
    TextView tvBeamProBottom;
    @Bind(R.id.tv_beam_scale)
    TextView tvBeamScale;
    @Bind(R.id.tv_beam_hoisting)
    TextView tvBeamHoisting;
    @Bind(R.id.tv_beam_guardRail)
    TextView tvBeamGuardRail;
    @Bind(R.id.tv_beam_air)
    TextView tvBeamAir;
    @Bind(R.id.tv_beam_keep)
    TextView tvBeamKeep;
    @Bind(R.id.tv_beam_upWarp)
    TextView tvBeamUpWarp;
    @Bind(R.id.tv_beam_status)
    TextView tvBeamStatus;
    @Bind(R.id.tv_beam_casts)
    TextView tvBeamCasts;
    @Bind(R.id.tv_beam_stretch)
    TextView tvBeamStretch;
    @Bind(R.id.tv_beam_grouting)
    TextView tvBeamGrouting;


    private String BeamRequestContent = "";
    private ArrayList<String> BeamName = null;
    private ArrayList<String> BeamLorR = null;
    private ArrayList<String> BeamID = null;

    ArrayAdapter<String> Id_Adapter = null;
    ArrayAdapter<String> LorR_Adapter = null;

    private final int MAXBEAM_SUCCESS = 1234;
    private final int MAXCHECK_SUCCESS = 12345;
    private final int FIND_SUCCESS = 92345;
    private final int FIND_FALL = 92349;
    private final int MAXBEAM_FALL = 4567;
    private final int MAXCHECK_FALL = 45678;


    @SuppressLint("HandlerLeak")
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
                case MAXBEAM_FALL:
                    //出错显示: 错误原因；
                    Toast.makeText(BeamDataSearchActivity.this, "获取信息出错，请刷新", Toast.LENGTH_SHORT).show();
                    break;
                case MAXCHECK_SUCCESS:

                    break;
                case MAXCHECK_FALL:
                    Toast.makeText(BeamDataSearchActivity.this, "获取信息出错，请确认查询请求", Toast.LENGTH_SHORT).show();
                    break;

                case FIND_SUCCESS:
                    String CurrentTime = AllStaticBean.formatter_accuracy.format(new Date(System.currentTimeMillis()));
                    findText.setText("定位信息：" + CurrentTime);
                    Toast.makeText(BeamDataSearchActivity.this, "定位成功", Toast.LENGTH_SHORT).show();
                    break;

                case FIND_FALL:
                    Toast.makeText(BeamDataSearchActivity.this, "定位失败，该梁板不符合定位条件", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam_search);
        ButterKnife.bind(this);
        initSpinner();
        if (BeamName != null) {

            onBind_BeamName_Spinner();
        }
        ivBack.setOnClickListener(this);
        tvTitleTable.setText("查询梁板信息");
        btnBeamSearch.setOnClickListener(this);
        findButton.setOnClickListener(this);
    }

    private void initSpinner() {
        if (AllStaticBean.MaxBeamData == null) {
            ManagerRequst.AllRequest(
                    "beam", "", "", "1",
                    max_handler,
                    MAXBEAM_SUCCESS,
                    MAXBEAM_FALL
            );
        } else {

            BeamName = new ArrayList<>();
            BeamName.add("");
            for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
                String name = AllStaticBean.MaxBeamData[i - 1].getbName();
                if (BeamName.size() == 1 || BeamName.indexOf(AllStaticBean.MaxBeamData[i - 1].getbName()) < 0) {
                    BeamName.add(name);
                }
            }
        }
    }

    private void SetBeamContent() {
        if (AllStaticBean.MaxBeamData != null) {
            BeamData BD = SearchNeedBeam();
            if (BD != null) {
                tvBeamName.setText(BD.getbName());
                tvBeamId.setText(BD.getbID());
                tvBeamSize.setText(BD.getSize());
                tvBeamSlope.setText(String.valueOf(BD.getSlope()));
                tvBeamConcrete.setText(String.valueOf(BD.getConcrete()));
                tvBeamSteel.setText(String.valueOf(BD.getSteel()));
                tvBeamIntensity.setText(String.valueOf(BD.getIntensity()));
                tvBeamLen.setText(String.valueOf(BD.getLen()));
                tvBeamHeight.setText(String.valueOf(BD.getHeight()));
                tvBeamLsl.setText(String.valueOf(BD.getLsl()));
                tvBeamLsr.setText(String.valueOf(BD.getLsr()));
                tvBeamLbl.setText(String.valueOf(BD.getLbl()));
                tvBeamLbr.setText(String.valueOf(BD.getLbr()));
                tvBeamRsl.setText(String.valueOf(BD.getRsl()));
                tvBeamRsr.setText(String.valueOf(BD.getRsr()));
                tvBeamRbl.setText(String.valueOf(BD.getRbl()));
                tvBeamRbr.setText(String.valueOf(BD.getRbr()));
                tvBeamLls.setText(String.valueOf(BD.getLls()));
                tvBeamLlb.setText(String.valueOf(BD.getLlb()));
                tvBeamLld.setText(String.valueOf(BD.getLld()));
                tvBeamRls.setText(String.valueOf(BD.getRls()));
                tvBeamRlb.setText(String.valueOf(BD.getRlb()));
                tvBeamRld.setText(String.valueOf(BD.getRld()));
                tvBeamEndSize.setText(String.valueOf(BD.getEndSize()));
                tvBeamDown.setText(String.valueOf(BD.getDown()));
                tvBeamTop.setText(String.valueOf(BD.getTop()));
                tvBeamBottom.setText(String.valueOf(BD.getBottom()));
                tvBeamProDown.setText(String.valueOf(BD.getProDown()));
                tvBeamProTop.setText(String.valueOf(BD.getProTop()));
                tvBeamProBottom.setText(String.valueOf(BD.getProBottom()));
                tvBeamScale.setText(String.valueOf(BD.getScale()));
                tvBeamHoisting.setText(String.valueOf(BD.getHoisting()));
                tvBeamGuardRail.setText(String.valueOf(BD.getGuardRail()));
                tvBeamAir.setText(String.valueOf(BD.getAir()));
                tvBeamKeep.setText(String.valueOf(BD.getKeep()));
                tvBeamUpWarp.setText(String.valueOf(BD.getUpWarp()));
                tvBeamStatus.setText(String.valueOf(BD.getStatus()));
                tvBeamCasts.setText(String.valueOf(BD.getCasts()));
                tvBeamStretch.setText(String.valueOf(BD.getStretch()));
                tvBeamGrouting.setText(String.valueOf(BD.getGrouting()));
                if (BD.getFind() == null) {
                    findText.setText("定位信息：未定位");
                } else {
                    Log.e("timer", BD.getFind().toString());
                    findText.setText("定位信息：" + AllStaticBean.formatter_accuracy.format(BD.getFind()));
                }
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_beam_search:
                if (!BeamRequestContent.equals("")) {
                    String SearchParam = BeamRequestContent.substring(0, BeamRequestContent.indexOf("_"));
                    Log.e("All", BeamRequestContent + "  " + SearchParam);
                    ManagerRequst.AllRequest(
                            "beam",
                            "bName",
                            SearchParam,
                            "0",
                            max_handler,
                            MAXCHECK_SUCCESS,
                            MAXCHECK_FALL
                    );
                }
                SetBeamContent();
                break;

            case R.id.find_button:
                if (!BeamRequestContent.equals("")) {
                    BeamData bd = SearchNeedBeam();
                    if (!(bd == null || bd.getStatus().equals("")
                            || bd.getStatus().equals("未预制")
                            || bd.getStatus().equals("已出场"))) {

                        String[] NameAndID = BeamRequestContent.split("_");
                        UpDataRequest.ChangeBeamFind(NameAndID[0], NameAndID[1], max_handler, FIND_SUCCESS, FIND_FALL);

                    } else
                        Toast.makeText(BeamDataSearchActivity.this, "该梁板状态不允许定位", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(BeamDataSearchActivity.this, "不允许定位空梁板", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    private void BoundSpinner() {
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, MaxSpinnerData);
//        //绑定 Adapter到控件
//        beamSearchSpinner.setAdapter(mAdapter);
//        beamSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                BeamRequestContent = MaxSpinnerData[i];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                BeamRequestContent = "";
//            }
//        });
//    }

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
                } else {
                    BeamLorR.clear();
                }
                BeamLorR.add("");
                if (BeamID == null) {
                    BeamID = new ArrayList<>();
                } else {
                    BeamID.clear();
                }
                BeamID.add("");
                if (Id_Adapter != null) {
                    //通知更新IdAdapert
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

        //筛选左右线
        for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName)
                    && BeamLorR.indexOf(temp.getbID().substring(0, 2)) < 0) {
                BeamLorR.add(temp.getbID().substring(0, 2));
            }
        }
        LorR_Adapter = new ArrayAdapter<>(this, R.layout.spinner_item, BeamLorR);
        beamSearchSpinnerLorR.setAdapter(LorR_Adapter);
        beamSearchSpinnerLorR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!beamName.equals("") && !BeamLorR.get(position).equals("")) {
                    onBind_ID_Sprinner(beamName, BeamLorR.get(position));
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

        for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
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
}