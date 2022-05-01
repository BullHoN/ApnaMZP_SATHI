package com.avit.apnamzpsathi.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationBroadCastReceiver extends BroadcastReceiver {

    private String TAG = "LocationUpdatesService";
    public static final String ACTION_PROCESS_UPDATES =
            "com.avit.apnamzpsathi.locationupdates.action" + ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            if(action.equals(ACTION_PROCESS_UPDATES)){
                LocationResult locationResult = LocationResult.extractResult(intent);
                if(locationResult == null){
                    return;
                }

                Location location =  locationResult.getLocations().get(0);
                Log.i(TAG, "onLocationResult: " + location.toString());

                sendLocationUpdates();
            }
        }
    }

    private void sendLocationUpdates(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Log.i(TAG, "sendLocationUpdates: ");
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

}
