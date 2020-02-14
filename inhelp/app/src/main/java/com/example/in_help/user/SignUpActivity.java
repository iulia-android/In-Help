package com.example.in_help.user;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.in_help.R;
import com.example.in_help.storage.UserDatabase;
import com.example.in_help.storage.StorageDatabase;

public class SignUpActivity extends AppCompatActivity {

    private UserDatabase userDatabase = new UserDatabase();
    private StorageDatabase storageDatabase = new StorageDatabase();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_sign_up);
        setListeners();
    }

    void setListeners(){
        findViewById(R.id.button_back_to_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            }
        });

        findViewById(R.id.button_singup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                signUp();
            }
        });
    }

    void signUp(){
        String emailString = ((EditText)findViewById(R.id.edit_text_email)).getText().toString();
        String name = ((EditText)findViewById(R.id.edit_text_name)).getText().toString();
        String passwordString = ((EditText)findViewById(R.id.edit_text_password)).getText().toString();
        String confirmPasswordString = ((EditText)findViewById(R.id.edit_text_confirm_password)).getText().toString();
        Integer ageNumber = Integer.parseInt(((ElegantNumberButton)findViewById(R.id.elegant_number_button_age)).getNumber());
        String countryString = ((EditText)findViewById(R.id.edit_text_country)).getText().toString();
        String townString = ((EditText)findViewById(R.id.edit_text_town)).getText().toString();
        String occupationString = ((EditText)findViewById(R.id.edit_text_occupation)).getText().toString();
        String descriptionString = ((EditText)findViewById(R.id.edit_text_description)).getText().toString();
        String preferencesString = ((EditText)findViewById(R.id.edit_text_preferences)).getText().toString();
        int selectedId = ((RadioGroup)findViewById(R.id.radio_group_offer_services)).getCheckedRadioButtonId();
        String offeredServicesString = ((RadioButton)findViewById(selectedId)).getText().toString();

        if (!passwordString.equals(confirmPasswordString)) {
            Toast.makeText(SignUpActivity.this, "Passwords don't match.",
                    Toast.LENGTH_SHORT).show();
        } else if (testPassword(passwordString)) {
                if(emailString != emailString.toLowerCase())
                    Toast.makeText(SignUpActivity.this, "The e-mail should only be in lowercase letters.",
                            Toast.LENGTH_SHORT).show();
                else{
                    userDatabase.signup(SignUpActivity.this, emailString, passwordString);
                    storageDatabase.insertNewUserData(emailString, name, ageNumber,
                            countryString, townString, occupationString, descriptionString, preferencesString,
                            offeredServicesString);
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }

    boolean testPassword(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean isAtLeast8 = password.length() >= 8;
        boolean hasDigits = password.matches(".*\\d+.*");
        boolean hasSpecial = !password.matches("[A-Za-z0-9 ]*");

        if (!isAtLeast8) {
            Toast.makeText(SignUpActivity.this, "Password must have at least 8 characters.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(hasUppercase && hasLowercase && hasSpecial && hasDigits)) {
            Toast.makeText(SignUpActivity.this, "Password must have lowercase, " +
                            "uppercase, at least one digit and one special character.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
