package com.atguigu.chinarallway.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

    private List<Integer> number = new ArrayList<>();


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
}
