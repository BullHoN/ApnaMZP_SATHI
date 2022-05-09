package com.avit.apnamzpsathi.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentOrdersBinding;
import com.avit.apnamzpsathi.model.OrderItem;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private OrderFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOrdersBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(OrderFragmentViewModel.class);

        viewModel.getOrdersFromServer(getContext(),"1234567890");

        binding.ordersList.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        OrdersAdapter ordersAdapter = new OrdersAdapter(new ArrayList<>(),getContext());
        binding.ordersList.setAdapter(ordersAdapter);

        viewModel.getOrderItemMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<OrderItem>>() {
            @Override
            public void onChanged(List<OrderItem> orderItems) {
                ordersAdapter.updateItems(orderItems);
            }
        });

        return binding.getRoot();
    }
}