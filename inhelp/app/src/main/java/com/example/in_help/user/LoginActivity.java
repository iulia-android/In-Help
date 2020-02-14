package com.example.in_help.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.in_help.R;
import com.example.in_help.storage.UserDatabase;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private UserDatabase userDatabase = new UserDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setListenters();
    }

    private void setListenters() {
        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            }
        });

        findViewById(R.id.button_singup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText email = findViewById(R.id.edit_text_email);
                final String userEmail = email.getText().toString();

                EditText password  = findViewById(R.id.edit_text_password);
                final String pass = password.getText().toString();

                userDatabase.login(LoginActivity.this, userEmail, pass);
            }
        });
    }
}
