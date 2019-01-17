package com.ns.stellarjet.notifications;

import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ns.networking.model.UpdateDeviceToken;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
//        super.onNewToken(s);
        SharedPreferencesHelper.saveDeviceToken(getApplicationContext() , s);
        sendDeviceTokenToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Toast.makeText(getApplicationContext(), "Message received ", Toast.LENGTH_SHORT).show();
        Log.d("MyFirebase", "onMessageReceived: " + remoteMessage.getFrom());
    }

    private void sendDeviceTokenToServer(String deviceToken){

        Call<UpdateDeviceToken> mUpdateDeviceTokenCall = RetrofitAPICaller.getInstance(getApplicationContext())
                .getStellarJetAPIs().updateDeviceToken(
                        SharedPreferencesHelper.getUserToken(getApplicationContext()) ,
                        SharedPreferencesHelper.getUserId(getApplicationContext()) ,
                        SharedPreferencesHelper.getUserType(getApplicationContext()),
                        deviceToken
                );

        mUpdateDeviceTokenCall.enqueue(new Callback<UpdateDeviceToken>() {
            @Override
            public void onResponse(@NonNull Call<UpdateDeviceToken> call, @NonNull Response<UpdateDeviceToken> response) {
                Log.d("DeviceToken", "onResponse: " + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UpdateDeviceToken> call, @NonNull Throwable t) {
                Log.d("DeviceToken", "onResponse: " + t);
            }
        });
    }

}
