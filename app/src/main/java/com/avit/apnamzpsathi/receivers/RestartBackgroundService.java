package com.avit.apnamzpsathi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.avit.apnamzpsathi.services.LocationUpdatesService;

public class RestartBackgroundService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context,new Intent(context, LocationUpdatesService.class));
        } else {
            context.startService(new Intent(context, LocationUpdatesService.class));
        }
    }
}
