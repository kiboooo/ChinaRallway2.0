package com.atguigu.chinarallway.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atguigu.chinarallway.Adapter.ProducerUIAdapter1;
import com.atguigu.chinarallway.Adapter.ProducerUIAdapter2;
import com.atguigu.chinarallway.Adapter.ProducerUIAdapter3;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.TaskWithBeam;
import com.atguigu.chinarallway.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kiboooo on 2017/10/9.
 *
 */

public class ProducerFragment extends Fragment {

    public static final String TAB_PAGE = "tab_page";
    private int mPage;
    private List<TaskWithBeam> taskWithBeamList = new ArrayList<>();

    public static ProducerFragment newInstance(int mPage) {
        Bundle args = new Bundle();
        args.putInt(TAB_PAGE,mPage);
        ProducerFragment fragment = new ProducerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(TAB_PAGE);
        if (AllStaticBean.taskWithBeams.length == 0) {
            Toast.makeText(getContext(), "生产任务未定制，请核对", Toast.LENGTH_SHORT).show();
        }else
            GetTaskWithBeam(AllStaticBean.taskWithBeams);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View ContentView = null;
        switch (mPage) {
            case 0:
                ContentView = inflater.inflate(R.layout.producer_plan_ui_1, container, false);
                RecyclerView UI_1_RecyclerView = ContentView.findViewById(R.id.ProducerRecyclerView_UI1);
                if (!taskWithBeamList.isEmpty()){
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
                    ProducerUIAdapter1 adapter1 = new ProducerUIAdapter1(taskWithBeamList);//放入查询好的生产任务单数组
                    UI_1_RecyclerView.setLayoutManager(linearLayoutManager1);
                    UI_1_RecyclerView.setAdapter(adapter1);
                }
                break;
            case 1:
                ContentView = inflater.inflate(R.layout.producer_plan_ui_2, container, false);
                RecyclerView UI_2_RecyclerView = (RecyclerView) ContentView.findViewById(R.id.ProducerRecyclerView_UI2);
                if (!taskWithBeamList.isEmpty()) {
                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
                    ProducerUIAdapter2 adapter2 = new ProducerUIAdapter2(taskWithBeamList);//放入查询好的生产任务单数组
                    UI_2_RecyclerView.setLayoutManager(linearLayoutManager2);
                    UI_2_RecyclerView.setAdapter(adapter2);
                }
                break;
            case 2:
                ContentView = inflater.inflate(R.layout.producer_plan_ui_3, container, false);
                RecyclerView UI_3_RecyclerView = (RecyclerView) ContentView.findViewById(R.id.ProducerRecyclerView_UI3);
                if (!taskWithBeamList.isEmpty()) {
                    LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
                    ProducerUIAdapter3 adapter3 = new ProducerUIAdapter3(taskWithBeamList);//放入查询好的生产任务单数组
                    UI_3_RecyclerView.setLayoutManager(linearLayoutManager3);
                    UI_3_RecyclerView.setAdapter(adapter3);
                }
                break;
        }
        return ContentView;
    }

    private void GetTaskWithBeam(TaskWithBeam[] taskWithBeams) {
        Collections.addAll(taskWithBeamList, taskWithBeams);
    }

}
