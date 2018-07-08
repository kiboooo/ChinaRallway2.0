package com.atguigu.chinarallway.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atguigu.chinarallway.Bean.TaskWithBeam;
import com.atguigu.chinarallway.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiboooo on 2017/10/14.
 */

public class ProducerUIAdapter2 extends RecyclerView.Adapter<ProducerUIAdapter2.ViewHolder> {


    private List<TaskWithBeam> mProducerData = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView BridgeName2,WedgeBlockLeftMinL, WedgeBlockLeftMinR, WedgeBlockLeftMaxL,
                WedgeBlockLeftMaxR, WedgeBlockRightMinL,WedgeBlockRightMinR, WedgeBlockRightMaxL,
                WedgeBlockRightMaxR, ScupperLeftMinL, ScupperLeftMaxL,
                ScupperLeftBetween, ScupperRightMinR, ScupperRightMaxR,ScupperRightBetween;

        public ViewHolder(View itemView) {
            super(itemView);
            BridgeName2 = itemView.findViewById(R.id.bridge_name);
            WedgeBlockLeftMinL = (TextView) itemView.findViewById(R.id.WedgeBlockLeft_minL);
            WedgeBlockLeftMinR = (TextView) itemView.findViewById(R.id.WedgeBlockLeft_minR);
            WedgeBlockLeftMaxL = (TextView) itemView.findViewById(R.id.WedgeBlockLeft_maxL);
            WedgeBlockLeftMaxR = (TextView) itemView.findViewById(R.id.WedgeBlockLeft_maxR);
            WedgeBlockRightMinL = (TextView) itemView.findViewById(R.id.WedgeBlockRight_minL);
            WedgeBlockRightMinR = (TextView) itemView.findViewById(R.id.WedgeBlockRight_minR);
            WedgeBlockRightMaxL = (TextView) itemView.findViewById(R.id.WedgeBlockRight_maxL);
            WedgeBlockRightMaxR = (TextView) itemView.findViewById(R.id.WedgeBlockRight_maxR);
            ScupperLeftMinL = (TextView) itemView.findViewById(R.id.ScupperLeft_minL);
            ScupperLeftMaxL = (TextView) itemView.findViewById(R.id.ScupperLeft_maxL);
            ScupperLeftBetween = (TextView) itemView.findViewById(R.id.ScupperLeft_Between);
            ScupperRightMinR = (TextView) itemView.findViewById(R.id.ScupperRight_minR);
            ScupperRightMaxR = (TextView) itemView.findViewById(R.id.ScupperRight_maxR);
            ScupperRightBetween = (TextView) itemView.findViewById(R.id.ScupperRight_Between);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_plan_ui2_item, parent, false);
        return new ViewHolder(mV);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskWithBeam data = mProducerData.get(position);
        holder.BridgeName2.setText(String.valueOf(data.getbName()+data.getbID()));
        holder.WedgeBlockLeftMinL.setText(String.valueOf(data.getLsl()));
        holder.WedgeBlockLeftMinR.setText(String.valueOf(data.getLsr()));
        holder.WedgeBlockLeftMaxL.setText(String.valueOf(data.getLbl()));
        holder.WedgeBlockLeftMaxR.setText(String.valueOf(data.getLbr()));
        holder.WedgeBlockRightMinL .setText(String.valueOf(data.getRsl()));
        holder.WedgeBlockRightMinR.setText(String.valueOf(data.getRsr()));
        holder.WedgeBlockRightMaxL.setText(String.valueOf(data.getRbl()));
        holder.WedgeBlockRightMaxR.setText(String.valueOf(data.getRbr()));
        holder.ScupperLeftMinL.setText(String.valueOf(data.getLls()));
        holder.ScupperLeftMaxL.setText(String.valueOf(data.getLbl()));
        holder.ScupperLeftBetween.setText(String.valueOf(data.getLlb()));
        holder.ScupperRightMinR.setText(String.valueOf(data.getRls()));
        holder.ScupperRightMaxR.setText(String.valueOf(data.getRlb()));
        holder.ScupperRightBetween.setText(String.valueOf(data.getRlb()));
    }

    public ProducerUIAdapter2(List<TaskWithBeam> producerDate) {
        mProducerData = producerDate;
    }

    @Override
    public int getItemCount() {
        return mProducerData.size();
    }
}
