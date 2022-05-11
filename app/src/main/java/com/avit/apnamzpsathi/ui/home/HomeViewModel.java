package com.avit.apnamzpsathi.ui.home;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.model.DeliveryInfoData;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeViewModel extends ViewModel {
    MutableLiveData<List<DeliveryInfoData>> mutableLiveData;
    MutableLiveData<List<DeliveryInfoData>> mutableIncentiveData;
    private MutableLiveData<Boolean> deliveryBoyStatusIsOnlineData;
    private SharedPreferences sharedPreferences;

    public HomeViewModel(){
        mutableLiveData = new MutableLiveData<>();
        mutableIncentiveData = new MutableLiveData<>();
        deliveryBoyStatusIsOnlineData = new MutableLiveData<>();
    }

    public void initlializeDeliveryBoyStatus(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
        deliveryBoyStatusIsOnlineData.setValue(sharedPreferences.getBoolean(SharedPrefNames.DELIVERY_BOY_STATUS,false));
    }

    public void toggleDeliveryBoyStatus(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean updatedStatus = !deliveryBoyStatusIsOnlineData.getValue();
        editor.putBoolean(SharedPrefNames.DELIVERY_BOY_STATUS,updatedStatus);
        deliveryBoyStatusIsOnlineData.setValue(updatedStatus);
    }

    public MutableLiveData<Boolean> getDeliveryBoyStatusIsOnlineData() {
        return deliveryBoyStatusIsOnlineData;
    }

    public MutableLiveData<List<DeliveryInfoData>> getMutableIncentiveData() {
        return mutableIncentiveData;
    }

    public MutableLiveData<List<DeliveryInfoData>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void getIncentiveDataFromServer(Context context){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<DeliveryInfoData>> call = networkAPI.getIncentivePricingInfo();
        call.enqueue(new Callback<List<DeliveryInfoData>>() {
            @Override
            public void onResponse(Call<List<DeliveryInfoData>> call, Response<List<DeliveryInfoData>> response) {
                mutableIncentiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DeliveryInfoData>> call, Throwable t) {
                Toasty.error(context,t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void getDataFromServer(Context context){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<DeliveryInfoData>> call = networkAPI.getDeliveryPricingInfo();
        call.enqueue(new Callback<List<DeliveryInfoData>>() {
            @Override
            public void onResponse(Call<List<DeliveryInfoData>> call, Response<List<DeliveryInfoData>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DeliveryInfoData>> call, Throwable t) {
                Toasty.error(context,t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });
    }

}
