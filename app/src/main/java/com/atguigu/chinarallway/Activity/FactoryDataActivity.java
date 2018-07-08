package com.atguigu.chinarallway.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.FactoryData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.atguigu.chinarallway.R.id.iv_back;

/**
 * Created by 陈雨田 on 2017/9/18.
 */

public class FactoryDataActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title_table)
    TextView tvTitleTable;
    @Bind(R.id.etfactory_name)
    EditText etfactoryName;
    @Bind(R.id.etfactory_project)
    EditText etfactoryProject;
    @Bind(R.id.etfactory_segment)
    EditText etfactorySegment;
    @Bind(R.id.etfactory_code)
    EditText etfactoryCode;
    @Bind(R.id.etfactory_manager)
    EditText etfactoryManager;
    @Bind(R.id.etfactory_phone)
    EditText etfactoryPhone;
    @Bind(R.id.etfactory_title)
    EditText etfactoryTitle;
    @Bind(R.id.etfactory_totalprod)
    EditText etfactoryTotalprod;
    @Bind(R.id.etfactory_distance)
    EditText etfactoryDistance;
    @Bind(R.id.etfactory_start)
    EditText etfactoryStart;
    @Bind(R.id.etfactory_end)
    EditText etfactoryEnd;
//    @Bind(R.id.etfactory_begin)
//    CalendarView etfactoryBegin;
//    @Bind(R.id.etfactory_finish)
//    CalendarView etfactoryFinish;
    @Bind(R.id.etfactory_maker)
    EditText etfactoryMaker;
    @Bind(R.id.etfactory_superunit)
    EditText etfactorySuperunit;
    @Bind(R.id.etfactory_part3)
    EditText etfactoryPart3;
    @Bind(R.id.etfactory_addr)
    EditText etfactoryAddr;
    @Bind(R.id.etfactory_builder)
    EditText etfactoryBuilder;
    @Bind(R.id.etfactory_supervisor)
    EditText etfactorySupervisor;
    @Bind(R.id.etfactory_intro)
    EditText etfactoryIntro;
    @Bind(R.id.etfactory_ablility)
    EditText etfactoryAblility;
    @Bind(R.id.etfactory_lowwam)
    EditText etfactoryLowwam;
    @Bind(R.id.etfactory_highwam)
    EditText etfactoryHighwam;
    @Bind(R.id.ivfactory_plan)
    ImageView ivfactoryPlan;
    @Bind(R.id.etfactory_build)
    EditText etfactoryBuild;
    @Bind(R.id.etfactory_store)
    EditText etfactoryStore;
    @Bind(R.id.btfactory_put)
    Button btfactoryPut;
    @Bind(R.id.etfactory_begin)
    MaterialCalendarView etfactoryBegin;
    @Bind(R.id.etfactory_finish)
    MaterialCalendarView etfactoryFinish;


    private String RequestName;
    private final int GET_PICTURE = 4521;

    private final int Upload_SUCCESS_FILE = 1207;
    private final int Upload_FALL = 1208;

    private File Image;
    private LoadingDialog mLoading;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Upload_SUCCESS_FILE:
                    mLoading.close();
                    Toast.makeText(FactoryDataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case Upload_FALL:
                    mLoading.close();
                    Toast.makeText(FactoryDataActivity.this, "修改失败，请核对重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_replay);
        ButterKnife.bind(this);
        ivfactoryPlan.setOnClickListener(this);
        RequestName = getIntent().getStringExtra("name");
        Image = new File(AllStaticBean.SaveImagePath + File.separator + RequestName + ".jpg");
        tvTitleTable.setText("修改梁场信息");
        ivBack.setOnClickListener(this);
        btfactoryPut.setOnClickListener(this);
//        etfactoryBegin.setOnDateChangeListener(this);
//        etfactoryFinish.setOnDateChangeListener(this);

        //自定义日历控件：
        etfactoryBegin.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {

                }
            }
        });
        etfactoryFinish.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {

                }

            }
        });

        try {
            SetFactoryContent();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /*令EditText失去焦点和获取焦点的函数*/
//    private void ClearFocus() {
//        for (EditText text : editTexts) {
//            text.setFocusable(false);
//            text.setFocusableInTouchMode(false);
//        }
//    }

//    private void RequestFocus() {
//        for (EditText text : editTexts) {
//            text.setFocusableInTouchMode(true);
//            text.setFocusable(true);
//            text.requestFocus();
//        }
//    }

    private void SetFactoryContent() throws FileNotFoundException {
        if (AllStaticBean.factoryDatas != null) {
            FactoryData FD = SearchNeedFactory();
//            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd", Locale.CHINA);
            if (FD != null) {
                etfactoryName.setText(FD.getName());
                etfactoryProject.setText(FD.getProject());
                etfactorySegment.setText(FD.getSegment());
                etfactoryCode.setText(FD.getCode());
                etfactoryManager.setText(FD.getManager());
                etfactoryPhone.setText(FD.getPhone());
                etfactoryTitle.setText(FD.getTitle());
                etfactoryTotalprod.setText(String.valueOf(FD.getTotalProd()));
                etfactoryDistance.setText(FD.getDistance());
                etfactoryStart.setText(FD.getStart());
                etfactoryEnd.setText(FD.getEnd());
                etfactoryMaker.setText(FD.getMaker());
                etfactorySuperunit.setText(FD.getSuperUnit());
                etfactoryPart3.setText(FD.getPart3());
                etfactoryAddr.setText(FD.getAddr());
                etfactoryBuilder.setText(FD.getBuilder());
                etfactorySupervisor.setText(FD.getSupervisor());
                etfactoryIntro.setText(FD.getIntro());
                etfactoryAblility.setText(String.valueOf(FD.getAbility()));
                etfactoryLowwam.setText(String.valueOf(FD.getLowWarn()));
                etfactoryHighwam.setText(String.valueOf(FD.getHighWarn()));
                etfactoryBuild.setText(FD.getBuild());
                etfactoryStore.setText(FD.getStore());

//                etfactoryBegin.setDate(FD.getBegin().getTime());
//                Log.e("SET etfactoryBegin", etfactoryBegin.getDate() + " ");
//                etfactoryFinish.setDate(FD.getFinish().getTime());
//                Log.e("SET etfactoryFinish", etfactoryFinish.getDate() + " ");

                etfactoryBegin.setSelectedDate(FD.getBegin());
                Log.e("SET etfactoryBegin", etfactoryBegin.getSelectedDate() + " ");
                etfactoryFinish.setSelectedDate(FD.getFinish());
                Log.e("SET etfactoryFinish", etfactoryFinish.getSelectedDate() + " ");

                ivfactoryPlan.setImageBitmap(GetBeamPlanImage());
            }
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//        String date = null;
//        switch (view.getId()) {
//
//            case R.id.etfactory_begin:
//                date = year + "-" + (month + 1) + "-" + dayOfMonth;
//                Log.e("onSelectedDayChange", "" + date);
//                try {
//                    etfactoryBegin.setDate(AllStaticBean.formatter.parse(date).getTime());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case R.id.etfactory_finish:
//                date = year + "-" + (month + 1) + "-" + dayOfMonth;
//                Log.e("onSelectedDayChange", "" + date);
//                try {
//                    etfactoryFinish.setDate(AllStaticBean.formatter.parse(date).getTime());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
    }

    private FactoryData SearchNeedFactory() {
        for (FactoryData factoryData : AllStaticBean.factoryDatas) {
            if (factoryData.getName().equals(RequestName)) {
                return factoryData;
            }
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        mLoading = new LoadingDialog(FactoryDataActivity.this, "数据上传中...");
        switch (view.getId()) {
            case iv_back:
                finish();
                break;
            case R.id.btfactory_put:
                mLoading.show();
                /*提交到数据库*/
                if (Image != null) {
                    UpDataRequest.ModifyFactoryDataRequest(FactoryDataTo(), Image,
                            Upload_SUCCESS_FILE, Upload_FALL, mHandler);
                } else
                    mHandler.sendEmptyMessage(Upload_FALL);
                break;
            case R.id.ivfactory_plan:
                /*打开相册上传相片*/
                Intent OpenPictuer = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenPictuer, GET_PICTURE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PICTURE && resultCode == Activity.RESULT_OK
                && data != null) {
            /*拿到返回，加载图片*/
            Uri SelectedImage = data.getData(); //获取图片的本地URL
            String[] filePasthColumns = {MediaStore.Images.Media.DATA}; //数据存放类型数组

            /**
             * Cursor 是每行的集合。
             * 使用 moveToFirst() 定位第一行。
             * 你必须知道每一列的名称。
             * 你必须知道每一列的数据类型。
             * Cursor 是一个随机的数据源。
             * 所有的数据都是通过下标取得。
             */
            assert SelectedImage != null;
            Cursor c = getContentResolver().query(SelectedImage, filePasthColumns,
                    null, null, null);
            //通过 Cursor 数据集合，操作所获取的 图片的数据

            if (c != null) {
                c.moveToFirst(); //移动光标到第一行
                int columnIndex = c.getColumnIndex(filePasthColumns[0]); // 返回指定列的名称，如果不存在返回-1
                String imagePath = c.getString(columnIndex); //获取url的字符串形式。
                Image = new File(imagePath);
                AllStaticBean.BeamPlanName = imagePath;
                Log.e("onActivityResult", imagePath + "  " + AllStaticBean.BeamPlanName);
                c.close();//关闭游标，释放资源
                ivfactoryPlan.setImageBitmap(BitmapFactory.decodeFile(imagePath)); // 运用BitMap加载图片
            }
        }
    }

    private FactoryData FactoryDataTo() {
        FactoryData factoryData = new FactoryData();
        factoryData.setAbility(Integer.parseInt(etfactoryAblility.getText().toString()));
        factoryData.setAddr(etfactoryAddr.getText().toString());
//        Log.e("etfactoryBegin", AllStaticBean.formatter.format(etfactoryBegin.getDate()));
//        Log.e("etfactoryFinish", AllStaticBean.formatter.format(etfactoryFinish.getDate()));
//        factoryData.setFinish(new Date(etfactoryFinish.getDate()));
//        factoryData.setBegin(new Date(etfactoryBegin.getDate()));
        factoryData.setFinish(new Date(etfactoryFinish.getSelectedDate().getDate().getTime()));
        factoryData.setBegin(new Date(etfactoryBegin.getSelectedDate().getDate().getTime()));
        factoryData.setBuild(etfactoryBuild.getText().toString());
        factoryData.setBuilder(etfactoryBuilder.getText().toString());
        factoryData.setCode(etfactoryCode.getText().toString());
        factoryData.setDistance(etfactoryDistance.getText().toString());
        factoryData.setEnd(etfactoryEnd.getText().toString());

        factoryData.setHighWarn(new BigDecimal(etfactoryHighwam.getText().toString()));
        factoryData.setIntro(etfactoryIntro.getText().toString());
        factoryData.setLowWarn(new BigDecimal(etfactoryLowwam.getText().toString()));
        factoryData.setMaker(etfactoryMaker.getText().toString());
        factoryData.setManager(etfactoryManager.getText().toString());
        factoryData.setPart3(etfactoryPart3.getText().toString());
        factoryData.setPhone(etfactoryPhone.getText().toString());
        factoryData.setPlan(AllStaticBean.BeamPlanName.substring(AllStaticBean.BeamPlanName.lastIndexOf("/") + 1));
        factoryData.setProject(etfactoryProject.getText().toString());
        factoryData.setSegment(etfactorySegment.getText().toString());
        factoryData.setStart(etfactoryStart.getText().toString());
        factoryData.setStore(etfactoryStore.getText().toString());
        factoryData.setSuperUnit(etfactorySuperunit.getText().toString());
        factoryData.setSupervisor(etfactorySupervisor.getText().toString());
        factoryData.setTitle(etfactoryTitle.getText().toString());
        factoryData.setName(etfactoryName.getText().toString());
        factoryData.setTotalProd(Short.parseShort(etfactoryTotalprod.getText().toString()));
        Log.e("FactoryDataTo", AllStaticBean.GsonToDate.toJson(factoryData));
        return factoryData;
    }

    private Bitmap GetBeamPlanImage() throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(AllStaticBean.BeamPlanName);

        return BitmapFactory.decodeStream(fileInputStream);
    }

}
