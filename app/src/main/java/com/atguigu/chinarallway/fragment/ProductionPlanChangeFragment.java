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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
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

    private ProgressBar progressBar;

    private final int MPSUCCESS = 1078;
    private final int MPFALL = 2078;
    private final int SPSUCCESS = 3078;
    private final int SPFALL = 4078;

    private List<Integer> number = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MPSUCCESS:
                    ManagerRequst.AllRequest(
                            "storePosition","","","1",
                            mHandler,
                            SPSUCCESS,
                            SPFALL
                    );
                    break;
                case MPFALL:

                    break;
                case SPSUCCESS:
                    ProgressHide();
                    break;
                case SPFALL:
                    ProgressHide();
                    break;
            }
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
        buildeLocation = view.findViewById(R.id.buildNumberSpinner);
        initNumberList(Integer.valueOf(bundle.getString("mOrder")));
        buildeLocation.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_item_production, number));
        saveNumber = view.findViewById(R.id.saveSpinner);
        saveLocation = view.findViewById(R.id.saveLocationSpinner);
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

    private void initNumberList(int x){
        number.add(x);
        for (int i = 1; i <=100 ; i++) {
            number.add(i);
        }
    }

    private void ProgressShow(){

    }

    private void ProgressHide(){

    }

    private void initData(){
        ManagerRequst.AllRequest(
                "makePosition", "", "", "1",
                mHandler,
                MPSUCCESS,
                MPFALL);
    }
}
