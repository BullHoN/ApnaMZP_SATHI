package com.avit.apnamzpsathi.ui.orders;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.apnamzpsathi.model.OrderItem;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderFragmentViewModel extends ViewModel {

    private MutableLiveData<List<OrderItem>> orderItemMutableLiveData;
    private String TAG = "OrdersFragment";

    public OrderFragmentViewModel(){
        orderItemMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<OrderItem>> getOrderItemMutableLiveData() {
        return orderItemMutableLiveData;
    }

    public void getOrdersFromServer(Context context, String deliveryBoyId){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<OrderItem>> call = networkAPI.getOrdersForDeliveryBoy(deliveryBoyId);
        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                orderItemMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                Toasty.error(context,"Some Error Occurred",Toasty.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

}
