package com.avit.apnamzpsathi.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.avit.apnamzpsathi.model.DeliverySathi;
import com.google.gson.Gson;

public class LocalDB {

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreference(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(SharedPrefNames.SHARED_DB_NAME,Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    public static void saveSathiDetails(Context context, DeliverySathi deliverySathi){
        SharedPreferences sf = LocalDB.getSharedPreference(context);

        Gson gson = new Gson();
        String deliverySathiString = gson.toJson(deliverySathi);

        SharedPreferences.Editor editor = sf.edit();
        editor.putString(SharedPrefNames.DELIVERY_SATHI_DATA,deliverySathiString);

        editor.apply();
    }

    public static DeliverySathi getDeliverySathiDetails(Context context){
        SharedPreferences sf = LocalDB.getSharedPreference(context);

        Gson gson = new Gson();
        String deliverySathiString = sf.getString(SharedPrefNames.DELIVERY_SATHI_DATA,null);
        if(deliverySathiString == null) return null;

        return gson.fromJson(deliverySathiString,DeliverySathi.class);
    }

}
