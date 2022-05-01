package com.avit.apnamzpsathi.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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

    public LocationUpdatesService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        getLocationUpdates();
        return START_STICKY;
    }
    
    private void sendLocationUpdates(){
        Log.i(TAG, "sendLocationUpdates: ");
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        DeliverySathi deliverySathi = new DeliverySathi("7505725957","25.13649844681555","82.56680760096513");
        Call<ResponseBody> call = networkAPI.sendLocationUpdates(deliverySathi);
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
        locationRequest.setFastestInterval(locationUpdatePeriod);
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
                sendLocationUpdates();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}