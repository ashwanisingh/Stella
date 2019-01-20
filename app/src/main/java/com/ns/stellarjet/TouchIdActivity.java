package com.ns.stellarjet;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.ns.networking.model.UserData;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.UIConstants;

import java.util.Objects;

public class TouchIdActivity extends AppCompatActivity implements BiometricCallback {

    private UserData mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toucd_id);

        mUserData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_USER_DATA);

        new BiometricManager.BiometricBuilder(TouchIdActivity.this)
                .setTitle(getString(R.string.biometric_title))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                .build()
                .authenticate(TouchIdActivity.this);
    }


    @Override
    public void onSdkVersionNotSupported() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        launchPasscodeActivity();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        launchPasscodeActivity();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        launchPasscodeActivity();
    }

    @Override
    public void onAuthenticationSuccessful() {
        launchHomeActivity();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
        launchPasscodeActivity();
    }

    private void launchHomeActivity(){
        Intent mHomeIntent = new Intent(TouchIdActivity.this , HomeActivity.class);
        // send bundle
        mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        startActivity(mHomeIntent);
        finish();
    }

    private void launchPasscodeActivity(){
        Intent mPasscodeIntent = new Intent(TouchIdActivity.this , PassCodeActivity.class);
        // send bundle
        mPasscodeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        startActivity(mPasscodeIntent);
        finish();
    }
}
