package com.ns.stellarjet.booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.*;
import com.ns.networking.model.guestrequest.*;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityPassengerListBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.*;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PassengerListActivity extends AppCompatActivity implements PaymentResultListener {

    private ActivityPassengerListBinding activityPassengerBinding;
    private List<String> mGuestNamesList = new ArrayList<>();
    private List<Integer> mGuestList = new ArrayList<>();
    private List<AddGuestRequestData> mGuestRequestDataList  = new ArrayList<>();
    private GuestPrefsRequest guestPrefsRequest = new GuestPrefsRequest();
    private EditGuestPrefsRequest editGuestPrefsRequest = new EditGuestPrefsRequest();
    private AddGuestPrefsRequest addGuestPrefsRequest = new AddGuestPrefsRequest();
    private boolean isGuestEdited = false;
    private boolean isOnlySelfTravelling = true;
    private boolean isOnlyGuestsSelected = false;
    private boolean isOnlyNewGuestsAdded = false;
    private int numOfGuests;
    private int mTotalSeats = 0;
    private int mPurchaseId = 0;
    private String mPaymentId = "";
    private String membershipType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPassengerBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_passenger_list);

        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());

        activityPassengerBinding.buttonPassengerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // get the number of guests
        numOfGuests = Objects.requireNonNull(getIntent().getExtras()).getInt("numOfGuests");

        // change the self and guests name
        if(numOfGuests == 1){
            activityPassengerBinding.textViewPassengerSelf.setText(getResources().getString(R.string.info_passenger_self));
            activityPassengerBinding.textViewPassengerGuests.setText(getResources().getString(R.string.info_passenger_guest));
        }else if(numOfGuests > 1){
            activityPassengerBinding.textViewPassengerSelf.setText(getResources().getString(R.string.info_passenger_self_guests));
            activityPassengerBinding.textViewPassengerGuests.setText(getResources().getString(R.string.info_passenger_guests));
        }

        mTotalSeats = numOfGuests;

        makeGuestList(numOfGuests);
        getGuestNames();
        makeEmptyListUI(numOfGuests);
        setTag();

        activityPassengerBinding.buttonConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startPayment();
                processGuestDetails();
            }
        });

        activityPassengerBinding.textViewPassengerSelf.setOnClickListener(v -> {
            isOnlySelfTravelling  =true;
            mTotalSeats = mTotalSeats +1;
            setMeAndGuestInfo();
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select_bg));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerListActivity.this , android.R.color.black)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(
                    ContextCompat.getColor(PassengerListActivity.this , R.color.colorPassengerText)
            );
        });

        activityPassengerBinding.textViewPassengerGuests.setOnClickListener(v -> {
            if(isOnlySelfTravelling){
                isOnlySelfTravelling  =false;
                mTotalSeats = mTotalSeats -1;
                setMeAndGuestInfo();
                activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select));
                activityPassengerBinding.textViewPassengerSelf.setTextColor(
                        ContextCompat.getColor(PassengerListActivity.this , R.color.colorPassengerText)
                );
                activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select_bg));
                activityPassengerBinding.textViewPassengerGuests.setTextColor(ContextCompat.getColor(PassengerListActivity.this ,
                        android.R.color.black));
            }
        });
    }

    private void processGuestDetails() {
        if(isAllDataEntered()){
            validateGuests();
        }else {
            UiUtils.Companion.showSimpleDialog(
                    PassengerListActivity.this ,
                    "Passenger details needs to be filled"
            );
        }
    }

    private boolean isAllDataEntered(){
        boolean isAllDataEntered = false;
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
            if(mGuestRequestDataList.get(i).getGuestMobileNUmber().equalsIgnoreCase("")
                    ||
                    mGuestRequestDataList.get(i).getGuestName().equalsIgnoreCase("")){
                isAllDataEntered = false;
                break;
            }else {
                isAllDataEntered = true;
            }
        }
        return isAllDataEntered;
    }

    private void makeGuestAddList(List<AddGuestRequestData> mGuestRequestDataList){
        List<GuestPrefsDataRequest> editGuestPrefList = new ArrayList<>();
        List<AddGuestPrefsDataRequest> addGuestPrefList = new ArrayList<>();
        mGuestList.clear();
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
            if(isOnlySelfTravelling && i==0){
                continue;
            }else {
                if(mGuestRequestDataList.get(i).getGuestStatus().equalsIgnoreCase("edit")){
                    String id = mGuestRequestDataList.get(i).getGuestId();
                    String name = mGuestRequestDataList.get(i).getGuestName();
                    String mobileNumber = mGuestRequestDataList.get(i).getGuestMobileNUmber();
                    List<Integer> mFoodsList = new ArrayList<>();
                    mFoodsList.add(mGuestRequestDataList.get(i).getGuestFoodPreferences());
                    GuestPrefsDataRequest editGuestPrefsData = new GuestPrefsDataRequest();
                    editGuestPrefsData.setGuestId(Integer.valueOf(id));
                    editGuestPrefsData.setName(name);
                    editGuestPrefsData.setPhone(mobileNumber);
                    editGuestPrefsData.setmFoodPrefsList(mFoodsList);
                    editGuestPrefList.add(editGuestPrefsData);
                    mGuestList.add(Integer.valueOf(id));
                }else if(mGuestRequestDataList.get(i).getGuestStatus().equalsIgnoreCase("add")){
                    String name = mGuestRequestDataList.get(i).getGuestName();
                    String mobileNumber = mGuestRequestDataList.get(i).getGuestMobileNUmber();
                    List<Integer> mFoodsList = new ArrayList<>();
                    mFoodsList.add(mGuestRequestDataList.get(i).getGuestFoodPreferences());
                    AddGuestPrefsDataRequest addGuestPrefsData = new AddGuestPrefsDataRequest();
                    addGuestPrefsData.setName(name);
                    addGuestPrefsData.setPhone(mobileNumber);
                    addGuestPrefsData.setmFoodPrefsList(mFoodsList);
                    addGuestPrefList.add(addGuestPrefsData);
//                mGuestList.add(Integer.valueOf(id));
                }else if(mGuestRequestDataList.get(i).getGuestStatus().equalsIgnoreCase("")){
                    String id = mGuestRequestDataList.get(i).getGuestId();
                    if(!id.equalsIgnoreCase("")){
                        mGuestList.add(Integer.valueOf(id));
                    }
                }
            }

        }
        isGuestEdited = false;
        isOnlyNewGuestsAdded = false;
        isOnlyGuestsSelected = false;
        if(editGuestPrefList.size() >0 && addGuestPrefList.size()==0){
            isGuestEdited = true;
            editGuestPrefsRequest = new EditGuestPrefsRequest();
            editGuestPrefsRequest.setEditGuestPrefsRequestList(editGuestPrefList);
        }
        if(addGuestPrefList.size()>0 && editGuestPrefList.size() == 0){
            isOnlyNewGuestsAdded = true;
            addGuestPrefsRequest = new AddGuestPrefsRequest();
            addGuestPrefsRequest.setAddGuestPrefsRequestList(addGuestPrefList);
        }
        if(editGuestPrefList.size() == 0 && addGuestPrefList.size() ==0){
            isOnlyGuestsSelected = true;
        }
        guestPrefsRequest = new GuestPrefsRequest();
        guestPrefsRequest.setEditGuestPrefsRequestList(editGuestPrefList);
        guestPrefsRequest.setAddGuestPrefsRequestList(addGuestPrefList);

        Log.d("guest", "makeGuestAddList: " + guestPrefsRequest);

        membershipType = SharedPreferencesHelper.getMembershipType(PassengerListActivity.this);
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            if(isGuestEdited){
                confirmOnlyExistingGuests();
            }else if(isOnlyNewGuestsAdded){
                confirmGuests();
            }else if(isOnlyGuestsSelected){
                if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                    bookFlight();
                }else if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO)){
//                    bookFlight();
                    getOrderId();
                }
            }else if(isOnlySelfTravelling){
                if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                    bookFlight();
                }else if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO)){
                    getOrderId();
                }
            }
        }else{
            UiUtils.Companion.showSimpleDialog(
                    PassengerListActivity.this ,
                    getResources().getString(R.string.error_not_connected_internet)
            );
        }
    }

    private void validateGuests(){
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
            String name = mGuestRequestDataList.get(i).getGuestName();
            String mobileNumber = mGuestRequestDataList.get(i).getGuestMobileNUmber();
            if(!isMobileNumberPresent(mobileNumber)){
                mGuestRequestDataList.get(i).setGuestName(name);
                mGuestRequestDataList.get(i).setGuestMobileNUmber(mobileNumber);
                mGuestRequestDataList.get(i).setGuestStatus("add");
            }else if(isNamePresent(name) && isMobileNumberPresent(mobileNumber)){
                mGuestRequestDataList.get(i).setGuestName(name);
                mGuestRequestDataList.get(i).setGuestMobileNUmber(mobileNumber);
                mGuestRequestDataList.get(i).setGuestStatus("");
            }else if(!isNamePresent(name) && isMobileNumberPresent(mobileNumber)){
                mGuestRequestDataList.get(i).setGuestName(name);
                mGuestRequestDataList.get(i).setGuestMobileNUmber(mobileNumber);
                mGuestRequestDataList.get(i).setGuestStatus("edit");
            }
        }
        makeGuestAddList(mGuestRequestDataList);
    }

    private boolean isNamePresent(String name){
        List<Contact> items = HomeActivity.sUserData.getContacts();
        boolean result = false;
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equalsIgnoreCase(name)){
                result = true;
            }
        }
        return result;
    }

    private boolean isMobileNumberPresent(String mobileNumber){
        List<Contact> items = HomeActivity.sUserData.getContacts();
        boolean result = false;
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getPhone().equalsIgnoreCase(mobileNumber)){
                result = true;
            }
        }
        return result;
    }



    private void makeGuestList(int numOfGuests) {
        for (int i = 0; i < numOfGuests; i++) {
            AddGuestRequestData mAddGuestRequestData = new AddGuestRequestData();
            mAddGuestRequestData.setGuestName("");
            mAddGuestRequestData.setGuestMobileNUmber("");
            mAddGuestRequestData.setGuestId("");
            mAddGuestRequestData.setGuestStatus("");
            mAddGuestRequestData.setGuestFoodPreferences(0);
            mGuestRequestDataList.add(mAddGuestRequestData);
        }
    }

    private void makeEmptyListUI(int numOfGuests){
        LayoutInflater linf;

        linf = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(this);


        for (int i = 0; i < numOfGuests; i++) {
            final View v = linf.inflate(R.layout.layout_row_passengers_list, null);
            v.setTag("Passenger "+ i);
            activityPassengerBinding.layoutPassengerScroll.addView(v);
        }
    }

    private void setMeAndGuestInfo(){
        int childCount = activityPassengerBinding.layoutPassengerScroll.getChildCount();
        if(childCount>0) {
            EditText mMobileNumberEditText =
                    activityPassengerBinding.layoutPassengerScroll
                            .getChildAt(0)
                            .findViewById(R.id.editText_passenger_self_mobile_number);
            AutoCompleteTextView mNamesAutoCompleteTextView =
                    activityPassengerBinding.layoutPassengerScroll
                            .getChildAt(0)
                            .findViewById(R.id.autoComplete_passenger_self_name);
            if (isOnlySelfTravelling) {
                TextView passengerName = activityPassengerBinding.layoutPassengerScroll.getChildAt(0).findViewById(R.id.textView_passenger_self);
                passengerName.setText("Me");

                mNamesAutoCompleteTextView.setText(HomeActivity.sUserData.getName());
                mMobileNumberEditText.setText(HomeActivity.sUserData.getPhone());
                mNamesAutoCompleteTextView.setEnabled(false);
                mNamesAutoCompleteTextView.setAlpha(0.4f);
                mMobileNumberEditText.setEnabled(false);
                mMobileNumberEditText.setAlpha(0.4f);
            }else {
                TextView passengerName = activityPassengerBinding.layoutPassengerScroll.getChildAt(0).findViewById(R.id.textView_passenger_self);
                passengerName.setText("Passenger Name (1) ");
                mNamesAutoCompleteTextView.getText().clear();
                mMobileNumberEditText.getText().clear();
                mNamesAutoCompleteTextView.setEnabled(true);
                mNamesAutoCompleteTextView.setAlpha(1.0f);
                mMobileNumberEditText.setEnabled(true);
                mMobileNumberEditText.setAlpha(1.0f);
                hideNameErrorField(mNamesAutoCompleteTextView);
                hideErrorField(mMobileNumberEditText);
            }
        }
    }

    private void setTag(){
        Context mContext = PassengerListActivity.this;

        ArrayAdapter<String> mPassengerAdapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.select_dialog_item ,
                mGuestNamesList
        );

        int childCount = activityPassengerBinding.layoutPassengerScroll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView passengerName = activityPassengerBinding.layoutPassengerScroll.getChildAt(i).findViewById(R.id.textView_passenger_self);
            passengerName.setText("Passenger Name ("+(i+1)+")");
            EditText mMobileNumberEditText =
                    activityPassengerBinding.layoutPassengerScroll
                            .getChildAt(i)
                            .findViewById(R.id.editText_passenger_self_mobile_number);
            AutoCompleteTextView mNamesAutoCompleteTextView =
                    activityPassengerBinding.layoutPassengerScroll
                            .getChildAt(i)
                            .findViewById(R.id.autoComplete_passenger_self_name);
            mNamesAutoCompleteTextView.setThreshold(1);
            mNamesAutoCompleteTextView.setAdapter(mPassengerAdapter);
            int finalI = i;
            mNamesAutoCompleteTextView.setOnItemClickListener((parent, view, namePosition, id) -> {
                String selectedName = mNamesAutoCompleteTextView.getAdapter().getItem(namePosition).toString();
                String mobileNumber = makeFilledData(selectedName , finalI);
                if(mobileNumber.isEmpty()){
                    mNamesAutoCompleteTextView.setText("");
                    mMobileNumberEditText.setText("");
                }else {
                    mMobileNumberEditText.setText(mobileNumber);
                }
            });
            mNamesAutoCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            if(finalI == (numOfGuests-1)){
                mMobileNumberEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }else {
                mMobileNumberEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }

            mNamesAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>=3){
                        hideNameErrorField(mNamesAutoCompleteTextView);
                        mGuestRequestDataList.get(finalI).setGuestName(mNamesAutoCompleteTextView.getText().toString());
                    }else {
                        showNameErrorField(mNamesAutoCompleteTextView);
                        mGuestRequestDataList.get(finalI).setGuestName("");
                    }
                }
            });
            mMobileNumberEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() == 10){
                        hideErrorField(mMobileNumberEditText);
                        if(!isPhoneNumberPresent(s.toString())){
                            mGuestRequestDataList.get(finalI).setGuestName(mNamesAutoCompleteTextView.getText().toString());
                            mGuestRequestDataList.get(finalI).setGuestMobileNUmber(mMobileNumberEditText.getText().toString());

                        }else {
                            UiUtils.Companion.showToast(mContext, "Passenger already selected");
                            mNamesAutoCompleteTextView.getText().clear();
                            mMobileNumberEditText.getText().clear();
                        }
                    }else{
                        mGuestRequestDataList.get(finalI).setGuestName("");
                        mGuestRequestDataList.get(finalI).setGuestMobileNUmber("");
                        showErrorField(mMobileNumberEditText);
                    }
                }
            });
        }
        if(childCount>0) {
            EditText mMobileNumberEditText =
                    activityPassengerBinding.layoutPassengerScroll
                            .getChildAt(0)
                            .findViewById(R.id.editText_passenger_self_mobile_number);
            AutoCompleteTextView mNamesAutoCompleteTextView =
                    activityPassengerBinding.layoutPassengerScroll
                            .getChildAt(0)
                            .findViewById(R.id.autoComplete_passenger_self_name);
            if (isOnlySelfTravelling) {
                TextView passengerName = activityPassengerBinding.layoutPassengerScroll.getChildAt(0).findViewById(R.id.textView_passenger_self);
                passengerName.setText("Me");

                mNamesAutoCompleteTextView.setText(HomeActivity.sUserData.getName());
                mMobileNumberEditText.setText(HomeActivity.sUserData.getPhone());
                mNamesAutoCompleteTextView.setEnabled(false);
                mNamesAutoCompleteTextView.setAlpha(0.4f);
                mMobileNumberEditText.setEnabled(false);
                mMobileNumberEditText.setAlpha(0.4f);
            }else{
                TextView passengerName = activityPassengerBinding.layoutPassengerScroll.getChildAt(0).findViewById(R.id.textView_passenger_self);
                passengerName.setText("Passenger Name");
            }
        }
    }
    private void showErrorField(EditText mSampleEditText){
        mSampleEditText.setTextColor(getResources().getColor(android.R.color.white));
        mSampleEditText.setBackground(getDrawable(R.drawable.drawable_edittext_error_background));
    }

    private void showNameErrorField(AutoCompleteTextView mAutoCompleteTextView){
        mAutoCompleteTextView.setTextColor(getResources().getColor(android.R.color.white));
        mAutoCompleteTextView.setBackground(getDrawable(R.drawable.drawable_edittext_error_background));
    }

    private void hideErrorField(EditText mSampleEditText){
        mSampleEditText.setTextColor(getResources().getColor(android.R.color.white));
        mSampleEditText.setBackground(getDrawable(R.drawable.drawable_edittext_background));
    }

    private void hideNameErrorField(AutoCompleteTextView mAutoCompleteTextView){
        mAutoCompleteTextView.setTextColor(getResources().getColor(android.R.color.white));
        mAutoCompleteTextView.setBackground(getDrawable(R.drawable.drawable_edittext_background));
    }

    private boolean isPhoneNumberPresent(String number){
        boolean isPresent = false;
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
            if(number.equalsIgnoreCase(mGuestRequestDataList.get(i).getGuestMobileNUmber())){
                isPresent = true;
            }
        }
        return isPresent;
    }

    private void getGuestNames(){
        List<Contact> items = HomeActivity.sUserData.getContacts();
        mGuestNamesList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mGuestNamesList.add(items.get(i).getName());
        }
    }

    private String makeFilledData(String name , int passengerPosition){
        List<Contact> items = HomeActivity.sUserData.getContacts();
        String mobileNumber = "";
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equalsIgnoreCase(name)){
                mobileNumber = items.get(i).getPhone();
                mGuestRequestDataList.get(passengerPosition).setGuestId(String.valueOf(items.get(i).getId()));
            }
        }
        return mobileNumber;
    }

    private void confirmGuests(){

        addGuestPrefsRequest.setToken(SharedPreferencesHelper.getUserToken(PassengerListActivity.this));

        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerListActivity.this);

        Call<GuestConfirmResponse> guestConfirmResponseCall =
                RetrofitAPICaller.getInstance(PassengerListActivity.this)
                        .getStellarJetAPIs().bookNewGuestInfo(
                        addGuestPrefsRequest
                );

        guestConfirmResponseCall.enqueue(new Callback<GuestConfirmResponse>() {
            @Override
            public void onResponse(Call<GuestConfirmResponse> call, Response<GuestConfirmResponse> response) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + response);
                if(response.body().getResultcode() ==1){
                    List<Integer> mGuestResponseIds = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getNew_contacts().size(); i++) {
                        mGuestResponseIds.add(response.body().getData().getNew_contacts().get(i).getGuest_id());
                    }
                    mGuestList.addAll(mGuestResponseIds);
                    if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                        bookFlight();
                    }else if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO)){
                        getOrderId();
                    }
                }
            }

            @Override
            public void onFailure(Call<GuestConfirmResponse> call, Throwable t) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + t);
                UiUtils.Companion.showSimpleDialog(
                        PassengerListActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }

    private void confirmOnlyExistingGuests(){

        editGuestPrefsRequest.setToken(SharedPreferencesHelper.getUserToken(PassengerListActivity.this));
        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerListActivity.this);
        Call<GuestConfirmResponse> guestConfirmResponseCall =
                RetrofitAPICaller.getInstance(PassengerListActivity.this)
                        .getStellarJetAPIs().bookExistingGuestInfo(
                        editGuestPrefsRequest
                );

        guestConfirmResponseCall.enqueue(new Callback<GuestConfirmResponse>() {
            @Override
            public void onResponse(Call<GuestConfirmResponse> call, Response<GuestConfirmResponse> response) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + response);
                if (response.body() != null && response.body().getResultcode() == 1) {
                    List<Integer> mGuestResponseIds = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getNew_contacts().size(); i++) {
                        mGuestResponseIds.add(response.body().getData().getNew_contacts().get(i).getGuest_id());
                    }
                    mGuestList.addAll(mGuestResponseIds);
                    if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                        bookFlight();
                    }else if(membershipType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO)){
                        getOrderId();
                    }
//                    bookFlight();
                }
            }

            @Override
            public void onFailure(Call<GuestConfirmResponse> call, Throwable t) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + t);
                UiUtils.Companion.showSimpleDialog(
                        PassengerListActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }

    private void bookFlight(){
        int selfTravelling;
        if(isOnlySelfTravelling){
            selfTravelling = 1;
        }else {
            selfTravelling = 0;
        }
        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerListActivity.this);
        Call<BookingConfirmResponse> mBookingConfirmResponseCall =  RetrofitAPICaller.getInstance(PassengerListActivity.this).getStellarJetAPIs()
                .confirmFlightBooking(
                        SharedPreferencesHelper.getUserToken(PassengerListActivity.this) ,
                        SharedPreferencesHelper.getFromCityId(PassengerListActivity.this) ,
                        SharedPreferencesHelper.getToCityId(PassengerListActivity.this) ,
                        SharedPreferencesHelper.getJourneyDate(PassengerListActivity.this) ,
                        SharedPreferencesHelper.getJourneyTime(PassengerListActivity.this) ,
                        SharedPreferencesHelper.getArrivalTime(PassengerListActivity.this) ,
                        SharedPreferencesHelper.getFlightId(PassengerListActivity.this) ,
                        HomeActivity.mSeatNamesId ,
                        mGuestList ,
                        selfTravelling,
                        SharedPreferencesHelper.getScheduleId(PassengerListActivity.this)
                );

        mBookingConfirmResponseCall.enqueue(new Callback<BookingConfirmResponse>() {
            @Override
            public void onResponse(@NotNull Call<BookingConfirmResponse> call, @NotNull Response<BookingConfirmResponse> response) {
                progress.hideProgress();
                if (response.body() != null && response.body().getResultcode() == 1) {
                    Log.d("Booking", "onResponse: " + response.body());
                    SharedPreferencesHelper.saveBookingId(
                            PassengerListActivity.this ,
                            String.valueOf(response.body().getData().getBooking_id()));
                    int seatCount = 0;
                    if(selfTravelling == 1){
                        seatCount = 1;
                    }
                    seatCount = seatCount + mGuestList.size();
                    int subscriptionSeatCount = SharedPreferencesHelper.getSeatCount(PassengerListActivity.this);
                    seatCount = subscriptionSeatCount - seatCount;
                    SharedPreferencesHelper.saveSeatCount(PassengerListActivity.this , seatCount);
//                    getUserData();
                    Intent mIntent = new Intent(
                            PassengerListActivity.this ,
                            BookingConfirmedActivity.class
                    );
                    mIntent.putExtra(UIConstants.BUNDLE_FROM_CITY , SharedPreferencesHelper.getFromCity(PassengerListActivity.this));
                    mIntent.putExtra(UIConstants.BUNDLE_TO_CITY , SharedPreferencesHelper.getToCity(PassengerListActivity.this));
                    if(selfTravelling == 0){
                        mIntent.putExtra(UIConstants.BUNDLE_IS_ONLY_GUEST_TRAVELLING , true);
                    }else {
                        mIntent.putExtra(UIConstants.BUNDLE_IS_ONLY_GUEST_TRAVELLING , false);
                    }
                    SharedPreferencesHelper.savePersonalizeTime(
                            PassengerListActivity.this,
                            StellarJetUtils.getPersonalizationHours(
                                    SharedPreferencesHelper.getJourneyTimeImMillis(PassengerListActivity.this)));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                }else if(response.code()==400){
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        int errorCode = mJsonObject.getInt("resultcode");
                        if(errorCode == 4){
                            // Operation timed out . please reselect seats
                            showTimeOutDialog();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookingConfirmResponse> call, @NotNull Throwable t) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + t);
                UiUtils.Companion.showSimpleDialog(
                        PassengerListActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }

    private void showTimeOutDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Operation timed out . please reselect seats");
        alertDialogBuilder.setPositiveButton("Ok",
                (arg0, arg1) -> onBackPressed());

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(dialog -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.colorButtonNew)
        ));
        alertDialog.show();

    }

    private void getUserData(){
        Call<LoginResponse> mCustomerDataResponseCall = RetrofitAPICaller.getInstance(PassengerListActivity.this)
                .getStellarJetAPIs().getCustomerData(
                        SharedPreferencesHelper.getUserToken(PassengerListActivity.this)
                );

        mCustomerDataResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body()!=null){
                    HomeActivity.sUserData = response.body().getData().getUser_data();
                }else {
                    try {
                        JSONObject mJsonObject  = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        if(errorMessage.equalsIgnoreCase(UIConstants.USER_TOKEN_EXPIRY)){
//                            getNewToken();
                            Log.d("Splash", "onResponse: Expiry");
                        }else {
                            UiUtils.Companion.showSimpleDialog(
                                    PassengerListActivity.this , errorMessage
                            );
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

    @Override
    public void onPaymentSuccess(String successMessage) {
        // call API and book flight
//        UiUtils.Companion.showToast(PassengerListActivity.this , successMessage);
        mPaymentId = successMessage;
        verifyPurchase();
    }

    @Override
    public void onPaymentError(int i, String s) {
        UiUtils.Companion.showToast(PassengerListActivity.this , s);
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_stellar_launcher);

        /**
         * Reference to current activity
         */
        final Activity activity = PassengerListActivity.this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", getResources().getString(R.string.app_name));

            JSONObject prefillDate = new JSONObject();
            prefillDate.put("contact", SharedPreferencesHelper.getUserPhone(PassengerListActivity.this));
            prefillDate.put("email", SharedPreferencesHelper.getUserEmail(PassengerListActivity.this));

            options.put("prefill" , prefillDate);

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", mPurchaseId);

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
//            options.put("amount", mTotalSeats*100000*100);
            int totalCost = mTotalSeats * SharedPreferencesHelper.getSeatCost(PassengerListActivity.this)*100;
            options.put("amount", totalCost);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("Passenger ", "Error in starting Razorpay Checkout", e);
        }
    }

    private void getOrderId(){
        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerListActivity.this);
        Call<PurchaseSeatsResponse> mPurchaseIdCall = RetrofitAPICaller.getInstance(PassengerListActivity.this)
                .getStellarJetAPIs().getGuestOrderId(
                        SharedPreferencesHelper.getUserToken(PassengerListActivity.this) ,
                        mTotalSeats
                );

        mPurchaseIdCall.enqueue(new Callback<PurchaseSeatsResponse>() {
            @Override
            public void onResponse(Call<PurchaseSeatsResponse> call, Response<PurchaseSeatsResponse> response) {
                progress.hideProgress();
                if(response.body()!=null){
                    mPurchaseId = response.body().getData().getPurchase_id();
                    startPayment();
                }
            }

            @Override
            public void onFailure(Call<PurchaseSeatsResponse> call, Throwable t) {
                progress.hideProgress();
                UiUtils.Companion.showServerErrorDialog(PassengerListActivity.this);
            }
        });

    }

    private void verifyPurchase(){
        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerListActivity.this);
        Call<VerifyPurchaseResponse> verifyPurchaseCall = RetrofitAPICaller.getInstance(PassengerListActivity.this)
                .getStellarJetAPIs().verifyPurchase(
                        SharedPreferencesHelper.getUserToken(PassengerListActivity.this) ,
                        mPaymentId,
                        String.valueOf(mPurchaseId)
                );

        verifyPurchaseCall.enqueue(new Callback<VerifyPurchaseResponse>() {
            @Override
            public void onResponse(Call<VerifyPurchaseResponse> call, Response<VerifyPurchaseResponse> response) {
                progress.hideProgress();
                if(response.body()!=null){
                    bookFlight();
                }
            }

            @Override
            public void onFailure(Call<VerifyPurchaseResponse> call, Throwable t) {
                progress.hideProgress();
                UiUtils.Companion.showServerErrorDialog(PassengerListActivity.this);
            }
        });

    }
}
