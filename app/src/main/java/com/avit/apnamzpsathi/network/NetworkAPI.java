package com.avit.apnamzpsathi.network;

import com.avit.apnamzpsathi.model.DeliveryInfoData;
import com.avit.apnamzpsathi.model.DeliverySathi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetworkAPI {
    String SERVER_URL = "http://192.168.1.3:5000/";

    @GET("/getDeliveryPriceInfoSathi")
    Call<List<DeliveryInfoData>> getDeliveryPricingInfo();

    @GET("/getIncentivePriceInfoSathi")
    Call<List<DeliveryInfoData>> getIncentivePricingInfo();

    @POST("/sathi/location/location_update/")
    Call<ResponseBody> sendLocationUpdates(@Body DeliverySathi deliverySathi);

}
