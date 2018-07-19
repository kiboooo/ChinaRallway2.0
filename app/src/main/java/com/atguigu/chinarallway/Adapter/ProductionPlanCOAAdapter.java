package com.atguigu.chinarallway.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.R;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiboooo on 2017/10/15.
 */

public class ProductionPlanCOAAdapter extends RecyclerView.Adapter<ProductionPlanCOAAdapter.ViewHolder> {

//    private List<TaskData> TaskDatas = new ArrayList<>();
    private TaskData[] TaskDatas ;
    private List<ViewHolder> viewHolder = new ArrayList<>();

//    public ProductionPlanCOAAdapter(List<TaskData> taskDatas) {
//        this.TaskDatas = taskDatas;
//    }
    public ProductionPlanCOAAdapter(TaskData[] taskDatas) {
        this.TaskDatas = taskDatas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView producerTaskDate;
        TextView producerBName;
        TextView producerBId;
        TextView producerMakeOrder;
        TextView producerMakePosID;
        TextView producerPedID;
        TextView producerPOS;
        TextView producerPemit;

        ViewHolder(View itemView) {
            super(itemView);
//            producerTaskDate =(TextView) itemView.findViewById(R.id.producerTaskDate);
//            producerBName =(TextView) itemView.findViewById(R.id.producerBName);
//            producerBId =(TextView) itemView.findViewById(R.id.producerBId);
//            producerMakeOrder =(TextView) itemView.findViewById(R.id.producerMake_Order);
//            producerMakePosID =(TextView) itemView.findViewById(R.id.producerMake_PosID);
//            producerPedID =(TextView) itemView.findViewById(R.id.producerPedID);
//            producerPOS =(TextView) itemView.findViewById(R.id.producerPOS);
//            producerPemit =(TextView) itemView.findViewById(R.id.producerPemit);

            producerTaskDate =(TextView) itemView.findViewById(R.id.dateText);
            producerBName =(TextView) itemView.findViewById(R.id.nameText);
            producerBId =(TextView) itemView.findViewById(R.id.numText);
            producerMakeOrder =(TextView) itemView.findViewById(R.id.buildText);
            producerMakePosID =(TextView) itemView.findViewById(R.id.buildNumberText);
            producerPedID =(TextView) itemView.findViewById(R.id.saveText);
            producerPOS =(TextView) itemView.findViewById(R.id.saveLocationText);
            producerPemit =(TextView) itemView.findViewById(R.id.checkState);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_plan_item, parent, false);
//        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_production_plan_item, parent, false);
        ViewHolder vH = new ViewHolder(mV);
        viewHolder.add(vH);
        return vH;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        TaskData data = TaskDatas.get(position);
        TaskData data = TaskDatas[position];
        holder.producerTaskDate.setText(AllStaticBean.formatter.format(data.getTaskDate()));
        holder.producerBName.setText(data.getbName());
        holder.producerBId.setText(data.getbID());
        holder.producerMakeOrder.setText(data.getMakeOrder());
        holder.producerMakePosID.setText(data.getMakePosId());
        holder.producerPedID.setText(String.valueOf(data.getPedID()));
        holder.producerPOS.setText(data.getPos());
        if (data.isPermit())
            holder.producerPemit.setText("审核通过");
        else
            holder.producerPemit.setText("未审核");
        holder.producerPemit.setSelected(data.isPermit());
    }

    @Override
    public int getItemCount() {

//        return AllStaticBean.TaskData.length;
//        return TaskDatas.size();
        return TaskDatas.length;
    }

    public TaskData GetTaskData(int position) throws ParseException {
        TaskData taskData = new TaskData();
        taskData.setbID(viewHolder.get(position).producerBId.getText().toString());
        taskData.setPermit(true);
        taskData.setbName(viewHolder.get(position).producerBName.getText().toString());
        taskData.setMakeOrder(viewHolder.get(position).producerMakeOrder.getText().toString());
        taskData.setMakePosId(viewHolder.get(position).producerMakePosID.getText().toString());
        taskData.setPedID(Short.parseShort(viewHolder.get(position).producerPedID.getText().toString()));
        taskData.setPos(viewHolder.get(position).producerPOS.getText().toString());
        taskData.setTaskDate(new Date(AllStaticBean.formatter.parse(viewHolder.get(position).
                producerTaskDate.getText().toString()).getTime()));
        return taskData;
    }

}
