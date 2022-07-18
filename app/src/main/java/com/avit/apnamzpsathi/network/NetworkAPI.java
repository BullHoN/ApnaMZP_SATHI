package com.avit.apnamzpsathi.network;

import com.avit.apnamzpsathi.model.CashInHand;
import com.avit.apnamzpsathi.model.DeliveryInfoData;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.model.DeliverySathiDayInfo;
import com.avit.apnamzpsathi.model.LoginPostData;
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
    String SERVER_URL = "http://192.168.63.85:5000/";
//    String SERVER_URL = "https://482c-2409-4063-2109-67d5-8589-6b8a-35ff-e19.ngrok.io";

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

    @POST("/sathi/login")
    Call<NetworkResponse> login(@Body LoginPostData loginPostData);

    @POST("/user_routes/updateFCM")
    Call<NetworkResponse> updateFcmToken(@Body DeliverySathi deliverySathi,@Query("user_type") String userType);

    @POST("/sathi/updateItemsOnTheWayPrice/{order_id}")
    Call<NetworkResponse> updateItemsOnTheWayPrice(@Path("order_id") String orderId,@Query("itemsOnTheWayActualCost") String itemsOnTheWayActualCost);

    @POST("/sathi/cancelItemsOnTheWay/{order_id}")
    Call<NetworkResponse> cancelItemsOnTheWay(@Path("order_id") String orderId);

    @GET("/sathi/cashInHand/{delivery_sathi_id}")
    Call<CashInHand> getCashInHand(@Path("delivery_sathi_id") String delvierySathiId);

    @GET("/sathi/dayInfo/{deliverySathi}")
    Call<DeliverySathiDayInfo> getDeliverySaathiDayInfo(@Path("deliverySathi") String deliverySathi, @Query("ordersDateString") String ordersDateString);

}
