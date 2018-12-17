package com.ns.stellarjet.booking;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.City;
import com.ns.networking.model.FlightScheduleData;
import com.ns.networking.model.FlightScheduleResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PlaceSelectAdapter;
import com.ns.stellarjet.databinding.FragmentToBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToFragment extends Fragment implements PlaceSelectAdapter.onPlaceSelectClickListener {

    private List<ObjectAnimator> mEnterObjectAnimatorList = new ArrayList<>();
    private List<ObjectAnimator> mReverseEnterObjectAnimatorList = new ArrayList<>();
    private List<ObjectAnimator> mExitObjectAnimatorList = new ArrayList<>();

    public ToFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentToBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_to, container, false);
        View mRootView = dataBinding.getRoot();

        List<City> mCitiesList = HomeActivity.sUserData.getCities();

        List<City> mDisplayCitiesList = new ArrayList<>();

        for (int i = 0; i < mCitiesList.size(); i++) {
            if(mCitiesList.get(i).getId() != HomeActivity.fromCityId){
                mDisplayCitiesList.add(mCitiesList.get(i));
            }
        }

        PlaceSelectAdapter mPlaceSelectAdapter = new PlaceSelectAdapter(
                this ,
                mDisplayCitiesList
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity() ,
                RecyclerView.VERTICAL,
                false
        );

        dataBinding.recyclerViewTo.setAdapter(mPlaceSelectAdapter);
        dataBinding.recyclerViewTo.setLayoutManager(layoutManager);

        int childCount = dataBinding.layoutToBase.getChildCount();
        int minimumEnterAnimationTime = childCount * 150;
        int maximumExitAnimationTime = childCount * 150;
        for (int i = 0; i < childCount; i++) {
            Log.d("DrawerActivity", "onCreate: child objects " + dataBinding.layoutToBase.getChildAt(i));
            if (i != 0){
                minimumEnterAnimationTime = minimumEnterAnimationTime + 150 ;
                maximumExitAnimationTime = maximumExitAnimationTime - 150;
            }
            mEnterObjectAnimatorList.add(createEnterObjectAnimators(
                    StellarJetUtils.getScreenWidth(getActivity()) ,
                    dataBinding.layoutToBase.getChildAt(i) , minimumEnterAnimationTime) );

            mReverseEnterObjectAnimatorList.add(createReverseEnterObjectAnimators(
                    dataBinding.layoutToBase.getChildAt(i) , minimumEnterAnimationTime) );

            mExitObjectAnimatorList.add(createExitObjectAnimators(
                    dataBinding.layoutToBase.getChildAt(i) , maximumExitAnimationTime) );
        }

        if(!PlaceSelectionActivity.isToFragmentVisible){
            startEntryAnimation();
        }else {
            startReverseEntryAnimation();
        }

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        PlaceSelectionActivity.isFromFragmentVisible = false;
        PlaceSelectionActivity.isToFragmentVisible= true;
    }

    @Override
    public void onPlaceSelected(String placeName, int placeId) {
//        Toast.makeText(getActivity(), placeId + "=="+ placeName, Toast.LENGTH_SHORT).show();
        HomeActivity.toCityId = placeId;
        HomeActivity.toCity = placeName;
        getFlightSchedules();
    }


    private void getFlightSchedules(){
        Call<FlightScheduleResponse> mFlightScheduleResponseCall = RetrofitAPICaller.getInstance(getActivity())
                .getStellarJetAPIs().getFlightSchedules(
                        SharedPreferencesHelper.getUserToken(getActivity()) ,
                        String.valueOf(HomeActivity.fromCityId) ,
                        String.valueOf(HomeActivity.toCityId) ,
                        "90"
                );

        mFlightScheduleResponseCall.enqueue(new Callback<FlightScheduleResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightScheduleResponse> call, @NonNull Response<FlightScheduleResponse> response) {
                Log.d("Calendar", "onResponse: " + response.body());
                if (response.body() != null) {
                    List<FlightScheduleData> mFlightScheduleDataList = response.body().getData();
                    Intent mCalendarIntent = new Intent(
                            getActivity() ,
                            CalendarActivity.class
                    );
                    mCalendarIntent.putExtra("dates" , (ArrayList<? extends Parcelable>)  mFlightScheduleDataList);
                    Objects.requireNonNull(getActivity()).startActivity(mCalendarIntent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightScheduleResponse> call, @NonNull Throwable t) {
                Log.d("Calendar", "onFailure: " + t);
            }
        });
    }

    private ObjectAnimator createEnterObjectAnimators(int width , View mChildView , int timeMillis){
        ObjectAnimator mViewObjectAnimator = ObjectAnimator.ofFloat(
                mChildView,
                "translationX",
                width, 0.0f);
        mViewObjectAnimator.setDuration(timeMillis);

        return mViewObjectAnimator;
    }

    private ObjectAnimator createExitObjectAnimators(View mChildView , int timeMillis){
        int screenwidth = StellarJetUtils.getScreenWidth(getActivity())+100;
        ObjectAnimator mViewObjectAnimator = ObjectAnimator.ofFloat(
                mChildView,
                "translationX",
                0.0f, -screenwidth);
        mViewObjectAnimator.setDuration(timeMillis);

        return mViewObjectAnimator;
    }

    private ObjectAnimator createReverseEnterObjectAnimators( View mChildView , int timeMillis){
        int screenwidth = StellarJetUtils.getScreenWidth(getActivity())+100;
        ObjectAnimator mViewObjectAnimator = ObjectAnimator.ofFloat(
                mChildView,
                "translationX",
                -screenwidth , 0.0f);
        mViewObjectAnimator.setDuration(timeMillis);

        return mViewObjectAnimator;
    }

    private void startEntryAnimation(){
        for (int i = 0; i < mEnterObjectAnimatorList.size(); i++) {
            mEnterObjectAnimatorList.get(i).start();
        }
    }

    private void startReverseEntryAnimation(){
        for (int i = 0; i < mReverseEnterObjectAnimatorList.size(); i++) {
            mReverseEnterObjectAnimatorList.get(i).start();
        }
    }

    private void startExitAnimation(){
        for (int i = 0; i < mExitObjectAnimatorList.size(); i++) {
            mExitObjectAnimatorList.get(i).start();
        }
    }

}
