package com.ns.stellarjet.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityPasswordBinding;
import com.ns.stellarjet.home.HomeActivity;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPasswordBinding mActivityPasswordBinding = DataBindingUtil.setContentView(
                this ,
                R.layout.activity_password
        );

        String username = getIntent().getStringExtra("userEmail");

        mActivityPasswordBinding.btnLogin.setOnClickListener(v -> {
            String password = mActivityPasswordBinding.editTextAccountPassword.getText().toString();
            Call<LoginResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(PasswordActivity.this)
                    .getStellarJetAPIs().doLogin(username , password);

            mLoginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d("Password", "onResponse: " + response.body());
                    if (response.body()!=null){
                        Intent mHomeIntent = new Intent(
                                PasswordActivity.this ,
                                HomeActivity.class
                        );
                        mHomeIntent.putExtra("UserData" , response.body().getData().getUser_data());
                        startActivity(mHomeIntent);
                    }else {
                        try {
                            JSONObject mJsonObject  = new JSONObject(response.errorBody().string());
                            String errorMessage = mJsonObject.getString("message");
                            Toast.makeText(PasswordActivity.this , errorMessage , Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("Password", "onFailure: " + t);
                }
            });
        });

    }
}
