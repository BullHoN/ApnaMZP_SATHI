package com.avit.apnamzpsathi.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.avit.apnamzpsathi.db.LocalDB;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.model.OrderItem;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.receivers.RestartBackgroundService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationUpdatesService extends Service {

    private String TAG = "LocationUpdatesService";
    private int locationUpdatePeriod = 1000 * 60 * 2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private DeliverySathi deliverySathi;
    private Gson gson;
    private boolean isRemoved;

    public LocationUpdatesService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");

        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "location_updates",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Delivery Boy Status Online")
                .setContentText("").build();

        startForeground(1, notification);

        isRemoved = false;
        getLocationUpdates();
        return START_STICKY;
    }
    
    private void sendLocationUpdates(String latitude,String longitude){
        Log.i(TAG, "sendLocationUpdates: ");
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        SharedPreferences sf = getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,MODE_PRIVATE);
        gson = new Gson();

        deliverySathi = LocalDB.getDeliverySathiDetails(getApplicationContext());
        deliverySathi.setLatitude(latitude);
        deliverySathi.setLongitude(longitude);

        Log.i(TAG, "sendLocationUpdates: " + sf.contains("new_order_data"));
//        if(sf.contains("new_order_data")){
//            OrderItem orderItem = gson.fromJson(sf.getString("new_order_data","{}"),OrderItem.class);
//            deliverySathi.setOrderId(orderItem.get_id());
//        }


        Call<ResponseBody> call = networkAPI.sendLocationUpdates(deliverySathi,null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocationUpdates(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(locationUpdatePeriod);
        locationRequest.setFastestInterval(locationUpdatePeriod/2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult == null){
                    return;
                }

                Location location =  locationResult.getLocations().get(0);

                // SAVE THE Location
                Log.i(TAG, "onLocationResult: " + location.toString());
                if(isRemoved) return;
                sendLocationUpdates(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private void removeFromLive(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<ResponseBody> call = networkAPI.sendLocationUpdates(deliverySathi,"del");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        isRemoved = true;
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }
}