package com.neuman.brutus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.neuman.brutus.offline.mode.UserOpsOffSync;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.utils.Globals;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Globals g;
    Intent intent;
    ProgressDialog dialog;
    TextInputEditText user_input, pass_input;
    String username, password;
    UserOpsOffSync offSyncUserOps = new UserOpsOffSync();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g = new Globals();

        dialog = g.progressDialog(this, false);

        // If user valid? Go Home
        intent = new Intent(LoginActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Check if User Credentials are stored in Local
        auto_login_if_returning();

        setContentView(R.layout.activity_login);

        user_input = findViewById(R.id.username);
        pass_input = findViewById(R.id.password);

        findViewById(R.id.login_button).setOnClickListener(v -> {
            username = user_input.getText().toString();
            password = pass_input.getText().toString();

            offSyncUserOps.offsync_validate_user(username, password, getApplicationContext(), intent, new retrofit2.Callback<SimpleResponse>(){
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (response.body() != null && response.body().getSuccess().contains("true")) {

                        JsonObject fetch_params = new JsonObject();
                        fetch_params.addProperty("login", username);
                        fetch_params.addProperty("password", password);

                        offSyncUserOps.writeto_offsync(response.body(), fetch_params, "login_req", getApplicationContext(), 1);

                        if (g.save_creds) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();
                        }

                        if (getApplicationContext() != null && intent != null) {
                            getApplicationContext().startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) { }
            });
        });
    }

    private void auto_login_if_returning() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String user = prefs.getString("username", null);
        String pass = prefs.getString("password", null);

        if (user != null && pass != null) {
            offSyncUserOps.offsync_validate_user(user, pass, getApplicationContext(), intent, new retrofit2.Callback<SimpleResponse>(){
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (response.body() != null && response.body().getSuccess().contains("true")) {

                        JsonObject fetch_params = new JsonObject();
                        fetch_params.addProperty("login", username);
                        fetch_params.addProperty("password", password);

                        offSyncUserOps.writeto_offsync(response.body(), fetch_params, "login_req", getApplicationContext(), 1);

                        if (g.save_creds) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("username", user);
                            editor.putString("password", pass);
                            editor.apply();
                        }

                        if (getApplicationContext() != null && intent != null) {
                            getApplicationContext().startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) { }
            });
        }
    }
}
