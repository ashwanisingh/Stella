package com.ns.stellarjet.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.ForgotPasswordResponse;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.PassCodeActivity;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityPasswordBinding;
import com.ns.stellarjet.utils.*;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class PasswordActivity extends AppCompatActivity {

    private ActivityPasswordBinding mActivityPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityPasswordBinding = DataBindingUtil.setContentView(
                this ,
                R.layout.activity_password
        );

        mActivityPasswordBinding.editTextAccountPassword
                .setTransformationMethod(
                        new MyPasswordTransformationMethod()
                );

        String username = getIntent().getStringExtra("userEmail");

        mActivityPasswordBinding.btnLogin.setOnClickListener(v -> {
            String password = mActivityPasswordBinding.editTextAccountPassword.getText().toString();
            if(password.isEmpty()){
                showPasswordErrorField();
            }else {
                if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
                    doLogin(username , password);
                }else{
                    UiUtils.Companion.showNoInternetDialog(PasswordActivity.this);
                }
            }
        });

        mActivityPasswordBinding.textviewForgotPassword.setOnClickListener(v -> {
            forgotPassword(username);
        });
    }


    private void forgotPassword(String username){
        Call<ForgotPasswordResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(PasswordActivity.this)
                .getStellarJetAPIs().forgotPassword(username);

        mLoginResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if(response.body()!=null){
                    UiUtils.Companion.showToast(
                            PasswordActivity.this,
                            response.body().getMessage()
                    );
                }else if(response.errorBody()!=null){
                    UiUtils.Companion.showServerErrorDialog(PasswordActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Log.d("Password", "onFailure: " + t);
                UiUtils.Companion.showServerErrorDialog(PasswordActivity.this);
            }
        });

    }

    private void doLogin(String username , String password){
        final Progress progress = Progress.getInstance();
        progress.showProgress(PasswordActivity.this);
        Call<LoginResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(PasswordActivity.this)
                .getStellarJetAPIs().doLogin(username , password);

        mLoginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progress.hideProgress();
                if (response.body()!=null){
                    Log.d("Password", "onResponse: " + response.body());
                    SharedPreferencesHelper.saveUserToken(PasswordActivity.this , response.body().getData().getToken());
                    SharedPreferencesHelper.saveUserId(PasswordActivity.this , String.valueOf(response.body().getData().getUser_data().getUser_id()));
                    SharedPreferencesHelper.saveUserRefreshToken(PasswordActivity.this, response.body().getData().getRefresh_token());
                    Intent mPassCodeIntent = new Intent(
                            PasswordActivity.this ,
                            PassCodeActivity.class
                    );
                    mPassCodeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , response.body().getData().getUser_data());
                    mPassCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mPassCodeIntent);
                }else {
                    try {
                        JSONObject mJsonObject  = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        UiUtils.Companion.showSimpleDialog(PasswordActivity.this , errorMessage );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progress.hideProgress();
                UiUtils.Companion.showServerErrorDialog(PasswordActivity.this);
                Log.d("Password", "onFailure: " + t);
            }
        });
    }

    private void showPasswordErrorField(){
        mActivityPasswordBinding.editTextAccountPassword.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        mActivityPasswordBinding.editTextAccountPassword.setBackground(getDrawable(R.drawable.drawable_edittext_error_background));
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
        animShake.setDuration(50);
        mActivityPasswordBinding.editTextAccountPassword.startAnimation(animShake);
    }

}
