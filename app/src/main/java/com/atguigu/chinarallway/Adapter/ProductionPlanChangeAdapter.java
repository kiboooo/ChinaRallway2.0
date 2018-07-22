package com.atguigu.chinarallway.Adapter;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.MakePosition;
import com.atguigu.chinarallway.Bean.StorePositionData;
import com.atguigu.chinarallway.Bean.TaskData;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.DeleteRequset;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.fragment.ProductionPlanChangeFragment;

import org.json.JSONArray;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ProductionPlanChangeAdapter extends RecyclerView.Adapter<ProductionPlanChangeAdapter.ViewHolder> {

    private Context mContext;
    private FragmentManager mManager;
    private TaskData[] TaskDatas = null;
    private List<ViewHolder> viewHolder = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ProductionPlanChangeFragment fragment;

    private final int DeleteSUCCESS = 4446;
    private final int DeleteFALL = 4447;
    private final int MPSUCCESS = 1078;
    private final int MPFALL = 2078;
    private final int SPSUCCESS = 3078;
    private final int SPFALL = 4078;

//    private List<TaskData> TaskDatas = new ArrayList<>();
//    public ProductionPlanChangeAdapter(List<TaskData> taskDatas) {
//        this.TaskDatas = taskDatas;
//    }

    public ProductionPlanChangeAdapter(TaskData[] taskDatas, Context context, FragmentManager manager) {
        this.TaskDatas = taskDatas;
        mContext = context;
        mManager = manager;
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.hide();
            switch (msg.what) {
                case DeleteSUCCESS:
                    Remove(msg.arg1);
                    break;
                case DeleteFALL:
                    String message = (String )msg.obj;
                    Toast.makeText(mContext, "删除失败，失败原因:"+message,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MPSUCCESS:
                    JSONArray jsonArray1 = (JSONArray) msg.obj;
                    AllStaticBean.makePositions = AllStaticBean.
                            GsonToDate.fromJson(jsonArray1.toString(),MakePosition[].class);
                    ManagerRequst.AllRequest(
                            "storePosition","","","1",
                            mHandler,
                            SPSUCCESS,
                            SPFALL
                    );
                    break;
                case MPFALL:
                    progressDialog.hide();
                    Toast.makeText(mContext, "请求出错", Toast.LENGTH_SHORT).show();
                    break;
                case SPSUCCESS:
                    JSONArray jsonArray2 = (JSONArray) msg.obj;
                    AllStaticBean.storePositionDatas = AllStaticBean.
                            GsonToDate.fromJson(jsonArray2.toString(),StorePositionData[].class);
                    progressDialog.hide();
                    fragment.show(mManager,"changerProduction");
                    break;
                case SPFALL:
                    progressDialog.hide();
                    Toast.makeText(mContext, "请求出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etProducerTaskDate;
        TextView etProducerBName;
        TextView etProducerBId;
//        EditText etProducerMakeOrder;
        TextView etProducerMakeOrder;
        TextView etProducerMakePosID;
        TextView etProducerPedID;
        TextView etProducerPOS;
        TextView etProducerPemit;
        Button deleteButton;
        Button changeButton;


        public ViewHolder(View itemView) {
            super(itemView);
//            etProducerTaskDate = (TextView) itemView.findViewById(R.id.etProducerTaskDate);
//            etProducerBName = (TextView) itemView.findViewById(R.id.etProducerBName);
//            etProducerBId = (TextView) itemView.findViewById(R.id.etProducerBId);
//            etProducerMakeOrder = (EditText) itemView.findViewById(R.id.etProducerMake_Order);
//            etProducerMakePosID =  itemView.findViewById(R.id.etProducerMake_PosID);
//            etProducerPedID =  itemView.findViewById(R.id.etProducerPedID);
//            etProducerPOS = itemView.findViewById(R.id.etProducerPOS);
//            etProducerPemit = (TextView) itemView.findViewById(R.id.etProducerPemit);
            etProducerTaskDate = (TextView) itemView.findViewById(R.id.dateText);
            etProducerBName = (TextView) itemView.findViewById(R.id.nameText);
            etProducerBId = (TextView) itemView.findViewById(R.id.numText);
            etProducerMakeOrder =itemView.findViewById(R.id.buildText);
            etProducerMakePosID =  itemView.findViewById(R.id.buildNumberText);
            etProducerPedID =  itemView.findViewById(R.id.saveText);
            etProducerPOS = itemView.findViewById(R.id.saveLocationText);
            etProducerPemit = (TextView) itemView.findViewById(R.id.checkState);
            deleteButton = itemView.findViewById(R.id.delete);
            changeButton = itemView.findViewById(R.id.change);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_production_change_item, parent, false);
        View mV = LayoutInflater.from(parent.getContext()).inflate(R.layout.producer_plan_item, parent, false);
        ViewHolder vH = new ViewHolder(mV);
        viewHolder.add(vH);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("请稍后...");
        return vH;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TaskData data = TaskDatas[position];
        final int positionLocal = position;
//        TaskData data = TaskDatas.get(position);
        holder.etProducerTaskDate.setText(AllStaticBean.formatter.format(data.getTaskDate()));
        holder.etProducerBName.setText(data.getbName());
        holder.etProducerBId.setText(String.valueOf(data.getbID()));
        holder.etProducerMakeOrder.setText(data.getMakeOrder());
        holder.etProducerMakePosID.setText(data.getMakePosId());
        holder.etProducerPedID.setText(String.valueOf(data.getPedID()));
        holder.etProducerPOS.setText(data.getPos());
        holder.etProducerPemit.setText(data.isPermit()?"审核":"未审核");
        holder.etProducerPemit.setSelected(data.isPermit());
        holder.changeButton.setVisibility(View.VISIBLE);
        holder.changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接跳转到修改的Dialog
                progressDialog.show();
                fragment = new ProductionPlanChangeFragment();
                fragment.setArguments(getTaskData(data));
                fragment.setCancelable(false);
                ManagerRequst.AllRequest(
                        "makePosition", "", "", "1",
                        mHandler,
                        MPSUCCESS,
                        MPFALL);
            }
        });
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("温馨提示：")
                        .setMessage("由于删除后，有可能会影响到整体的生产计划，确认继续删除吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //执行删除，刷新页表数据,并删除数据库中的表项
                                progressDialog.show();
                                DeleteRequset.DeleteProductionPlan(data,
                                        DeleteSUCCESS, DeleteFALL, mHandler,positionLocal);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return TaskDatas.length;
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

    private void Remove(int position) {
        int length = TaskDatas.length;
        TaskData[] temp = new TaskData[length - 1];
        for (int i = 0,j = 0; i < length; i++) {
            if (i != position) {
                temp[j++] = TaskDatas[i];
            }
        }
        TaskDatas = temp;
        AllStaticBean.RemoveArray(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private Bundle getTaskData(TaskData data) {
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putString("bName", data.getbName());
            bundle.putString("bId", data.getbID());
            bundle.putString("mOrder", data.getMakeOrder());
            bundle.putString("mPosId", data.getMakePosId());
            bundle.putString("mPos", data.getPos());
            bundle.putString("mPedId", String.valueOf(data.getPedID()));
        }
        return bundle;
    }

}
