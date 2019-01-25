package com.ns.stellarjet.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.BookingConfirmResponse;
import com.ns.networking.model.GuestConfirmResponse;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.model.guestrequest.*;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PassengersAdapter;
import com.ns.stellarjet.databinding.ActivityPassengerBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import com.ns.stellarjet.utils.UIConstants;
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

public class PassengerActivity extends AppCompatActivity implements PassengersAdapter.onConfirmButtonEnableStateListener, PassengersAdapter.onConfirmButtonDisableStateListener {

    private ActivityPassengerBinding activityPassengerBinding;
    //    private boolean isSelfTravelling = false;
    private GuestPrefsRequest guestPrefsRequest = new GuestPrefsRequest();
    private boolean isGuestEdited = false;
    private List<Integer> mGuestList = new ArrayList<>();
    private EditGuestPrefsRequest editGuestPrefsRequest = new EditGuestPrefsRequest();
    private AddGuestPrefsRequest addGuestPrefsRequest = new AddGuestPrefsRequest();
    private boolean isOnlySelfTravelling = true;
    private boolean isOnlyGuestsSelected = false;
    private boolean isOnlyNewGuestsAdded = false;
    private List<Integer> mGuestIdsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPassengerBinding = DataBindingUtil.setContentView(this, R.layout.activity_passenger);

        int numOfGuests = Objects.requireNonNull(getIntent().getExtras()).getInt("numOfGuests");

        if(numOfGuests == 1){
            activityPassengerBinding.textViewPassengerSelf.setText(getResources().getString(R.string.info_passenger_self));
            activityPassengerBinding.textViewPassengerGuests.setText(getResources().getString(R.string.info_passenger_guest));
        }else if(numOfGuests > 1){
            activityPassengerBinding.textViewPassengerSelf.setText(getResources().getString(R.string.info_passenger_self_guests));
            activityPassengerBinding.textViewPassengerGuests.setText(getResources().getString(R.string.info_passenger_guests));
        }

        /* set RecyclerView */
        PassengersAdapter mPassengersAdapter = new PassengersAdapter(
                this ,
                this,
                HomeActivity.sUserData.getContacts() ,
                numOfGuests ,
                true
        );

        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengersAdapter);
        activityPassengerBinding.recyclerViewPassengerList.setLayoutFrozen(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                PassengerActivity.this ,
                RecyclerView.VERTICAL ,
                false
        );
        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengersAdapter);
        activityPassengerBinding.recyclerViewPassengerList.setLayoutManager(layoutManager);

        activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);

        if(isOnlySelfTravelling && numOfGuests ==1){
            activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
            activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
        }

        activityPassengerBinding.textViewPassengerSelf.setOnClickListener(v -> {
            isOnlySelfTravelling  =true;
            PassengersAdapter.changeSelfInfo(PassengerActivity.this , true);
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select_bg));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , android.R.color.black)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , R.color.colorPassengerText)
            );
            if(isOnlySelfTravelling && numOfGuests ==1){
                activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
                activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
            }
        });

        activityPassengerBinding.textViewPassengerGuests.setOnClickListener(v -> {
            if(isOnlySelfTravelling){
                isOnlySelfTravelling  =false;
                PassengersAdapter.changeSelfInfo(PassengerActivity.this,false);
                activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select));
                activityPassengerBinding.textViewPassengerSelf.setTextColor(
                        ContextCompat.getColor(PassengerActivity.this , R.color.colorPassengerText)
                );
                activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select_bg));
                activityPassengerBinding.textViewPassengerGuests.setTextColor(ContextCompat.getColor(PassengerActivity.this ,
                        android.R.color.black));
                if(numOfGuests == 1){
                    activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
                    activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);
                }
            }
        });

        activityPassengerBinding.buttonPassengerBack.setOnClickListener(v -> onBackPressed());

        activityPassengerBinding.buttonConfirmBooking.setOnClickListener(v ->{

            if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
                if(isGuestEdited){
                    confirmOnlyExistingGuests();
                }else if(isOnlyNewGuestsAdded){
                    confirmGuests();
                }else if(isOnlyGuestsSelected){
                    bookFlight();
                }else if(isOnlySelfTravelling){
                    bookFlight();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
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
        progress.showProgress(PassengerActivity.this);
        Call<BookingConfirmResponse> mBookingConfirmResponseCall =  RetrofitAPICaller.getInstance(PassengerActivity.this).getStellarJetAPIs()
                .confirmFlightBooking(
                        SharedPreferencesHelper.getUserToken(PassengerActivity.this) ,
                        SharedPreferencesHelper.getFromCityId(PassengerActivity.this) ,
                        SharedPreferencesHelper.getToCityId(PassengerActivity.this) ,
                        SharedPreferencesHelper.getJourneyDate(PassengerActivity.this) ,
                        SharedPreferencesHelper.getJourneyTime(PassengerActivity.this) ,
                        SharedPreferencesHelper.getArrivalTime(PassengerActivity.this) ,
                        SharedPreferencesHelper.getFlightId(PassengerActivity.this) ,
                        HomeActivity.mSeatNamesId ,
                        mGuestList ,
                        selfTravelling ,
                        SharedPreferencesHelper.getScheduleId(PassengerActivity.this)
                );

        mBookingConfirmResponseCall.enqueue(new Callback<BookingConfirmResponse>() {
            @Override
            public void onResponse(@NotNull  Call<BookingConfirmResponse> call,@NotNull Response<BookingConfirmResponse> response) {
                progress.hideProgress();
                if (response.body() != null && response.body().getResultcode() == 1) {
                    Log.d("Booking", "onResponse: " + response.body());
                    SharedPreferencesHelper.saveBookingId(
                            PassengerActivity.this ,
                            String.valueOf(response.body().getData().getBooking_id()));
                    getUserData();
                    Intent mIntent = new Intent(
                            PassengerActivity.this ,
                            BookingConfirmedActivity.class
                    );
                    mIntent.putExtra(UIConstants.BUNDLE_FROM_CITY , SharedPreferencesHelper.getFromCity(PassengerActivity.this));
                    mIntent.putExtra(UIConstants.BUNDLE_TO_CITY , SharedPreferencesHelper.getToCity(PassengerActivity.this));
                    SharedPreferencesHelper.savePersonalizeTime(
                            PassengerActivity.this,
                            StellarJetUtils.getPersonalizationHours(
                                    SharedPreferencesHelper.getJourneyTimeImMillis(PassengerActivity.this)));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                    clearAllBooinngData();
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookingConfirmResponse> call,@NotNull Throwable t) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + t);
                Toast.makeText(PassengerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData(){
        Call<LoginResponse> mCustomerDataResponseCall = RetrofitAPICaller.getInstance(PassengerActivity.this)
                .getStellarJetAPIs().getCustomerData(
                        SharedPreferencesHelper.getUserToken(PassengerActivity.this)
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
                            Toast.makeText(PassengerActivity.this , errorMessage , Toast.LENGTH_LONG).show();
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
    public void isButtonEnabled(boolean isEnabled,
                                List<AddGuestRequestData> mGuestRequestDataList) {
        activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
        List<AddGuestRequestData> mGuestRequestDataListTemp = new ArrayList<>();
        if(isOnlySelfTravelling && mGuestRequestDataList.size() == 1){
//            mGuestRequestDataList.remove(0);
            isOnlySelfTravelling = true;
        }else if(isOnlySelfTravelling && mGuestRequestDataList.size() > 0){
            mGuestRequestDataListTemp = mGuestRequestDataList;
//            mGuestRequestDataListTemp.remove(0);
        }else {
            isOnlySelfTravelling = false;
            mGuestRequestDataListTemp = mGuestRequestDataList;
        }
        makeGuestAddList(mGuestRequestDataListTemp);

    }

    @Override
    public void disableButton(boolean isEnabled) {
        activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);
    }

    private void confirmGuests(){

        addGuestPrefsRequest.setToken(SharedPreferencesHelper.getUserToken(PassengerActivity.this));

        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerActivity.this);

        Call<GuestConfirmResponse> guestConfirmResponseCall =
                RetrofitAPICaller.getInstance(PassengerActivity.this)
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
                    bookFlight();

                }
            }

            @Override
            public void onFailure(Call<GuestConfirmResponse> call, Throwable t) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + t);
                Toast.makeText(PassengerActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmOnlyExistingGuests(){

        editGuestPrefsRequest.setToken(SharedPreferencesHelper.getUserToken(PassengerActivity.this));
        final Progress progress = Progress.getInstance();
        progress.showProgress(PassengerActivity.this);
        Call<GuestConfirmResponse> guestConfirmResponseCall =
                RetrofitAPICaller.getInstance(PassengerActivity.this)
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
                    bookFlight();
                }
            }

            @Override
            public void onFailure(Call<GuestConfirmResponse> call, Throwable t) {
                progress.hideProgress();
                Log.d("Booking", "onResponse: " + t);
                Toast.makeText(PassengerActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeGuestAddList(List<AddGuestRequestData> mGuestRequestDataList){
        List<GuestPrefsDataRequest> editGuestPrefList = new ArrayList<>();
        List<AddGuestPrefsDataRequest> addGuestPrefList = new ArrayList<>();
        mGuestList.clear();
        mGuestIdsList.clear();
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
            if(isOnlySelfTravelling && i==0){
                continue;
            }else {
                if(mGuestRequestDataList.get(i).getGuestStatus().equalsIgnoreCase("edit")){
                    String id = mGuestRequestDataList.get(i).getGuestId();
                    String mobileNumber = mGuestRequestDataList.get(i).getGuestMobileNUmber();
                    List<Integer> mFoodsList = new ArrayList<>();
                    mFoodsList.add(mGuestRequestDataList.get(i).getGuestFoodPreferences());
                    GuestPrefsDataRequest editGuestPrefsData = new GuestPrefsDataRequest();
                    editGuestPrefsData.setGuestId(Integer.valueOf(id));
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
                    mGuestList.add(Integer.valueOf(id));
                    mGuestIdsList.add(Integer.valueOf(id));
                }
            }

        }

        if(editGuestPrefList.size() >0 && addGuestPrefList.size()==0){
            isGuestEdited = true;
            editGuestPrefsRequest.setEditGuestPrefsRequestList(editGuestPrefList);
        }
        if(addGuestPrefList.size()>0 && editGuestPrefList.size() == 0){
            isOnlyNewGuestsAdded = true;
            addGuestPrefsRequest.setAddGuestPrefsRequestList(addGuestPrefList);
        }
        if(editGuestPrefList.size() == 0 && addGuestPrefList.size() ==0){
            isOnlyGuestsSelected = true;
        }
        guestPrefsRequest.setEditGuestPrefsRequestList(editGuestPrefList);
        guestPrefsRequest.setAddGuestPrefsRequestList(addGuestPrefList);

        Log.d("guest", "makeGuestAddList: " + guestPrefsRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PassengersAdapter.mSelectedPhoneNumberList.clear();
        PassengersAdapter.mGuestRequestDataList.clear();
        PassengersAdapter.mPassengerInfoViewHolder = null;
    }


    private void clearAllBooinngData(){
        SharedPreferencesHelper.saveFlightId(PassengerActivity.this , 0);
        SharedPreferencesHelper.saveArrivalTime(PassengerActivity.this , "");
        SharedPreferencesHelper.saveFromCityId(PassengerActivity.this , 0);
        SharedPreferencesHelper.saveToCityId(PassengerActivity.this , 0);
        SharedPreferencesHelper.saveToCity(PassengerActivity.this , "");
        SharedPreferencesHelper.saveFromCity(PassengerActivity.this , "");
        SharedPreferencesHelper.saveJourneyTimeImMillis(PassengerActivity.this , 0);
        SharedPreferencesHelper.saveJourneyTime(PassengerActivity.this , "");
        SharedPreferencesHelper.saveJourneyDate(PassengerActivity.this , "");
        SharedPreferencesHelper.saveArrivalTime(PassengerActivity.this , "");
    }
}
