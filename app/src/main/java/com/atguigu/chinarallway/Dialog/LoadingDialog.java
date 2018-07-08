package com.atguigu.chinarallway.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.chinarallway.R;
import com.ldoublem.loadingviewlib.view.LVBlock;

/**
 * Created by Kiboooo on 2017/9/25.
 *
 */

public class LoadingDialog {
    private LVBlock mLoading;
    private Dialog mLoadingDialog;

    public LoadingDialog(Context context, String msg) {

        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_view);
        mLoading = (LVBlock) view.findViewById(R.id.lv_block_dialog);
        TextView logingText = (TextView) view.findViewById(R.id.loading_text);
        logingText.setText(msg);
        mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void show() {
        mLoadingDialog.show();
        mLoading.startAnim();
    }

    public void close() {
        if (mLoadingDialog!=null) {
            mLoading.stopAnim();
            mLoadingDialog.dismiss();
            //mLoadingDialog=null;
        }
    }

}
