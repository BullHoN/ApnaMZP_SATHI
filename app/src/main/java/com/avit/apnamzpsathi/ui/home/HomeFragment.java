package com.avit.apnamzpsathi.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentHomeBinding;
import com.avit.apnamzpsathi.model.DeliveryInfoData;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.services.LocationBroadCastReceiver;
import com.avit.apnamzpsathi.services.LocationUpdatesService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String TAG = "HomeFragment";
    private int locationUpdatePeriod = 1000 * 60 * 2;
    private DeliverySathi deliverySathi;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getDataFromServer(getContext());
        viewModel.getIncentiveDataFromServer(getContext());

        binding.orderPayItems.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        DeliverySathiInfoAdapter orderPayAdapter = new DeliverySathiInfoAdapter(getContext(),new ArrayList<>());
        binding.orderPayItems.setAdapter(orderPayAdapter);

        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<DeliveryInfoData>>() {
            @Override
            public void onChanged(List<DeliveryInfoData> deliveryInfoData) {
                orderPayAdapter.replaceItems(deliveryInfoData);
            }
        });

        binding.incentiveItems.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        DeliverySathiInfoAdapter incentiveAdapter = new DeliverySathiInfoAdapter(getContext(),new ArrayList<>());
        binding.incentiveItems.setAdapter(incentiveAdapter);

        viewModel.getMutableIncentiveData().observe(getViewLifecycleOwner(), new Observer<List<DeliveryInfoData>>() {
            @Override
            public void onChanged(List<DeliveryInfoData> list) {
                incentiveAdapter.replaceItems(list);
            }
        });

        binding.deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_ordersFragment);
            }
        });

        deliverySathi = new DeliverySathi("7505725957","25.13649844681555","82.56680760096513");
        // TODO: Change Status

        // TODO: lOCATION UPDATES
//        getTheLocationPermission();

        return binding.getRoot();
    }

    private void sendLocationUpdates(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

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

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), LocationBroadCastReceiver.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LocationBroadCastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressLint("MissingPermission")
    private void getLocationUpdates(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(locationUpdatePeriod);
        locationRequest.setFastestInterval(locationUpdatePeriod);
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
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayLocationSettingsRequest(getContext());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toasty.error(getContext(),"Permission Denied",Toasty.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
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
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
//                        getAndSetFusedLocation();
                        getLocationUpdates();
//                        getActivity().startService(new Intent(getContext().getApplicationContext(), LocationUpdatesService.class));
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
//                            getAndSetFusedLocation();
                            getLocationUpdates();
//                            getActivity().startService(new Intent(getContext().getApplicationContext(), LocationUpdatesService.class));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LocationUpdatesService", "onDestroy: ");
        fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
    }
}