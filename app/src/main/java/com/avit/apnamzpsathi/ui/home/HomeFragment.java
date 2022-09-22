package com.avit.apnamzpsathi.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.apnamzpsathi.LocationServiceToogleActivity;
import com.avit.apnamzpsathi.MainActivity;
import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentHomeBinding;
import com.avit.apnamzpsathi.db.LocalDB;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.model.CashInHand;
import com.avit.apnamzpsathi.model.DeliveryInfoData;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.model.DeliverySathiDayInfo;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.model.OrderItem;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.services.LocationBroadCastReceiver;
import com.avit.apnamzpsathi.services.LocationUpdatesService;
import com.avit.apnamzpsathi.utils.ErrorUtils;
import com.avit.apnamzpsathi.utils.PrettyStrings;
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
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {
    // comment
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private String TAG = "HomeFragment";
    private SharedPreferences sharedPreferences;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Intent backgroundLocationUpdatesService;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        gson = new Gson();

        viewModel.getDataFromServer(getContext());
        viewModel.getIncentiveDataFromServer(getContext());

        getScreenOverlayPermission();

        getCashInHand();
        getTodayDayInfo();

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

//        deliverySathi = new DeliverySathi("7505725957","25.13649844681555","82.56680760096513");
        // Change Status

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,Context.MODE_PRIVATE);
        viewModel.initlializeDeliveryBoyStatus(sharedPreferences);

        viewModel.getDeliveryBoyStatusIsOnlineData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    binding.statusText.setText("OFFLINE");
                    binding.statusText.setTextColor(getResources().getColor(R.color.errorColor));
                    // STOP SENDING LOCATION UPDATES
                    stopService();
                }
                else {
                    binding.statusText.setText("ONLINE");
                    binding.statusText.setTextColor(getResources().getColor(R.color.successColor));

                    // START SENDING LOCATION UPDATES
//                    getTheLocationPermission();
                    startService();
                }
            }
        });

        binding.statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.toggleDeliveryBoyStatus();
            }
        });

        binding.earningsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_earningFragment);
            }
        });

        return binding.getRoot();
    }

    private void getScreenOverlayPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!Settings.canDrawOverlays(getContext())){
                if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
                    final Intent intent =new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", getActivity().getPackageName());
                    new AlertDialog.Builder(getContext())
                            .setTitle("Please Enable the additional permissions")
                            .setMessage("You will not receive notifications while the app is in background if you disable these permissions")
                            .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
                else {
                    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(myIntent);
                }
            }
        }
    }

    private void getNotRespondedOrders(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<OrderItem>> call = networkAPI.getNotRespondedOrders(LocalDB.getDeliverySathiDetails(getContext()).getPhoneNo());
        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if(!response.isSuccessful()){
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(getContext(),errorResponse.getDesc(),Toasty.LENGTH_LONG)
                            .show();
                    return;
                }

                for(int i=0;i<response.body().size();i++){
                    OrderItem curr = response.body().get(i);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("new_order_arrived",true);

                    Bundle bundle = new Bundle();
                    bundle.putString("new_order_data",gson.toJson(curr));
                    openAcceptOrderFragment(bundle);

                }

            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                Toasty.error(getContext(),t.getMessage(),Toasty.LENGTH_LONG)
                        .show();
            }
        });

    }

    private void openAcceptOrderFragment(Bundle bundle){
        Navigation.findNavController(binding.getRoot()).navigate(R.id.acceptOrderFragment,bundle);
    }

    private void getCashInHand(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<CashInHand> call = networkAPI.getCashInHand(LocalDB.getDeliverySathiDetails(getContext()).getPhoneNo());
        call.enqueue(new Callback<CashInHand>() {
            @Override
            public void onResponse(Call<CashInHand> call, Response<CashInHand> response) {

                if(!response.isSuccessful()){
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(getContext(),errorResponse.getDesc(),Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                CashInHand cashInHand = response.body();

                binding.cashInHand.setText("₹" + cashInHand.getCashInHand());
                if(cashInHand.getCashInHand() >= 0){
                    binding.cashInHand.setTextColor(getResources().getColor(R.color.successColor));
                }
                else {
                    binding.cashInHand.setTextColor(getResources().getColor(R.color.errorColor));
                }
            }

            @Override
            public void onFailure(Call<CashInHand> call, Throwable t) {
                Toasty.error(getContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

    private void getTodayDayInfo(){
        String deliverySathi = LocalDB.getDeliverySathiDetails(getContext()).getPhoneNo();
        Date todayDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<DeliverySathiDayInfo> call = networkAPI.getDeliverySaathiDayInfo(deliverySathi,simpleDateFormat.format(todayDate),false);
        call.enqueue(new Callback<DeliverySathiDayInfo>() {
            @Override
            public void onResponse(Call<DeliverySathiDayInfo> call, Response<DeliverySathiDayInfo> response) {

                if(!response.isSuccessful()){
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(getContext(),errorResponse.getDesc(),Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                DeliverySathiDayInfo deliverySathiDayInfo = response.body();
                binding.todayEarning.setText("₹" + deliverySathiDayInfo.getEarnings());
                binding.rides.setText(String.valueOf(deliverySathiDayInfo.getNoOfOrders()));
                binding.incentiveAmount.setText(PrettyStrings.getPriceInRupees(deliverySathiDayInfo.getIncentives()));
            }

            @Override
            public void onFailure(Call<DeliverySathiDayInfo> call, Throwable t) {
                Toasty.error(getContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

    private void getTheLocationPermission(){
        Log.i(TAG, "getTheLocationPermission: ");
        Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()){
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
                        startService();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startService(){
        Log.i(TAG, "startService: ");
        backgroundLocationUpdatesService = new Intent(getActivity().getApplicationContext(),LocationUpdatesService.class);
        getActivity().getApplicationContext().startForegroundService(backgroundLocationUpdatesService);
//        getActivity().startService(backgroundLocationUpdatesService);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            ContextCompat.startForegroundService(getContext(),backgroundLocationUpdatesService);
////            getActivity().getApplicationContext().startForegroundService(backgroundLocationUpdatesService);
//        } else {
//            getActivity().startService(backgroundLocationUpdatesService);
//        }
    }

    private void stopService(){
        Log.i(TAG, "stopService: ");
        if(backgroundLocationUpdatesService != null) getActivity().stopService(backgroundLocationUpdatesService);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,Context.MODE_PRIVATE);

        if(!sharedPreferences.contains("new_order_arrived") || !sharedPreferences.getBoolean("new_order_arrived",true)){
//            getNotRespondedOrders();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
//        stopService();
    }
}