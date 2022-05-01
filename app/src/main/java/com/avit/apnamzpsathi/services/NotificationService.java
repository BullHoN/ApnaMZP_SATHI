package com.avit.apnamzpsathi.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
   
    private String TAG = "NotificationService";
   
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG, "onNewToken: " + s);
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: ");
    }
}