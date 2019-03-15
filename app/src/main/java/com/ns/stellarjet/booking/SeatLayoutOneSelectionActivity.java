package com.ns.stellarjet.booking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.ns.networking.model.FlightSeatsConfirmResponse;
import com.ns.networking.model.flightsseats.FlightSeatListResponse;
import com.ns.networking.model.flightsseats.FlightSeats;
import com.ns.networking.model.flightsseats.SeatsLockedByUser;
import com.ns.networking.model.guestrequest.BookedSeatsRequest;
import com.ns.networking.model.guestrequest.SeatInfo;
import com.ns.networking.model.seatrequest.SeatSelectionRequest;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.drawer.PurchaseActivity;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.*;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatLayoutOneSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    Button mSeatConfirmedButton;
    Button mBackButton;
    Button mAlphaButton;
    Button mBetaButton;
    Button mCharlieButton;
    Button mDeltaButton;
    Button mEchoButton;
    Button mFoxtrotButton;
    Button mGolfButton;
    Button mHotelButton;
    TextView mAlphaTextView;
    TextView mBetaTextView;
    TextView mCharlieTextView;
    TextView mDeltaTextView;
    TextView mEchoTextView;
    TextView mFoxtrotTextView;
    TextView mGolfTextView;
    TextView mHotelTextView;
    TextView mRightSunTextView;
    TextView mLeftSunTextView;
    LinearLayout mLeftLinearLayout;
    LinearLayout mRightLinearLayout;
    TextView mSeatsAvailableTextView;

    private List<FlightSeats> mFlightSeatList = new ArrayList<>();
    private List<SeatSelectionRequest> mSelectedSeatList = new ArrayList<>();
    private List<BookedSeatsRequest> mBookedSeatsList= new ArrayList<>();
    private List<Integer> mConfirmedSeatsList = new ArrayList<>();
    private String flowFrom = "";
    private boolean isReturnFromPassenger = false;
    private int mNumOfSeatsLocked = 0;
    private int numOfSeatsAvailable = 0;
    private int mGlobalSeatCount = 0;
    private String membershipUserType;
    private ArrayList<String> mSeatNamesList = new ArrayList<>();
    private List<SeatInfo> mSeatInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seat_eight);

        mSeatConfirmedButton = findViewById(R.id.button_seat_confirmed);
        mBackButton = findViewById(R.id.button_seat_selection_back);
        mAlphaButton = findViewById(R.id.button_seats_alpha);
        mBetaButton= findViewById(R.id.button_seats_beta);
        mCharlieButton = findViewById(R.id.button_seats_charlie);
        mDeltaButton = findViewById(R.id.button_seats_delta);
        mEchoButton = findViewById(R.id.button_seats_echo);
        mFoxtrotButton = findViewById(R.id.button_seats_foxtrot);
        mGolfButton = findViewById(R.id.button_seats_golf);
        mHotelButton = findViewById(R.id.button_seats_hotel);
        mAlphaTextView = findViewById(R.id.textView_seat_alpha);
        mBetaTextView= findViewById(R.id.textView_seat_beta);
        mCharlieTextView= findViewById(R.id.textView_seat_charlie);
        mDeltaTextView= findViewById(R.id.textView_seat_delta);
        mEchoTextView= findViewById(R.id.textView_seat_echo);
        mFoxtrotTextView= findViewById(R.id.textView_seat_foxtrot);
        mGolfTextView= findViewById(R.id.textView_seat_golf);
        mHotelTextView= findViewById(R.id.textView_seat_hotel);
        mSeatsAvailableTextView = findViewById(R.id.textView_seat_eight_available_seats);
        mRightSunTextView = findViewById(R.id.textView_right_sun_status);
        mLeftSunTextView= findViewById(R.id.textView_left_sun_status);
        mLeftLinearLayout = findViewById(R.id.layout_left_sun_status);
        mRightLinearLayout = findViewById(R.id.layout_right_sun_status);


        String direction = Objects.requireNonNull(getIntent().getExtras()).getString("direction");
        String sunRiseSet = getIntent().getExtras().getString("sunRiseSet");
        flowFrom = getIntent().getExtras().getString("flowFrom");

        String sunStatusDisplay = null;
        if (sunRiseSet != null) {
            if(sunRiseSet.equalsIgnoreCase("rise")){
                sunStatusDisplay = getResources().getString(R.string.info_flight_sunrise);
            }else if(sunRiseSet.equalsIgnoreCase("set")){
                sunStatusDisplay = getResources().getString(R.string.info_flight_sunset);
            }
        }
        if (direction != null) {
            if(direction.equalsIgnoreCase("right")){
                mRightLinearLayout.setVisibility(View.VISIBLE);
                mRightSunTextView.setText(sunStatusDisplay);
            }else if(direction.equalsIgnoreCase("left")){
                mLeftLinearLayout.setVisibility(View.VISIBLE);
                mLeftSunTextView.setText(sunStatusDisplay);
            }
        }

        // gets the flight details like total seats, avail seats , flight seat parameters
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            getFlightSeats();
        }else{
            UiUtils.Companion.showSimpleDialog(
                    SeatLayoutOneSelectionActivity.this,
                    getResources().getString(R.string.error_not_connected_internet)
            );
        }

        mConfirmedSeatsList.addAll(HomeActivity.mSeatNamesId);

        mAlphaButton.setOnClickListener(this);
        mBetaButton.setOnClickListener(this);
        mCharlieButton.setOnClickListener(this);
        mDeltaButton.setOnClickListener(this);
        mEchoButton.setOnClickListener(this);
        mFoxtrotButton.setOnClickListener(this);
        mGolfButton.setOnClickListener(this);
        mHotelButton.setOnClickListener(this);

        mBackButton.setOnClickListener(v -> onBackPressed());

        mSeatConfirmedButton.setOnClickListener(v -> {
            Log.d("BookSeats", "onClick: " + mSelectedSeatList);
            HomeActivity.mSeatNames.clear();
            HomeActivity.mSeatNamesId.clear();
            for (int i = 0; i < mSelectedSeatList.size(); i++) {
                if(mSelectedSeatList.get(i).isSelected()){
                    int seatID = Integer.valueOf(mSelectedSeatList.get(i).getSeatId());
                    HomeActivity.mSeatNames.add(mSelectedSeatList.get(i).getSeatName());
                    HomeActivity.mSeatNamesId.add(seatID);
                }
            }
            int numOfGuests = HomeActivity.mSeatNamesId.size();
            if(numOfGuests == 0){
                UiUtils.Companion.showSimpleDialog(
                        SeatLayoutOneSelectionActivity.this ,
                        "Please select seats"
                );
            }else {
                Intent mGuestAddIntent = new Intent(SeatLayoutOneSelectionActivity.this , PassengerListActivity.class);
                mGuestAddIntent.putExtra("numOfGuests" , numOfGuests);
                mGuestAddIntent.putStringArrayListExtra("seatNamesList" , mSeatNamesList);
                mGuestAddIntent.putParcelableArrayListExtra("seatInfoList" , (ArrayList<? extends Parcelable>) mSeatInfoList);
                startActivity(mGuestAddIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlobalSeatCount = SharedPreferencesHelper.getSeatCount(SeatLayoutOneSelectionActivity.this);
        mGlobalSeatCount = mGlobalSeatCount - mNumOfSeatsLocked;
        membershipUserType = SharedPreferencesHelper.getMembershipType(SeatLayoutOneSelectionActivity.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        isReturnFromPassenger = true;

        for (int i = 0; i < mSelectedSeatList.size(); i++) {
            mSelectedSeatList.get(i).setSelected(false);
        }
        mFlightSeatList.clear();
        mSelectedSeatList.clear();
        mBookedSeatsList.clear();
        mConfirmedSeatsList.clear();

        resetSeats(mAlphaButton);
        resetSeats(mBetaButton);
        resetSeats(mCharlieButton);
        resetSeats(mDeltaButton);
        resetSeats(mEchoButton);
        resetSeats(mFoxtrotButton);
        resetSeats(mGolfButton);
        resetSeats(mHotelButton);

        // gets the flight details like total seats, avail seats , flight seat parameters
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            getFlightSeats();
        }else{
            UiUtils.Companion.showSimpleDialog(
                    SeatLayoutOneSelectionActivity.this ,
                    getResources().getString(R.string.error_not_connected_internet)
            );
        }
    }

    private void resetSeats(Button mDesiredButton){
        String seatPosition = getSeatPosition(mDesiredButton);
        if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
            mDesiredButton.setBackgroundResource(R.drawable.ic_seat_available);
        }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
            mDesiredButton.setBackgroundResource(R.drawable.ic_seat_available_reverse);
        }
    }

    private void getFlightSeats(){
        Call<FlightSeatListResponse> mFlightSeatsDataResponseCall =
                RetrofitAPICaller.getInstance(SeatLayoutOneSelectionActivity.this)
                        .getStellarJetAPIs().getFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatLayoutOneSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getFromCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatLayoutOneSelectionActivity.this)
                );

        mFlightSeatsDataResponseCall.enqueue(new Callback<FlightSeatListResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatListResponse> call,@NonNull Response<FlightSeatListResponse> response) {
                if(response.body()!=null){
                    Log.d("Booking", "onResponse: " + response.body());
                    mFlightSeatList = response.body().getData().getFlight_seats();
                    List<Integer> mBookedSeats = response.body().getData().getFlight_seat_availability().getBooked();
                    List<Integer> mLockedSeats = response.body().getData().getFlight_seat_availability().getLocked();
                    List<SeatsLockedByUser> mLockedSeatsByUser = response.body().getData().getSeats_locked_by_user();
                    setSeats();
                    setBookedAndLockedSeats(mBookedSeats);
                    setBookedAndLockedSeats(mLockedSeats);
                    setLockedSeatsByUser(mLockedSeatsByUser);
                    numOfSeatsAvailable = response.body().getData().getFlight_seat_availability().getAvailable_seats();
                    String seatsAvailable =
                            String.valueOf(numOfSeatsAvailable)
                                    +" seats available";
                    mSeatsAvailableTextView.setText(seatsAvailable);
                }else{
                    JSONObject mJsonObject = null;
                    try {
                        if (response.errorBody() != null) {
                            mJsonObject = new JSONObject(response.errorBody().string());
                        }
                        String errorMessage = null;
                        if (mJsonObject != null) {
                            errorMessage = mJsonObject.getString("message");
                        }
                        UiUtils.Companion.showSimpleDialog(
                                SeatLayoutOneSelectionActivity.this, errorMessage
                        );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatListResponse> call,@NonNull Throwable t) {
                Log.d("Booking", "onFailure: " + t);
                UiUtils.Companion.showSimpleDialog(
                        SeatLayoutOneSelectionActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            unlockSeats();
        }else{
            UiUtils.Companion.showSimpleDialog(
                    SeatLayoutOneSelectionActivity.this ,
                    getResources().getString(R.string.error_not_connected_internet)
            );
        }
    }

    private void unlockSeats(){
        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(SeatLayoutOneSelectionActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatLayoutOneSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatLayoutOneSelectionActivity.this) ,
                        SharedPreferencesHelper.getFromCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatLayoutOneSelectionActivity.this),
                        mConfirmedSeatsList,
                        null
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Response<FlightSeatsConfirmResponse> response) {
                if (response.body() != null) {
                    Log.d("Booking", "onResponse: " +response.body());
                    HomeActivity.mSeatNamesId = response.body().getData().getFlight_seat_availability().getLocked();
                    HomeActivity.mSeatNames.clear();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                Log.d("Booking", "onFailure: " +t);
                UiUtils.Companion.showSimpleDialog(
                        SeatLayoutOneSelectionActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }

    // set the seat name and seat id dynamically from API response
    private void setSeats(){
        for (int i = 0; i < mFlightSeatList.size(); i++) {
            int sortedOrder = mFlightSeatList.get(i).getSort_order();
            switch (sortedOrder){
                case 1:
                    setSeatName(i , mAlphaButton , mAlphaTextView);
                    break;
                case 2:
                    setSeatName(i , mBetaButton , mBetaTextView);
                    break;
                case 3:
                    setSeatName(i , mCharlieButton , mCharlieTextView);
                    break;
                case 4:
                    setSeatName(i , mDeltaButton , mDeltaTextView);
                    break;
                case 5:
                    setSeatName(i , mEchoButton , mEchoTextView);
                    break;
                case 6:
                    setSeatName(i , mFoxtrotButton , mFoxtrotTextView);
                    break;
                case 7:
                    setSeatName(i , mGolfButton , mGolfTextView);
                    break;
                case 8:
                    setSeatName(i , mHotelButton , mHotelTextView);
                    break;
            }
        }
    }

    // create a booked and looked seat list and list with seatid , seat selection ,
    private void setSeatName(int index , Button mSelectedButton , TextView mSelectedTextView){
        String seatName = mFlightSeatList.get(index).getSeat_code();
        String seatButtonText = seatName.substring(0,1);
        mSelectedButton.setText(seatButtonText);
        mSelectedTextView.setText(seatName);
        int seatId = mFlightSeatList.get(index).getId();
        mSelectedButton.setTag(String.valueOf(seatId));
        SeatSelectionRequest mSeatSelectionRequest = new SeatSelectionRequest();
        mSeatSelectionRequest.setSeatId(String.valueOf(seatId));
        mSeatSelectionRequest.setSelected(false);
        mSeatSelectionRequest.setSeatName(seatName);
        mSelectedSeatList.add(mSeatSelectionRequest);
        BookedSeatsRequest mBookedSeatsRequest = new BookedSeatsRequest();
        mBookedSeatsRequest.setSeatId(seatId);
        mBookedSeatsRequest.setmDesiredButton(mSelectedButton);
        mBookedSeatsRequest.setSeatPosition(getSeatPosition(mSelectedButton));
        mBookedSeatsRequest.setSeatName(seatName);
        mBookedSeatsList.add(mBookedSeatsRequest);
    }

    @Override
    public void onClick(View v) {
        flowFrom = "seats";
        isReturnFromPassenger = false;
        switch (v.getId()){
            case R.id.button_seats_alpha:
                makeSelectedSeatList(mAlphaButton ,  getResources().getString(R.string.tag_seat_straight) );
                break;
            case R.id.button_seats_beta:
                makeSelectedSeatList(mBetaButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_charlie:
                makeSelectedSeatList(mCharlieButton, getResources().getString(R.string.tag_seat_reverse));
                break;
            case R.id.button_seats_delta:
                makeSelectedSeatList(mDeltaButton, getResources().getString(R.string.tag_seat_reverse));
                break;
            case R.id.button_seats_echo:
                makeSelectedSeatList(mEchoButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_foxtrot:
                makeSelectedSeatList(mFoxtrotButton, getResources().getString(R.string.tag_seat_reverse));
                break;
            case R.id.button_seats_golf:
                makeSelectedSeatList(mGolfButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_hotel:
                makeSelectedSeatList(mHotelButton, getResources().getString(R.string.tag_seat_reverse));
                break;
        }
    }

    // change the seat ui to reserve or available
    private void selectButtonSelection(Button mSelectedButton , String seatFace , boolean isSelected){
        if(seatFace.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
            if(isSelected){
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_selected);
            }else {
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_available);
            }
        }else if(seatFace.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
            if (isSelected){
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_selected_reverse);
            }else {
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_available_reverse);
            }
        }
    }


    // sets the booked amd locked seats button to disable state
    private void setBookedAndLockedSeats(List<Integer> mSelectedAndLockedSeatsList){
        for (int i = 0; i < mBookedSeatsList.size(); i++) {
            int seatId = mBookedSeatsList.get(i).getSeatId();
            String seatPosition = mBookedSeatsList.get(i).getSeatPosition();
            Button mDesiredButton = mBookedSeatsList.get(i).getmDesiredButton();
            String mSeatName = mBookedSeatsList.get(i).getSeatName();
            for (int j = 0; j < mSelectedAndLockedSeatsList.size(); j++) {
                int bookedSeatId = mSelectedAndLockedSeatsList.get(j);
                if(seatId == bookedSeatId){
                    mDesiredButton.setEnabled(false);
                    mDesiredButton.setText("");
                    hideBookedSeatsText(mSeatName);
                    if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_booked);
                    }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_booked_reverse);
                        mDesiredButton.setText("");
                    }
                }
            }
        }
    }

    private void hideBookedSeatsText(String seatName){
        if(seatName.equalsIgnoreCase("alpha")){
            mAlphaTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("bravo")){
            mBetaTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("charlie")){
            mCharlieTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("delta")){
            mDeltaTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("echo")){
            mEchoTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("foxtrot")){
            mFoxtrotTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("golf")){
            mGolfTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("hotel")){
            mHotelTextView.setAlpha(0.3f);
        }
    }

    private void setLockedSeatsByUser(List<SeatsLockedByUser> mSelectedAndLockedSeatsList){
        int numOfConfirmedSeats = mSelectedAndLockedSeatsList.size();
        String confirmSeatsDisplay;
        if(numOfConfirmedSeats == 0){
            confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats);
        }else {
            if(numOfConfirmedSeats > 1) {
                confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats) + " - " + numOfConfirmedSeats;
            } else {
                confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seat) + " - " + numOfConfirmedSeats;
            }
        }
        mSeatConfirmedButton.setText(confirmSeatsDisplay);
        for (int i = 0; i < mSelectedAndLockedSeatsList.size(); i++) {
            mConfirmedSeatsList.add(mSelectedAndLockedSeatsList.get(i).getFlight_seat_id());
        }
        mNumOfSeatsLocked = mSelectedAndLockedSeatsList.size();
        mGlobalSeatCount = mGlobalSeatCount - mNumOfSeatsLocked;
        for (int i = 0; i < mBookedSeatsList.size(); i++) {
            int seatId = mBookedSeatsList.get(i).getSeatId();
            String seatPosition = mBookedSeatsList.get(i).getSeatPosition();
            Button mDesiredButton = mBookedSeatsList.get(i).getmDesiredButton();
            for (int j = 0; j < mSelectedAndLockedSeatsList.size(); j++) {
                int bookedSeatId = mSelectedAndLockedSeatsList.get(j).getFlight_seat_id();
                if(seatId == bookedSeatId){
                    makeSelectedSeatList(mDesiredButton, seatPosition);
                    mDesiredButton.setEnabled(true);
                    if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_selected);
                    }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_selected_reverse);
                    }
                }
            }
        }
    }

    // set the selected seat selection to true
    private void makeSelectedSeatList(Button mSelectedButton, String seatPosition){
        String seatId = (String) mSelectedButton.getTag();
        for (int i = 0; i < mSelectedSeatList.size(); i++) {
            if(mSelectedSeatList.get(i).getSeatId().equalsIgnoreCase(seatId)){
                boolean isSelected = mSelectedSeatList.get(i).isSelected();
                String seatName = mSelectedSeatList.get(i).getSeatName();
                isSelected = !isSelected;
                mSelectedSeatList.get(i).setSelected(isSelected);
                if(flowFrom.equalsIgnoreCase("home")){
                    mSeatNamesList.add(seatName);
                    mSeatInfoList.add(new SeatInfo(seatName , Integer.parseInt(seatId)));
                }
                if(isSelected){
                    // call confirm seats
                    if(!flowFrom.equalsIgnoreCase("home")){
                        List<Integer> mLockedSeatList = new ArrayList<>();
                        mLockedSeatList.add(Integer.valueOf(mSelectedSeatList.get(i).getSeatId()));
                        if(!isReturnFromPassenger){
                            if(mGlobalSeatCount==0){
                                String userType = SharedPreferencesHelper.getUserType(SeatLayoutOneSelectionActivity.this);
                                if(userType.equalsIgnoreCase("primary")){
                                    // launch dialog ;;to ask recharge status and launch com.ns.stellarjet.drawer.PurchaseActivity
                                    showPrimaryUserSeatUnavailabilityDialog();
                                }else if(userType.equalsIgnoreCase("secondary")){
                                    String primaryUsername = SharedPreferencesHelper.getCurrentPrimaryUserName(SeatLayoutOneSelectionActivity.this);
                                    UiUtils.Companion.showSimpleDialog(
                                            SeatLayoutOneSelectionActivity.this , "You ran out of seats .Please contact Mr."+primaryUsername
                                                    + "to recharge the seats ");
                                }
                            }else {
                                confirmSingleSeats(mLockedSeatList ,
                                        mSelectedButton ,
                                        seatPosition ,
                                        i ,
                                        seatName ,
                                        seatId);
                            }
                        }
                    }
                }else {
                    // call unlock seats
                    if(!flowFrom.equalsIgnoreCase("home")){
                        List<Integer> mLockedSeatList = new ArrayList<>();
                        mLockedSeatList.add(Integer.valueOf(mSelectedSeatList.get(i).getSeatId()));
                        if(!isReturnFromPassenger){
                            unlockSingleSeats(mLockedSeatList ,
                                    mSelectedButton ,
                                    seatPosition,
                                    i ,
                                    seatName,
                                    seatId);
                        }
                    }
                }
            }
        }
    }

    private void confirmSingleSeats(List<Integer> mLockSeatsList, Button mDesiredButton, String seatPosition,
                                    int position, String seatName, String seatId){
        Progress mProgress = Progress.getInstance();
        mProgress.showProgress(SeatLayoutOneSelectionActivity.this);

        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(SeatLayoutOneSelectionActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatLayoutOneSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getFromCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatLayoutOneSelectionActivity.this),
                        null,
                        mLockSeatsList
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Response<FlightSeatsConfirmResponse> response) {
                mProgress.hideProgress();
                if (response.code() == 200) {
                    Log.d("Booking", "onResponse: " +response.body());
                    mConfirmedSeatsList.addAll(mLockSeatsList);
                    mNumOfSeatsLocked = mNumOfSeatsLocked + 1;
                    String confirmSeatsDisplay = "";

                    if(mNumOfSeatsLocked > 1) {
                        confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats) + " - " + mNumOfSeatsLocked;
                    } else {
                        confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seat) + " - " + mNumOfSeatsLocked;
                    }
                    mSeatConfirmedButton.setText(confirmSeatsDisplay);
                    numOfSeatsAvailable = numOfSeatsAvailable  - 1;
                    String seatsAvailable =
                            String.valueOf(numOfSeatsAvailable)
                                    +" seats available";
                    mSeatsAvailableTextView.setText(seatsAvailable);
                    /* Subscription seats avail check*/
                    String userType = SharedPreferencesHelper.getUserType(SeatLayoutOneSelectionActivity.this);
                    if(membershipUserType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                        mGlobalSeatCount = mGlobalSeatCount -1;
                        Log.wtf("SeatCount", "onResponse: lock==> " + mGlobalSeatCount);
                        if(mGlobalSeatCount==0&& userType.equalsIgnoreCase("primary")){
                            // launch dialog ;;to ask recharge status and launch com.ns.stellarjet.drawer.PurchaseActivity
                            showPrimaryUserSeatUnavailabilityDialog();
                        }else if(mGlobalSeatCount==0&& userType.equalsIgnoreCase("secondary")){
                            String primaryUsername = SharedPreferencesHelper.getCurrentPrimaryUserName(SeatLayoutOneSelectionActivity.this);
                            UiUtils.Companion.showSimpleDialog(
                                    SeatLayoutOneSelectionActivity.this , "You ran out of seats .Please contact Mr."+primaryUsername
                                            + "to recharge the seats ");
                        }
                    }
                    selectButtonSelection(mDesiredButton, seatPosition ,true);
                    mSelectedSeatList.get(position).setSelected(true);
                    mSeatNamesList.add(seatName);
                    mSeatInfoList.add(new SeatInfo(seatName , Integer.parseInt(seatId)));
                }else if(response.code()==400){
                    selectButtonSelection(mDesiredButton, seatPosition ,false);
                    mSelectedSeatList.get(position).setSelected(false);
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        UiUtils.Companion.showSimpleDialog(
                                SeatLayoutOneSelectionActivity.this, errorMessage
                        );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else if(response.code()==500){
                    UiUtils.Companion.showSimpleDialog(
                            SeatLayoutOneSelectionActivity.this,
                            getResources().getString(R.string.error_server)
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                mProgress.hideProgress();
                Log.d("Booking", "onFailure: " +t);
                UiUtils.Companion.showSimpleDialog(
                        SeatLayoutOneSelectionActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }

    private void unlockSingleSeats(List<Integer> mUnlockSeatsList, Button mDesiredButton,
                                   String seatPosition, int position, String seatName, String seatId){
        Progress mProgress = Progress.getInstance();
        mProgress.showProgress(SeatLayoutOneSelectionActivity.this);

        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(SeatLayoutOneSelectionActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatLayoutOneSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getFromCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatLayoutOneSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatLayoutOneSelectionActivity.this),
                        mUnlockSeatsList,
                        null
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Response<FlightSeatsConfirmResponse> response) {
                mProgress.hideProgress();
                if(response.code()==200){
                    Log.d("Booking", "onResponse: " +response.body());
                    mConfirmedSeatsList.removeAll(mUnlockSeatsList);
                    mNumOfSeatsLocked = mNumOfSeatsLocked - 1;
                    String confirmSeatsDisplay;
                    if(mNumOfSeatsLocked == 0){
                        confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats);
                    }else {
                        confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats)
                                + " - "+ mNumOfSeatsLocked;
                    }
                    mSeatConfirmedButton.setText(confirmSeatsDisplay);
                    numOfSeatsAvailable = numOfSeatsAvailable + 1;
                    String seatsAvailable =
                            String.valueOf(numOfSeatsAvailable)
                                    +" seats available";
                    mSeatsAvailableTextView.setText(seatsAvailable);
                    if(membershipUserType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                        mGlobalSeatCount = mGlobalSeatCount + 1;
                        Log.wtf("SeatCount", "onResponse: unlock ==>" + mGlobalSeatCount);
                    }
                    selectButtonSelection(mDesiredButton, seatPosition ,false);
                    mSelectedSeatList.get(position).setSelected(false);
                    mSeatNamesList.remove(seatName);
                    for (int i = 0; i < mSeatInfoList.size(); i++) {
                        if(mSeatInfoList.get(i).getSeatName().equalsIgnoreCase(seatName)){
                            mSeatInfoList.remove(i);
                        }
                    }
                }else if(response.code()==500){
                    UiUtils.Companion.showSimpleDialog(
                            SeatLayoutOneSelectionActivity.this,
                            getResources().getString(R.string.error_server)
                    );
                }else if(response.code()==400){
                    selectButtonSelection(mDesiredButton, seatPosition ,false);
                    mSelectedSeatList.get(position).setSelected(false);
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        UiUtils.Companion.showSimpleDialog(
                                SeatLayoutOneSelectionActivity.this,
                                errorMessage
                        );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                mProgress.hideProgress();
                Log.d("Booking", "onFailure: " +t);
                UiUtils.Companion.showSimpleDialog(
                        SeatLayoutOneSelectionActivity.this,
                        getResources().getString(R.string.error_server)
                );
            }
        });
    }


    // returns the seat postion ie reverse or straight position
    private String getSeatPosition(Button mDesiredButton){
        String seatPosition = "";
        if(mDesiredButton == mAlphaButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mBetaButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mCharlieButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mDeltaButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mEchoButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mFoxtrotButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mGolfButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mHotelButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }
        return seatPosition;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mSelectedSeatList.size(); i++) {
            mSelectedSeatList.get(i).setSelected(false);
        }
        mFlightSeatList.clear();
        mSelectedSeatList.clear();
        mBookedSeatsList.clear();
        mConfirmedSeatsList.clear();

        resetSeats(mAlphaButton);
        resetSeats(mBetaButton);
        resetSeats(mCharlieButton);
        resetSeats(mDeltaButton);
        resetSeats(mEchoButton);
        resetSeats(mFoxtrotButton);
        resetSeats(mGolfButton);
        resetSeats(mHotelButton);
    }

    private void showPrimaryUserSeatUnavailabilityDialog(){
        String userName = SharedPreferencesHelper.getUserName(SeatLayoutOneSelectionActivity.this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hi Mr."+userName+", you have consumed your seats.Please recharge");
        alertDialogBuilder.setPositiveButton("Ok",
                (arg0, arg1) -> startActivity(new Intent(SeatLayoutOneSelectionActivity.this , PurchaseActivity.class)));

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
        });
        alertDialog.show();
    }
}
