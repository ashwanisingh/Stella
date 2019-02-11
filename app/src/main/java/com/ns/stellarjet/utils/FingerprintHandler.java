package com.ns.stellarjet.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.legacy.app.ActivityCompat;
import com.ns.networking.model.UserData;
import com.ns.stellarjet.PassCodeActivity;
import com.ns.stellarjet.R;
import com.ns.stellarjet.home.HomeActivity;


@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;
    private UserData mUserData;


    public FingerprintHandler(Context mContext , UserData userDataParams) {
        context = mContext;
        mUserData = userDataParams;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        Log.d("FingerprintHandler", "onAuthenticationError " + errString);
    }

    @Override
    public void onAuthenticationFailed() {
        showPassCodeDialog();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        Log.d("FingerprintHandler", "onAuthenticationHelp: " +helpString);
    }


    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        launchHomeActivity();

    }


    private void launchHomeActivity(){
        Intent mHomeIntent = new Intent(context, HomeActivity.class);
        // send bundle
        mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        mHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mHomeIntent);
        ((Activity)context).finish();
    }


    private void launchPasscodeActivity(){
        Intent mPasscodeIntent = new Intent(context , PassCodeActivity.class);
        // send bundle
        mPasscodeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        mPasscodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mPasscodeIntent);
        ((Activity)context).finish();
    }


    private void showPassCodeDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Authentication failed. please enter passcode");
        alertDialogBuilder.setPositiveButton("Ok",
                (arg0, arg1) -> launchPasscodeActivity());

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    context.getResources().getColor(R.color.colorButtonNew)
            );
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    context.getResources().getColor(R.color.colorButtonNew)
            );
        });

        alertDialog.show();
    }


}
