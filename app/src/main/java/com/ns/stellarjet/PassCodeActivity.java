package com.ns.stellarjet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.UserData;
import com.ns.stellarjet.databinding.ActivityPassCodeBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.login.LoginActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.UIConstants;

import java.util.Objects;

public class PassCodeActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityPassCodeBinding activityPassCodeBinding;
    private EditText mCurrentEditText;
    private String mFirstEditTextPassCode;
    private String mSecondEditTextPassCode;
    private String mThreeEditTextPassCode;
    private String mFourEditTextPassCode;
    private UserData mUserData;

    private int mPassCodeEntryOne = -1;
    private int mAttemptsTried = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);

        activityPassCodeBinding = DataBindingUtil.setContentView(this , R.layout.activity_pass_code);

        mUserData = getIntent().getExtras().getParcelable(UIConstants.BUNDLE_USER_DATA);

        Log.d("PassCode", "onCreate: " + mUserData);

        String savedPassCode = SharedPreferencesHelper.getPassCode(PassCodeActivity.this);
        if (savedPassCode.isEmpty()){
            activityPassCodeBinding.textViewPasscodeTitle.setText(getResources().getString(R.string.passcode_title_create_passcode));
        }else {
            activityPassCodeBinding.textViewPasscodeTitle.setText(getResources().getString(R.string.passcode_title_enter_passcode));
        }

        activityPassCodeBinding.buttonPasscodeOne.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeTwo.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeThree.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeFour.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeFive.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeSix.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeSeven.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeEight.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeNine.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeZero.setOnClickListener(this);
        activityPassCodeBinding.buttonPasscodeDel.setOnClickListener(this);
        activityPassCodeBinding.editTextPasscodeNumberOne.requestFocus();
        mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberOne;

        activityPassCodeBinding.editTextPasscodeNumberOne.addTextChangedListener(new TextWatcher() {
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
                    mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberTwo;
                    activityPassCodeBinding.editTextPasscodeNumberTwo.requestFocus();
                }
            }
        });

        activityPassCodeBinding.editTextPasscodeNumberTwo.addTextChangedListener(new TextWatcher() {
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
                    mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberThree;
                    activityPassCodeBinding.editTextPasscodeNumberThree.requestFocus();
                }
            }
        });

        activityPassCodeBinding.editTextPasscodeNumberThree.addTextChangedListener(new TextWatcher() {
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
                    mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberFour;
                    activityPassCodeBinding.editTextPasscodeNumberFour.requestFocus();
                }
            }
        });

        activityPassCodeBinding.editTextPasscodeNumberFour.addTextChangedListener(new TextWatcher() {
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
    }

    private void disableKeyPad(){
        activityPassCodeBinding.buttonPasscodeOne.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeTwo.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeThree.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeFour.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeFive.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeSix.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeSeven.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeEight.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeNine.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeZero.setEnabled(false);
        activityPassCodeBinding.buttonPasscodeDel.setEnabled(false);
    }

    private void enableKeyPad(){
        activityPassCodeBinding.buttonPasscodeOne.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeTwo.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeThree.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeFour.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeFive.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeSix.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeSeven.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeEight.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeNine.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeZero.setEnabled(true);
        activityPassCodeBinding.buttonPasscodeDel.setEnabled(true);
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
        if(mCurrentEditText == activityPassCodeBinding.editTextPasscodeNumberOne){
            mCurrentEditText.setText("");
        }else if(mCurrentEditText == activityPassCodeBinding.editTextPasscodeNumberTwo){
            mCurrentEditText.setText("");
            mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberOne;
        }else if(mCurrentEditText == activityPassCodeBinding.editTextPasscodeNumberThree){
            mCurrentEditText.setText("");
            mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberTwo;
        }else if(mCurrentEditText == activityPassCodeBinding.editTextPasscodeNumberFour){
            mCurrentEditText.setText("");
            mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberThree;
        }
    }

    private void clearPassCode(){
        activityPassCodeBinding.editTextPasscodeNumberOne.setText("");
        activityPassCodeBinding.editTextPasscodeNumberTwo.setText("");
        activityPassCodeBinding.editTextPasscodeNumberThree.setText("");
        activityPassCodeBinding.editTextPasscodeNumberFour.setText("");
        activityPassCodeBinding.editTextPasscodeNumberOne.requestFocus();
        mCurrentEditText = activityPassCodeBinding.editTextPasscodeNumberOne;
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
            activityPassCodeBinding.textViewPasscodeTitle.setText(getResources().getString(R.string.passcode_title_confirm_passcode));
            clearPassCode();
            if(mPassCodeEntryOne == -1){
                mPassCodeEntryOne = Integer.parseInt(passCode);
            }else {
                int mPassCodeEntryTwo = Integer.parseInt(passCode);
                if(mPassCodeEntryOne == mPassCodeEntryTwo){
                    SharedPreferencesHelper.savePassCode(PassCodeActivity.this , passCode);
                    launchHomeActivity();
                }else {
                    clearPassCode();
                    activityPassCodeBinding.textViewPasscodeTitle.setText(getResources().getString(R.string.passcode_title_create_passcode));
                    Toast.makeText(PassCodeActivity.this, "PassCode not matching ", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(this, " Launch Login Activity", Toast.LENGTH_SHORT).show();
                SharedPreferencesHelper.clearAllSharedPreferencesData(PassCodeActivity.this);
                Intent mIntent = new Intent(
                        PassCodeActivity.this ,
                        LoginActivity.class
                );
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
                Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            }else if(attemptNumber == 2){
                showAttemptsRemaining(attemptNumber);
            }else {
                Toast.makeText(this, (totalAttempts - mAttemptsTried) + " attempt remaining", Toast.LENGTH_SHORT).show();
            }
        }else if(savedPassCode.equals(passCode)){
            Log.d("Passcode", "savePassCode: Access Granted");
            launchHomeActivity();
        }
    }

    private void launchHomeActivity(){
        Intent mHomeIntent = new Intent(PassCodeActivity.this , HomeActivity.class);
        // send bundle
        mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , mUserData);
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
        dialog.show();
    }
}