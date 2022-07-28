package com.avit.apnamzpsathi.ui.earnings;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.apnamzpsathi.model.DeliverySathiDayInfo;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.utils.ErrorUtils;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EarningsViewModel extends ViewModel {

    private MutableLiveData<DeliverySathiDayInfo> mutableLiveData;

    public EarningsViewModel(){
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DeliverySathiDayInfo> getMutableLiveData() {
        return mutableLiveData;
    }

    public void getDeliverySathiInfo(Context context, String deliverySathi, String ordersDateString, boolean isMonthly){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<DeliverySathiDayInfo> call = networkAPI.getDeliverySaathiDayInfo(deliverySathi,ordersDateString,isMonthly);
        call.enqueue(new Callback<DeliverySathiDayInfo>() {
            @Override
            public void onResponse(Call<DeliverySathiDayInfo> call, Response<DeliverySathiDayInfo> response) {

                if(!response.isSuccessful()){
                    NetworkResponse errorResponse = ErrorUtils.parseErrorResponse(response);
                    Toasty.error(context,errorResponse.getDesc(),Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DeliverySathiDayInfo> call, Throwable t) {
                Toasty.error(context,t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

}
