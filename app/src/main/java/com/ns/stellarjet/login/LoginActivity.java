package com.ns.stellarjet.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.ValidateCustomerResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityLoginBinding;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mActivityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain binding
        mActivityLoginBinding = DataBindingUtil.
                setContentView(this , R.layout.activity_login);

        /* remove the dummy login */
        mActivityLoginBinding.editTextAccountId.setText("vignesh@admin.com");

        mActivityLoginBinding.btnLoginConfirm.setOnClickListener(v -> {
            String userName = mActivityLoginBinding.editTextAccountId.getText().toString();
            if(userName.isEmpty()){
                showAccountIdErrorField();
            }else {
                if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
                    validateUser(userName);
                }else{
                    Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void validateUser(String userName){
        final Progress progress = Progress.getInstance();
        progress.showProgress(LoginActivity.this);
        Call<ValidateCustomerResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(LoginActivity.this)
                .getStellarJetAPIs().doValidateCustomer(userName );

        mLoginResponseCall.enqueue(new Callback<ValidateCustomerResponse>() {
            @Override
            public void onResponse(Call<ValidateCustomerResponse> call, Response<ValidateCustomerResponse> response) {
                progress.hideProgress();
                if(response.body()!=null){
                    Log.d("Login", "onResponse: " + response.body());
                    String userType = response.body().getData().getUsertype();
                    if(userType.equalsIgnoreCase("primary")){
                        Intent mPasswordIntent = new Intent(
                                LoginActivity.this ,
                                PasswordActivity.class
                        );
                        mPasswordIntent.putExtra("userEmail" , response.body().getData().getUsername());
                        SharedPreferencesHelper.saveUserType(LoginActivity.this , response.body().getData().getUsertype());
                        startActivity(mPasswordIntent);
                    }else if(userType.equalsIgnoreCase("secondary")){
                        Intent mOTPIntent = new Intent(
                                LoginActivity.this ,
                                OTPActivity.class
                        );
                        mOTPIntent.putExtra("userEmail" , response.body().getData().getUsername());
                        startActivity(mOTPIntent);
                    }
                }else {
                    try {
                        JSONObject mJsonObject  = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        Toast.makeText(LoginActivity.this , errorMessage , Toast.LENGTH_LONG).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidateCustomerResponse> call, Throwable t) {
                progress.hideProgress();
                Toast.makeText(LoginActivity.this , "Server Error" , Toast.LENGTH_LONG).show();
                Log.d("Login", "onResponse: " + t);
            }
        });
    }

    private void showAccountIdErrorField(){
        mActivityLoginBinding.editTextAccountId.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        mActivityLoginBinding.editTextAccountId.setBackground(getDrawable(R.drawable.drawable_edittext_error_background));
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
        animShake.setDuration(50);
        mActivityLoginBinding.editTextAccountId.startAnimation(animShake);
    }
}
