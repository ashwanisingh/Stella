package com.ns.stellarjet.drawer;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.Booking;
import com.ns.networking.model.CancelBookingResponse;
import com.ns.networking.model.CancelBookingUsers;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityCancelBookingBinding;
import com.ns.stellarjet.drawer.adapter.BookingListAdapter;
import com.ns.stellarjet.drawer.adapter.CancelBookingUsersAdapter;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import com.ns.stellarjet.utils.UiUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CancelBookingActivity extends AppCompatActivity implements Function1<CancelBookingUsers, Unit> {

    private List<CancelBookingUsers> mSelectedUserList = new ArrayList<>();
    private List<CancelBookingUsers> mUsersList = new ArrayList<>();
    private boolean isAllSelected = false;
    private ActivityCancelBookingBinding activityBinding;
    private Booking mBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cancel_booking);

        activityBinding = DataBindingUtil.
                setContentView(this , R.layout.activity_cancel_booking);

        mBooking = getIntent().getExtras().getParcelable("BookingData");

        if(isAllSelected){
            activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_select);
        }

        mUsersList = new ArrayList<>();

        if (mBooking != null && mBooking.getTravelling_self() == 1) {
            String name = Objects.requireNonNull(Objects.requireNonNull(mBooking.getPrefs()).getMain_passenger()).getName();
            String seatName = Objects.requireNonNull(Objects.requireNonNull(mBooking.getPrefs().getMain_passenger()).getSeats_info()).getSeat_code();
            int seatId = mBooking.getPrefs().getMain_passenger().getSeats_info().getSeat_id();
            String status = mBooking.getPrefs().getMain_passenger().getStatus();
            if(status.equalsIgnoreCase("Confirmed")){
                mUsersList.add(new CancelBookingUsers(
                        seatName ,
                        seatId ,
                        name,
                        false));
            }
        }

        int guestCount = mBooking.getPrefs().getCo_passengers().size();
        for (int i = 0; i < guestCount; i++) {
            String name = mBooking.getPrefs().getCo_passengers().get(i).getName();
            String seatName = mBooking.getPrefs().getCo_passengers().get(i).getSeats_info().getSeat_code();
            int seatId = mBooking.getPrefs().getCo_passengers().get(i).getSeats_info().getSeat_id();
            String status = mBooking.getPrefs().getCo_passengers().get(i).getStatus();
            if(status.equalsIgnoreCase("Confirmed")){
                mUsersList.add(new CancelBookingUsers(
                        seatName ,
                        seatId ,
                        name,
                        false));
            }
        }

        CancelBookingUsersAdapter cancelBookingUsersAdapter = new CancelBookingUsersAdapter(mUsersList , this , isAllSelected);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this , RecyclerView.VERTICAL , false);
        activityBinding.recyclerViewCancelBooking.setLayoutManager(layoutManager);
        activityBinding.recyclerViewCancelBooking.setAdapter(cancelBookingUsersAdapter);
//        cancelBookingUsersAdapter.selectAll(isAllSelected);

        activityBinding.layoutCancelBookingAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAllSelected){
                    activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_select);
                    isAllSelected = true;
                    String cancelAll = getResources().getString(R.string.biometric_negative_button_text) +
                            " All Seats";
                    activityBinding.buttonCancelBooking.setText(cancelAll);
                    mSelectedUserList.clear();
                    for (int i = 0; i < mUsersList.size(); i++) {
                        mSelectedUserList.add(new CancelBookingUsers(
                                mUsersList.get(i).getSeatName(),
                                mUsersList.get(i).getSeatId(),
                                mUsersList.get(i).getPassengerName() ,
                                true
                        ));
                    }

                }else {
                    activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_unselect);
                    isAllSelected = false;
                    String cancelAll = getResources().getString(R.string.biometric_negative_button_text);
                    activityBinding.buttonCancelBooking.setText(cancelAll);
                    mSelectedUserList.clear();
                }
                cancelBookingUsersAdapter.selectAll(isAllSelected);
            }
        });

        activityBinding.buttonCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StellarJetUtils.isConnectingToInternet(CancelBookingActivity.this)){
                    if(mSelectedUserList.size()!=0){
                        showCancellationDialog();
                    }else {
                        UiUtils.Companion.showToast(CancelBookingActivity.this , "No Users are Selected");
                    }
                }else{
                    UiUtils.Companion.showNoInternetDialog(CancelBookingActivity.this);
                }
            }
        });
    }


    private void showCancellationDialog(){
        StringBuilder seatNames = new StringBuilder();
        StringBuilder passengerNames = new StringBuilder();
        for (int i = 0; i < mSelectedUserList.size(); i++) {
            if(seatNames.length() == 0){
                seatNames = new StringBuilder(mSelectedUserList.get(i).getSeatName());
            }else {
                seatNames.append(",").append(mSelectedUserList.get(i).getSeatName());
            }
        }
        for (int i = 0; i < mSelectedUserList.size(); i++) {
            if(passengerNames.length() == 0){
                passengerNames = new StringBuilder(mSelectedUserList.get(i).getPassengerName());
            }else {
                passengerNames.append(",").append(mSelectedUserList.get(i).getPassengerName());
            }
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to cancel " + seatNames + " of " + passengerNames);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        cancelBooking();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> finish());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
            }
        });
        alertDialog.show();
    }

    @Override
    public Unit invoke(CancelBookingUsers cancelBookingUsers) {
        Log.d("CancelBooking", "invoke: " + cancelBookingUsers.getSeatId());
        if(cancelBookingUsers.isSelected()){
            if(mSelectedUserList.contains(cancelBookingUsers)){
                mSelectedUserList.remove(cancelBookingUsers);
            }else {
                mSelectedUserList.add(cancelBookingUsers);
            }
        }else {
            String name = cancelBookingUsers.getSeatName();
            for (int i = 0; i < mSelectedUserList.size(); i++) {
                if(name.equalsIgnoreCase(mSelectedUserList.get(i).getSeatName())){
                    mSelectedUserList.remove(i);
                }
            }
        }

        for (int i = 0; i < mSelectedUserList.size(); i++) {
            if(mSelectedUserList.get(i).isSelected()){
                isAllSelected = true;
            }
        }

        if(mUsersList.size() == mSelectedUserList.size() && isAllSelected){
            activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_select);
            String cancelAll = getResources().getString(R.string.biometric_negative_button_text) +
                    " All Seats";
            activityBinding.buttonCancelBooking.setText(cancelAll);
        }else if(mSelectedUserList.size()==0){
            activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_unselect);
            isAllSelected = false;
            String cancelAll = getResources().getString(R.string.biometric_negative_button_text);
            activityBinding.buttonCancelBooking.setText(cancelAll);
        }else if(mUsersList.size() != mSelectedUserList.size()){
            activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_unselect);
            isAllSelected = false;
            String cancelAll = getResources().getString(R.string.biometric_negative_button_text);
            activityBinding.buttonCancelBooking.setText(cancelAll);
            if(mSelectedUserList.size()==1){
                String cancelDisplay = getResources().getString(R.string.biometric_negative_button_text) +" "+
                        mSelectedUserList.size() + " Seat";
                activityBinding.buttonCancelBooking.setText(cancelDisplay);
            }else if(mSelectedUserList.size()>1){
                String cancelDisplay = getResources().getString(R.string.biometric_negative_button_text) +" "+
                        mSelectedUserList.size() + " Seats";
                activityBinding.buttonCancelBooking.setText(cancelDisplay);
            }
        }else if(mSelectedUserList.size() == mUsersList.size() && !isAllSelected){
            activityBinding.imageViewCancelBookingAll.setImageResource(R.mipmap.ic_food_unselect);
            isAllSelected = false;
            String cancelAll = getResources().getString(R.string.biometric_negative_button_text);
            activityBinding.buttonCancelBooking.setText(cancelAll);
            if(mSelectedUserList.size()==1){
                String cancelDisplay = getResources().getString(R.string.biometric_negative_button_text) +" "+
                        mSelectedUserList.size() + " Seat";
                activityBinding.buttonCancelBooking.setText(cancelDisplay);
            }else if(mSelectedUserList.size()>1){
                String cancelDisplay = getResources().getString(R.string.biometric_negative_button_text) +" "+
                        mSelectedUserList.size() + " Seats";
                activityBinding.buttonCancelBooking.setText(cancelDisplay);
            }
        }
        return null;
    }

    private void cancelBooking(){
        List<Integer> mSeatIdList = new ArrayList<>();
        for (int i = 0; i < mSelectedUserList.size(); i++) {
            mSeatIdList.add(mSelectedUserList.get(i).getSeatId());
        }
        Progress mProgress =Progress.getInstance();
        mProgress.showProgress(CancelBookingActivity.this);
        Call<CancelBookingResponse> cancelBookingResponseCall = RetrofitAPICaller.getInstance(CancelBookingActivity.this).getStellarJetAPIs()
                .cancelBooking(
                        SharedPreferencesHelper.getUserToken(CancelBookingActivity.this),
                        mBooking.getBooking_id(),
                        mSeatIdList
                );
        cancelBookingResponseCall.enqueue(new Callback<CancelBookingResponse>() {
            @Override
            public void onResponse(Call<CancelBookingResponse> call, Response<CancelBookingResponse> response) {
                mProgress.hideProgress();
                if (response.body()!=null){
                    int subsriptionSeats = SharedPreferencesHelper.getSeatCount(CancelBookingActivity.this);
                    int seatCount  =subsriptionSeats + mSeatIdList.size();
                    SharedPreferencesHelper.saveSeatCount(CancelBookingActivity.this , seatCount);
                    UiUtils.Companion.showToast(CancelBookingActivity.this , "Booking Cancelled");
                    /*if(BookingsDetailsActivity.Companion.getBookingData().getTravelling_self()==1){
                        int seatId = BookingsDetailsActivity.Companion.getBookingData()
                                .getPrefs().getMain_passenger().getSeats_info().getSeat_id();
                        for (int i = 0; i < mSelectedUserList.size(); i++) {
                            if(mSelectedUserList.get(i).getSeatId() == seatId){
                                BookingsDetailsActivity.Companion.getBookingData()
                                        .getPrefs().getMain_passenger().get
                            }
                        }
                    }*/
                    finish();
                }else{
                    UiUtils.Companion.showServerErrorDialog(CancelBookingActivity.this);
                }

            }

            @Override
            public void onFailure(Call<CancelBookingResponse> call, Throwable t) {
                mProgress.hideProgress();
                UiUtils.Companion.showServerErrorDialog(CancelBookingActivity.this);
            }
        });

        /*cancelBookingCall.enqueue(object : Callback<CancelBookingResponse> {
            override fun onResponse(
                    call: Call<CancelBookingResponse>,
            response:
            Response<CancelBookingResponse>) {
                progress.hideProgress()
                var subscriptionSeats = SharedPreferencesHelper.getSeatCount(this@BookingsDetailsActivity)
                mSeatCount += subscriptionSeats
                SharedPreferencesHelper.saveSeatCount(this@BookingsDetailsActivity , mSeatCount)
                UiUtils.showToast(this@BookingsDetailsActivity , "Booking Canceled")
                finish()
                *//*if (response.body()!=null){
                    UiUtils.showToast(this@BookingsDetailsActivity , response.message())
                    finish()
                }else{
//                    UiUtils.showToast(this@BookingsDetailsActivity , "Something went wrong")
                }*//*
            }

            override fun onFailure(call: Call<CancelBookingResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@BookingsDetailsActivity)
            }
        })*/
    }
}
