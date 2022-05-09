package com.avit.apnamzpsathi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.avit.apnamzpsathi.services.LocationBroadCastReceiver;
import com.avit.apnamzpsathi.services.LocationUpdatesService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private String TAG = "LocationUpdatesService";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int locationUpdatePeriod = 1000 * 60 * 2;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Intent backgroundLocationUpdatesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.i("NotificationService", "FCM failed");
                        }

                        String token = task.getResult();
                        Log.i("NotificationService", "onComplete: " + token);

                    }
                });

//        getTheLocationPermission();

    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), LocationBroadCastReceiver.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LocationBroadCastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressLint("MissingPermission")
    private void getLocationUpdates(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(locationUpdatePeriod);
        locationRequest.setFastestInterval(locationUpdatePeriod/2);
        locationRequest.setMaxWaitTime(locationUpdatePeriod * 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());

//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                if(locationResult == null){
//                    return;
//                }
//
//                Location location =  locationResult.getLocations().get(0);
//
//                // SAVE THE Location
//                Log.i(TAG, "onLocationResult: " + location.toString());
//                sendLocationUpdates();
//            }
//        }, Looper.getMainLooper());

    }

    private void getTheLocationPermission(){
        Log.i(TAG, "getTheLocationPermission: ");
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()){
                            displayLocationSettingsRequest(getApplicationContext());
                            Log.i(TAG, "onPermissionsChecked: ");
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
//                        getAndSetFusedLocation();
//                        getLocationUpdates();
//                        getActivity().startService(new Intent(getContext().getApplicationContext(), LocationUpdatesService.class));
//                        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
                        startService();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
//                            getAndSetFusedLocation();
//                            getLocationUpdates();
//                            startForegroundService(new Intent(getApplicationContext(),LocationUpdatesService.class)
//                            startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
                            startService();
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void startService(){
        backgroundLocationUpdatesService = new Intent(getApplicationContext(),LocationUpdatesService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(getApplicationContext(),backgroundLocationUpdatesService);
        } else {
            startService(backgroundLocationUpdatesService);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
//        stopService(backgroundLocationUpdatesService);
    }
}