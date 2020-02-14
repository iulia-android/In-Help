package com.example.in_help.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.in_help.R;
import com.example.in_help.storage.StorageDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddingServiceActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    public static String choosedCategory = "Cleaning";

    private EditText name;
    private RadioGroup category;
    private EditText description;
    private EditText price;
    private RadioGroup periodic;
    private ImageView mImageView;
    private Uri mImageUri;

    StorageDatabase storageDatabase = new StorageDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_service);

        name = findViewById(R.id.edit_text_service_name);
        category = findViewById(R.id.radio_group_category);
        description = findViewById(R.id.edit_text_description);
        price = findViewById(R.id.edit_text_price);
        periodic = findViewById(R.id.radio_group_periodic);
        mImageView = findViewById(R.id.image_view_chosen_image);

        setListenters();
    }

    private void setListenters() {
        findViewById(R.id.button_back_to_services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(AddingServiceActivity.this,
                        ServicesActivity.class);
                intent.putExtra("category_from_add_service_back_button",choosedCategory);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_choose_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openFileChooser();
            }
        });


        findViewById(R.id.button_enter_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String nameOfService = name.getText().toString();
                name.getText().clear();

                final float priceService = Float.parseFloat(price.getText().toString());
                price.getText().clear();

                final String descriptionOfService = description.getText().toString();
                description.getText().clear();

                int selectedCategory = category.getCheckedRadioButtonId();
                RadioButton radioButtonCategory = findViewById(selectedCategory);
                final String categoryOfService = radioButtonCategory.getText().toString();
                category.clearCheck();

                final boolean periodicBoolean;
                RadioButton radioButton = findViewById(R.id.radio_button_periodic);
                periodicBoolean = radioButton.isChecked();
                periodic.clearCheck();

                String days = "";
                CheckBox monday = findViewById(R.id.checkbox_monday);
                CheckBox tuesday = findViewById(R.id.checkbox_tuesday);
                CheckBox wednesday = findViewById(R.id.checkbox_wednesday);
                CheckBox thursday = findViewById(R.id.checkbox_thursday);
                CheckBox friday = findViewById(R.id.checkbox_friday);
                CheckBox saturday = findViewById(R.id.checkbox_saturday);
                CheckBox sunday = findViewById(R.id.checkbox_sunday);
                if( monday.isChecked() ){
                    days += "Monday";
                    monday.setChecked(false);
                }
                if( tuesday.isChecked() ){
                    days += " Tuesday";
                    tuesday.setChecked(false);
                }
                if( wednesday.isChecked() ){
                    days += " Wednesday";
                    wednesday.setChecked(false);
                }
                if( thursday.isChecked() ){
                    days += " Thursday";
                    thursday.setChecked(false);
                }
                if( friday.isChecked() ){
                    days += " Friday";
                    friday.setChecked(false);
                }
                if( saturday.isChecked() ){
                    days += " Saturday";
                    saturday.setChecked(false);
                }
                if( sunday.isChecked() ){
                    days += " Sunday";
                    sunday.setChecked(false);
                }
                final String auxDays = days;

                EditText editTextTime = findViewById(R.id.edit_text_set_a_time);
                final String time = editTextTime.getText().toString();
                editTextTime.getText().clear();

                EditText editTextDate = findViewById(R.id.edit_text_set_a_date);
                String timeStamp = null;
                if( !periodicBoolean ){
                    timeStamp = editTextDate.getText().toString();
                }
                final String timeStampFinal = timeStamp;

                String username = "noUser";
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    username = user.getEmail();
                }

                final String usernameConstant = username;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    String city = null;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String test = String.valueOf(document.get("email"));
                                if( test.equals(usernameConstant) ) {
                                    city = String.valueOf(document.get("town")).toLowerCase();
                                    break;
                                }
                            }
                        }

                        storageDatabase.uploadService(AddingServiceActivity.this, nameOfService,
                                priceService, descriptionOfService, categoryOfService, periodicBoolean,
                                usernameConstant, mImageUri, city, auxDays, time, timeStampFinal);
                    }
                });

            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }
}
