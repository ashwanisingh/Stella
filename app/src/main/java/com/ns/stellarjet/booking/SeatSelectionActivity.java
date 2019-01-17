package com.ns.stellarjet.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ns.networking.model.FlightSeatsConfirmResponse;
import com.ns.networking.model.flightsseats.FlightSeatListResponse;
import com.ns.networking.model.flightsseats.FlightSeats;
import com.ns.networking.model.flightsseats.SeatsLockedByUser;
import com.ns.networking.model.guestrequest.BookedSeatsRequest;
import com.ns.networking.model.seatrequest.SeatSelectionRequest;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.button_two_seat_confirmed)
    Button mSeatConfirmedButton;
    @BindView(R.id.button_seat_selection_back)
    Button mBackButton;
    @BindView(R.id.button_seats_alpha)
    Button mAlphaButton;
    @BindView(R.id.button_seats_beta)
    Button mBetaButton;
    @BindView(R.id.button_seats_charlie)
    Button mCharlieButton;
    @BindView(R.id.button_seats_delta)
    Button mDeltaButton;
    @BindView(R.id.button_seats_echo)
    Button mEchoButton;
    @BindView(R.id.button_seats_foxtrot)
    Button mFoxtrotButton;
    @BindView(R.id.button_seats_golf)
    Button mGolfButton;
    @BindView(R.id.button_seats_hotel)
    Button mHotelButton;
    @BindView(R.id.button_seats_indigo)
    Button mIndigoButton;
    @BindView(R.id.button_seats_juliet)
    Button mJulietButton;
    @BindView(R.id.button_seats_kilo)
    Button mKiloButton;
    @BindView(R.id.button_seats_lima)
    Button mLimoButton;

    @BindView(R.id.textView_seat_alpha)
    TextView mAlphaTextView;
    @BindView(R.id.textView_seat_beta)
    TextView mBetaTextView;
    @BindView(R.id.textView_seat_charlie)
    TextView mCharlieTextView;
    @BindView(R.id.textView_seat_delta)
    TextView mDeltaTextView;
    @BindView(R.id.textView_seat_echo)
    TextView mEchoTextView;
    @BindView(R.id.textView_seat_foxtrot)
    TextView mFoxtrotTextView;
    @BindView(R.id.textView_seat_golf)
    TextView mGolfTextView;
    @BindView(R.id.textView_seat_hotel)
    TextView mHotelTextView;
    @BindView(R.id.textView_seat_indigo)
    TextView mIndigoTextView;
    @BindView(R.id.textView_seat_juliet)
    TextView mJulietTextView;
    @BindView(R.id.textView_seat_kilo)
    TextView mKiloTextView;
    @BindView(R.id.textView_seat_lima)
    TextView mLimoTextView;
    @BindView(R.id.textView_two_right_sun_status)
    TextView mRightSunTextView;
    @BindView(R.id.textView_two_left_sun_status)
    TextView mLeftSunTextView;
    @BindView(R.id.layout_two_left_sun_status)
    LinearLayout mLeftLinearLayout;
    @BindView(R.id.layout_two_right_sun_status)
    LinearLayout mRightLinearLayout;

    private boolean isAlphaBooked = false;
    private boolean isBravoBooked = false;
    private boolean isCharlieBooked = false;
    private boolean isDeltaBooked = false;
    private boolean isEchoBooked = false;
    private boolean isFoxtrotBooked = false;
    private boolean isGolfBooked = false;
    private boolean isHotelBooked = false;
    private boolean isIndigoBooked = false;
    private boolean isJulietBooked = false;
    private boolean isKiloBooked = false;
    private boolean isLimoBooked = false;

    private List<FlightSeats> mFlightSeatList = new ArrayList<>();
    private List<SeatSelectionRequest> mSelectedSeatList = new ArrayList<>();
    private List<BookedSeatsRequest> mBookedSeatsList= new ArrayList<>();
    private List<Integer> mConfirmedSeatsList = new ArrayList<>();
    private String flowFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_seat_twelve);

        ButterKnife.bind(SeatSelectionActivity.this);


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

        mAlphaButton.setOnClickListener(this);
        mBetaButton.setOnClickListener(this);
        mCharlieButton.setOnClickListener(this);
        mDeltaButton.setOnClickListener(this);
        mEchoButton.setOnClickListener(this);
        mFoxtrotButton.setOnClickListener(this);
        mGolfButton.setOnClickListener(this);
        mHotelButton.setOnClickListener(this);
        mIndigoButton.setOnClickListener(this);
        mJulietButton.setOnClickListener(this);
        mKiloButton.setOnClickListener(this);
        mLimoButton.setOnClickListener(this);

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
                Toast.makeText(SeatSelectionActivity.this, "Please select seats", Toast.LENGTH_SHORT).show();
            }else {
                Intent mGuestAddIntent = new Intent(SeatSelectionActivity.this , PassengerListActivity.class);
                mGuestAddIntent.putExtra("numOfGuests" , numOfGuests);
                startActivity(mGuestAddIntent);
            }
        });

        // gets the flight details like total seats, avail seats , flight seat parameters
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            getFlightSeats();
        }else{
            Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }

        mConfirmedSeatsList.addAll(HomeActivity.mSeatNamesId);
//        HomeActivity.mSeatNamesId.clear();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getFlightSeats(){

        Call<FlightSeatListResponse> mFlightSeatsDataResponseCall =
                RetrofitAPICaller.getInstance(SeatSelectionActivity.this)
                        .getStellarJetAPIs().getFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getFromCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatSelectionActivity.this)
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
                        Toast.makeText(SeatSelectionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatListResponse> call,@NonNull Throwable t) {
                Log.d("Booking", "onFailure: " + t);
                Toast.makeText(SeatSelectionActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            unlockSeats();
        }else{
            Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void unlockSeats(){
        List<Integer> mUnLockedSeatsList = new ArrayList<>();
        for (int i = 0; i < mSelectedSeatList.size(); i++) {
            if(mSelectedSeatList.get(i).isSelected()){
                int seatID = Integer.valueOf(mSelectedSeatList.get(i).getSeatId());
                mUnLockedSeatsList.add(seatID);
            }
        }
        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(SeatSelectionActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getUserId(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFromCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatSelectionActivity.this),
                        mUnLockedSeatsList,
                        null
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call, @NonNull Response<FlightSeatsConfirmResponse> response) {
                if (response.body() != null) {
                    Log.d("Booking", "onResponse: " +response.body());
                    HomeActivity.mSeatNamesId = response.body().getData().getFlight_seat_availability().getLocked();
                    HomeActivity.mSeatNames.clear();
//                    mConfirmedSeatsList.clear();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call, @NonNull Throwable t) {
                Log.d("Booking", "onFailure: " +t);
                Toast.makeText(SeatSelectionActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();

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
                case 9:
                    setSeatName(i , mIndigoButton , mIndigoTextView);
                    break;
                case 10:
                    setSeatName(i , mJulietButton , mJulietTextView);
                    break;
                case 11:
                    setSeatName(i , mKiloButton , mKiloTextView);
                    break;
                case 12:
                    setSeatName(i , mLimoButton , mLimoTextView);
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
        mBookedSeatsList.add(mBookedSeatsRequest);
    }

    @Override
    public void onClick(View v) {
        flowFrom = "seats";
        switch (v.getId()){
            case R.id.button_seats_alpha:
                isAlphaBooked = !isAlphaBooked;
                selectButtonSelection(mAlphaButton , getResources().getString(R.string.tag_seat_straight) , isAlphaBooked);
                makeSelectedSeatList(mAlphaButton);
                break;
            case R.id.button_seats_beta:
                isBravoBooked = !isBravoBooked;
                selectButtonSelection(mBetaButton , getResources().getString(R.string.tag_seat_straight), isBravoBooked);
                makeSelectedSeatList(mBetaButton);
                break;
            case R.id.button_seats_charlie:
                isCharlieBooked = !isCharlieBooked;
                selectButtonSelection(mCharlieButton, getResources().getString(R.string.tag_seat_reverse) ,isCharlieBooked);
                makeSelectedSeatList(mCharlieButton);
                break;
            case R.id.button_seats_delta:
                isDeltaBooked = !isDeltaBooked;
                selectButtonSelection(mDeltaButton, getResources().getString(R.string.tag_seat_reverse) , isDeltaBooked);
                makeSelectedSeatList(mDeltaButton);
                break;
            case R.id.button_seats_echo:
                isEchoBooked = !isEchoBooked;
                selectButtonSelection(mEchoButton, getResources().getString(R.string.tag_seat_straight) , isEchoBooked);
                makeSelectedSeatList(mEchoButton);
                break;
            case R.id.button_seats_foxtrot:
                isFoxtrotBooked = !isFoxtrotBooked;
                selectButtonSelection(mFoxtrotButton, getResources().getString(R.string.tag_seat_reverse), isFoxtrotBooked);
                makeSelectedSeatList(mFoxtrotButton);
                break;
            case R.id.button_seats_golf:
                isGolfBooked = !isGolfBooked;
                selectButtonSelection(mGolfButton, getResources().getString(R.string.tag_seat_straight), isGolfBooked);
                makeSelectedSeatList(mGolfButton);
                break;
            case R.id.button_seats_hotel:
                isHotelBooked = !isHotelBooked;
                selectButtonSelection(mHotelButton, getResources().getString(R.string.tag_seat_reverse), isHotelBooked);
                makeSelectedSeatList(mHotelButton);
                break;
            case R.id.button_seats_indigo:
                isIndigoBooked = !isIndigoBooked;
                selectButtonSelection(mIndigoButton, getResources().getString(R.string.tag_seat_straight), isIndigoBooked);
                makeSelectedSeatList(mIndigoButton);
                break;
            case R.id.button_seats_juliet:
                isJulietBooked = !isJulietBooked;
                selectButtonSelection(mJulietButton, getResources().getString(R.string.tag_seat_reverse), isJulietBooked);
                makeSelectedSeatList(mJulietButton);
                break;
            case R.id.button_seats_kilo:
                isKiloBooked = !isKiloBooked;
                selectButtonSelection(mKiloButton, getResources().getString(R.string.tag_seat_straight), isKiloBooked);
                makeSelectedSeatList(mKiloButton);
                break;
            case R.id.button_seats_lima:
                isLimoBooked = !isLimoBooked;
                selectButtonSelection(mLimoButton, getResources().getString(R.string.tag_seat_reverse), isLimoBooked);
                makeSelectedSeatList(mLimoButton);
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
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_reverse_selected);
            }else {
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_reverse_available);
            }
        }
    }


    // sets the booked amd locked seats button to disable state
    private void setBookedAndLockedSeats(List<Integer> mSelectedAndLockedSeatsList){
        for (int i = 0; i < mBookedSeatsList.size(); i++) {
            int seatId = mBookedSeatsList.get(i).getSeatId();
            String seatPosition = mBookedSeatsList.get(i).getSeatPosition();
            Button mDesiredButton = mBookedSeatsList.get(i).getmDesiredButton();
            for (int j = 0; j < mSelectedAndLockedSeatsList.size(); j++) {
                int bookedSeatId = mSelectedAndLockedSeatsList.get(j);
                if(seatId == bookedSeatId){
                    mDesiredButton.setEnabled(false);
                    if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_booked);
                    }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_reverse_booked);
                    }
                }
            }
        }
    }

    private void setLockedSeatsByUser(List<SeatsLockedByUser> mSelectedAndLockedSeatsList){
        for (int i = 0; i < mBookedSeatsList.size(); i++) {
            int seatId = mBookedSeatsList.get(i).getSeatId();
            String seatPosition = mBookedSeatsList.get(i).getSeatPosition();
            Button mDesiredButton = mBookedSeatsList.get(i).getmDesiredButton();
            for (int j = 0; j < mSelectedAndLockedSeatsList.size(); j++) {
                int bookedSeatId = mSelectedAndLockedSeatsList.get(j).getFlight_seat_id();
                if(seatId == bookedSeatId){
                    makeSelectedSeatList(mDesiredButton);
                    if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_selected);
                    }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_reverse_selected);
                    }
                }
            }
        }
    }

    // set the selected seat selection to true
    private void makeSelectedSeatList(Button mSelectedButton){
        String seatId = (String) mSelectedButton.getTag();
        for (int i = 0; i < mSelectedSeatList.size(); i++) {
            if(mSelectedSeatList.get(i).getSeatId().equalsIgnoreCase(seatId)){
                boolean isSelected = mSelectedSeatList.get(i).isSelected();
                isSelected = !isSelected;
                if(isSelected){
                    // call confirm seats
                    if(!flowFrom.equalsIgnoreCase("home")){
                        List<Integer> mLockedSeatList = new ArrayList<>();
                        mLockedSeatList.add(Integer.valueOf(mSelectedSeatList.get(i).getSeatId()));
                        confirmSingleSeats(mLockedSeatList);
                    }
                }else {
                    // call unlock seats
                    if(!flowFrom.equalsIgnoreCase("home")){
                        List<Integer> mLockedSeatList = new ArrayList<>();
                        mLockedSeatList.add(Integer.valueOf(mSelectedSeatList.get(i).getSeatId()));
                        unlockSingleSeats(mLockedSeatList);
                    }
                }
                setSeatSelectionOnState(mSelectedSeatList.get(i).getSeatName() , isSelected);
                mSelectedSeatList.get(i).setSelected(isSelected);
            }
        }
    }

    private void setSeatSelectionOnState(String seatName , boolean isSelected){
        switch (seatName){
            case "Alpha":
                isAlphaBooked = isSelected;
                break;
            case "Brave":
                isBravoBooked = isSelected;
                break;
            case "Charlie":
                isCharlieBooked = isSelected;
                break;
            case "Delta":
                isDeltaBooked = isSelected;
                break;
            case "Echo":
                isEchoBooked = isSelected;
                break;
            case "Foxtrot":
                isFoxtrotBooked = isSelected;
                break;
            case "Golf":
                isGolfBooked = isSelected;
                break;
            case "Hotel":
                isHotelBooked = isSelected;
                break;
            case "Indigo":
                isIndigoBooked = isSelected;
                break;
            case "Juliet":
                isJulietBooked = isSelected;
                break;
            case "Kilo":
                isKiloBooked = isSelected;
                break;
            case "Limo":
                isLimoBooked = isSelected;
                break;
        }

    }

    private void confirmSingleSeats(List<Integer> mLockSeatsList){
        Progress mProgress = Progress.getInstance();
        mProgress.showProgress(SeatSelectionActivity.this);

        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(SeatSelectionActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getUserId(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFromCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatSelectionActivity.this),
                        null,
                        mLockSeatsList
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Response<FlightSeatsConfirmResponse> response) {
                mProgress.hideProgress();
                if (response.body() != null) {
                    Log.d("Booking", "onResponse: " +response.body());
                    mConfirmedSeatsList.addAll(mLockSeatsList);
                }else if(response.code()==400){
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        Toast.makeText(SeatSelectionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else if(response.code()==500){
                    Toast.makeText(SeatSelectionActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                mProgress.hideProgress();
                Log.d("Booking", "onFailure: " +t);
                Toast.makeText(SeatSelectionActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unlockSingleSeats(List<Integer> mUnlockSeatsList){
        Progress mProgress = Progress.getInstance();
        mProgress.showProgress(SeatSelectionActivity.this);

        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(SeatSelectionActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFlightId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getUserId(SeatSelectionActivity.this) ,
                        SharedPreferencesHelper.getFromCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getToCityId(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyDate(SeatSelectionActivity.this),
                        SharedPreferencesHelper.getJourneyTime(SeatSelectionActivity.this),
                        mUnlockSeatsList,
                        null
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Response<FlightSeatsConfirmResponse> response) {
                mProgress.hideProgress();
                if (response.body() != null) {
                    if(response.code()==200){
                        Log.d("Booking", "onResponse: " +response.body());
                        mConfirmedSeatsList.removeAll(mUnlockSeatsList);
                    }else if(response.code()==500){
                        Toast.makeText(SeatSelectionActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }else if(response.code()==400){
                        JSONObject mJsonObject;
                        try {
                            mJsonObject = new JSONObject(response.errorBody().string());
                            String errorMessage = mJsonObject.getString("message");
                            Toast.makeText(SeatSelectionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else if(response.errorBody()!=null){
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        Toast.makeText(SeatSelectionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                mProgress.hideProgress();
                Log.d("Booking", "onFailure: " +t);
                Toast.makeText(SeatSelectionActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();
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
        }else if(mDesiredButton == mIndigoButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mJulietButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mKiloButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mLimoButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }

        return seatPosition;
    }
}
