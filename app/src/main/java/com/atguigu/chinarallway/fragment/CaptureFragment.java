package com.atguigu.chinarallway.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.chinarallway.Bean.AllStaticBean;
import com.atguigu.chinarallway.Bean.BeamData;
import com.atguigu.chinarallway.Dialog.LoadingDialog;
import com.atguigu.chinarallway.R;
import com.atguigu.chinarallway.RequstServer.ManagerRequst;
import com.atguigu.chinarallway.RequstServer.UpDataRequest;
import com.google.gson.Gson;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 陈雨田 on 2017/9/26.
 * Tip：上传视屏的Fragment界面
 */

public class CaptureFragment extends Fragment implements View.OnClickListener {

    private final String KEY_STATUSMESSAGE = "com.jmolsmobile.statusmessage";
    private final String KEY_ADVANCEDSETTINGS = "com.jmolsmobile.advancedsettings";
    private final String KEY_FILENAME = "com.jmolsmobile.outputfilename";

    private final String[] RESOLUTION_NAMES = new String[]{"1080p", "720p", "480p"};
    private final String[] QUALITY_NAMES = new String[]{"high", "medium", "low"};

    private String statusMessage = null;
    private String filename = null;

    private ImageView thumbnailIv;
    private TextView statusTv;
    private Spinner resolutionSp;
    private Spinner qualitySp;

    private RelativeLayout advancedRl;
    private Spinner fileNameSpinner;
    private Spinner fileNameSpinner_LR;
    private Spinner fileNameSpinner_ID;
    private EditText maxDurationEt;
    private EditText maxFilesizeEt;
    private EditText fpsEt;
    private CheckBox showTimerCb;
    private CheckBox allowFrontCameraCb;
    private LoadingDialog loading;

    private BeamData beamData = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final Button captureBtn = (Button) rootView.findViewById(R.id.btn_capturevideo);
        final Button captureUp = (Button) rootView.findViewById(R.id.Up_capturevideo);
        captureBtn.setOnClickListener(this);
        captureUp.setOnClickListener(this);

        thumbnailIv = (ImageView) rootView.findViewById(R.id.iv_thumbnail);
        thumbnailIv.setOnClickListener(this);
        statusTv = (TextView) rootView.findViewById(R.id.tv_status);
        advancedRl = (RelativeLayout) rootView.findViewById(R.id.rl_advanced);
        fileNameSpinner = (Spinner) rootView.findViewById(R.id.et_filename);
        fileNameSpinner_ID = rootView.findViewById(R.id.et_filename_ID);
        fileNameSpinner_LR = rootView.findViewById(R.id.et_filename_LR);
        fpsEt = (EditText) rootView.findViewById(R.id.et_fps);
        allowFrontCameraCb = (CheckBox) rootView.findViewById(R.id.cb_show_camera_switch);
        maxFilesizeEt = (EditText) rootView.findViewById(R.id.et_filesize);
//        maxDurationEt = (EditText) rootView.findViewById(R.id.et_duration);
//        showTimerCb = (CheckBox) rootView.findViewById(R.id.cb_showtimer);

        if (savedInstanceState != null) {
            statusMessage = savedInstanceState.getString(KEY_STATUSMESSAGE);
            filename = savedInstanceState.getString(KEY_FILENAME);
            //advancedRl.setVisibility(savedInstanceState.getInt(KEY_ADVANCEDSETTINGS));
        }

        updateStatusAndThumbnail();
//        initializeSpinners(rootView);
        loading = new LoadingDialog(getContext(),"正在加载数据");
        loading.show();
        requestData();
        return rootView;
    }

    private void requestData(){
        ManagerRequst.AllRequest(
                "beam", "","", "1",
                handler,
                1,
                0
        );
    }
    private void initializeSpinners() {
        final BeamData[] originalData = AllStaticBean.MaxBeamData;
        final List<String> spinnerData = new ArrayList<>();
        for (BeamData beamData : originalData) {
            if(spinnerData.indexOf(beamData.getbName())<0)
//            spinnerData.add(beamData.getbName()+" "+beamData.getbID());
                spinnerData.add(beamData.getbName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,spinnerData);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        fileNameSpinner.setAdapter(arrayAdapter);
        fileNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onBind_LorR_Sprinner(spinnerData.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //绑定梁板的左右；
    private void onBind_LorR_Sprinner(final String beamName) {
        final List<String> BeamLorR = new ArrayList<>();
        //筛选左右线
        for (int i = 1; i < AllStaticBean.MaxBeamData.length + 1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName)
                    && BeamLorR.indexOf(temp.getbID().substring(0, 2)) < 0) {
                BeamLorR.add(temp.getbID().substring(0, 2));
            }
        }
        ArrayAdapter<String> LorR_Adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,BeamLorR );
        fileNameSpinner_LR.setAdapter(LorR_Adapter);
        fileNameSpinner_LR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!beamName.equals("") && !BeamLorR.get(position).equals("")) {
                    onBind_ID_Sprinner(beamName,BeamLorR.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //绑定梁板的Id
    private void onBind_ID_Sprinner(final String beamName, final String lr) {
        final List<String> BeamID = new ArrayList<>();
        for (int i = 1; i < AllStaticBean.MaxBeamData.length+1; i++) {
            BeamData temp = AllStaticBean.MaxBeamData[i - 1];
            if (temp.getbName().equals(beamName) && temp.getbID().substring(0, 2).equals(lr)) {
                BeamID.add(temp.getbID().substring(2));
            }
        }
        ArrayAdapter<String> Id_Adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, BeamID);
        fileNameSpinner_ID.setAdapter(Id_Adapter);
        fileNameSpinner_ID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filename = beamName+" "+lr+BeamID.get(position);
                for (BeamData maxBeamDatum : AllStaticBean.MaxBeamData) {
                    if (maxBeamDatum.getbID().equals(lr + BeamID.get(position))
                            && maxBeamDatum.getbName().equals(beamName)) {
                        beamData = maxBeamDatum;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_STATUSMESSAGE, statusMessage);
        outState.putString(KEY_FILENAME, filename);
        outState.putInt(KEY_ADVANCEDSETTINGS, advancedRl.getVisibility());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_capturevideo) {
            startVideoCaptureActivity();
        } else if (v.getId() == R.id.iv_thumbnail) {
            playVideo();
        } else if (v.getId() == R.id.Up_capturevideo) {

            if (statusTv.getText().equals(getString(R.string.status_nocapture))) {
                Toast.makeText(getContext(), "请录制视频", Toast.LENGTH_SHORT).show();
            } else {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                filename+="_"+simpleDateFormat.format(date)+".mp4";
                System.out.println(statusTv.getText().toString());
                String name = (new File(filename)).getName();
                String path = statusTv.getText().toString();
                int index = statusTv.getText().toString().indexOf("/");
                path = statusTv.getText().toString().substring(index,path.length());
                showDialog("正在上传");
                /*上传视屏*/
                UpDataRequest.FileUpLoadRequest(
                        beamData.getbID(),
                        beamData.getbName(),
                        "video",
                        name.substring(0,name.indexOf(".")),
                        "mp4",
                        new File(path),
                        10, 11,handler);

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.capture_demo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_advanced:
                toggleAdvancedSettings();
                break;

        }
        return true;
    }

    private void toggleAdvancedSettings() {
        advancedRl.setVisibility(advancedRl.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }


    private boolean canHandleIntent(Intent intent) {
        final PackageManager mgr = getActivity().getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void startVideoCaptureActivity() {
        final CaptureConfiguration config = createCaptureConfiguration();
        if(filename==null){
            Toast.makeText(getContext(),"请您先选择桥和梁板信息", Toast.LENGTH_SHORT).show();
            return;
        }

        final Intent intent = new Intent(getActivity(), VideoCaptureActivity.class);
        intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
        intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            filename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
            statusMessage = String.format(getString(R.string.status_capturesuccess), filename);
        } else if (resultCode == Activity.RESULT_CANCELED) {
            filename = null;
            statusMessage = getString(R.string.status_capturecancelled);
        } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
            filename = null;
            statusMessage = getString(R.string.status_capturefailed);
        }
        updateStatusAndThumbnail();

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateStatusAndThumbnail() {
        if (statusMessage == null) {
            statusMessage = getString(R.string.status_nocapture);
        }
        statusTv.setText(statusMessage);

        final Bitmap thumbnail = getThumbnail();

        if (thumbnail != null) {
            thumbnailIv.setImageBitmap(thumbnail);
        } else {
            thumbnailIv.setImageResource(R.drawable.thumbnail_placeholder);
        }
    }

    private Bitmap getThumbnail() {
        if (filename == null) return null;
        return ThumbnailUtils.createVideoThumbnail(filename, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }

    private CaptureConfiguration createCaptureConfiguration() {
//        final PredefinedCaptureConfigurations.CaptureResolution resolution = getResolution(resolutionSp.getSelectedItemPosition());
//        final PredefinedCaptureConfigurations.CaptureQuality quality = getQuality(qualitySp.getSelectedItemPosition());
        final PredefinedCaptureConfigurations.CaptureResolution resolution =
                PredefinedCaptureConfigurations.CaptureResolution.RES_480P;
        final PredefinedCaptureConfigurations.CaptureQuality quality =
                PredefinedCaptureConfigurations.CaptureQuality.MEDIUM;

        CaptureConfiguration.Builder builder = new CaptureConfiguration.Builder(resolution, quality);

        try {
//            int maxDuration = Integer.valueOf(maxDurationEt.getEditableText().toString());
//            builder.maxDuration(maxDuration);
            builder.maxDuration(10);
        } catch (final Exception e) {
            //NOP
        }
        try {
            int maxFileSize = Integer.valueOf(maxFilesizeEt.getEditableText().toString());
            builder.maxFileSize(maxFileSize);
        } catch (final Exception e) {
            //NOP
        }
        try {
            int fps = Integer.valueOf(fpsEt.getEditableText().toString());
            builder.frameRate(fps);
        } catch (final Exception e) {
            //NOP
        }
//        if (showTimerCb.isChecked()) {
        builder.showRecordingTime();
//            }
        if (!allowFrontCameraCb.isChecked()) {
            builder.noCameraToggle();
        }

        return builder.build();
    }

    private PredefinedCaptureConfigurations.CaptureQuality getQuality(int position) {
        final PredefinedCaptureConfigurations.CaptureQuality[] quality =
                new PredefinedCaptureConfigurations.CaptureQuality[]{PredefinedCaptureConfigurations.CaptureQuality.HIGH, PredefinedCaptureConfigurations.CaptureQuality.MEDIUM,
                PredefinedCaptureConfigurations.CaptureQuality.LOW};
        return quality[position];
    }

    private PredefinedCaptureConfigurations.CaptureResolution getResolution(int position) {
        final PredefinedCaptureConfigurations.CaptureResolution[] resolution = new PredefinedCaptureConfigurations.CaptureResolution[]{PredefinedCaptureConfigurations.CaptureResolution.RES_1080P,
                PredefinedCaptureConfigurations.CaptureResolution.RES_720P, PredefinedCaptureConfigurations.CaptureResolution.RES_480P};
        return resolution[position];
    }

    public void playVideo() {
        if (filename == null) return;

        final Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(Uri.parse(filename), "video/*");
        try {
            startActivity(videoIntent);
        } catch (ActivityNotFoundException e) {
            // NOP
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    dissmisDialog();
                    Toast.makeText(getContext(), "加载数据成功", Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = (JSONArray)msg.obj;
                    AllStaticBean.MaxBeamData = (new Gson()).fromJson(jsonArray.toString(),BeamData[].class);
                    initializeSpinners();
                    break;
                }
                case 0:{
                    dissmisDialog();
                    Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 10:{
                    dissmisDialog();
                    Toast.makeText(getContext(), "上传成功",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 11:{
                    dissmisDialog();
                    Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    private void dissmisDialog(){
        loading.close();
    }

    private void showDialog(String msg){
        loading = new LoadingDialog(getContext(),msg);
        loading.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}