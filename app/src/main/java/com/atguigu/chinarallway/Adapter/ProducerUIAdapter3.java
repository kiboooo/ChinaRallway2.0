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

public class ProducerUIAdapter3 extends RecyclerView.Adapter<ProducerUIAdapter3.ViewHolder> {


        private List<TaskWithBeam> mProducerData = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView BridgeName3,MixProtectBottom, MixProtectDown, MixProtectTop,
                EmbedmentExpansionJoint, EmbedmentLiftingHole, EmbedmentFence, EmbedmentAir;

        public ViewHolder(View itemView) {
            super(itemView);
            BridgeName3 = itemView.findViewById(R.id.bridge_name);
            MixProtectBottom = (TextView) itemView.findViewById(R.id.MixProtect_Bottom);
            MixProtectDown = (TextView) itemView.findViewById(R.id.MixProtect_Down);
            MixProtectTop = (TextView) itemView.findViewById(R.id.MixProtect_Top);
            EmbedmentExpansionJoint = (TextView) itemView.findViewById(R.id.Embedment_expansion_joint);
            EmbedmentLiftingHole = (TextView) itemView.findViewById(R.id.Embedment_lifting_hole);
            EmbedmentFence = (TextView) itemView.findViewById(R.id.Embedment_Fence);
            EmbedmentAir = (TextView) itemView.findViewById(R.id.Embedment_Air);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_plan_ui3_item, parent, false);
        return new ViewHolder(mV);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskWithBeam data = mProducerData.get(position);
        holder.BridgeName3.setText(String.valueOf(data.getbName()+data.getbID()));
        holder.MixProtectBottom.setText(String.valueOf(data.getProBottom()));
        holder.MixProtectDown.setText(String.valueOf(data.getProDown()));
        holder. MixProtectTop.setText(String.valueOf(data.getTop()));
        holder.EmbedmentExpansionJoint.setText(String.valueOf(data.getScale()));
        holder.EmbedmentLiftingHole.setText(String.valueOf(data.getHoisting()));
        holder.EmbedmentFence.setText(String.valueOf(data.getGuardRail()));
        holder. EmbedmentAir.setText(String.valueOf(data.getAir()));
    }

    @Override
    public int getItemCount() {
        return mProducerData.size();
    }

        public ProducerUIAdapter3(List<TaskWithBeam> producerDate) {
        mProducerData = producerDate;
    }

}
