package com.atguigu.chinarallway.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.atguigu.chinarallway.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈雨田 on 2017/9/14.
 */

public class FactorySearchActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.iv_bridge)
    ImageView ivBridge;
    @Bind(R.id.beam_search)
    Spinner beamSearch;
    @Bind(R.id.bt_search)
    Button btSearch;
    @Bind(R.id.search_layout)
    LinearLayout searchLayout;
    @Bind(R.id.bt_change)
    Button btChange;

    private String FactoryContent = "";
    private String[] FactoryContents = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_search);
        ButterKnife.bind(this);
        FactoryContents = new String[AllStaticBean.factoryDatas.length + 1];
        FactoryContents[0] = "";
        for (int i = 0; i < AllStaticBean.factoryDatas.length; i++) {
            FactoryContents[i + 1] = AllStaticBean.factoryDatas[0].getName();
        }
        tvTitleTable.setText("梁场信息查询");
        ivBack.setOnClickListener(this);
        btChange.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        BoundSpinner();
    }

    private void BoundSpinner() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, FactoryContents);
        //绑定 Adapter到控件
        beamSearch.setAdapter(mAdapter);
        beamSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FactoryContent = FactoryContents[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                FactoryContent = "";
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_search:
                if (!FactoryContent.equals("")) {
                    Intent intent = new Intent(this, FactoryChangeDataActivity.class);
                    intent.putExtra("name", FactoryContent);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "查询选择梁表不能为空", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_change:
                if (!FactoryContent.equals("")) {
                    Intent intent = new Intent(this, FactoryDataActivity.class);
                    intent.putExtra("name", FactoryContent);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "修改选择梁表不能为空", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
