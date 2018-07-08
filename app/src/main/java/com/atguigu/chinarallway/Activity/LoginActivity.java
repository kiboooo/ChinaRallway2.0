package com.atguigu.chinarallway.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.SavePassWordData;
import com.atguigu.chinarallway.Bean.UserData;
import com.atguigu.chinarallway.Dialog.LoginDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.LoginRequst;
import com.atguigu.chinarallway.application.MyApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 陈雨田 on 2017/9/11.
 * Function : 登录界面
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.keep_password)
    CheckBox keepPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.login_set_server)
    TextView setServer;

    DownLoadChangeObserver downLoadChangeObserver;

    public static final int SUCCESS = 666;
    public static final int FALL = 888;
    public static final int VERSION_UPDATE = 6668;
    public static final int VERSION_FALL = 6688;
    public static UserData userData = new UserData();
    private BroadcastReceiver receiver;
    private long mReference;
    private DownloadManager manager;

    private LoginDialog loginDialog;
    private ProgressDialog progressDialog;


    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};
    /**
     * 得到SharedPreferences对象的一种方法：
     * PreferenceManager类中的getDefaultSharedPreferences（）方法
     * 这是一个静态方法，他接收一个Context参数，并自动使用当前应用程序的包名作为前缀来命名SharedPreferences文件
     * 得到了SharedPreferences对象之后，就可以开始向SharedPreferences文件中储存数据了。
     * 分三步：
     * 1.调用SharedPreferences对象的edit（）方法来获取一个 SharedPreferences.Editor对象。
     * 2.向 SharedPreferences.Editor 对象中添加数据，比如添加一个布尔型数据就使用 putBoolean（）方法，
     * 添加一个字符串则使用putString（）等；
     * 3. 调用apply（）方法将添加的数据提交，从而完成数据的存储操作。
     */
    private static SharedPreferences sharedPreferences;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Log.e("text", userData.getUid() + userData.getUname() + "  " + userData.getRoleId());
                    loginDialog.close();
                    SavePassWordData savePassWordData = (SavePassWordData) msg.obj;
                    userData = savePassWordData.getUserData();
                    //存放用户名和密码的
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //判断是否勾选，并且把用户名和密码保存。
                    if (keepPassword.isChecked()) {
                        editor.putBoolean("remenber_password", true);
                        editor.putString("Username", userData.getUid());
                        editor.putString("Password", savePassWordData.getPassword());

                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    switch (userData.getRoleId()) {
                        case 1:
                            /*管理员*/
                            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                            break;
                        case 2:
                            /*生产者*/
                            startActivity(new Intent(LoginActivity.this, ProducerActivity.class));
                            break;
                        case 3:
                            /*梁场技术员*/
                            startActivity(new Intent(LoginActivity.this, BeamfieldActivity.class));
                            break;
                        case 4:
                            /*架梁技术员*/
                            startActivity(new Intent(LoginActivity.this, BeamTechnicianActivity.class));
                            break;
                        case 5:
                            /*实验员*/
                            startActivity(new Intent(LoginActivity.this, LaboratoryActivity.class));
                            break;
                    }
                    finish();
                    break;
                case FALL:
                    loginDialog.close();
                    Toast.makeText(LoginActivity.this, "输入信息错误，请核对！", Toast.LENGTH_SHORT).show();
                    break;

                case VERSION_UPDATE:
                    /*执行下载的逻辑*/
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("发现新版本");
                    dialog.setMessage("为了确保您的用户体验，请及时更新");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("更 新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DownloadAPK(AllStaticBean.URL+AllStaticBean.APKDownLoad);
                            progressDialog.show();
                        }
                    });
                    dialog.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
                case VERSION_FALL:
                    Log.e("VERSION_FALL", "当前是最新版本！");
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication.getinstance().addActivity(this);
        ButterKnife.bind(this);
        initView();
        SetImmerseBar();
        getPermission();
    }

    private void getPermission(){
        //核对权限
        boolean isAllGet = checkePermissionAllGet(permissions);
        if(!isAllGet){
            //依次请求权限
            ActivityCompat.requestPermissions(this,permissions,100);
        }
    }

    //响应权限请求
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100){
            boolean flag = true;
            for (int result : grantResults) {
                if(result == PackageManager.PERMISSION_DENIED){
                    flag = false;
                    break;
                }
            }
            if(!flag){
                openAppDetails();
            }
        }
    }

    private void openAppDetails() {
        //若然发现权限遭到拒绝，强制跳转进行手动授权。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("梁板视频的录制需要文件读写，相机，声音录制权限，否则无法正常使用应用，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private boolean checkePermissionAllGet(String[] permissions){
        boolean flag = true;
        for (String s : permissions) {
            if(ContextCompat.checkSelfPermission(this,s)== PackageManager.PERMISSION_DENIED){
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean onKeyDown(final int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog exitDialog = new AlertDialog.Builder(this).create();
            exitDialog.setTitle("温馨提示：");
            exitDialog.setMessage("你确定要退出吗");
            exitDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyApplication.getinstance().closeAllActiivty();
                }
            });
            exitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            exitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                //不允许点击back时，导致Dialog消失。
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return true;
                }
            });
            exitDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        MyApplication.getinstance().removeActivity(this);
    }

    /**
     * 这里使用了SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION，表示会让应用的主体内容占用系统导航栏的空间，
     * 然后又调用了setNavigationBarColor()方法将导航栏设置成透明色。
     * <p>
     * * 在布局文件中设置完毕后，还需要在onCreate()里面加上如下的代码：
     * 设置状态栏颜色为透明：getWindow().setStatusBarColor(Color.TRANSPARENT);
     * 设置状态栏和APP的位置关系：
     * getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
     * setSystemUiVisibility(int visibility)传入的实参类型如下：
     * <p>
     * SYSTEM_UI_FLAG_FULLSCREEN： 隐藏状态栏
     * SYSTEM_UI_FLAG_HIDE_NAVIGATION： 隐藏导航栏
     * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN： Activity全屏显示，但是状态栏不会被覆盖掉，让状态栏上浮在Activity上
     * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION： 让导航栏悬浮在Activity上
     * SYSTEM_UI_FLAG_LAYOUT_STABLE： 保持整个View的稳定，使其不会随着SystemUI的变化而变化；
     * SYSTEM_UI_FLAG_IMMERSIVE：沉浸模式；
     * SYSTEM_UI_FLAG_IMMERSIVE_STICKY：沉浸模式且状态栏和导航栏出现片刻后会自动隐藏；
     */
    private void SetImmerseBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private void initView() {
        btnLogin.setOnClickListener(this);
        setServer.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("APK下载中");
        progressDialog.setMessage("请耐心等候:");
        progressDialog.setCancelable(false);
        downLoadChangeObserver = new DownLoadChangeObserver(mHandler);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    //下载完成了
                    //获取当前完成任务的ID
                    long reference = intent.
                            getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    //判断是否是下载的APK
                    if (mReference == reference) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(reference);
                        Cursor myDownload = manager.query(query);
                        if (myDownload.moveToFirst()) {
                            if (DownloadManager.STATUS_SUCCESSFUL ==
                                    myDownload.getInt(myDownload.
                                            getColumnIndex(DownloadManager.COLUMN_STATUS))) {

                                String apkFilePath = myDownload.
                                        getString(myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                Log.e("下载URl为", apkFilePath);

                                File file = new File(AllStaticBean.downloadAPKPath.substring(0,
                                        AllStaticBean.downloadAPKPath.lastIndexOf("/"))
                                        + apkFilePath.substring(apkFilePath.lastIndexOf("/")));

                                InstallApk(context, file);
                                progressDialog.dismiss();
                            }
                            progressDialog.dismiss();
                        }
                        myDownload.close();
                    }
                    progressDialog.dismiss();
                }

                if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                    //广播被点击了
                    progressDialog.dismiss();
                }
            }
        };

        IntentFilter filter = new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE ) ;
        registerReceiver( receiver , filter ) ;

        if (sharedPreferences.getBoolean("remenber_password", false)) {
            etName.setText(sharedPreferences.getString("Username", ""));
            etPassword.setText(sharedPreferences.getString("Password", ""));
            keepPassword.setChecked(true);
        }
        Log.e("check", "准备checkVersion");
        /*检查版本更新*/
        LoginRequst.CheckVersion(getApplicationContext(), mHandler, VERSION_UPDATE, VERSION_FALL);
    }

    @Override
    public void onClick(View v) {
        loginDialog = new LoginDialog(this, "正在登陆，请稍后...");
        switch (v.getId()) {
            case R.id.btn_login: {
                if (JudgeNull(etName) && JudgeNull(etPassword)) {
                    loginDialog.show();
                    try {
                        LoginRequst.RLogin(String.
                                        valueOf(etName.getText())
                                , String.valueOf(etPassword.getText())
                                , CodingSHA2_256(String.valueOf(etPassword.getText()))
                                , mHandler);
                    } catch (Exception e) {
                        loginDialog.close();
                    }
                }else {
                    if (!JudgeNull(etName) && JudgeNull(etPassword))
                        Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    else if (JudgeNull(etName) && !JudgeNull(etPassword))
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.login_set_server: {
                Intent intent = new Intent(this,SetServerParamActivity.class);
                startActivity(intent);
            }
        }
    }

    private String CodingSHA2_256(String Password) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(Password.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();

        for (byte aByte : bytes) {
            String temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    private boolean JudgeNull(EditText edittext) {
        return !(edittext.getText().toString().length() == 0);
    }

    /*下载APK*/
    private void DownloadAPK(final String Url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url));
        request.setDescription("ChinaRallWay更新下载中...");
        request.setTitle("APK下载");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        File file = new File(AllStaticBean.downloadAPKPath);
        Log.e("存放APK路径", file.getPath());

        //调用下载器，放入文件的存放地址下载；
        request.setDestinationUri(Uri.fromFile(file));
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            mReference =manager.enqueue(request);
        }
    }

    /*打开安装程序*/
    private void InstallApk(final Context context ,File SavaApkPath) {
        Intent intent =  new Intent(Intent.ACTION_VIEW);
        if ( Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context,
                    "com.atguigu.chinarallway.fileprovider", SavaApkPath);
            Log.e("安装apk的路径:", apkUri.toString() + "  " + apkUri.getPath());
            /*大坑！！！，set属性千万不要写在add属性后面！！！*/
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(SavaApkPath),
                            "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    /*注册ContentObserver内容监听者*/
    class DownLoadChangeObserver extends ContentObserver {

        public DownLoadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }

    /*下载过程监听*/
    private void updateView() {
        int[] bytesAndStatus = new int[] { -1, -1, 0 };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mReference);
        Cursor c = null;
        try {
            c = manager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (bytesAndStatus[2]==DownloadManager.STATUS_FAILED||
                        bytesAndStatus[2]==DownloadManager.STATUS_PAUSED)
                    progressDialog.dismiss();
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        progressDialog.setMax(bytesAndStatus[1]);
        progressDialog.setProgress(bytesAndStatus[0]);
//        Log.e("updateView", bytesAndStatus[0] + "----" + bytesAndStatus[1] + "  status: " + bytesAndStatus[2]);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //设置监听Uri.parse("content://downloads/my_downloads")
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"),
                true, downLoadChangeObserver);

        Log.e("内存分配：", "最大内存：" + Runtime.getRuntime().maxMemory() +
                " 本APP最大内存：" + Runtime.getRuntime().totalMemory());
    }
    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(downLoadChangeObserver);
    }
}
