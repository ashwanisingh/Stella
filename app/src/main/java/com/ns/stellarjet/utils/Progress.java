package com.ns.stellarjet.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import com.ns.stellarjet.R;

import java.util.Objects;

public class Progress {

    public static Progress progress;
    private Dialog mDialog;

    public Progress() {
    }

    public static Progress getInstance() {
        if (progress == null)
            progress = new Progress();
        return progress;
    }

    public void showProgress(Context mContext) {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_progres);
        mDialog.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
