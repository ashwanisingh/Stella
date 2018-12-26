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
import com.ns.networking.model.guestrequest.*;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PassengerListAdapter;
import com.ns.stellarjet.databinding.ActivityPassengerBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PassengerActivity extends AppCompatActivity implements PassengerListAdapter.onConfirmButtonEnableStateListener, PassengerListAdapter.onConfirmButtonDisableStateListener {

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
        PassengerListAdapter mPassengerListAdapter = new PassengerListAdapter(
                this ,
                this,
                HomeActivity.sUserData.getContacts() ,
                numOfGuests ,
                true
        );

        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengerListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                PassengerActivity.this ,
                RecyclerView.VERTICAL ,
                false
        );
        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengerListAdapter);
        activityPassengerBinding.recyclerViewPassengerList.setLayoutManager(layoutManager);

        activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);

        if(isOnlySelfTravelling && numOfGuests ==1){
            activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
            activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
        }

        activityPassengerBinding.textViewPassengerSelf.setOnClickListener(v -> {
            isOnlySelfTravelling  =true;
            PassengerListAdapter.changeSelfInfo(PassengerActivity.this , true);
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_button_background));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , android.R.color.white)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , R.color.colorLoginButton)
            );
            if(isOnlySelfTravelling && numOfGuests ==1){
                activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
                activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
            }
        });

        activityPassengerBinding.textViewPassengerGuests.setOnClickListener(v -> {
            isOnlySelfTravelling  =false;
            PassengerListAdapter.changeSelfInfo(PassengerActivity.this,false);
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , R.color.colorLoginButton)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_button_background));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(ContextCompat.getColor(PassengerActivity.this ,
                    android.R.color.white));
            if(numOfGuests == 1){
                activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
                activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);
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
                        SharedPreferencesHelper.getUserId(PassengerActivity.this) ,
                        HomeActivity.fromCityId ,
                        HomeActivity.toCityId ,
                        HomeActivity.journeyDate,
                        HomeActivity.journeyTime ,
                        HomeActivity.arrivalTime ,
                        HomeActivity.flightId ,
                        HomeActivity.mSeatNamesId ,
                        mGuestList ,
                        selfTravelling
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
                    Intent mIntent = new Intent(
                            PassengerActivity.this ,
                            BookingConfirmedActivity.class
                    );
                    SharedPreferencesHelper.savePersonalizeTime(
                            PassengerActivity.this,
                            StellarJetUtils.getPersonalizationHours(HomeActivity.journeyTimeInMillis));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                    HomeActivity.Companion.clearAllBookingData();
                    Log.d("Booking", "onResponse: " + HomeActivity.journeyDate);
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
            mGuestRequestDataListTemp.remove(0);
        }else {
            isOnlySelfTravelling = false;
        }
        makeGuestAddList(mGuestRequestDataListTemp);

    }

    @Override
    public void disableButton(boolean isEnabled) {
        activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);
    }

    private void confirmGuests(){

        addGuestPrefsRequest.setUserId(SharedPreferencesHelper.getUserId(PassengerActivity.this));
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
        editGuestPrefsRequest.setUserId(SharedPreferencesHelper.getUserId(PassengerActivity.this));
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
        for (int i = 0; i < mGuestRequestDataList.size(); i++) {
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
        PassengerListAdapter.mSelectedPhoneNumberList.clear();
        PassengerListAdapter.mGuestRequestDataList.clear();
        PassengerListAdapter.mPassengerInfoViewHolder = null;

    }
}
