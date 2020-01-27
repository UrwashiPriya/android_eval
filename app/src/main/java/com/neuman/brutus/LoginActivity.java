package com.neuman.brutus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.neuman.brutus.utils.AccessControls;
import com.neuman.brutus.utils.Globals;

public class LoginActivity extends AppCompatActivity {

    Globals g = new Globals();
    Intent intent;
    AccessControls access;
    ProgressDialog dialog;
    String username, password;
    TextInputEditText user_input, pass_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        access = new AccessControls(getApplicationContext());
        dialog = g.progressDialog(this, false);

        // If user valid? Go Home
        intent = new Intent(LoginActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Check if User Credentials are stored in Local
        auto_login_if_returning(access, dialog);

        setContentView(R.layout.activity_login);

        user_input = findViewById(R.id.username);
        pass_input = findViewById(R.id.password);

        findViewById(R.id.login_button).setOnClickListener(v -> {
            username = user_input.getText().toString();
            password = pass_input.getText().toString();

            access.validate_user(username, password, g.save_creds, getApplicationContext(), intent, dialog);
        });
    }

    private void auto_login_if_returning(AccessControls access, ProgressDialog dialog) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (prefs.getString("username", null) != null && prefs.getString("password", null) != null) {
            access.validate_user(prefs.getString("username", null),  prefs.getString("password", null), g.save_creds, getApplicationContext(), intent, dialog);
        }
    }
}
