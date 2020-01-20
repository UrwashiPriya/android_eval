package com.neuman.brutus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.neuman.brutus.Utils.AccessControls;
import com.neuman.brutus.Utils.Globals;

import java.util.HashSet;

public class LoginActivity extends AppCompatActivity {

    String username, password;
    TextInputEditText user_input, pass_input;
    Globals g;
    AccessControls access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        g = new Globals();
        access = new AccessControls(getApplicationContext());

        user_input = findViewById(R.id.username);
        pass_input = findViewById(R.id.password);

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user_input.getText().toString();
                password = pass_input.getText().toString();

                if (access.validate_user(username, password, g.save_creds)) {
                    Intent intent = new Intent(LoginActivity.this, DashActivity.class);
                    startActivity(intent);
                } else {
                    // Show a toast mafaqer
                }
            }
        });
    }
}
