package com.ns.stellarjet;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
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
                Log.d("Splash", "onResponse: " + response.body());
                Intent mHomeIntent = new Intent(
                        SplashScreenActivity.this ,
                        PassCodeActivity.class
                );
                mHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA, response.body().getData().getUser_data());
                startActivity(mHomeIntent);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("splash", "onFailure: " + t);
            }
        });
    }


}
