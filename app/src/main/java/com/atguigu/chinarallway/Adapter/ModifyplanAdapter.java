package com.atguigu.chinarallway.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.atguigu.chinarallway.Activity.ModifyplanActivity;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈雨田 on 2017/10/13.
 */

public class ModifyplanAdapter extends RecyclerView.Adapter<ModifyplanAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private Short[] mSeq=null;
    private Context context;

    private String BeamRequestContent = "";
    private String SideChoose = "";
    private String bIDChoose = "";

    List<String> numberlist1 = new ArrayList<>();

    List<String> bNamelist1 = new ArrayList<>();

    List<String> bIDlist1 = new ArrayList<>();

    List<String> sidechooselist1 = new ArrayList<>();

  /*  public List<String> getNumberlist1() {
        return numberlist1;
    }

    public List<String> getbNamelist1() {
        return bNamelist1;
    }

    public List<String> getbIDlist1() {
        return bIDlist1;
    }

    public List<String> getSidechooselist1() {
        return sidechooselist1;
    }*/

    public Short[] getmSeq() {
        return mSeq;
    }


    public ModifyplanAdapter(Context context, ArrayList<String>title){
        this.mInflater=LayoutInflater.from(context);
        this.context = context;
        this.mSeq=new Short[AllStaticBean.buildPlens.length];
        for (short i=0;i<AllStaticBean.buildPlens.length;i++){
            short index= (short) (i+1);
            mSeq[i]=index;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_modifyplan,parent,false);
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        bNamelist1.clear();
        sidechooselist1.clear();
        bIDlist1.clear();

        List<Integer> change = new ArrayList<>();
        holder.et_seq.setText(String.valueOf(mSeq[position]));

        final ModifyplanActivity activity = (ModifyplanActivity) context;

        /*获取所有的桥名*/
        final String[] name = activity.getbNameList().clone();
        Log.e("first",AllStaticBean.buildPlens[position].getbName());


        name[0] = AllStaticBean.buildPlens[position].getbName().split(" ")[0];
        bNamelist1.add(AllStaticBean.buildPlens[position].getbName());

        holder.tv_bName_modify.setText(bNamelist1.get(0));//设置这条计划的桥名

        for (int i = 1; i < name.length; i++) {
            if (name[0].equals(name[i].split(" ")[0])
                    && name[i].split(" ")[1].equals("未预制")){
                change.add(i);
            }
        }

        /*获取所有的梁编号*/
        final String[] Id = activity.getbID().clone();
//        for (int i = 0; i <Id.length ; i++) {
//            Log.e("Id",Id[i]);
//        }

        final String[] side = activity.getSide().clone();
        side[0]=AllStaticBean.buildPlens[position].getSide();
        sidechooselist1.add(AllStaticBean.buildPlens[position].getSide());
        Log.e("sidefirst",AllStaticBean.buildPlens[position].getSide());


        //final String[] NID = new String[change.size() + 1];
        //NID[0]=AllStaticBean.buildPlens[position].getbID();
        //Log.e("NID[0]",NID[0]);

        /*显示梁编号的数组*/
        final List<String> NID = new ArrayList<>();

        NID.add(AllStaticBean.buildPlens[position].getbID());

        bIDlist1.add(AllStaticBean.buildPlens[position].getbID());//添加为显示Spinner的第一个
        Log.e("bIDfirst",AllStaticBean.buildPlens[position].getbID());

        for (int i = 0; i < change.size(); i++) {
               // NID[i + 1] = Id[change.get(i)];
                 NID.add(Id[change.get(i)]);
        }

//
//
//
//
///*
//        for (int i=0;i<name.length;i++){
//            //bNamelist1.add(name[i]);
//            if(!bNamelist1.contains(name[i])){
//                bNamelist1.add(name[i]);
//            }
//        }*/
//
//
//     /*   final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(context,R.layout.spinner_item,bNamelist1);
//        arrayAdapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        holder.spinner_bName_modify.setAdapter(arrayAdapter1);
//        holder.spinner_bName_modify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                //if(pos!=0){
//                    //numberlist1.clear();
//                    bIDlist1.clear();
//                    BeamRequestContent = name[pos];
//                   AllStaticBean.buildPlens[position].setbName(BeamRequestContent);
//                    for(int i=0;i<AllStaticBean.MaxBeamData.length;i++){
//                        String[] param = activity.getMaxSpinnerData()[i].split("_");
//                        if(BeamRequestContent.equals(param[0])){
//                            //numberlist1.add(param[1]);
//                            if(!bIDlist1.contains(param[1]))
//                                bIDlist1.add(param[1]);
//                        }
//                    }
//                   *//* for (int i=0;i<AllStaticBean.buildPlens.length;i++)
//                        Log.e("testname",AllStaticBean.buildPlens[i].getbName());*//*
//                //}
//
//                for (int i = 0; i <bIDlist1.size() ; i++) {
//                    Log.e("bIDlist",bIDlist1.get(i));
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });*/
//
//     /*   for(int i=0;i< Id.length;i++){
//            bIDlist1.add(Id[i]);
//        }*/

            /*梁编号的Spinner*/
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(context, R.layout.spinner_item, bIDlist1);
        holder.spinner_bID_modify.setAdapter(arrayAdapter2);
        arrayAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        holder.spinner_bID_modify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id1) {
                if (pos != 0) {
                    bIDChoose = bIDlist1.get(pos);
                    AllStaticBean.buildPlens[position].setbID(bIDChoose);
//                    for (int i = 0; i < AllStaticBean.buildPlens.length; i++)
//                        Log.e("testID", AllStaticBean.buildPlens[i].getbID());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        for (int i=0;i<side.length;i++){
            if(!sidechooselist1.contains(side[i]))
                sidechooselist1.add(side[i]);
        }

        /*左右线的Spnner*/
        final ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(context,R.layout.spinner_item,side);
        holder.spinner_side_modify.setAdapter(arrayAdapter3);
        arrayAdapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        holder.spinner_side_modify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                bIDlist1.clear();
                    if(pos != 0){
                        SideChoose = side[pos];
                        AllStaticBean.buildPlens[position].setSide(SideChoose);

                        for (int i = 0; i <NID.size() ; i++) {
                            if (NID.get(i).substring(0,2).equals(SideChoose)){
                                //确定梁编号的显示
                                bIDlist1.add(NID.get(i));
                            }
                        }
                        for (int i=0;i<AllStaticBean.buildPlens.length;i++)
                            Log.e("testside",AllStaticBean.buildPlens[i].getSide());
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return mSeq.length;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_bName_modify;
        public TextView et_seq;
        //public Spinner spinner_bName_modify;
        public Spinner spinner_side_modify;
        public Spinner spinner_bID_modify;


        public ViewHolder(View view){
            super(view);
            et_seq = view.findViewById(R.id.et_seq1);
            //spinner_bName_modify = view.findViewById(R.id.spinner_bName_modify);
            spinner_side_modify =  view.findViewById(R.id.spinner_side_modify);
            spinner_bID_modify =view.findViewById(R.id.spinner_bID_modify);

            tv_bName_modify = view.findViewById(R.id.tv_bName_modify);

        }

    }



}