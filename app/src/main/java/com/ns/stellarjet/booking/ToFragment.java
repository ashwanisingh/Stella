package com.ns.stellarjet.booking;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ns.networking.model.City;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.PlaceSelectAdapter;
import com.ns.stellarjet.databinding.FragmentToBinding;
import com.ns.stellarjet.home.HomeActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToFragment extends Fragment implements PlaceSelectAdapter.onPlaceSelectClickListener {

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
            if(mCitiesList.get(i).getId() != HomeActivity.fromCity){
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

        return mRootView;
    }

    @Override
    public void onPlaceSelected(String placeName, int placeId) {
        Toast.makeText(getActivity(), placeId + "=="+ placeName, Toast.LENGTH_SHORT).show();
        HomeActivity.toCity = placeId;
    }
}
