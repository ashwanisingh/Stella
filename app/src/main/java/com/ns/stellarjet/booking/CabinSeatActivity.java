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

public class CabinSeatActivity extends AppCompatActivity implements View.OnClickListener {

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
    Button mIndigoButton;
    Button mJulietButton;
    Button mKiloButton;
    Button mLimoButton;
    Button mMikeButton;
    Button mNovemberButton;
    Button mOscarButton;
    Button mPapaButton;
    Button mQuebecButton;

    TextView mAlphaTextView;
    TextView mBetaTextView;
    TextView mCharlieTextView;
    TextView mDeltaTextView;
    TextView mEchoTextView;
    TextView mFoxtrotTextView;
    TextView mGolfTextView;
    TextView mHotelTextView;
    TextView mIndigoTextView;
    TextView mJulietTextView;
    TextView mKiloTextView;
    TextView mLimoTextView;
    TextView mMikeTextView;
    TextView mNovemberTextView;
    TextView mOscarTextView;
    TextView mPapaTextView;
    TextView mQuebecTextView;
    TextView mRightSunTextView ;
    TextView mLeftSunTextView;
    LinearLayout mLeftLinearLayout ;
    LinearLayout mRightLinearLayout;
    TextView mSeatsAvailableTextView ;

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
        setContentView(R.layout.activtiy_seat_cabin);

//         ActivityCa  = DataBindingUtil.setContentView(this , R.layout.activtiy_seat_cabin);
        mSeatConfirmedButton = findViewById(R.id.button_seat_confirmed);
        mBackButton = findViewById(R.id.button_seat_cabin_back);
        mAlphaButton = findViewById(R.id.button_seats_a);
        mBetaButton= findViewById(R.id.button_seats_b);
        mCharlieButton = findViewById(R.id.button_seats_c);
        mDeltaButton = findViewById(R.id.button_seats_d);
        mEchoButton = findViewById(R.id.button_seats_e);
        mFoxtrotButton = findViewById(R.id.button_seats_f);
        mGolfButton = findViewById(R.id.button_seats_g);
        mHotelButton = findViewById(R.id.button_seats_h);
        mIndigoButton = findViewById(R.id.button_seats_i);
        mJulietButton = findViewById(R.id.button_seats_j);
        mKiloButton = findViewById(R.id.button_seats_k);
        mLimoButton= findViewById(R.id.button_seats_l);
        mMikeButton= findViewById(R.id.button_seats_m);
        mNovemberButton= findViewById(R.id.button_seats_n);
        mOscarButton= findViewById(R.id.button_seats_o);
        mPapaButton= findViewById(R.id.button_seats_p);
        mQuebecButton = findViewById(R.id.button_seats_q);
        mAlphaTextView = findViewById(R.id.textView_seat_a);
        mBetaTextView= findViewById(R.id.textView_seat_b);
        mCharlieTextView= findViewById(R.id.textView_seat_c);
        mDeltaTextView= findViewById(R.id.textView_seat_d);
        mEchoTextView= findViewById(R.id.textView_seat_e);
        mFoxtrotTextView= findViewById(R.id.textView_seat_f);
        mGolfTextView= findViewById(R.id.textView_seat_g);
        mHotelTextView= findViewById(R.id.textView_seat_h);
        mIndigoTextView= findViewById(R.id.textView_seat_i);
        mJulietTextView= findViewById(R.id.textView_seat_j);
        mKiloTextView= findViewById(R.id.textView_seat_k);
        mLimoTextView= findViewById(R.id.textView_seat_l);
        mMikeTextView= findViewById(R.id.textView_seat_m);
        mNovemberTextView= findViewById(R.id.textView_seat_n);
        mOscarTextView= findViewById(R.id.textView_seat_o);
        mPapaTextView= findViewById(R.id.textView_seat_p);
        mQuebecTextView= findViewById(R.id.textView_seat_q);
        mSeatsAvailableTextView = findViewById(R.id.textView_cabin_seats_available);
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
        mLimoButton.setOnClickListener(this);
        mMikeButton.setOnClickListener(this);
        mNovemberButton.setOnClickListener(this);
        mOscarButton.setOnClickListener(this);
        mQuebecButton.setOnClickListener(this);

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
                        CabinSeatActivity.this, "Please select seats"
                );
            }else {
                Intent mGuestAddIntent = new Intent(CabinSeatActivity.this , PassengerListActivity.class);
                mGuestAddIntent.putExtra("numOfGuests" , numOfGuests);
                mGuestAddIntent.putStringArrayListExtra("seatNamesList" , mSeatNamesList);
                mGuestAddIntent.putParcelableArrayListExtra("seatInfoList" , (ArrayList<? extends Parcelable>) mSeatInfoList);
                startActivity(mGuestAddIntent);
            }
        });

        // gets the flight details like total seats, avail seats , flight seat parameters
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            getFlightSeats();
        }else{
            UiUtils.Companion.showNoInternetDialog(CabinSeatActivity.this);
        }

        mConfirmedSeatsList.addAll(HomeActivity.mSeatNamesId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlobalSeatCount = SharedPreferencesHelper.getSeatCount(CabinSeatActivity.this);
        mGlobalSeatCount = mGlobalSeatCount - mNumOfSeatsLocked;
        membershipUserType = SharedPreferencesHelper.getMembershipType(CabinSeatActivity.this);
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
        resetSeats(mIndigoButton);
        resetSeats(mJulietButton);
        resetSeats(mKiloButton);
        resetSeats(mLimoButton);
        resetSeats(mMikeButton);
        resetSeats(mNovemberButton);
        resetSeats(mOscarButton);
        resetSeats(mPapaButton);
        resetSeats(mQuebecButton);

        // gets the flight details like total seats, avail seats , flight seat parameters
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            getFlightSeats();
        }else{
            UiUtils.Companion.showNoInternetDialog(CabinSeatActivity.this);
        }
    }

    private void resetSeats(Button mDesiredButton){
        String seatPOsition = getSeatPosition(mDesiredButton);
        if(seatPOsition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_straight))){
            mDesiredButton.setBackgroundResource(R.drawable.ic_seat_available);
        }else if(seatPOsition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_reverse))){
            mDesiredButton.setBackgroundResource(R.drawable.ic_seat_available_reverse);
        }
    }

    private void getFlightSeats(){

        Call<FlightSeatListResponse> mFlightSeatsDataResponseCall =
                RetrofitAPICaller.getInstance(CabinSeatActivity.this)
                        .getStellarJetAPIs().getFlightSeats(
                        SharedPreferencesHelper.getUserToken(CabinSeatActivity.this) ,
                        SharedPreferencesHelper.getFlightId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getFromCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getToCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyDate(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyTime(CabinSeatActivity.this)
                );

        mFlightSeatsDataResponseCall.enqueue(new Callback<FlightSeatListResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatListResponse> call, @NonNull Response<FlightSeatListResponse> response) {
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
                                CabinSeatActivity.this, errorMessage
                        );
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatListResponse> call,@NonNull Throwable t) {
                Log.d("Booking", "onFailure: " + t);
                UiUtils.Companion.showServerErrorDialog(CabinSeatActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(StellarJetUtils.isConnectingToInternet(getApplicationContext())){
            unlockSeats();
        }else{
            UiUtils.Companion.showNoInternetDialog(CabinSeatActivity.this);
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
                RetrofitAPICaller.getInstance(CabinSeatActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(CabinSeatActivity.this) ,
                        SharedPreferencesHelper.getFlightId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getFromCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getToCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyDate(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyTime(CabinSeatActivity.this),
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
                UiUtils.Companion.showServerErrorDialog(CabinSeatActivity.this);
            }
        });
    }

    // set the seat name and seat id dynamically from API response
    private void setSeats(){
        for (int i = 0; i < mFlightSeatList.size(); i++) {
//            int sortedOrder = mFlightSeatList.get(i).getSort_order();
            switch (i){
                case 0:
                    setSeatName(i , mAlphaButton , mAlphaTextView);
                    break;
                case 1:
                    setSeatName(i , mBetaButton , mBetaTextView);
                    break;
                case 2:
                    setSeatName(i , mCharlieButton , mCharlieTextView);
                    break;
                case 3:
                    setSeatName(i , mDeltaButton , mDeltaTextView);
                    break;
                case 4:
                    setSeatName(i , mEchoButton , mEchoTextView);
                    break;
                case 5:
                    setSeatName(i , mFoxtrotButton , mFoxtrotTextView);
                    break;
                case 6:
                    setSeatName(i , mGolfButton , mGolfTextView);
                    break;
                case 7:
                    setSeatName(i , mHotelButton , mHotelTextView);
                    break;
                case 8:
                    setSeatName(i , mIndigoButton , mIndigoTextView);
                    break;
                case 9:
                    setSeatName(i , mJulietButton , mJulietTextView);
                    break;
                case 10:
                    setSeatName(i , mKiloButton , mKiloTextView);
                    break;
                case 11:
                    setSeatName(i , mLimoButton , mLimoTextView);
                    break;
                case 12:
                    setSeatName(i , mMikeButton , mMikeTextView);
                    break;
                case 13:
                    setSeatName(i , mNovemberButton , mNovemberTextView);
                    break;
                case 14:
                    setSeatName(i , mOscarButton , mOscarTextView);
                    break;
                case 15:
                    setSeatName(i , mPapaButton , mPapaTextView);
                    break;
                case 16:
                    setSeatName(i , mQuebecButton, mQuebecTextView);
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
            case R.id.button_seats_a:
                makeSelectedSeatList(mAlphaButton,getResources().getString(R.string.tag_seat_right));
                break;
            case R.id.button_seats_b:
                makeSelectedSeatList(mBetaButton, getResources().getString(R.string.tag_seat_left));
                break;
            case R.id.button_seats_c:
                makeSelectedSeatList(mCharlieButton, getResources().getString(R.string.tag_seat_left));
                break;
            case R.id.button_seats_d:
                makeSelectedSeatList(mDeltaButton, getResources().getString(R.string.tag_seat_right));
                break;
            case R.id.button_seats_e:
                makeSelectedSeatList(mEchoButton, getResources().getString(R.string.tag_seat_right));
                break;
            case R.id.button_seats_f:
                makeSelectedSeatList(mFoxtrotButton, getResources().getString(R.string.tag_seat_left));
                break;
            case R.id.button_seats_g:
                makeSelectedSeatList(mGolfButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_h:
                makeSelectedSeatList(mHotelButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_i:
                makeSelectedSeatList(mIndigoButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_j:
                makeSelectedSeatList(mJulietButton, getResources().getString(R.string.tag_seat_reverse));
                break;
            case R.id.button_seats_k:
                makeSelectedSeatList(mKiloButton, getResources().getString(R.string.tag_seat_reverse));
                break;
            case R.id.button_seats_l:
                makeSelectedSeatList(mLimoButton, getResources().getString(R.string.tag_seat_reverse));
                break;
            case R.id.button_seats_m:
                makeSelectedSeatList(mLimoButton, getResources().getString(R.string.tag_seat_left));
                break;
            case R.id.button_seats_n:
                makeSelectedSeatList(mLimoButton, getResources().getString(R.string.tag_seat_left));
                break;
            case R.id.button_seats_o:
                makeSelectedSeatList(mLimoButton, getResources().getString(R.string.tag_seat_left));
                break;
            case R.id.button_seats_p:
                makeSelectedSeatList(mLimoButton, getResources().getString(R.string.tag_seat_straight));
                break;
            case R.id.button_seats_q:
                makeSelectedSeatList(mLimoButton, getResources().getString(R.string.tag_seat_reverse));
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
        }else if(seatFace.equalsIgnoreCase(getResources().getString(R.string.tag_seat_right))){
            if (isSelected){
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_selected_right_face);
            }else {
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_available_right_face);
            }
        }else if(seatFace.equalsIgnoreCase(getResources().getString(R.string.tag_seat_left))){
            if (isSelected){
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_selected_left_face);
            }else {
                mSelectedButton.setBackgroundResource(R.drawable.ic_seat_available_left_face);
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
                    }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_right))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_booked_right_face);
                    }else if(seatPosition.equalsIgnoreCase(getResources().getString(R.string.tag_seat_left))){
                        mDesiredButton.setBackgroundResource(R.drawable.ic_seat_booked_left_face);
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
        }else if(seatName.equalsIgnoreCase("Indigo")){
            mIndigoTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Juliet")){
            mJulietTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Kilo")){
            mKiloTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Lima")){
            mLimoTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Mike")){
            mMikeTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("November")){
            mNovemberTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Oscar")){
            mOscarTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Papa")){
            mPapaTextView.setAlpha(0.3f);
        }else if(seatName.equalsIgnoreCase("Quebec")){
            mQuebecTextView.setAlpha(0.3f);
        }
    }

    private void setLockedSeatsByUser(List<SeatsLockedByUser> mSelectedAndLockedSeatsList){
        int numOfConfirmedSeats = mSelectedAndLockedSeatsList.size();
        String confirmSeatsDisplay;
        if(numOfConfirmedSeats == 0){
            confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats);
        }else {
            confirmSeatsDisplay = getResources().getString(R.string.booking_confirm_seats) + " - "+ numOfConfirmedSeats;
        }
        mNumOfSeatsLocked = mSelectedAndLockedSeatsList.size();
        mGlobalSeatCount = mGlobalSeatCount - mNumOfSeatsLocked;
        mSeatConfirmedButton.setText(confirmSeatsDisplay);
        for (int i = 0; i < mSelectedAndLockedSeatsList.size(); i++) {
            mConfirmedSeatsList.add(mSelectedAndLockedSeatsList.get(i).getFlight_seat_id());
        }
        for (int i = 0; i < mBookedSeatsList.size(); i++) {
            int seatId = mBookedSeatsList.get(i).getSeatId();
            String seatPosition = mBookedSeatsList.get(i).getSeatPosition();
            Button mDesiredButton = mBookedSeatsList.get(i).getmDesiredButton();
            for (int j = 0; j < mSelectedAndLockedSeatsList.size(); j++) {
                int bookedSeatId = mSelectedAndLockedSeatsList.get(j).getFlight_seat_id();
                if(seatId == bookedSeatId){
                    makeSelectedSeatList(mDesiredButton , seatPosition);
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
    private void makeSelectedSeatList(Button mSelectedButton,String seatPosition){
        String seatId = (String) mSelectedButton.getTag();
        for (int i = 0; i < mSelectedSeatList.size(); i++) {
            if(mSelectedSeatList.get(i).getSeatId().equalsIgnoreCase(seatId)){
                boolean isSelected = mSelectedSeatList.get(i).isSelected();
                String seatName = mSelectedSeatList.get(i).getSeatName();
                isSelected = !isSelected;
                mSelectedSeatList.get(i).setSelected(isSelected);
                if(isSelected){
                    // call confirm seats
                    if(!flowFrom.equalsIgnoreCase("home")){
                        List<Integer> mLockedSeatList = new ArrayList<>();
                        mLockedSeatList.add(Integer.valueOf(mSelectedSeatList.get(i).getSeatId()));
                        if(!isReturnFromPassenger){
                            if(mGlobalSeatCount==0){
                                String userType = SharedPreferencesHelper.getUserType(CabinSeatActivity.this);
                                if(userType.equalsIgnoreCase("primary")){
                                    // launch dialog ;;to ask recharge status and launch com.ns.stellarjet.drawer.PurchaseActivity
                                    showPrimaryUserSeatUnavailabilityDialog();
                                }else if(userType.equalsIgnoreCase("secondary")){
                                    String primaryUsername = SharedPreferencesHelper.getCurrentPrimaryUserName(CabinSeatActivity.this);
                                    UiUtils.Companion.showSimpleDialog(
                                            CabinSeatActivity.this , "You ran out of seats .Please contact Mr."+primaryUsername
                                                    + "to recharge the seats ");
                                }
                            }else{
                                confirmSingleSeats(mLockedSeatList,
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
                        unlockSingleSeats(mLockedSeatList,
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

    private void confirmSingleSeats(List<Integer> mLockSeatsList, Button mDesiredButton, String seatPosition,
                                    int position, String seatName, String seatId){
        Progress mProgress = Progress.getInstance();
        mProgress.showProgress(CabinSeatActivity.this);

        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(CabinSeatActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(CabinSeatActivity.this) ,
                        SharedPreferencesHelper.getFlightId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getFromCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getToCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyDate(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyTime(CabinSeatActivity.this),
                        null,
                        mLockSeatsList
                );

        mFlightSeatsConfirmCall.enqueue(new Callback<FlightSeatsConfirmResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Response<FlightSeatsConfirmResponse> response) {
                mProgress.hideProgress();
                if (response.body() != null) {
                    Log.d("Booking", "onResponse: " +response.body());
                    /* adding the seats to list*/
                    mConfirmedSeatsList.addAll(mLockSeatsList);
                    /* increase locked seats count to thi booking */
                    mNumOfSeatsLocked = mNumOfSeatsLocked + 1;
                    String confirmSeatsDisplay =
                            getResources().getString(R.string.booking_confirm_seats) + " - "+
                                    mNumOfSeatsLocked;
                    mSeatConfirmedButton.setText(confirmSeatsDisplay);
                    numOfSeatsAvailable = numOfSeatsAvailable  - 1;
                    String seatsAvailable =
                            String.valueOf(numOfSeatsAvailable)
                                    +" seats available";
                    mSeatsAvailableTextView.setText(seatsAvailable);

                    /* Subscription seats avail check*/
                    String userType = SharedPreferencesHelper.getUserType(CabinSeatActivity.this);
                    if(membershipUserType.equalsIgnoreCase(UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION)){
                        mGlobalSeatCount = mGlobalSeatCount - 1;
                        Log.wtf("SeatCount", "onResponse: lock==> " + mGlobalSeatCount);
                        if(mGlobalSeatCount==0&& userType.equalsIgnoreCase("primary")){
                            // launch dialog to ask recharge status and launch com.ns.stellarjet.drawer.PurchaseActivity
                            showPrimaryUserSeatUnavailabilityDialog();
                        }else if(mGlobalSeatCount==0&& userType.equalsIgnoreCase("secondary")){
                            String primaryUsername = SharedPreferencesHelper.getCurrentPrimaryUserName(CabinSeatActivity.this);
                            UiUtils.Companion.showSimpleDialog(
                                    CabinSeatActivity.this , "You ran out of seats .Please contact Mr."+primaryUsername
                                            + "to recharge the seats ");
                        }
                    }
                    selectButtonSelection(mDesiredButton, seatPosition ,true);
                    mSelectedSeatList.get(position).setSelected(true);
                    mSeatNamesList.add(seatName);
                    mSeatInfoList.add(new SeatInfo(seatName , Integer.parseInt(seatId)));
                }else if(response.code()==400){
                    mSelectedSeatList.get(position).setSelected(false);
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        UiUtils.Companion.showSimpleDialog(CabinSeatActivity.this, errorMessage);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else if(response.code()==500){
                    UiUtils.Companion.showServerErrorDialog(CabinSeatActivity.this);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                mProgress.hideProgress();
                Log.d("Booking", "onFailure: " +t);
                UiUtils.Companion.showServerErrorDialog(CabinSeatActivity.this);
            }
        });
    }

    private void unlockSingleSeats(List<Integer> mUnlockSeatsList, Button mDesiredButton,
                                   String seatPosition, int position, String seatName, String seatId){
        Progress mProgress = Progress.getInstance();
        mProgress.showProgress(CabinSeatActivity.this);

        Call<FlightSeatsConfirmResponse> mFlightSeatsConfirmCall =
                RetrofitAPICaller.getInstance(CabinSeatActivity.this)
                        .getStellarJetAPIs().confirmFlightSeats(
                        SharedPreferencesHelper.getUserToken(CabinSeatActivity.this) ,
                        SharedPreferencesHelper.getFlightId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getFromCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getToCityId(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyDate(CabinSeatActivity.this),
                        SharedPreferencesHelper.getJourneyTime(CabinSeatActivity.this),
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
                        UiUtils.Companion.showServerErrorDialog(CabinSeatActivity.this);
                    }else if(response.code()==400){
                        mSelectedSeatList.get(position).setSelected(false);
                        JSONObject mJsonObject;
                        try {
                            mJsonObject = new JSONObject(response.errorBody().string());
                            String errorMessage = mJsonObject.getString("message");
                            UiUtils.Companion.showSimpleDialog(CabinSeatActivity.this, errorMessage);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else if(response.errorBody()!=null){
                    JSONObject mJsonObject;
                    try {
                        mJsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = mJsonObject.getString("message");
                        UiUtils.Companion.showSimpleDialog(CabinSeatActivity.this, errorMessage);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightSeatsConfirmResponse> call,@NonNull Throwable t) {
                mProgress.hideProgress();
                Log.d("Booking", "onFailure: " +t);
                UiUtils.Companion.showServerErrorDialog(CabinSeatActivity.this);
            }
        });
    }

    // returns the seat postion ie reverse or straight position
    private String getSeatPosition(Button mDesiredButton){
        String seatPosition = "";
        if(mDesiredButton == mAlphaButton){
            seatPosition = getResources().getString(R.string.tag_seat_right);
        }else if(mDesiredButton == mBetaButton){
            seatPosition = getResources().getString(R.string.tag_seat_left);
        }else if(mDesiredButton == mCharlieButton){
            seatPosition = getResources().getString(R.string.tag_seat_left);
        }else if(mDesiredButton == mDeltaButton){
            seatPosition = getResources().getString(R.string.tag_seat_right);
        }else if(mDesiredButton == mEchoButton){
            seatPosition = getResources().getString(R.string.tag_seat_right);
        }else if(mDesiredButton == mFoxtrotButton){
            seatPosition = getResources().getString(R.string.tag_seat_left);
        }else if(mDesiredButton == mGolfButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mHotelButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mIndigoButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mJulietButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mKiloButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mLimoButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }else if(mDesiredButton == mMikeButton){
            seatPosition = getResources().getString(R.string.tag_seat_left);
        }else if(mDesiredButton == mNovemberButton){
            seatPosition = getResources().getString(R.string.tag_seat_left);
        }else if(mDesiredButton == mOscarButton){
            seatPosition = getResources().getString(R.string.tag_seat_left);
        }else if(mDesiredButton == mPapaButton){
            seatPosition = getResources().getString(R.string.tag_seat_straight);
        }else if(mDesiredButton == mQuebecButton){
            seatPosition = getResources().getString(R.string.tag_seat_reverse);
        }

        return seatPosition;
    }

    private void showPrimaryUserSeatUnavailabilityDialog(){
        String userName = SharedPreferencesHelper.getUserName(CabinSeatActivity.this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hi Mr."+userName+", you have consumed your seats.Please recharge");
        alertDialogBuilder.setPositiveButton("Ok",
                (arg0, arg1) -> startActivity(new Intent(CabinSeatActivity.this , PurchaseActivity.class)));

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorButtonNew));
        });
        alertDialog.show();
    }
}
