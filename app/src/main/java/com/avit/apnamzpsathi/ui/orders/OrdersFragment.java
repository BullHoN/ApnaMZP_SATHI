package com.avit.apnamzpsathi.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentOrdersBinding;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.model.OrderItem;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class OrdersFragment extends Fragment implements OrdersAdapter.OrdersActions {

    private FragmentOrdersBinding binding;
    private OrderFragmentViewModel viewModel;
    private String TAG = "OrdersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOrdersBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(OrderFragmentViewModel.class);


        // TODO: Change the data
        viewModel.getOrdersFromServer(getContext(),"1234567890",4);

        binding.ordersList.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        OrdersAdapter ordersAdapter = new OrdersAdapter(new ArrayList<>(),getContext(),this);
        binding.ordersList.setAdapter(ordersAdapter);

        viewModel.getOrderItemMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<OrderItem>>() {
            @Override
            public void onChanged(List<OrderItem> orderItems) {
                ordersAdapter.updateItems(orderItems);
            }
        });

        binding.deliveryFilter.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.new_orders:
                        viewModel.getOrdersFromServer(getContext(),"1234567890",4);
                        return;
                    case R.id.out_for_delivery:
                        viewModel.getOrdersFromServer(getContext(),"1234567890",5);
                        return;
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void updateOrderStatus(String orderId, Integer updatedStatus) {
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<NetworkResponse> call = networkAPI.updateOrderStatus(orderId,updatedStatus);
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse> call, Response<NetworkResponse> response) {
                Toasty.success(getContext(),"Order Updated",Toasty.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(Call<NetworkResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toasty.error(getContext(),"Some Error Occurred",Toasty.LENGTH_SHORT)
                        .show();
            }
        });
    }
}