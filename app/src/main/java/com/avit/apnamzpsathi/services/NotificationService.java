package com.avit.apnamzpsathi.services;

import android.app.Notification;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.avit.apnamzpsathi.MainActivity;
import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.ui.acceptorder.AcceptOrderActivity;
import com.avit.apnamzpsathi.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import es.dmoral.toasty.Toasty;

public class NotificationService extends FirebaseMessagingService {
   
    private String TAG = "NotificationService";
    public static final String CHANNEL_ORDER_ID = "OrdersStatusChannel";
    private NotificationManagerCompat notificationManager;
    private static int ORDER_NOTIFICATION_ID = 2;
   
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG, "onNewToken: " + s);
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get("type");

        Log.i(TAG, "onMessageReceived: " + type);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        createNotificationChannels();

        if(type.contains("order")){

            String title = remoteMessage.getData().get("title");
            String desc = remoteMessage.getData().get("desc");
            String data = remoteMessage.getData().get("data");

            Log.i(TAG, "onMessageReceived: " + title);
            Log.i(TAG, "onMessageReceived: " + desc);
            Log.i(TAG, "onMessageReceived: " + data);

            handleNotification(title,desc,data);
        }

    }

    private void handleNotification(String title,String desc,String data){

        NotificationUtils.playSound(getApplicationContext());
        NotificationUtils.startVibration(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), AcceptOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("new_order",true);

        intent.setAction("com.avit.apnamzp_sathi.NEW_ORDER_NOTIFICATION");

        SharedPreferences sf = getApplicationContext().getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();

        editor.putString("new_order_data",data);
        editor.putBoolean("new_order_arrived",true);
        editor.apply();

        startActivity(intent);
//        if(!NotificationUtils.isAppIsInBackground(getApplicationContext())){
//
//        }
//        else {
//            showOrderNotification(title,desc);
//        }
    }

    private void showOrderNotification(String title,String desc){
        Intent ordersIntent = new Intent(getApplicationContext(), MainActivity.class);
        ordersIntent.putExtra("action","orders");
        ordersIntent.setAction("com.avit.apnamzpsathi_newnotificationBackgroundAction");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,ordersIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ORDER_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        if(ORDER_NOTIFICATION_ID > 1000000){
            ORDER_NOTIFICATION_ID = 2;
        }

        notificationManager.notify(ORDER_NOTIFICATION_ID++,notification);

    }

    public void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel orderStatusChannel = new NotificationChannel(CHANNEL_ORDER_ID,"Orders Status Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            orderStatusChannel.setDescription("This channel is responsible for all the notification regarding your order status.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(orderStatusChannel);

        }
    }


}