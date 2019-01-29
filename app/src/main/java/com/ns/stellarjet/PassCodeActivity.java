package com.ns.stellarjet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ns.networking.model.SecondaryUserData;
import com.ns.networking.model.UserData;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.home.PrimaryUsersActivity;
import com.ns.stellarjet.login.LoginActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.UIConstants;

import java.util.ArrayList;
import java.util.Objects;

public class PassCodeActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mCurrentEditText;
    private String mFirstEditTextPassCode;
    private String mSecondEditTextPassCode;
    private String mThreeEditTextPassCode;
    private String mFourEditTextPassCode;
    private UserData mUserData;
    private SecondaryUserData mSecondaryUserData;

    @BindView(R.id.button_passcode_one)
    Button mPasscodeOneButton;
    @BindView(R.id.button_passcode_two)
    Button mPasscodeTwoButton;
    @BindView(R.id.button_passcode_three)
    Button mPasscodeThreeButton;
    @BindView(R.id.button_passcode_four)
    Button mPasscodeFOurButton;
    @BindView(R.id.button_passcode_five)
    Button mPasscodeFiveButton;
    @BindView(R.id.button_passcode_six)
    Button mPasscodeSixButton;
    @BindView(R.id.button_passcode_seven)
    Button mPasscodeSevenButton;
    @BindView(R.id.button_passcode_eight)
    Button mPasscodeEightButton;
    @BindView(R.id.button_passcode_nine)
    Button mPasscodeNineButton;
    @BindView(R.id.button_passcode_zero)
    Button mPasscodeZeroButton;
    @BindView(R.id.button_passcode_del)
    Button mPasscodeDelButton;
    @BindView(R.id.textView_passcode_heading)
    TextView mPasswordHeading;
    @BindView(R.id.editText_passcode_number_one)
    EditText mNumberOneEditText;
    @BindView(R.id.editText_passcode_number_two)
    EditText mNumberTwoEditText;
    @BindView(R.id.editText_passcode_number_three)
    EditText mNumberThreeEditText;
    @BindView(R.id.editText_passcode_number_four)
    EditText mNumberFourEditText;
    @BindView(R.id.passcode_back)
    Button mPassCodeBackButton;

    private int mPassCodeEntryOne = -1;
    private int mAttemptsTried = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);

        ButterKnife.bind(this);

        if(SharedPreferencesHelper.getUserType(PassCodeActivity.this).equalsIgnoreCase("primary")){
            mUserData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_USER_DATA);
        }else if(SharedPreferencesHelper.getUserType(PassCodeActivity.this).equalsIgnoreCase("secondary")){
            mSecondaryUserData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_SECONDARY_USER_DATA);
            mUserData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_USER_DATA);
        }


        Log.d("PassCode", "onCreate: " + mUserData);

        String savedPassCode = SharedPreferencesHelper.getPassCode(PassCodeActivity.this);
        if (savedPassCode.isEmpty()){
            mPasswordHeading.setText(getResources().getString(R.string.passcode_title_heading_create));
        }else {
            mPasswordHeading.setText(getResources().getString(R.string.passcode_title_heading));
        }

        mPassCodeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPasscodeOneButton.setOnClickListener(this);
        mPasscodeTwoButton.setOnClickListener(this);
        mPasscodeThreeButton.setOnClickListener(this);
        mPasscodeFOurButton.setOnClickListener(this);
        mPasscodeFiveButton.setOnClickListener(this);
        mPasscodeSixButton.setOnClickListener(this);
        mPasscodeSevenButton.setOnClickListener(this);
        mPasscodeEightButton.setOnClickListener(this);
        mPasscodeNineButton.setOnClickListener(this);
        mPasscodeZeroButton.setOnClickListener(this);
        mPasscodeDelButton.setOnClickListener(this);

        mNumberOneEditText.requestFocus();
        mCurrentEditText = mNumberOneEditText;

        mNumberOneEditText.addTextChangedListener(new TextWatcher() {
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
                    mCurrentEditText = mNumberTwoEditText;
                    mNumberTwoEditText.requestFocus();
                }
            }
        });

        mNumberTwoEditText.addTextChangedListener(new TextWatcher() {
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
                    mCurrentEditText = mNumberThreeEditText;
                    mNumberThreeEditText.requestFocus();
                }
            }
        });

        mNumberThreeEditText.addTextChangedListener(new TextWatcher() {
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
                    mCurrentEditText = mNumberFourEditText;
                    mNumberFourEditText.requestFocus();
                }
            }
        });

        mNumberFourEditText.addTextChangedListener(new TextWatcher() {
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

    private void disableKeyPad() {
        mPasscodeOneButton.setEnabled(false);
        mPasscodeTwoButton.setEnabled(false);
        mPasscodeThreeButton.setEnabled(false);
        mPasscodeFOurButton.setEnabled(false);
        mPasscodeFiveButton.setEnabled(false);
        mPasscodeSixButton.setEnabled(false);
        mPasscodeSevenButton.setEnabled(false);
        mPasscodeEightButton.setEnabled(false);
        mPasscodeNineButton.setEnabled(false);
        mPasscodeZeroButton.setEnabled(false);
        mPasscodeDelButton.setEnabled(false);
    }


    private void enableKeyPad(){
        mPasscodeOneButton.setEnabled(true);
        mPasscodeTwoButton.setEnabled(true);
        mPasscodeThreeButton.setEnabled(true);
        mPasscodeFOurButton.setEnabled(true);
        mPasscodeFiveButton.setEnabled(true);
        mPasscodeSixButton.setEnabled(true);
        mPasscodeSevenButton.setEnabled(true);
        mPasscodeEightButton.setEnabled(true);
        mPasscodeNineButton.setEnabled(true);
        mPasscodeZeroButton.setEnabled(true);
        mPasscodeDelButton.setEnabled(true);
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
        if(mCurrentEditText == mNumberOneEditText){
            mCurrentEditText.setText("");
        }else if(mCurrentEditText == mNumberTwoEditText){
            mCurrentEditText.setText("");
            mCurrentEditText = mNumberOneEditText;
        }else if(mCurrentEditText == mNumberThreeEditText){
            mCurrentEditText.setText("");
            mCurrentEditText = mNumberTwoEditText;
        }else if(mCurrentEditText == mNumberFourEditText){
            mCurrentEditText.setText("");
            mCurrentEditText = mNumberThreeEditText;
        }
    }

    private void clearPassCode(){
        mNumberOneEditText.setText("");
        mNumberTwoEditText.setText("");
        mNumberThreeEditText.setText("");
        mNumberFourEditText.setText("");
        mNumberOneEditText.requestFocus();
        mCurrentEditText = mNumberOneEditText;
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
            mPasswordHeading.setText(
                    getResources().getString(R.string.passcode_title_heading_confirm));
            clearPassCode();
            if(mPassCodeEntryOne == -1){
                mPassCodeEntryOne = Integer.parseInt(passCode);
            }else {
                int mPassCodeEntryTwo = Integer.parseInt(passCode);
                if(mPassCodeEntryOne == mPassCodeEntryTwo){
                    SharedPreferencesHelper.savePassCode(PassCodeActivity.this , passCode);
                    decideNextLaunchActivity();
                }else {
                    clearPassCode();
                    mPasswordHeading.setText(getResources().getString(R.string.passcode_title_heading_create));
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
            decideNextLaunchActivity();
        }
    }

    private void decideNextLaunchActivity(){
        String userType = SharedPreferencesHelper.getUserType(PassCodeActivity.this);
        int currentPrimaryUserId = SharedPreferencesHelper.getCurrentPrimaryUserId(PassCodeActivity.this);
        if(userType.equalsIgnoreCase("Primary")){
            launchHomeActivity();
        }else if(userType.equalsIgnoreCase("Secondary") &&
                currentPrimaryUserId!=0){
            launchHomeActivity();
        }else if(userType.equalsIgnoreCase("Secondary") &&
                currentPrimaryUserId==0){
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
        Intent mHomeIntent = new Intent(PassCodeActivity.this , PrimaryUsersActivity.class);
        // send bundle
        mHomeIntent.putParcelableArrayListExtra(UIConstants.BUNDLE_SECONDARY_USER_DATA, (ArrayList<? extends Parcelable>) mSecondaryUserData.getPrimary_users());
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
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogParams) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
            }
        });
        dialog.show();
    }
}