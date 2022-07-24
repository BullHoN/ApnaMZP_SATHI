package com.avit.apnamzpsathi.ui.acceptorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentAcceptOrderBinding;
import com.avit.apnamzpsathi.db.LocalDB;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.model.OrderItem;
import com.avit.apnamzpsathi.model.UserInfo;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.utils.ErrorUtils;
import com.avit.apnamzpsathi.utils.NotificationUtils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AcceptOrderFragment extends Fragment {

    private FragmentAcceptOrderBinding binding;
    private OrderItem orderItem;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private long waitTime = 1000 * 60 * 5;
    private CircularProgressIndicator waitTimeProgressBar;
    private LinearLayout acceptOrderButton, rejectOrderButton;
    private TextView reamingTimeTextview;
    private int minutes = 4;
    private int seconds = 60;
    private String TAG = "OrderNotifications";
    private CountDownTimer cancelOrderTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAcceptOrderBinding.inflate(inflater,container,false);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_DB_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        orderItem = gson.fromJson(sharedPreferences.getString("new_order_data","{}"),OrderItem.class);

        binding.shopName.setText("Shop Name: " + orderItem.getShopInfo().getName());
        binding.shopPhoneNo.setText("Phone No: " + orderItem.getShopInfo().getPhoneNo());
        binding.shopAddress.setText("Address: " + orderItem.getShopInfo().getRawAddress());

        waitTimeProgressBar = binding.remainingTimeProgressBar;
        reamingTimeTextview = binding.remainingTime;
        acceptOrderButton = binding.acceptOrderButton;
        rejectOrderButton = binding.rejectOrderButton;

        waitTimeProgressBar.setMax(60 * 5);

        cancelOrderTimer = new CountDownTimer(waitTime,waitTime/(5*60)) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long l) {
                seconds--;
                if (seconds == 0) {
                    minutes--;
                    seconds = 60;
                }
                reamingTimeTextview.setText("Perform Action Within " + minutes + ":" + seconds + " Minutes ");
                waitTimeProgressBar.setProgress(waitTimeProgressBar.getProgress() + 1, true);
            }

            @Override
            public void onFinish() {
                rejectOrder("No Delivery Sathi Available");
                Log.i(TAG, "onFinish: cancel order");
            }
        }.start();

        binding.acceptOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationUtils.stopSound();
                acceptOrder();
            }
        });

        binding.rejectOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationUtils.stopSound();
                rejectOrder("Delivery Sathi Rejected");
            }
        });

        removeNewSathi();

        return binding.getRoot();
    }

    private void removeNewSathi(){
        SharedPreferences sf = getActivity().getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();

        editor.putBoolean("new_order_arrived",false);
        editor.apply();
    }

    private void acceptOrder() {
        cancelOrderTimer.cancel();

        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<NetworkResponse> call = networkAPI.acceptOrder(orderItem.get_id());
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse> call, Response<NetworkResponse> response) {
                if(!response.isSuccessful())       {
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(getContext(),errorResponse.getDesc(),Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                NetworkResponse successResponse = response.body();
                openOrdersFragment();
                if(successResponse.isSuccess()){
                    Toasty.success(getContext(),"Order Accepted",Toasty.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<NetworkResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void openOrdersFragment(){
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }

    private void rejectOrder(String reason) {

        cancelOrderTimer.cancel();

        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<NetworkResponse> call = networkAPI.rejectOrder(orderItem.get_id(),LocalDB.getDeliverySathiDetails(getContext()).getPhoneNo(),reason);
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse> call, Response<NetworkResponse> response) {
                if(!response.isSuccessful()){
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(getContext(),errorResponse.getDesc(),Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                NetworkResponse successResponse = response.body();
                openOrdersFragment();
                if(successResponse.isSuccess()){
                    Toasty.success(getContext(),"Order Accepted",Toasty.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<NetworkResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }


}