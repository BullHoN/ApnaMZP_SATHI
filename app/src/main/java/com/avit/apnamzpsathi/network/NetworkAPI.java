package com.avit.apnamzpsathi.network;

import com.avit.apnamzpsathi.model.DeliveryInfoData;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.model.OrderItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkAPI {
    String SERVER_URL = "http://192.168.1.3:5000/";

    @GET("/getDeliveryPriceInfoSathi")
    Call<List<DeliveryInfoData>> getDeliveryPricingInfo();

    @GET("/getIncentivePriceInfoSathi")
    Call<List<DeliveryInfoData>> getIncentivePricingInfo();

    @POST("/sathi/location/location_update/")
    Call<ResponseBody> sendLocationUpdates(@Body DeliverySathi deliverySathi,@Query("action") String action);

    @GET("/sathi/orders/{delivery_sathi}")
    Call<List<OrderItem>> getOrdersForDeliveryBoy(@Path("delivery_sathi") String deliverySathi, @Query("order_status") Integer orderStatus);

    @POST("/partner/order/updateStatus")
    Call<NetworkResponse> updateOrderStatus(@Query("orderId") String orderId,@Query("orderStatus") Integer orderStatus);

    @GET("/login")
    Call<ResponseBody> login(@Query("phoneNo") String phoneNo,@Query("password") String password);

    @POST("/user_routes/updateFCM")
    Call<ResponseBody> updateFcmToken(@Body DeliverySathi deliverySathi,@Query("user_type") String userType);

}
