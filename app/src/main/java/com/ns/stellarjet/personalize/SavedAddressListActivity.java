package com.ns.stellarjet.personalize;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.ns.networking.model.SavedAddressResponse;
import com.ns.networking.model.SavedAddresse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivitySavedAddressBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.personalize.adapter.SavedAddressListAdapter;
import com.ns.stellarjet.utils.Progress;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import com.ns.stellarjet.utils.UIConstants;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SavedAddressListActivity extends AppCompatActivity implements Function1<SavedAddresse, Unit> {

    private ActivitySavedAddressBinding binding;
    private String cabType ;
    private String selectedCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_saved_address
        );

        cabType = Objects.requireNonNull(getIntent().getExtras()).getString(UIConstants.BUNDLE_CAB_TYPE);
        if(cabType.equalsIgnoreCase(UIConstants.BUNDLE_CAB_TYPE_PICK )){
            selectedCity = Objects.requireNonNull(getIntent().getExtras()).getString(UIConstants.BUNDLE_FROM_CITY);
            binding.textViewSavedAddressCurrentLocation.setText(getResources().getString(R.string.pick_current_location));
        }else if(cabType.equalsIgnoreCase(UIConstants.BUNDLE_CAB_TYPE_DROP)){
            selectedCity = Objects.requireNonNull(getIntent().getExtras()).getString(UIConstants.BUNDLE_TO_CITY);
            binding.textViewSavedAddressCurrentLocation.setText(getResources().getString(R.string.drop_current_location));
        }

        binding.textViewSavedAddressCurrentLocation.setOnClickListener(v -> {
            LatLng latLng = null;
            if(selectedCity.equalsIgnoreCase("Bengaluru")){
                latLng = new LatLng(12.9716 , 77.5946);
            }else if(selectedCity.equalsIgnoreCase("Mumbai")){
                latLng = new LatLng( 19.0760, 72.8777);
            }else if(selectedCity.equalsIgnoreCase("Delhi")){
                latLng = new LatLng(28.7041, 77.1025);
            }
            Intent mAddAddressIntent = new Intent(
                    SavedAddressListActivity.this ,
                    AddAddressScrollActivity.class
            );
            int selectedCityId = 0;
            int citiesSize = HomeActivity.sUserData.getCities().size();
            for (int i = 0; i < citiesSize; i++) {
                if(HomeActivity.sUserData.getCities().get(i).getName().equalsIgnoreCase(selectedCity)){
                    selectedCityId = HomeActivity.sUserData.getCities().get(i).getId();
                }
            }
            mAddAddressIntent.putExtra(UIConstants.BUNDLE_SELECTED_CITY_ID , selectedCityId);
            mAddAddressIntent.putExtra(UIConstants.BUNDLE_CAB_TYPE , cabType);
            mAddAddressIntent.putExtra(UIConstants.BUNDLE_CAB_LATLONG , latLng);
            mAddAddressIntent.putExtra(UIConstants.BUNDLE_SELECTED_CITY , selectedCity);

            startActivity(mAddAddressIntent);
            finish();
        });

        binding.buttonSavedAddressBack.setOnClickListener(v -> onBackPressed());

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint("Search your area in " + selectedCity);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(15.0f);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextColor(getResources().getColor(android.R.color.black));
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setBackground(getResources().getDrawable(R.drawable.drawable_location_picker));
        /*((Button)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button))
                .setVisibility(View.GONE);*/

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("SavedAddress", "Place: " + place.getAddress());
                setAddress(place.getLatLng());
             /*   updateLocation(place.getLatLng());
                mNewAddressTextView.setText(place.getAddress());*/
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("SavedAddress", "An error occurred: " + status);
            }
        });

        if(StellarJetUtils.isConnectingToInternet(SavedAddressListActivity.this)){
            getSavedAddress();
        }else{
            Toast.makeText(SavedAddressListActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void getSavedAddress(){
        Progress progress = Progress.getInstance();
        progress.showProgress(this);
        Call<SavedAddressResponse> boardingPassCall = RetrofitAPICaller.getInstance(this)
                .getStellarJetAPIs().getSavedAddress(
                        SharedPreferencesHelper.getUserToken(this)
                );

        boardingPassCall.enqueue(new Callback<SavedAddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<SavedAddressResponse> call, @NonNull Response<SavedAddressResponse> response) {
                progress.hideProgress();
                List<SavedAddresse> addresses = new ArrayList<>();
                if (response.body() != null) {
                    for (int i = 0; i < response.body().getData().getAddresses().size(); i++) {
                        if(response.body().getData().getAddresses().get(i).getCity_info().getName().equalsIgnoreCase(selectedCity)){
                            addresses.add(response.body().getData().getAddresses().get(i));
                        }
                    }
//                    addresses = response.body().getData().getAddresses();
                    setSavedAddress(addresses);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SavedAddressResponse> call,@NonNull Throwable t) {
                progress.hideProgress();
                Toast.makeText(SavedAddressListActivity.this , "Server Error" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSavedAddress(List<SavedAddresse> addressList){
        SavedAddressListAdapter savedAddressAdapter  = new SavedAddressListAdapter(addressList ,
                SavedAddressListActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                SavedAddressListActivity.this ,
                RecyclerView.VERTICAL ,
                false
        );
        binding.recyclerViewSavedAddress.setLayoutManager(layoutManager);
        binding.recyclerViewSavedAddress.setAdapter(savedAddressAdapter);
    }

    @Override
    public Unit invoke(SavedAddresse savedAddresse) {
        if(cabType.equalsIgnoreCase(UIConstants.BUNDLE_CAB_TYPE_PICK )){
            SharedPreferencesHelper.saveCabPickupPersoalize(this , savedAddresse.getAddress_tag());
            SharedPreferencesHelper.saveCabPickupPersoalizeID(this , String.valueOf(savedAddresse.getId()));
        }else if(cabType.equalsIgnoreCase(UIConstants.BUNDLE_CAB_TYPE_DROP)){
            SharedPreferencesHelper.saveCabDropPersonalize(this , savedAddresse.getAddress_tag());
            SharedPreferencesHelper.saveCabDropPersonalizeID(this , String.valueOf(savedAddresse.getId()));
        }
        finish();
        return Unit.INSTANCE;
    }


    private void setAddress(LatLng latLng){
        Geocoder geocoder = new Geocoder(SavedAddressListActivity.this, Locale.getDefault());
        List<Address> addresses = null; //1 num of possible location returned
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city;
        if (addresses != null&& addresses.size()>0) {
            city = addresses.get(0).getLocality();

            /*if(city.equalsIgnoreCase("Bengaluru") ||
                    city.equalsIgnoreCase("Delhi") || city.equalsIgnoreCase("New Delhi")
                    || city.equalsIgnoreCase("Mumbai")){*/
            String citySelected = "";
            if(selectedCity.equalsIgnoreCase("delhi") || selectedCity.equalsIgnoreCase("new delhi")){
                citySelected = "delhi";
            }else {
                citySelected = selectedCity;
            }
            if(citySelected.equalsIgnoreCase("delhi")){
                if(city.equalsIgnoreCase("Delhi") || city.equalsIgnoreCase("New Delhi")){
                    Intent mIntent = new Intent(
                            SavedAddressListActivity.this ,
                            AddAddressScrollActivity.class
                    );
                    int selectedCityId = 0;
                    int citiesSize = HomeActivity.sUserData.getCities().size();
                    for (int i = 0; i < citiesSize; i++) {
                        if(HomeActivity.sUserData.getCities().get(i).getName().equalsIgnoreCase(selectedCity)){
                            selectedCityId = HomeActivity.sUserData.getCities().get(i).getId();
                        }
                    }
                    mIntent.putExtra(UIConstants.BUNDLE_SELECTED_CITY_ID , selectedCityId);
                    mIntent.putExtra(UIConstants.BUNDLE_CAB_TYPE , cabType);
                    mIntent.putExtra(UIConstants.BUNDLE_CAB_LATLONG , latLng);
                    mIntent.putExtra(UIConstants.BUNDLE_SELECTED_CITY , selectedCity);
                    startActivity(mIntent);
                    finish();
                }else{
                    Toast.makeText(SavedAddressListActivity.this, "Please select a "+cabType+ " location", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(city.equalsIgnoreCase(selectedCity)){
                    Intent mIntent = new Intent(
                            SavedAddressListActivity.this ,
                            AddAddressScrollActivity.class
                    );
                    int selectedCityId = 0;
                    int citiesSize = HomeActivity.sUserData.getCities().size();
                    for (int i = 0; i < citiesSize; i++) {
                        if(HomeActivity.sUserData.getCities().get(i).getName().equalsIgnoreCase(selectedCity)){
                            selectedCityId = HomeActivity.sUserData.getCities().get(i).getId();
                        }
                    }
                    mIntent.putExtra(UIConstants.BUNDLE_SELECTED_CITY_ID , selectedCityId);
                    mIntent.putExtra(UIConstants.BUNDLE_CAB_TYPE , cabType);
                    mIntent.putExtra(UIConstants.BUNDLE_CAB_LATLONG , latLng);
                    mIntent.putExtra(UIConstants.BUNDLE_SELECTED_CITY , selectedCity);
                    startActivity(mIntent);
                    finish();
                }else{
                    Toast.makeText(SavedAddressListActivity.this, "Please select a "+cabType+ " location", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}
