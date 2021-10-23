package com.avit.apnamzpsathi.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentHomeBinding;
import com.avit.apnamzpsathi.model.DeliveryInfoData;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

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

        // TODO: Change Status


        return binding.getRoot();
    }
}