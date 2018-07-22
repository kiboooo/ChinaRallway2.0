package com.atguigu.chinarallway.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.MakePosition;
import com.atguigu.chinarallway.Bean.StorePositionData;
import com.atguigu.chinarallway.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlanChangeFragment extends DialogFragment {

    //    private Spinner bridgeName;
//    private Spinner bridgeNumber;
    private TextView bridgeName;
    private TextView bridgeNumber;
    private Spinner builde;
    private Spinner buildeLocation;
    private Spinner saveNumber;
    private Spinner saveLocation;
    private MaterialCalendarView start;
    private MaterialCalendarView end;

    private List<Integer> order = new ArrayList<>();
    private List<String> MPnumber = new ArrayList<>();
    private List<String> saveIDList = new ArrayList<>();
    private List<String> saveLocationList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_production_plan_change, null);
        Bundle bundle = getArguments();
        bridgeName = view.findViewById(R.id.nameText);
        bridgeName.setText(bundle.getString("bName"));
        bridgeNumber = view.findViewById(R.id.numberText);
        bridgeNumber.setText(bundle.getString("bId"));
        builde = view.findViewById(R.id.buildSpinner);
        initMakePositionOrder(Integer.valueOf(bundle.getString("mOrder")));
        builde.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_item_production, order));
        buildeLocation = view.findViewById(R.id.buildNumberSpinner);
        initMakePositionNum(bundle.getString("mPosId"));
        buildeLocation.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_production, MPnumber));
        saveNumber = view.findViewById(R.id.saveSpinner);
        saveLocation = view.findViewById(R.id.saveLocationSpinner);
        initSave(bundle.getString("mPedId"), bundle.getString("mPos"));
        saveNumber.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_production, saveIDList));
        saveLocation.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_production, saveLocationList));
        start = view.findViewById(R.id.dateBegin);
        end = view.findViewById(R.id.dateEnd);
        builder.setView(view)
                .setPositiveButton("完  成",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {

                            }
                        })
                .setNegativeButton("取  消", null);
        return builder.create();
    }

    private void initMakePositionOrder(int x) {
        order.add(x);
        for (int i = 1; i <= 100; i++) {
            order.add(i);
        }
    }

    private void initMakePositionNum(String x) {
        MPnumber.add(x);
        for (int i = 0; i < AllStaticBean.makePositions.length; i++) {
            MakePosition temp = AllStaticBean.makePositions[i];
            if (temp.isIdle() && !MPnumber.contains(temp.getMakePosID())) {
                MPnumber.add(temp.getMakePosID());
            }
        }
    }

    private void initSave(String id, String location) {
        saveIDList.add(id);
        saveLocationList.add(id + "_" + location);
        for (int i = 0; i < AllStaticBean.storePositionDatas.length; i++) {
            StorePositionData temp = AllStaticBean.storePositionDatas[i];
            if ("空闲".equals(temp.getStatus())) {
                if (!saveIDList.contains(String.valueOf(temp.getPedID()))) {
                    saveIDList.add(String.valueOf(temp.getPedID()));
                }
                saveLocationList.add(String.valueOf(temp.getPedID())
                        + "_" + temp.getPos());

            }
        }
    }
}