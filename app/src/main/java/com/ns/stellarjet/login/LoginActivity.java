package com.ns.stellarjet.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.model.ValidateCustomerResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.networking.retrofit.StellarApiService;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.PassengerActivity;
import com.ns.stellarjet.databinding.ActivityLoginBinding;
import com.ns.stellarjet.utils.StellarJetUtils;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain binding
        ActivityLoginBinding mActivityLoginBinding =  DataBindingUtil.
                setContentView(this , R.layout.activity_login);

        /* remove the dummy login */
        mActivityLoginBinding.editTextAccountId.setText("vignesh@admin.com");

        mActivityLoginBinding.btnLoginConfirm.setOnClickListener(v -> {
            String userName = mActivityLoginBinding.editTextAccountId.getText().toString();
            Call<ValidateCustomerResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(LoginActivity.this)
                    .getStellarJetAPIs().doValidateCustomer(userName );

            mLoginResponseCall.enqueue(new Callback<ValidateCustomerResponse>() {
                @Override
                public void onResponse(Call<ValidateCustomerResponse> call, Response<ValidateCustomerResponse> response) {
                    Log.d("Login", "onResponse: " + response.body());
                    if(response.body()!=null){
                        String userType = response.body().getData().getUsertype();
                        if(userType.equalsIgnoreCase("primary")){
                            Intent mPasswordIntent = new Intent(
                                    LoginActivity.this ,
                                    PasswordActivity.class
                            );
                            mPasswordIntent.putExtra("userEmail" , response.body().getData().getUsername());
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
                    Log.d("Login", "onResponse: " + t);
                }
            });
        });
    }
}
