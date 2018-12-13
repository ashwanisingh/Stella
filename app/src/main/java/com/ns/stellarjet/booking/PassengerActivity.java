package com.ns.stellarjet.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.*;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PassengerListAdapter;
import com.ns.stellarjet.databinding.ActivityPassengerBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PassengerActivity extends AppCompatActivity implements PassengerListAdapter.onConfirmButtonEnableStateListener, PassengerListAdapter.onConfirmButtonDisableStateListener {

    private ActivityPassengerBinding activityPassengerBinding;
    private boolean isSelfTravelling = false;
    private boolean isCabRequired = false;
    private GuestPrefsRequest guestPrefsRequest = new GuestPrefsRequest();
    private boolean isEditGuestOnly = false;
    private List<Integer> mGuestList = new ArrayList<>();
    private EditGuestPrefsRequest editGuestPrefsRequest = new EditGuestPrefsRequest();
    private boolean isOnlySelfTravelling = false;
    private boolean isOnlyGuestsSelected = false;
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

        activityPassengerBinding.textViewPassengerSelf.setOnClickListener(v -> {
            isOnlySelfTravelling  =true;
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_button_background));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , android.R.color.white)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , R.color.colorLoginButton)
            );
        });

        activityPassengerBinding.textViewPassengerGuests.setOnClickListener(v -> {
            isOnlySelfTravelling  =false;
            activityPassengerBinding.textViewPassengerSelf.setBackground(getDrawable(R.drawable.drawable_passenger_select));
            activityPassengerBinding.textViewPassengerSelf.setTextColor(
                    ContextCompat.getColor(PassengerActivity.this , R.color.colorLoginButton)
            );
            activityPassengerBinding.textViewPassengerGuests.setBackground(getDrawable(R.drawable.drawable_button_background));
            activityPassengerBinding.textViewPassengerGuests.setTextColor(ContextCompat.getColor(PassengerActivity.this ,
                    android.R.color.white));
        });

        activityPassengerBinding.buttonPassengerBack.setOnClickListener(v -> onBackPressed());

        activityPassengerBinding.buttonConfirmBooking.setOnClickListener(v ->{
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
                            null ,
                            1
                    );

            mBookingConfirmResponseCall.enqueue(new Callback<BookingConfirmResponse>() {
                @Override
                public void onResponse(@NotNull  Call<BookingConfirmResponse> call,@NotNull Response<BookingConfirmResponse> response) {
                    Log.d("Booking", "onResponse: " + response.body());
                    if (response.body() != null && response.body().getResultcode() == 1) {
                        SharedPreferencesHelper.saveBookingId(
                                PassengerActivity.this ,
                                String.valueOf(response.body().getData().getBooking_id()));
                        Intent mIntent = new Intent(
                                PassengerActivity.this ,
                                BookingConfirmedActivity.class
                        );
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
                        HomeActivity.Companion.clearAllBookingData();
                        Log.d("Booking", "onResponse: " + HomeActivity.journeyDate);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BookingConfirmResponse> call,@NotNull Throwable t) {
                    Log.d("Booking", "onResponse: " + t);
                }
            });
        });



        PassengerListAdapter mPassengerListAdapter = new PassengerListAdapter(
                this ,
                this,
                HomeActivity.sUserData.getContacts() ,
                numOfGuests
        );

        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengerListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                PassengerActivity.this ,
                RecyclerView.VERTICAL ,
                false
        );
        activityPassengerBinding.recyclerViewPassengerList.setAdapter(mPassengerListAdapter);
        activityPassengerBinding.recyclerViewPassengerList.setLayoutManager(layoutManager);

       /* activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);*/
    }

    @Override
    public void isButtonEnabled(boolean isEnabled,
                                List<AddGuestRequestData> mGuestRequestDataList) {
        activityPassengerBinding.buttonConfirmBooking.setEnabled(true);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 1.0);
        if(isSelfTravelling && mGuestRequestDataList.size() == 1){
            mGuestRequestDataList.remove(0);
            isOnlySelfTravelling = true;
        }else if(isSelfTravelling && mGuestRequestDataList.size() > 0){
            mGuestRequestDataList.remove(0);
        }else {
            isOnlySelfTravelling = false;
        }
        makeGuestAddList(mGuestRequestDataList);

    }

    @Override
    public void disableButton(boolean isEnabled) {
        activityPassengerBinding.buttonConfirmBooking.setEnabled(false);
        activityPassengerBinding.buttonConfirmBooking.setAlpha((float) 0.4);
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
            isEditGuestOnly = true;
            editGuestPrefsRequest.setEditGuestPrefsRequestList(editGuestPrefList);
        }
        if(editGuestPrefList.size() == 0 && addGuestPrefList.size() ==0&& !isOnlySelfTravelling){
            isOnlyGuestsSelected = true;
        }
        guestPrefsRequest.setEditGuestPrefsRequestList(editGuestPrefList);
        guestPrefsRequest.setAddGuestPrefsRequestList(addGuestPrefList);

        Log.d("guest", "makeGuestAddList: " + guestPrefsRequest);
    }
}
