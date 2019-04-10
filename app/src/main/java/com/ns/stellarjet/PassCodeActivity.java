package com.ns.stellarjet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.ForgotPasswordResponse;
import com.ns.networking.model.UserData;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.databinding.ActivityPassCodeBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.home.PrimaryUsersActivity;
import com.ns.stellarjet.login.LoginActivity;
import com.ns.stellarjet.login.PasswordActivity;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.UIConstants;
import com.ns.stellarjet.utils.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Objects;

public class PassCodeActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mCurrentEditText;
    private String mFirstEditTextPassCode;
    private String mSecondEditTextPassCode;
    private String mThreeEditTextPassCode;
    private String mFourEditTextPassCode;
    private UserData mUserData;

    private int mPassCodeEntryOne = -1;
    private int mAttemptsTried = 0;
    private ActivityPassCodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this , R.layout.activity_pass_code);

        mUserData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_USER_DATA);

        boolean isEntryDone = SharedPreferencesHelper.isEntryLogin(PassCodeActivity.this);

        if(isEntryDone){
            binding.textViewPasscodeTitle.setText(getResources().getString(R.string.passcode_title_hello_again));
        }else {
            binding.textViewPasscodeTitle.setText(getResources().getString(R.string.passcode_title_hello));
        }

        String savedPassCode = SharedPreferencesHelper.getPassCode(PassCodeActivity.this);
        if (savedPassCode.isEmpty()){

            binding.textViewPasscodeHeading.setText(getResources().getString(R.string.passcode_title_heading_create));
        }else {
            binding.textViewPasscodeHeading.setText(getResources().getString(R.string.passcode_title_heading));
        }


        binding.passcodeBack.setOnClickListener(v -> onBackPressed());


        binding.buttonPasscodeOne.setOnClickListener(this);
        binding.buttonPasscodeTwo.setOnClickListener(this);
        binding.buttonPasscodeThree.setOnClickListener(this);
        binding.buttonPasscodeFour.setOnClickListener(this);
        binding.buttonPasscodeFive.setOnClickListener(this);
        binding.buttonPasscodeSix.setOnClickListener(this);
        binding.buttonPasscodeSeven.setOnClickListener(this);
        binding.buttonPasscodeEight.setOnClickListener(this);
        binding.buttonPasscodeNine.setOnClickListener(this);
        binding.buttonPasscodeZero.setOnClickListener(this);
        binding.buttonPasscodeDel.setOnClickListener(this);

        /*binding.editTextPasscodeNumberOne.setTransformationMethod(new MyPasswordTransformationMethod());
        binding.editTextPasscodeNumberTwo.setTransformationMethod(new MyPasswordTransformationMethod());
        binding.editTextPasscodeNumberThree.setTransformationMethod(new MyPasswordTransformationMethod());
        binding.editTextPasscodeNumberFour.setTransformationMethod(new MyPasswordTransformationMethod());*/
        binding.editTextPasscodeNumberOne.requestFocus();
        mCurrentEditText = binding.editTextPasscodeNumberOne;

        binding.editTextPasscodeNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if(inputText.length()==1){
                    mFirstEditTextPassCode = inputText;
                    mCurrentEditText = binding.editTextPasscodeNumberTwo;
                    binding.editTextPasscodeNumberTwo.requestFocus();
                }
            }
        });

        binding.editTextPasscodeNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if(inputText.length()==1){
                    mSecondEditTextPassCode = inputText;
                    mCurrentEditText = binding.editTextPasscodeNumberThree;
                    binding.editTextPasscodeNumberThree.requestFocus();
                }
            }
        });

        binding.editTextPasscodeNumberThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if(inputText.length()==1){
                    mThreeEditTextPassCode = inputText;
                    mCurrentEditText = binding.editTextPasscodeNumberFour;
                    binding.editTextPasscodeNumberFour.requestFocus();
                }
            }
        });

        binding.editTextPasscodeNumberFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if(inputText.length()==1){
                    mFourEditTextPassCode = inputText;
                    disableKeyPad();
                    new Handler().postDelayed(
                            () -> {
                                savePassCode();
                                enableKeyPad();
                            }, 150
                    );
                }
            }
        });

        binding.textviewForgotPassword.setOnClickListener(v -> {
            String username = mUserData.getEmail();
            forgotPassword(username);
        });
    }

    private void forgotPassword(String username) {
        final Progress progress = Progress.getInstance();
        progress.showProgress(PassCodeActivity.this);
        Call<ForgotPasswordResponse> mLoginResponseCall = RetrofitAPICaller.getInstance(PassCodeActivity.this)
                .getStellarJetAPIs().forgotPassword(username);

        mLoginResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if(response.body()!=null){
                    SharedPreferencesHelper.clearAllSharedPreferencesData(PassCodeActivity.this);
                    UiUtils.Companion.showToast(
                            PassCodeActivity.this,
                            response.body().getMessage()
                    );

                    startActivity(new Intent(PassCodeActivity.this , LoginActivity.class));
                    finish();
                }else if(response.errorBody()!=null){
                    UiUtils.Companion.showServerErrorDialog(PassCodeActivity.this);
                }
                progress.hideProgress();
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Log.d("Password", "onFailure: " + t);
                UiUtils.Companion.showServerErrorDialog(PassCodeActivity.this);
                progress.hideProgress();
            }
        });

    }

    private void disableKeyPad() {
        binding.buttonPasscodeOne.setEnabled(false);
        binding.buttonPasscodeTwo.setEnabled(false);
        binding.buttonPasscodeThree.setEnabled(false);
        binding.buttonPasscodeFour.setEnabled(false);
        binding.buttonPasscodeFive.setEnabled(false);
        binding.buttonPasscodeSix.setEnabled(false);
        binding.buttonPasscodeSeven.setEnabled(false);
        binding.buttonPasscodeEight.setEnabled(false);
        binding.buttonPasscodeNine.setEnabled(false);
        binding.buttonPasscodeZero.setEnabled(false);
        binding.buttonPasscodeDel.setEnabled(false);
    }


    private void enableKeyPad(){
        binding.buttonPasscodeOne.setEnabled(true);
        binding.buttonPasscodeTwo.setEnabled(true);
        binding.buttonPasscodeThree.setEnabled(true);
        binding.buttonPasscodeFour.setEnabled(true);
        binding.buttonPasscodeFive.setEnabled(true);
        binding.buttonPasscodeSix.setEnabled(true);
        binding.buttonPasscodeSeven.setEnabled(true);
        binding.buttonPasscodeEight.setEnabled(true);
        binding.buttonPasscodeNine.setEnabled(true);
        binding.buttonPasscodeZero.setEnabled(true);
        binding.buttonPasscodeDel.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_passcode_one:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_one));
                break;
            case R.id.button_passcode_two:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_two));
                break;
            case R.id.button_passcode_three:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_three));
                break;
            case R.id.button_passcode_four:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_four));
                break;
            case R.id.button_passcode_five:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_five));
                break;
            case R.id.button_passcode_six:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_six));
                break;
            case R.id.button_passcode_seven:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_seven));
                break;
            case R.id.button_passcode_eight:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_eight));
                break;
            case R.id.button_passcode_nine:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_nine));
                break;
            case R.id.button_passcode_zero:
                mCurrentEditText.setText(getString(R.string.passcode_kwypad_zero));
                break;
            case R.id.button_passcode_del:
                deletePassCode();
                break;
        }
    }

    private void deletePassCode(){
        if(mCurrentEditText == binding.editTextPasscodeNumberOne){
            mCurrentEditText.setText("");
        }else if(mCurrentEditText == binding.editTextPasscodeNumberTwo){
            mCurrentEditText.setText("");
            mCurrentEditText = binding.editTextPasscodeNumberOne;
        }else if(mCurrentEditText == binding.editTextPasscodeNumberThree){
            mCurrentEditText.setText("");
            mCurrentEditText = binding.editTextPasscodeNumberTwo;
        }else if(mCurrentEditText == binding.editTextPasscodeNumberFour){
            mCurrentEditText.setText("");
            mCurrentEditText = binding.editTextPasscodeNumberThree;
        }
    }

    private void clearPassCode(){
        binding.editTextPasscodeNumberOne.setText("");
        binding.editTextPasscodeNumberTwo.setText("");
        binding.editTextPasscodeNumberThree.setText("");
        binding.editTextPasscodeNumberFour.setText("");
        binding.editTextPasscodeNumberOne.requestFocus();
        mCurrentEditText = binding.editTextPasscodeNumberOne;
        mFirstEditTextPassCode = "";
        mSecondEditTextPassCode= "";
        mThreeEditTextPassCode = "";
        mFourEditTextPassCode = "";
    }

    private void savePassCode(){
        String passCode = mFirstEditTextPassCode + mSecondEditTextPassCode +
                mThreeEditTextPassCode + mFourEditTextPassCode;
        Log.d("Passcode", "savePassCode: " + passCode);
        String savedPassCode = SharedPreferencesHelper.getPassCode(PassCodeActivity.this);
        if(savedPassCode.equals("")){
            binding.textViewPasscodeHeading.setText(
                    getResources().getString(R.string.passcode_title_heading_confirm));
            clearPassCode();
            if(mPassCodeEntryOne == -1){
                mPassCodeEntryOne = Integer.parseInt(passCode);
            }else {
                int mPassCodeEntryTwo = Integer.parseInt(passCode);
                if(mPassCodeEntryOne == mPassCodeEntryTwo){
                    SharedPreferencesHelper.savePassCode(PassCodeActivity.this , passCode);
                    saveLoginData();
                    decideNextLaunchActivity();
                }else {
                    clearPassCode();
                    binding.textViewPasscodeHeading.setText(getResources().getString(R.string.passcode_title_heading_create));
                    UiUtils.Companion.showSimpleDialog(
                            PassCodeActivity.this,
                            "PassCode not matching "
                    );
                    mPassCodeEntryOne = -1;
                }
            }
            Log.d("Passcode", "savePassCode: save new preferences");
        }else if(!savedPassCode.equals(passCode)){
            Log.d("Passcode", "savePassCode: Access Denied");
            clearPassCode();
            mAttemptsTried = mAttemptsTried + 1;
            int totalAttempts = 3;
            int attemptNumber = totalAttempts - mAttemptsTried;
            if(attemptNumber == 0){
                SharedPreferencesHelper.clearAllSharedPreferencesData(PassCodeActivity.this);
                Intent mIntent = new Intent(
                        PassCodeActivity.this ,
                        LoginActivity.class
                );
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
                UiUtils.Companion.showToast(PassCodeActivity.this , "Please login again");
            }else if(attemptNumber == 2){
                showAttemptsRemaining(attemptNumber);
            }else {
                UiUtils.Companion.showToast(PassCodeActivity.this , (totalAttempts - mAttemptsTried) + " attempt remaining");
            }
        }else if(savedPassCode.equals(passCode)){
            Log.d("Passcode", "savePassCode: Access Granted");
            decideNextLaunchActivity();
        }
    }

    private void decideNextLaunchActivity(){
        String userType = SharedPreferencesHelper.getUserType(PassCodeActivity.this);
        int currentPrimaryUserId = SharedPreferencesHelper.getCurrentPrimaryUserId(PassCodeActivity.this);
        boolean isPrimaryUserSelected = SharedPreferencesHelper.isPrimaryUserSelected(PassCodeActivity.this);
        if(userType.equalsIgnoreCase("Primary")){
            launchHomeActivity();
        }else if(userType.equalsIgnoreCase("Secondary") &&
                currentPrimaryUserId!=0 &&
                isPrimaryUserSelected){
            launchHomeActivity();
        }else if(userType.equalsIgnoreCase("Secondary") &&
                currentPrimaryUserId==0 &&
                !isPrimaryUserSelected){
            launchPrimaryUserListActivity();
        }
    }

    private void launchHomeActivity(){
        Intent mHomeIntent = new Intent(PassCodeActivity.this , HomeActivity.class);
        // send bundle
        mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
        mHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mHomeIntent);
        finish();
    }

    private void launchPrimaryUserListActivity(){
        saveLoginData();
        Intent mHomeIntent = new Intent(PassCodeActivity.this , PrimaryUsersActivity.class);
        // send bundle
        mHomeIntent.putParcelableArrayListExtra(UIConstants.BUNDLE_SECONDARY_USER_DATA,
                (ArrayList<? extends Parcelable>) mUserData.getPrimary_users());
        mHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mHomeIntent);
        finish();
    }

    private void showAttemptsRemaining(int attemptsRemaining){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(PassCodeActivity.this);
        builder.setMessage(attemptsRemaining +" attempts remaining");

        // add the buttons
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogParams -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorButtonNew)));
        dialog.show();
    }

    private void saveLoginData(){
        SharedPreferencesHelper.saveUserName(PassCodeActivity.this , mUserData.getName());
        SharedPreferencesHelper.saveUserEmail(PassCodeActivity.this , mUserData.getEmail());
        SharedPreferencesHelper.saveUserPhone(PassCodeActivity.this , mUserData.getPhone());
        SharedPreferencesHelper.saveLoginStatus(PassCodeActivity.this , true);
        SharedPreferencesHelper.saveSeatCount(PassCodeActivity.this , mUserData.getCustomer_prefs().getSeats_available());
        SharedPreferencesHelper.saveSeatCount(
                PassCodeActivity.this,
                mUserData.getCustomer_prefs().getSeats_available());
        String prepaidTerms = mUserData.getCustomer_prefs().getMembership_details()
                .getPrepaid_terms();
        if(prepaidTerms.equalsIgnoreCase("true")){
            SharedPreferencesHelper.saveMembershipType(
                    PassCodeActivity.this,
                    UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION);
        }else if(prepaidTerms.equalsIgnoreCase("false")){
            SharedPreferencesHelper.saveMembershipType(
                    PassCodeActivity.this,
                    UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO);
        }

        String seatCost = mUserData.getCustomer_prefs()
                .getMembership_details().getSeat_cost();
        SharedPreferencesHelper.saveSeatCost(PassCodeActivity.this , Integer.valueOf(seatCost));
    }
}