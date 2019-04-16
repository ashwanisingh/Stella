package com.ns.stellarjet.personalize;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.ns.networking.model.SavedAddressResponse;
import com.ns.networking.model.SavedAddresse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivitySavedAddressBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.personalize.adapter.SavedAddressListAdapter;
import com.ns.stellarjet.utils.*;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

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
            int citiesSize = 0;
            if(HomeActivity.sUserData != null && HomeActivity.sUserData.getCities() != null) {
                citiesSize = HomeActivity.sUserData.getCities().size();
                for (int i = 0; i < citiesSize; i++) {
                    if (HomeActivity.sUserData.getCities().get(i).getName().equalsIgnoreCase(selectedCity)) {
                        selectedCityId = HomeActivity.sUserData.getCities().get(i).getId();
                    }
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

        autocompleteFragment.getView().setBackground(getResources().getDrawable(R.drawable.drawable_location_picker_searc_icon));



        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(13.5f);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setTextColor(getResources().getColor(android.R.color.black));
        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setHintTextColor(getResources().getColor(android.R.color.black));

        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setBackground(getResources().getDrawable(R.drawable.drawable_location_picker_searc_edit));


        // Already it was commented
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
            UiUtils.Companion.showSimpleDialog(
                    SavedAddressListActivity.this,
                    getResources().getString(R.string.error_not_connected_internet)
            );
        }



        ///////////////////////////////////////////////////////////////////////////////////////////////////

        // Initialize Places.
        /*Places.initialize(getApplicationContext(), "AIzaSyC1uZYkROp06LPKTv6kkj7Zt1FUmsZGAq");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);


        // Define a Place ID.
        String placeId = "INSERT_PLACE_ID_HERE";

        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();*/



    }


    private void getSavedAddress(){
        Progress progress = Progress.getInstance();
        progress.showProgress(this);
        Call<SavedAddressResponse> savedAddressCall = RetrofitAPICaller.getInstance(this)
                .getStellarJetAPIs().getSavedAddress(
                        SharedPreferencesHelper.getUserToken(this)
                );

        savedAddressCall.enqueue(new Callback<SavedAddressResponse>() {
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
                    setSavedAddress(addresses);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SavedAddressResponse> call,@NonNull Throwable t) {
                progress.hideProgress();
                UiUtils.Companion.showSimpleDialog(
                        SavedAddressListActivity.this ,
                        getResources().getString(R.string.error_server)
                );
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
            if(citySelected.equalsIgnoreCase("delhi")) {
                if(city.equalsIgnoreCase("Delhi") || city.equalsIgnoreCase("New Delhi")){
                    Intent mIntent = new Intent(
                            SavedAddressListActivity.this ,
                            AddAddressScrollActivity.class
                    );
                    int selectedCityId = 0;
                    int citiesSize = HomeActivity.sUserData.getCities().size();
                    for (int i = 0; i < citiesSize; i++) {
                        if(HomeActivity.sUserData.getCities().get(i).getName().equalsIgnoreCase(selectedCity)) {
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
                    UiUtils.Companion.showSimpleDialog(
                            SavedAddressListActivity.this,
                            "Please select a "+cabType+ " location in " + selectedCity
                    );
                }
            }else{
                if(city != null && city.equalsIgnoreCase(selectedCity)){
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
                    UiUtils.Companion.showSimpleDialog(
                            SavedAddressListActivity.this,
                            "Please select a "+cabType+ " location in " + selectedCity
                    );
                }
            }

        }
    }

    /////////////////////////////////////////

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

}
