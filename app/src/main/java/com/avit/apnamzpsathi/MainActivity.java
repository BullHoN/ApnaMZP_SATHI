package com.avit.apnamzpsathi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.avit.apnamzpsathi.db.LocalDB;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.model.OrderItem;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.services.LocationBroadCastReceiver;
import com.avit.apnamzpsathi.services.LocationUpdatesService;
import com.avit.apnamzpsathi.utils.ErrorUtils;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private String TAG = "LocationUpdatesService";
    private NavController navController;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private String broadCastAction = "com.avit.apnamzp_sathi.NEW_ORDER_SATHI_NOTIFICATION";
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment_container);
        gson = new Gson();

        FirebaseCrashlytics.getInstance().setUserId(LocalDB.getDeliverySathiDetails(getApplicationContext()).getPhoneNo());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.i("NotificationService", "FCM failed");
                        }

                        String token = task.getResult();

                        DeliverySathi deliverySathi = LocalDB.getDeliverySathiDetails(getApplicationContext());
                        deliverySathi.setFcmId(token);
                        LocalDB.saveSathiDetails(getApplicationContext(),deliverySathi);
                        sendFcmIdToServer(deliverySathi);
                    }
                });

        if(getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals("com.avit.apnamzp_sathi.NEW_ORDER_NOTIFICATION")){
            openAcceptOrderFragment(null);
//            SharedPreferences sf = getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,MODE_PRIVATE);
//            SharedPreferences.Editor editor = sf.edit();
//
//            editor.putBoolean("new_order_arrived",false);
//            editor.apply();
        }
        // Broadcast receiver

//        intentFilter = new IntentFilter();
//        intentFilter.addAction(broadCastAction);
//
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent.getAction().equals(broadCastAction)){
//                    Log.i(TAG, "onReceive: ");
//                    openAcceptOrderFragment();
//                }
//            }
//        };

    }

    private void openAcceptOrderFragment(Bundle bundle){
        navController.navigate(R.id.acceptOrderFragment,bundle);
    }

    private void sendFcmIdToServer(DeliverySathi deliverySathi){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);


        Call<NetworkResponse> call = networkAPI.updateFcmToken(deliverySathi,"sathi");
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse> call, Response<NetworkResponse> response) {
                if(!response.isSuccessful()){
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(getApplicationContext(),errorResponse.getDesc(),Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(),"Some Error Occurred",Toasty.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences sf = getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sf.edit();
//
//        Log.i(TAG, "onResume: " + sf.getBoolean("new_order_arrived",false));
//
//        if(!sf.contains("new_order_arrived")){
//            return;
//        }
//
//        boolean newOrderArrived = sf.getBoolean("new_order_arrived",false);
//        if(!newOrderArrived){
//            return;
//        }
//        editor.putBoolean("new_order_arrived",!newOrderArrived);
//        editor.apply();
//
//        if(newOrderArrived){
//            openAcceptOrderFragment(null);
//        }

//        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }
}