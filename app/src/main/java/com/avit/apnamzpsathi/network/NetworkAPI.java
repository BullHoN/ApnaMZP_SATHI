package com.avit.apnamzpsathi.network;

import com.avit.apnamzpsathi.model.DeliveryInfoData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkAPI {
    String SERVER_URL = "http://192.168.1.3:5000/";

    @GET("/getDeliveryPriceInfoSathi")
    Call<List<DeliveryInfoData>> getDeliveryPricingInfo();

    @GET("/getIncentivePriceInfoSathi")
    Call<List<DeliveryInfoData>> getIncentivePricingInfo();

}
