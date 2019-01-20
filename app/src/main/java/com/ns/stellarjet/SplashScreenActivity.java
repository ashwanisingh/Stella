package com.ns.stellarjet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.model.RefreshTokenResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.login.LoginActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import com.ns.stellarjet.utils.UIConstants;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

//import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        /*try {
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
        }*/
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

    private void getUserData(){
        Call<LoginResponse> mCustomerDataResponseCall = RetrofitAPICaller.getInstance(SplashScreenActivity.this)
                .getStellarJetAPIs().getCustomerData(
                        SharedPreferencesHelper.getUserToken(SplashScreenActivity.this),
                        SharedPreferencesHelper.getUserId(SplashScreenActivity.this)
                );

        mCustomerDataResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body()!=null){
                    Log.d("Splash", "onResponse: " + response.body());
                    Intent mHomeIntent = new Intent(
                            SplashScreenActivity.this ,
                            TouchIdActivity.class
                    );
                    mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA, response.body().getData().getUser_data());
                    startActivity(mHomeIntent);
                    finish();
                }else {
                    try {
                        JSONObject mJsonObject  = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        if(errorMessage.equalsIgnoreCase(UIConstants.USER_TOKEN_EXPIRY)){
//                            getNewToken();
                            Log.d("Splash", "onResponse: Expiry");
                        }else {
                            Toast.makeText(SplashScreenActivity.this , errorMessage , Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("splash", "onFailure: " + t);
            }
        });
    }

    private void jump() {
        clearPreferences();
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


    private void getNewToken(){
        Call<RefreshTokenResponse> mRefrsehTokenResponseCall = RetrofitAPICaller.getInstance(SplashScreenActivity.this)
                .getStellarJetAPIs().getUpdatedToken(
                        SharedPreferencesHelper.getUserRefreshToken(SplashScreenActivity.this)
                );
        mRefrsehTokenResponseCall.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                Log.d("Login", "onResponse: " + response.body());
                SharedPreferencesHelper.saveUserToken(SplashScreenActivity.this , response.body().getData().getToken());
                SharedPreferencesHelper.saveUserRefreshToken(SplashScreenActivity.this, response.body().getData().getRefresh_token());
                getUserData();
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.d("Login", "onFailure: " + t);
            }
        });
    }

    private void clearPreferences(){
        SharedPreferencesHelper.saveFoodPersonalize(SplashScreenActivity.this , false);
        SharedPreferencesHelper.saveCabDropPersonalizeID(SplashScreenActivity.this , "");
        SharedPreferencesHelper.saveCabDropPersonalize(SplashScreenActivity.this , "");
        SharedPreferencesHelper.saveCabPickupPersoalizeID(SplashScreenActivity.this , "");
        SharedPreferencesHelper.saveCabPickupPersoalize(SplashScreenActivity.this , "");
        SharedPreferencesHelper.saveCabPersonalize(SplashScreenActivity.this , false);
        SharedPreferencesHelper.saveBookingId(SplashScreenActivity.this , "");
    }
}
