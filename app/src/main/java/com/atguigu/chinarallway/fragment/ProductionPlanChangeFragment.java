package com.atguigu.chinarallway.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.MakePosition;
import com.atguigu.chinarallway.Bean.StorePositionData;
import com.atguigu.chinarallway.Interface.OnTaskDataChangeBack;
import com.atguigu.chinarallway.R;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlanChangeFragment extends DialogFragment {

    private TextView bridgeName;
    private TextView bridgeNumber;
    private Spinner builde;
    private Spinner buildeLocation;
    private Spinner saveNumber;
    private Spinner saveLocation;
//    private MaterialCalendarView start;

    private List<Integer> order = new ArrayList<>();
    private List<String> MPnumber = new ArrayList<>();
    private List<String> saveIDList = new ArrayList<>();
    private List<String> saveLocationList = new ArrayList<>();

    private int orderNum;
    private String orderLocation;
    private String storeNum;
    private String storeLocation;
//    private CalendarDay newDate;

    private OnTaskDataChangeBack mOnTaskDataChangeBack;

    public void setOnTaskDataChangeBack(OnTaskDataChangeBack onTaskDataChangeBack){
        mOnTaskDataChangeBack = onTaskDataChangeBack;
    }
//
//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_production_plan_change, null);
        final Bundle bundle = getArguments();
        bridgeName = view.findViewById(R.id.nameText);
        bridgeName.setText(bundle.getString("bName"));
        bridgeNumber = view.findViewById(R.id.numberText);
        bridgeNumber.setText(bundle.getString("bId"));
        builde = view.findViewById(R.id.buildSpinner);
        orderNum = Integer.valueOf(bundle.getString("mOrder"));
        initMakePositionOrder(orderNum);
        builde.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_item_production, order));
        buildeLocation = view.findViewById(R.id.buildNumberSpinner);
        orderLocation = bundle.getString("mPosId");
        initMakePositionNum(orderLocation);
        buildeLocation.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_production, MPnumber));
        saveNumber = view.findViewById(R.id.saveSpinner);
        saveLocation = view.findViewById(R.id.saveLocationSpinner);
        storeNum = bundle.getString("mPedId");
        storeLocation = bundle.getString("mPos");
        initSave(storeNum, storeLocation);
        saveNumber.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_production, saveIDList));
        saveLocation.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item_production, saveLocationList));
//        start = view.findViewById(R.id.dateBegin);
//        start.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                newDate = date;
//            }
//        });
        onBind_Spinner();
        builder.setView(view)
                .setPositiveButton("完  成",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                mOnTaskDataChangeBack.changeBackTaskData(bundle.getString("bName"),bundle.getString("bId"),
                                        orderNum,orderLocation,storeNum,storeLocation,bundle.getInt("mPosition"));
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

    private void onBind_Spinner(){
        builde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderNum = order.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buildeLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderLocation = MPnumber.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storeNum = saveIDList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (saveLocationList.get(i).split("_").length > 2) {
                    storeLocation = saveLocationList.get(i).split("_")[1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}