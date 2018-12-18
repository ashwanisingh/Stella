package com.ns.stellarjet.personalize;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.ns.networking.model.AddAddressResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.utils.PermissionUtils;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Locale;

public class AddAddressScrollActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    private GoogleMap mMap;
    private EditText mAddressEditText;
    private EditText mFlatNoEditText;
    private EditText mNickNameEditText;
    private TextView mLocationTypeTextView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Location myLocation;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_scroll);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        mAddressEditText = findViewById(R.id.editText_add_address);
        mFlatNoEditText = findViewById(R.id.editText_add_address_flat_no);
        mNickNameEditText = findViewById(R.id.editText_add_address_nickname);
        mAddressEditText.setKeyListener(null);
        Button mAddAddressButton = findViewById(R.id.button_add_address_confirm);
        mLocationTypeTextView = findViewById(R.id.textView_set_location);

        mAddAddressButton.setOnClickListener(v -> {
            String cityId = "3";
            String address = mFlatNoEditText.getText().toString() +" "+ mAddressEditText.getText().toString();
            String nickName = mNickNameEditText.getText().toString();
            if(!cityId.isEmpty() && !address.isEmpty()&& !nickName.isEmpty()){
                addAddress(cityId , address , nickName);
            }else {
                Toast.makeText(AddAddressScrollActivity.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
            }
        });


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

    }


    private void addAddress(String cityId , String address , String nickName){
        Call<AddAddressResponse> mAddressResponseCall = RetrofitAPICaller.getInstance(AddAddressScrollActivity.this)
                .getStellarJetAPIs().addNewAddress(
                        SharedPreferencesHelper.getUserToken(AddAddressScrollActivity.this) ,
                        SharedPreferencesHelper.getUserId(AddAddressScrollActivity.this),
                        cityId ,
                        address ,
                        nickName
                );

        mAddressResponseCall.enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddAddressResponse> call,@NonNull Response<AddAddressResponse> response) {
                Log.d("Address", "onResponse: " + response.body());
                if (response.body() != null && response.body().getResultcode() == 1) {
                    Toast.makeText(
                            AddAddressScrollActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddAddressResponse> call,@NonNull Throwable t) {
                Log.d("Address", "onResponse: " + t);
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void getAddress(LatLng location){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            mAddressEditText.setText(address);
        }catch(Exception e){
            Log.d("Maps", "onLocationChanged: " + e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(
                AddAddressScrollActivity.this,
                "Please Enable GPS and Internet",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (lm != null) {
            myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider;
            if (lm != null) {
                provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }
        }
        if(myLocation!=null){
            LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17), 1500, null);
        }
        googleMap.setOnCameraIdleListener(() -> {
            Log.d("Maps", "onCameraIdle: " + mMap.getCameraPosition().target);
            getAddress(mMap.getCameraPosition().target);
        });
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

}
