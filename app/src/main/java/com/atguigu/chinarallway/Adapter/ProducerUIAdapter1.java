package com.atguigu.chinarallway.Adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atguigu.chinarallway.Bean.TaskWithBeam;
import com.atguigu.chinarallway.R;

import java.util.List;

/**
 * Created by Kiboooo on 2017/10/12.
 */

public class ProducerUIAdapter1 extends RecyclerView.Adapter<ProducerUIAdapter1.ViewHolder>{

    private List<TaskWithBeam> ProducerData;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView producerBID,producerLen,producerHeight,
                producerSlope,producerEndSize,producerDown
                ,producerTop,producerBottom,producerConcrete,
                producerMakeOrder,producerMakePosID,producerPos;

        public ViewHolder(View itemView) {
            super(itemView);
            producerBID = (TextView) itemView.findViewById(R.id.producerBID);
            producerLen = (TextView) itemView.findViewById(R.id.producerLen);
            producerHeight = (TextView) itemView.findViewById(R.id.producerHeight);
            producerSlope = (TextView) itemView.findViewById(R.id.producerSlope);
            producerEndSize = (TextView) itemView.findViewById(R.id.producerEndSize);
            producerDown = (TextView) itemView.findViewById(R.id.producerDown);
            producerTop = (TextView) itemView.findViewById(R.id.producerTop);
            producerBottom = (TextView) itemView.findViewById(R.id.producerBottom);
            producerConcrete = (TextView) itemView.findViewById(R.id.producerConcrete);
            producerMakeOrder = (TextView) itemView.findViewById(R.id.producerMakeOrder);
            producerMakePosID = (TextView) itemView.findViewById(R.id.producerMakePosID);
            producerPos = (TextView) itemView.findViewById(R.id.producerPos);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_plan_ui1_item, parent, false);
        return new ViewHolder(mV);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TaskWithBeam taskWithBeam = ProducerData.get(position);
        holder.producerBID.setText(taskWithBeam.getbName()+taskWithBeam.getbID());
        holder.producerBottom.setText(String.valueOf(taskWithBeam.getBottom()));
        holder.producerConcrete.setText(String.valueOf(taskWithBeam.getConcrete()));
        holder.producerDown.setText(String.valueOf(taskWithBeam.getDown()));
        holder.producerEndSize.setText(String.valueOf(taskWithBeam.getEndSize()));
        holder.producerHeight.setText(String.valueOf(taskWithBeam.getHeight()));
        holder.producerLen.setText(String.valueOf(taskWithBeam.getLen()));
        holder.producerMakeOrder.setText(String.valueOf(taskWithBeam.getMakeOrder()));
        holder.producerMakePosID.setText(String.valueOf(taskWithBeam.getMakePosId()));
        holder.producerPos.setText(String.valueOf(taskWithBeam.getPos()));
        holder.producerSlope.setText(String.valueOf(taskWithBeam.getSlope()));
        holder.producerTop.setText(String.valueOf(taskWithBeam.getTop()));
    }

    public ProducerUIAdapter1(List<TaskWithBeam> producerData) {
        ProducerData = producerData;
    }

    @Override
    public int getItemCount() {
        return ProducerData.size();
    }

}
