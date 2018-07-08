package com.atguigu.chinarallway.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.R;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ProductionPlanChangeAdapter extends RecyclerView.Adapter<ProductionPlanChangeAdapter.ViewHolder> {


    private List<TaskData> TaskDatas = new ArrayList<>();
    private List<ViewHolder> viewHolder = new ArrayList<>();

    public ProductionPlanChangeAdapter(List<TaskData> taskDatas) {
        this.TaskDatas = taskDatas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etProducerTaskDate;
        TextView etProducerBName;
        TextView etProducerBId;
        EditText etProducerMakeOrder;
        TextView etProducerMakePosID;
        TextView etProducerPedID;
        TextView etProducerPOS;
        TextView etProducerPemit;


        public ViewHolder(View itemView) {
            super(itemView);
            etProducerTaskDate = (TextView) itemView.findViewById(R.id.etProducerTaskDate);
            etProducerBName = (TextView) itemView.findViewById(R.id.etProducerBName);
            etProducerBId = (TextView) itemView.findViewById(R.id.etProducerBId);
            etProducerMakeOrder = (EditText) itemView.findViewById(R.id.etProducerMake_Order);
            etProducerMakePosID =  itemView.findViewById(R.id.etProducerMake_PosID);
            etProducerPedID =  itemView.findViewById(R.id.etProducerPedID);
            etProducerPOS = itemView.findViewById(R.id.etProducerPOS);
            etProducerPemit = (TextView) itemView.findViewById(R.id.etProducerPemit);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_production_change_item, parent, false);
        ViewHolder vH = new ViewHolder(mV);
        viewHolder.add(vH);
        return vH;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskData data = TaskDatas.get(position);
        holder.etProducerTaskDate.setText(AllStaticBean.formatter.format(data.getTaskDate()));
        holder.etProducerBName.setText(data.getbName());
        holder.etProducerBId.setText(String.valueOf(data.getbID()));
        holder.etProducerMakeOrder.setText(data.getMakeOrder());
        holder.etProducerMakePosID.setText(data.getMakePosId());
        holder.etProducerPedID.setText(String.valueOf(data.getPedID()));
        holder.etProducerPOS.setText(data.getPos());
        holder.etProducerPemit.setText("未审核");
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return TaskDatas.size();
    }

    public TaskData GetTaskData(int position) throws ParseException {
        TaskData taskData = new TaskData();
        taskData.setbID(viewHolder.get(position).etProducerBId.getText().toString());
        taskData.setPermit(false);
        taskData.setbName(viewHolder.get(position).etProducerBName.getText().toString());
        taskData.setMakeOrder(viewHolder.get(position).etProducerMakeOrder.getText().toString());
        taskData.setMakePosId(viewHolder.get(position).etProducerMakePosID.getText().toString());
        taskData.setPedID(Short.parseShort(viewHolder.get(position).etProducerPedID.getText().toString()));
        taskData.setPos(viewHolder.get(position).etProducerPOS.getText().toString());
        taskData.setTaskDate(new Date(AllStaticBean.formatter.parse(viewHolder.get(position).
                etProducerTaskDate.getText().toString()).getTime()));
        return taskData;
    }

}
