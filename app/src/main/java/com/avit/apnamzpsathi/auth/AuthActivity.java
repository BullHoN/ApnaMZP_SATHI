package com.avit.apnamzpsathi.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.avit.apnamzpsathi.MainActivity;
import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.db.LocalDB;
import com.avit.apnamzpsathi.db.SharedPrefNames;
import com.avit.apnamzpsathi.model.DeliverySathi;
import com.avit.apnamzpsathi.model.LoginPostData;
import com.avit.apnamzpsathi.model.NetworkResponse;
import com.avit.apnamzpsathi.network.NetworkAPI;
import com.avit.apnamzpsathi.network.RetrofitClient;
import com.avit.apnamzpsathi.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthActivity extends AppCompatActivity {

    private String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if(LocalDB.getSharedPreference(getApplicationContext()).contains(SharedPrefNames.DELIVERY_SATHI_DATA)){
            openHomeActivity();
        }

        TextInputEditText phoneNoView = findViewById(R.id.phone_no);
        TextInputEditText passwordView = findViewById(R.id.password);

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = phoneNoView.getText().toString();
                String password = passwordView.getText().toString();

                if(!Validation.isValidPhoneNo(phoneNo) || !Validation.isValidPassword(password)){
                    Toasty.error(getApplicationContext(),"Invalid PhoneNo or Password",Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                logIn(phoneNo,password);

            }
        });

    }

    private void logIn(String phoneNo,String password){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<NetworkResponse> call = networkAPI.login(new LoginPostData(phoneNo,password));
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse> call, Response<NetworkResponse> response) {

                NetworkResponse networkResponse = response.body();

                if(networkResponse == null){
                    Toasty.error(getApplicationContext(),"Some Error Occured",Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                if(networkResponse.isSuccess()){
                    Toasty.success(getApplicationContext(),"Login Successfull",Toasty.LENGTH_SHORT)
                            .show();

                    DeliverySathi deliverySathi = new DeliverySathi(phoneNo);
                    LocalDB.saveSathiDetails(getApplicationContext(),deliverySathi);

                    openHomeActivity();

                }
                else {
                    Toasty.error(getApplicationContext(),networkResponse.getDesc(),Toasty.LENGTH_SHORT)
                    .show();
                }


            }

            @Override
            public void onFailure(Call<NetworkResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    private void openHomeActivity(){
        Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeActivity);
        finish();
    }

}