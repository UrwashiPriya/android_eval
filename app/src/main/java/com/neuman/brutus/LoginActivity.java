package com.neuman.brutus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.neuman.brutus.Utils.AccessControls;
import com.neuman.brutus.Utils.Globals;

import java.util.HashSet;

public class LoginActivity extends AppCompatActivity {

    Globals g;
    Intent intent;
    AccessControls access;
    ProgressDialog dialog;
    String username, password;
    TextInputEditText user_input = findViewById(R.id.username), pass_input = findViewById(R.id.password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        g = new Globals();
        access = new AccessControls(getApplicationContext());
        dialog = g.progressDialog(this, false);
        ProgressBar progress = findViewById(R.id.progress_bar_);
        progress.setVisibility(View.VISIBLE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = prefs.getString("username", null);
        password = prefs.getString("password", null);

        if (username != null && password != null) {
            intent = new Intent(LoginActivity.this, DashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            access.validate_user(username, password, g.save_creds, getApplicationContext(), intent, dialog);
        }

        findViewById(R.id.login_button).setOnClickListener(v -> {
            username = user_input.getText().toString();
            password = pass_input.getText().toString();

            intent = new Intent(LoginActivity.this, DashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            access.validate_user(username, password, g.save_creds, getApplicationContext(), intent, dialog);
        });
    }
}
