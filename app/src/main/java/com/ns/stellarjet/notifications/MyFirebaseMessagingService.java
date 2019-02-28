package com.ns.stellarjet.notifications;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ns.networking.model.UpdateDeviceToken;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.SplashScreenActivity;
import com.ns.stellarjet.drawer.BookingsDetailsActivity;
import com.ns.stellarjet.drawer.PurchaseActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
//        super.onNewToken(s);
        SharedPreferencesHelper.saveDeviceToken(getApplicationContext() , s);
//        sendDeviceTokenToServer(s);
    }

    private void sendDeviceTokenToServer(String deviceToken){

        Call<UpdateDeviceToken> mUpdateDeviceTokenCall = RetrofitAPICaller.getInstance(getApplicationContext())
                .getStellarJetAPIs().updateDeviceToken(
                        SharedPreferencesHelper.getUserToken(getApplicationContext()) ,
                        SharedPreferencesHelper.getUserType(getApplicationContext()),
                        deviceToken,
                        "android"
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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Log.d("MessageToken", "onMessageReceived: " + remoteMessage.getData().get("actionData"));
        if(remoteMessage.getData()!=null){
            getImage(remoteMessage);
        }

    }

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("body");
        Config.actionCode = Integer.parseInt(Objects.requireNonNull(data.get("actionCode")));
        String actionData = data.get("actionData");
        JSONObject mActionDataJsonObject;
        try {
            mActionDataJsonObject = new JSONObject(actionData);
            Config.bookingId = mActionDataJsonObject.getString("bookingId");
            Config.phoneNumber = mActionDataJsonObject.getString("phoneNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendNotification();
    }

    private void sendNotification(){
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent  pendingIntent = getDestinedLandingPendingIntent();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_stellar_launcher)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(Config.content)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        if(pendingIntent!=null){
            notificationBuilder.setContentIntent(pendingIntent);
        }
        Intent callIntent;
        switch (Config.actionCode){
            case 3:
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: "+Config.phoneNumber));
                pendingIntent = PendingIntent.getActivity(this, 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.addAction(R.mipmap.ic_stellar_launcher , "Call Customer care" , pendingIntent);
                break;
            case 5:
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: "+Config.phoneNumber));
                pendingIntent = PendingIntent.getActivity(this, 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.addAction(R.mipmap.ic_stellar_launcher , "Call Driver" , pendingIntent);
                break;
        }
        notificationManager.notify(1, notificationBuilder.build());
    }

    private PendingIntent getDestinedLandingPendingIntent(){
        PendingIntent pendingIntent = null;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        Intent intent;

        switch (Config.actionCode){
            case 1:
                if(isAppIsInBackground(getApplicationContext())){
                    intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                    stackBuilder.addNextIntentWithParentStack(intent);
                    pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                }else{
                    pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
                }
                break;
            case 2:
                pendingIntent = getBookingDetailsPendingIntent();
                break;
            case 3:
//                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
                break;
            case 4:
                pendingIntent = getBookingDetailsPendingIntent();
                break;
            case 5:
//                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
                break;
            case 6:
                pendingIntent = getBookingDetailsPendingIntent();
                break;
            case 7:
                pendingIntent = getPurchasePendingIntent();
                break;
            case 8:
                pendingIntent = getPurchasePendingIntent();
                break;
        }
        return pendingIntent;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }

        return isInBackground;
    }

    private PendingIntent getBookingDetailsPendingIntent(){
        Intent intent;
        PendingIntent pendingIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        if(isAppIsInBackground(getApplicationContext())){
            intent = new Intent(getApplicationContext(), BookingsDetailsActivity.class);
            intent.putExtra("from" , "notifications");
            intent.putExtra("bookingId" , Config.bookingId);
            intent.putExtra("bookingType" , "upcoming");
            stackBuilder.addNextIntentWithParentStack(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            intent = new Intent(getApplicationContext(), BookingsDetailsActivity.class);
            intent.putExtra("from" , "notifications");
            intent.putExtra("bookingId" , Config.bookingId);
            intent.putExtra("bookingType" , "upcoming");
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        }
        return pendingIntent;
    }

    private PendingIntent getPurchasePendingIntent(){
        Intent intent;
        PendingIntent pendingIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        if(isAppIsInBackground(getApplicationContext())){
            intent = new Intent(getApplicationContext(), PurchaseActivity.class);
            stackBuilder.addNextIntentWithParentStack(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            intent = new Intent(getApplicationContext() , PurchaseActivity.class);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        }
        return pendingIntent;
    }
}
