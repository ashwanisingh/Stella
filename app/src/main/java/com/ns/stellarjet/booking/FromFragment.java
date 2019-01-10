package com.ns.stellarjet.booking;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.City;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PlaceSelectAdapter;
import com.ns.stellarjet.databinding.FragmentFromBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FromFragment extends Fragment implements PlaceSelectAdapter.onPlaceSelectClickListener {

    private List<ObjectAnimator> mEnterObjectAnimatorList = new ArrayList<>();
    private List<ObjectAnimator> mReverseEnterObjectAnimatorList = new ArrayList<>();
    private List<ObjectAnimator> mExitObjectAnimatorList = new ArrayList<>();

    public FromFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentFromBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_from, container, false);
        View mRootView = dataBinding.getRoot();

        List<City> mCitiesList = HomeActivity.sUserData.getCities();

        PlaceSelectAdapter mPlaceSelectAdapter = new PlaceSelectAdapter(
                this ,
                mCitiesList
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity() ,
                RecyclerView.VERTICAL,
                false
        );
        dataBinding.recyclerViewFrom.setAdapter(mPlaceSelectAdapter);
        dataBinding.recyclerViewFrom.setLayoutManager(layoutManager);


        int childCount = dataBinding.layoutFromBase.getChildCount();
        int minimumEnterAnimationTime = childCount * 150;
        int maximumExitAnimationTime = childCount * 150;
        for (int i = 0; i < childCount; i++) {
            Log.d("DrawerActivity", "onCreate: child objects " + dataBinding.layoutFromBase.getChildAt(i));
            if (i != 0){
                minimumEnterAnimationTime = minimumEnterAnimationTime + 150 ;
                maximumExitAnimationTime = maximumExitAnimationTime - 150;
            }
            mEnterObjectAnimatorList.add(createEnterObjectAnimators(
                    StellarJetUtils.getScreenWidth(getActivity()) ,
                    dataBinding.layoutFromBase.getChildAt(i) , minimumEnterAnimationTime) );

            mReverseEnterObjectAnimatorList.add(createReverseEnterObjectAnimators(
                    dataBinding.layoutFromBase.getChildAt(i) , minimumEnterAnimationTime) );

            mExitObjectAnimatorList.add(createExitObjectAnimators(
                    dataBinding.layoutFromBase.getChildAt(i) , maximumExitAnimationTime) );
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
        PlaceSelectionActivity.isFromFragmentVisible = true;
        PlaceSelectionActivity.isToFragmentVisible= false;
    }

    @Override
    public void onPlaceSelected(String placeName, int placeId) {
        SharedPreferencesHelper.saveFromCityId(getActivity() , placeId);
        SharedPreferencesHelper.saveFromCity(getActivity() , placeName);
        startExitAnimation();
        new Handler().postDelayed(() -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_container ,  new ToFragment())
                .addToBackStack(null)
                .commit(), 450);
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
