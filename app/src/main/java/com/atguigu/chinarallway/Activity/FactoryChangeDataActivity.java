package com.atguigu.chinarallway.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.FactoryData;
import com.atguigu.chinarallway.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kiboooo on 2017/9/28.
 */

public class FactoryChangeDataActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.TVfactory_name)
    TextView TVfactoryName;
    @Bind(R.id.TVfactory_project)
    TextView TVfactoryProject;
    @Bind(R.id.TVfactory_segment)
    TextView TVfactorySegment;
    @Bind(R.id.TVfactory_code)
    TextView TVfactoryCode;
    @Bind(R.id.TVfactory_manager)
    TextView TVfactoryManager;
    @Bind(R.id.TVfactory_phone)
    TextView TVfactoryPhone;
    @Bind(R.id.TVfactory_title)
    TextView TVfactoryTitle;
    @Bind(R.id.TVfactory_totalprod)
    TextView TVfactoryTotalprod;
    @Bind(R.id.TVfactory_distance)
    TextView TVfactoryDistance;
    @Bind(R.id.TVfactory_start)
    TextView TVfactoryStart;
    @Bind(R.id.TVfactory_end)
    TextView TVfactoryEnd;
    @Bind(R.id.TVfactory_begin)
    TextView TVfactoryBegin;
    @Bind(R.id.TVfactory_finish)
    TextView TVfactoryFinish;
    @Bind(R.id.TVfactory_maker)
    TextView TVfactoryMaker;
    @Bind(R.id.TVfactory_superunit)
    TextView TVfactorySuperunit;
    @Bind(R.id.TVfactory_part3)
    TextView TVfactoryPart3;
    @Bind(R.id.TVfactory_addr)
    TextView TVfactoryAddr;
    @Bind(R.id.TVfactory_builder)
    TextView TVfactoryBuilder;
    @Bind(R.id.TVfactory_supervisor)
    TextView TVfactorySupervisor;
    @Bind(R.id.TVfactory_intro)
    TextView TVfactoryIntro;
    @Bind(R.id.TVfactory_ablility)
    TextView TVfactoryAblility;
    @Bind(R.id.TVfactory_lowwam)
    TextView TVfactoryLowwam;
    @Bind(R.id.TVfactory_highwam)
    TextView TVfactoryHighwam;
    @Bind(R.id.ivfactory_plan_change)
    ImageView ivfactoryPlanChange;
    @Bind(R.id.TVfactory_build)
    TextView TVfactoryBuild;
    @Bind(R.id.TVfactory_store)
    TextView TVfactoryStore;
    private String ChangeFactoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_change);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        tvTitleTable.setText("梁场信息");
        ChangeFactoryName = getIntent().getStringExtra("name");
        try {
            SetFactoryContent();
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"图片获取失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void SetFactoryContent() throws FileNotFoundException {
        if (AllStaticBean.factoryDatas != null) {
            FactoryData FD = SearchNeedFactory();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            if (FD != null) {
                TVfactoryName.setText(FD.getName());
                TVfactoryProject.setText(FD.getProject());
                TVfactorySegment.setText(FD.getSegment());
                TVfactoryCode.setText(FD.getCode());
                TVfactoryManager.setText(FD.getManager());
                TVfactoryPhone.setText(FD.getPhone());
                TVfactoryTitle.setText(FD.getTitle());
                TVfactoryTotalprod.setText(String.valueOf(FD.getTotalProd()));
                TVfactoryDistance.setText(FD.getDistance());
                TVfactoryStart.setText(FD.getStart());
                TVfactoryEnd.setText(FD.getEnd());
                TVfactoryMaker.setText(FD.getMaker());
                TVfactorySuperunit.setText(FD.getSuperUnit());
                TVfactoryPart3.setText(FD.getPart3());
                TVfactoryAddr.setText(FD.getAddr());
                TVfactoryBuilder.setText(FD.getBuilder());
                TVfactorySupervisor.setText(FD.getSupervisor());
                TVfactoryIntro.setText(FD.getIntro());
                TVfactoryAblility.setText(String.valueOf(FD.getAbility()));
                TVfactoryLowwam.setText(String.valueOf(FD.getLowWarn()));
                TVfactoryHighwam.setText(String.valueOf(FD.getHighWarn()));
                TVfactoryBuild.setText(FD.getBuild());
                TVfactoryStore.setText(FD.getStore());
                Log.e("SetFactoryContent", formatter.format(FD.getBegin()) + "  " + formatter.format(FD.getFinish()));
                TVfactoryBegin.setText(formatter.format(FD.getBegin()));
                TVfactoryFinish.setText(formatter.format(FD.getFinish()));

                ivfactoryPlanChange.setImageBitmap(GetBeamPlanImage());
            }
        }
    }

    private FactoryData SearchNeedFactory() {
        for (FactoryData factoryData : AllStaticBean.factoryDatas) {
            if (factoryData.getName().equals(ChangeFactoryName)) {
                return factoryData;
            }
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //用文件流的方式打开图片，并转换为Bitmap，以便可以用ImageView显示。
    private Bitmap GetBeamPlanImage() throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(AllStaticBean.BeamPlanName);

        return BitmapFactory.decodeStream(fileInputStream);
    }

}
