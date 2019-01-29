package com.ns.stellarjet.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ns.networking.model.SecondaryUserLoginResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.PassCodeActivity;
import com.ns.stellarjet.R;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.UIConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    @BindView(R.id.editText_account_otp)
    EditText mOtpEditText;
    @BindView(R.id.btn_otp_login)
    Button mOtpLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_six);

        ButterKnife.bind(OTPActivity.this);

        String username = getIntent().getStringExtra("userEmail");


        mOtpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpEntered = mOtpEditText.getText().toString();
                if(!otpEntered.isEmpty()){
                    doSecondaryUserLogin(username , otpEntered);
                }else {
                    Toast.makeText(OTPActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void doSecondaryUserLogin(String userNameParams , String otpParams){
        Call<SecondaryUserLoginResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(OTPActivity.this)
                .getStellarJetAPIs().loginSecondaryUser(
                        userNameParams, otpParams);

        mLoginResponseCall.enqueue(new Callback<SecondaryUserLoginResponse>() {
            @Override
            public void onResponse(Call<SecondaryUserLoginResponse> call, Response<SecondaryUserLoginResponse> response) {
                if(response.body()!=null){
                    Log.d("OTP", "onResponse: " +response.body());
                    String token = response.body().getData().getToken();
                    String refreshToken = response.body().getData().getRefresh_token();
                    SharedPreferencesHelper.saveUserToken(OTPActivity.this , token);
                    SharedPreferencesHelper.saveUserRefreshToken(OTPActivity.this , refreshToken);
                    SharedPreferencesHelper.saveLoginStatus(OTPActivity.this , true);
                    Intent mPassCodeIntent = new Intent(
                            OTPActivity.this ,
                            PassCodeActivity.class
                    );
                    mPassCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mPassCodeIntent.putExtra(UIConstants.BUNDLE_SECONDARY_USER_DATA , response.body().getData());
                    startActivity(mPassCodeIntent);
                }else {
                    Toast.makeText(OTPActivity.this, "OTP Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SecondaryUserLoginResponse> call, Throwable t) {
                Log.d("OTP", "onResponse: " +t);
            }
        });

    }
}
