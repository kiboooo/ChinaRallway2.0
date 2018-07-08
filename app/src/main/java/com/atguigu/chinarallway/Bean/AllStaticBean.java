package com.atguigu.chinarallway.Bean;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kiboooo on 2017/9/13.
 * 所有的类，以及请求；
 *
 */

public class AllStaticBean {

    public static String mRespenseCookie = null;
    /*测试服务器*/
//    public static String URL = "http://139.199.20.248:8080/ChinaRailWay";
//    public static String URL = "http://222.24.63.119:8080/ChinaRailWay";


    /* 阿里云的服务器请求 */
    public static String URL = "http://47.95.217.16/ChinaRailWay";
    public static String StartProductonURL = "http://47.95.217.16/crbf";
//    public static String StartProductonURL = "http://222.24.63.119/crbf";
    public static String LoginURL = "/login";
    public static String GroupURL = "/search";
    public static String StartURL = "/buildPlan/make.do";
    public static String UpdateURL = "/task/update.do";
    public static String CheckVersionURL = "/checkVersion";
    public static String ProductionURL = "/modifyTask";
    public static String BuildPlanURL = "/modifyBuildPlan";
    public static String ModifyURL = "/modify";
    public static String FactoryModifyURL = "/factoryModify";
    public static String AddInfoURL = "/addInfo";
    public static String FileUploadURL = "/fileUpload";
    public static String ChangeBeamStatusURL = "/changeBeamStatus";
    public static String FileSearchURL = "/fileSearch";
    public static String FileDownLoadURL = "/download";
    public static String APKDownLoad = "/getAPK";
    public static String changeBeamFind = "/changeBeamFind";
    public static String BeamPlanName = "";
    public static String BeamVideoName = "";
    public static BridgeData[] BridgeName = null;
    public static StoreData[] mStoreData = null;
    public static FactoryData[] factoryDatas = null;
    public static BeamData[] MaxBeamData = null;
    public static MakePosition[] makePositions = null;
    public static StorePositionData[] storePositionDatas = null;
    public static CheckRecData[] checkRecDatas = null;
    public static TaskWithBeam[] taskWithBeams = null;
    public static BuildPlan[] buildPlens = null;
    public static TaskData[] TaskData = null;
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static SimpleDateFormat formatter_accuracy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static String SaveImagePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"CrbfImages";
    public static String SaveVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Movies";
    public static String downloadVideoDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"downloadVideo";
    public static String downloadAPKPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "ChinaRallWay.apk";

    public static Gson GsonToDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static Gson GsonToData_Accuracy = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /*深度复制的函数，实现数组的Clone效果*/
    public static <T> List<T> DeepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
