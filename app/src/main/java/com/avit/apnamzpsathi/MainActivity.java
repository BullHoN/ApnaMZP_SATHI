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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.avit.apnamzpsathi.db.LocalDB;
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
    private String broadCastAction = "com.avit.apnamzp_partner.NEW_ORDER_SATHI_NOTIFICATION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment_container);

        String action = getIntent().getAction();

        if(action != null && action.equals("com.avit.apnamzp_partner.NEW_ORDER_NOTIFICATION")){
            openOrdersFragment();
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.i("NotificationService", "FCM failed");
                        }

                        String token = task.getResult();
                        Log.i("NotificationService", "onComplete: " + token);

                        DeliverySathi deliverySathi = LocalDB.getDeliverySathiDetails(getApplicationContext());

                        Log.i(TAG, "onComplete: " + deliverySathi.getFcmId() + "\n" + token);

                        if(deliverySathi.getFcmId() == null || !deliverySathi.getFcmId().equals(token)){

                            deliverySathi.setFcmId(token);
                            LocalDB.saveSathiDetails(getApplicationContext(),deliverySathi);

                            sendFcmIdToServer(deliverySathi);
                        }

                    }
                });

        // Broadcast receiver

        intentFilter = new IntentFilter();
        intentFilter.addAction(broadCastAction);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(broadCastAction)){
                    Log.i(TAG, "onReceive: ");
                    openOrdersFragment();
                }
            }
        };

    }

    private void openOrdersFragment(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("new_order_notification",true);
        navController.navigate(R.id.ordersFragment,bundle);
    }

    private void sendFcmIdToServer(DeliverySathi deliverySathi){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);


        Call<ResponseBody> call = networkAPI.updateFcmToken(deliverySathi,"sathi");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(getApplicationContext(),"Some Error Occurred",Toasty.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}