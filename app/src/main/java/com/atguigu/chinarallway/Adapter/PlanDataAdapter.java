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

import com.atguigu.chinarallway.Activity.MakePlanActivity;
import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈雨田 on 2017/10/12.
 */

public class PlanDataAdapter extends RecyclerView.Adapter<PlanDataAdapter.ViewHolder>{


    private LayoutInflater mInflater;
    private Short[] mSeq=null;
    private ArrayList<String> mTitle=new ArrayList<>(1);
    private Context context;
    private String BeamRequestContent = "";
    private String SideChoose = "";
    private String bIDChoose = "";

    List<String> numberlist = new ArrayList<>();

    List<String> SpinnerNumberlist = new ArrayList<>();

    List<String> bNamelist = new ArrayList<>();

    //Map<String,String> bNamelist = new HashMap<>();

    List<String> bIDlist = new ArrayList<>();

    List<String> sidechooselist = new ArrayList<>();

    List<Integer> mSeqlist = new ArrayList<>();

//    private int k=0;
//    private int n=0;
//    private int m=0;


    public List<String> getbIDlist(){return bIDlist;}

    public List<String> getSidechooselist(){return sidechooselist;}

    public List<String> getbNamelist() {return bNamelist;}

    public boolean isbIDlist(){
        for (int i=0;i<getmSeq().length;i++) {
            if (bIDlist.get(i).equals(" ")) {
                return false;
            }
        }
        return true;
    }

    public boolean isSidechooselist(){
        for (int i=0;i<getmSeq().length;i++) {
            if (sidechooselist.get(i).equals(" ")) {
                return false;
            }
        }
        return true;
    }

    public boolean isbNamelist() {
        for (int i=0;i<getmSeq().length;i++) {
            if (bNamelist.get(i).equals(" ")) {
                return false;
            }
        }
        return true;
    }

    public short Max(){return getmSeq()[getmSeq().length-1];}

    public Short[] getmSeq(){return mSeq;}

    public PlanDataAdapter(Context context,ArrayList<String>title){
        this.context = context;
        this.mInflater=LayoutInflater.from(context);
        this.mSeq=new Short[1];

        short max = getMax();
        /*for (short i=0;i<1;i++){
            short index= (short) (i+1);
            mSeq[i]=index;
        }*/
        mSeq[0] = (short)(max+1);
        //mSeq[0] = max;
    }

    private short getMax(){
        if (AllStaticBean.MaxBeamData != null
                && AllStaticBean.buildPlens != null
                && AllStaticBean.buildPlens.length != 0) {

            short max = AllStaticBean.buildPlens[0].getSeq();
            for (int i1 = 1; i1 < AllStaticBean.buildPlens.length; i1++) {

                if(AllStaticBean.buildPlens[i1].getSeq()>max)
                    max = AllStaticBean.buildPlens[i1].getSeq();

            }

            return max;

        }else return 0;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_makeplan,parent,false);
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        numberlist.add(" ");
        SpinnerNumberlist.add(" ");
        bNamelist.add(" ");
        bIDlist.add(" ");
        sidechooselist.add(" ");

        holder.et_seq.setText(String.valueOf(mSeq[position]));
        final MakePlanActivity activity = (MakePlanActivity)context;
        //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_item,activity.getBridgeSpinnerData());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,R.layout.spinner_item,activity.getBridgename());
        holder.spinner_bName.setAdapter(arrayAdapter);
        holder.spinner_bName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                numberlist.clear();
                SpinnerNumberlist.clear();
                if(pos != 0){
                    //BeamRequestContent = activity.getBridgeSpinnerData()[pos];
                    BeamRequestContent = activity.getBridgename().get(pos);
                   // bNamelist.put(BeamRequestContent, BeamRequestContent);
                    /*桥名的List 用于Spinner*/
                    if (bNamelist.size() == position + 1) {
                        bNamelist.set(position, BeamRequestContent);
                    }else
                        bNamelist.add(position, BeamRequestContent);

                    /*梁编号的List 用于Spinner*/
                    numberlist.add("");
                    for(int i = 0; i<activity.getMaxSpinnerData().length; i++) {
                        String[] param = activity.getMaxSpinnerData()[i].split("_");
                        Log.e("testid",param[1]);
                        if(BeamRequestContent.equals(param[0]) && param[2].equals("未预制")){
                            numberlist.add(param[1]);
                        }
                    }
                }
                for (int j=0;j<bNamelist.size();j++)
                    Log.e("test3",bNamelist.get(j));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BeamRequestContent = "";

            }
        });


        final List<String> sidelist = new ArrayList<>();
        sidelist.add(" ");
        sidelist.add("左");
        sidelist.add("右");
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(context,R.layout.spinner_item,sidelist);
        holder.spinner_side.setAdapter(arrayAdapter1);
        holder.spinner_side.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos != 0){
                    SideChoose = sidelist.get(pos);
                    if(!SideChoose.equals("")){

                        if (sidechooselist.size() == position + 1) {
                            sidechooselist.set(position, SideChoose);
                        } else {
                            sidechooselist.add(position, SideChoose);
                        }

                        /*筛选出相应的桥梁字段*/
                        if (numberlist.size() > 1) {
                            List<String> temp = null;
                            try {
                                temp = AllStaticBean.DeepCopy(numberlist);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            SpinnerNumberlist.clear();
                            SpinnerNumberlist.add(" ");
                            for (int i = 1; i < temp.size(); i++) {
                                if (temp.get(i).substring(0, 1).equals(sidechooselist.get(position))) {
                                    SpinnerNumberlist.add(temp.get(i));
                                }
                            }
                        }
                    }
                }
                for (int j=0;j<sidechooselist.size();j++)
                    Log.e("test2",sidechooselist.get(j));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(context,R.layout.spinner_item,SpinnerNumberlist);
        holder.spinner_bID.setAdapter(arrayAdapter2);
        holder.spinner_bID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos != 0){
                    bIDChoose = SpinnerNumberlist.get(pos);
                    if (bIDlist.size() == position + 1) {
                        bIDlist.set(position, bIDChoose);
                    }else
                        bIDlist.add(position, bIDChoose);
                }
                for (int j=0;j<bIDlist.size();j++)
                Log.e("test1",bIDlist.get(j));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        holder.et_Time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(mSeq==null)
            return 0;
        else
            return mSeq.length;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView et_seq ;
        public Spinner spinner_bName;
        public Spinner spinner_side;
        public Spinner spinner_bID;

        public ViewHolder(View view){
            super(view);
            et_seq = (TextView)view.findViewById(R.id.et_seq1);
            spinner_bName = (Spinner) view.findViewById(R.id.spinner_bridge);
            spinner_side = (Spinner) view.findViewById(R.id.spinner_leftright);
            spinner_bID = (Spinner) view.findViewById(R.id.spinner_bID);
        }

    }




    public void add(String text, int position) {

        this.mSeq=new Short[this.mSeq.length+1];
        Log.e("length", String.valueOf(mSeq.length));
        short max = getMax();
        Log.e("max", String.valueOf(max));
        for (short i = 0; i<this.mSeq.length; i++){
            mSeq[i]=(++max);
            Log.e("mSeq", String.valueOf(mSeq[i]));
        }
        ArrayList<String> list = new ArrayList<>(mTitle.size()+1);
        list.addAll(0,mTitle);
        mTitle = list;
        notifyDataSetChanged();
    }


}
