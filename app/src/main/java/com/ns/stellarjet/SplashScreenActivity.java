package com.ns.stellarjet;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.networking.utils.Constants;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.login.LoginActivity;
import com.ns.stellarjet.login.PasswordActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import com.ns.stellarjet.utils.UIConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            VideoView videoHolder = new VideoView(this);
            ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT ,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            videoHolder.setLayoutParams(mLayoutParams);
            videoHolder.setFitsSystemWindows(true);
            setContentView(videoHolder);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.stellar_jet_splash);
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoHolder.start();
        } catch (Exception ex) {
            jump();
        }

        /*if(StellarJetUtils.isConnectingToInternet(SplashScreenActivity.this)){
            if(SharedPreferencesHelper.isUserLoggedIn(SplashScreenActivity.this)){
                getUserData();
            }else {
                startActivity(new Intent(SplashScreenActivity.this , LoginActivity.class));
                finish();
            }
        }else{
            Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void getUserData(){
        Call<LoginResponse> mCustomerDataResponseCall = RetrofitAPICaller.getInstance(SplashScreenActivity.this)
                .getStellarJetAPIs().getCustomerData(
                        SharedPreferencesHelper.getUserToken(SplashScreenActivity.this),
                        SharedPreferencesHelper.getUserId(SplashScreenActivity.this)
                );

        mCustomerDataResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("Splash", "onResponse: " + response.body());
                Intent mHomeIntent = new Intent(
                        SplashScreenActivity.this ,
                        PassCodeActivity.class
                );
                mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA, response.body().getData().getUser_data());
                startActivity(mHomeIntent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("splash", "onFailure: " + t);
            }
        });
    }

    private void jump() {
        if (isFinishing())
            return;
        if(StellarJetUtils.isConnectingToInternet(SplashScreenActivity.this)){
            if(SharedPreferencesHelper.isUserLoggedIn(SplashScreenActivity.this)){
                getUserData();
            }else {
                startActivity(new Intent(SplashScreenActivity.this , LoginActivity.class));
                finish();
            }
        }else{
            Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

}
