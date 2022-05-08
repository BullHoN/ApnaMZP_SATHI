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
    private String TAG = "HomeFragment";
    private DeliverySathi deliverySathi;

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

        return binding.getRoot();
    }

}