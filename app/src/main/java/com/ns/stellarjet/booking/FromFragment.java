package com.ns.stellarjet.booking;


import android.os.Bundle;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FromFragment extends Fragment implements PlaceSelectAdapter.onPlaceSelectClickListener {

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

        return mRootView;
    }

    @Override
    public void onPlaceSelected(String placeName, int placeId) {
        HomeActivity.fromCityId = placeId;
        HomeActivity.fromCity = placeName;
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_container ,  new ToFragment())
                .addToBackStack(null)
                .commit();
    }
}
