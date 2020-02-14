package com.example.in_help.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.in_help.R;
import com.example.in_help.storage.StorageDatabase;
import com.example.in_help.storage.UserDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IndividualServiceActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference servicesReference = db.collection("services");
    private final UserDatabase user = new UserDatabase();
    private final FirebaseAuth auth = user.getAuth();
    private final FirebaseUser currenUser = auth.getCurrentUser();
    final String currentEmail = currenUser.getEmail();

    private TextView textViewName;
    private TextView textViewDescription;
    private TextView textViewPrice;
    private TextView textViewPeriodic;
    private TextView textViewUsername;
    private TextView textViewCity;
    private RatingBar ratingBar;
    private ImageView imageView;
    private TextView textViewCategory;
    private Intent intent;
    private Button addToCart;
    private EditText preciseLocation;
    private StorageDatabase storageDatabase = new StorageDatabase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_service);

        intent = getIntent();
        textViewName = findViewById(R.id.text_view_service_item_name);
        textViewDescription = findViewById(R.id.text_view_service_item_description_replaced);
        textViewPrice = findViewById(R.id.text_view_service_item_price_replace);
        textViewPeriodic = findViewById(R.id.text_view_service_item_periodic);
        textViewUsername = findViewById(R.id.text_view_service_item_username);
        textViewCity = findViewById(R.id.text_view_service_item_city_replace);
        ratingBar = findViewById(R.id.rating_bar_service_item_replace);
        imageView = findViewById(R.id.image_view_service_item_image);
        textViewCategory =findViewById(R.id.text_view_item_service_category);
        addToCart = findViewById(R.id.button_add_to_cart);
        preciseLocation = findViewById(R.id.edit_text_precise_location);

        final String name = intent.getStringExtra("service_item_name");
        final String description = intent.getStringExtra("service_item_description");
        final float price = intent.getFloatExtra("service_item_price", 0);
        final boolean periodic = intent.getBooleanExtra("service_item_periodic", false);
        final String username = intent.getStringExtra("service_item_username");
        final String city = intent.getStringExtra("service_item_city");
        final float rating = intent.getFloatExtra("service_item_rating", 0);
        final String category = intent.getStringExtra("service_item_category");
        final String days = intent.getStringExtra("service_item_days");
        final String time = intent.getStringExtra("service_item_time");
        final float numberOfRatings = intent.getFloatExtra("service_item_nr_of_ratings", 0);
        final String timestamp = intent.getStringExtra("service_item_nr_of_timestamp");
        final String customerName = intent.getStringExtra("service_customer_name");
        final String customerLocation = intent.getStringExtra("precise_location");


        textViewName.setText(name);
        textViewCategory.setText("Category: " + category);
        String aux_time_available;
        if(!description.equals("")) {
            textViewDescription.setText(description);
        }
        if(price>0){
            textViewPrice.setText(Float.toString(price) + " Lei");
        }
        if( periodic ){
            aux_time_available = "This service is periodic.";
            aux_time_available += "\nAvailable by:\n    " + days + "\n     " + time;
        } else {
            aux_time_available = "This service is not periodic.";
            aux_time_available += "\nAvailable on:\n    " + days + "\n     " + time;
        }
        if( !periodic ){
            aux_time_available +=  " " + timestamp;
        }
        textViewPeriodic.setText(aux_time_available);
        textViewUsername.setText(username);
        textViewCity.setText(city);
        ratingBar.setRating(rating);

        final String auxNameOfservice = name.replace(" ","_");
        final String imageName = "service_image/" + auxNameOfservice.toLowerCase() + "_" + username + ".png";
        storageDatabase.getServiceImageStorage(imageName, imageView);

        if(!customerName.equals("No")) {
            addToCart.setBackgroundTintList(ContextCompat.getColorStateList(IndividualServiceActivity.this, R.color.gray));
            preciseLocation.setEnabled(false);
            preciseLocation.setText("  Service isn't available.");
            preciseLocation.setTextColor(Color.GRAY);
            preciseLocation.setTextSize(20);
            preciseLocation.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_trasparent, null));
        }

        findViewById(R.id.button_back_to_services_recycler_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent1 = new Intent(IndividualServiceActivity.this, ServicesActivity.class);
                intent1.putExtra("category_from_selected_service_back_button", intent.getStringExtra("service_item_category"));
                startActivity(intent1);
            }
        });

        findViewById(R.id.button_service_item_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageDatabase.submitReview(name, username, ratingBar.getRating());
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerName.equals("No")) {
                    String enteredLocation = preciseLocation.getText().toString();
                    if(enteredLocation.trim().equals(""))
                        Toast.makeText(IndividualServiceActivity.this, "Empty location.",
                                Toast.LENGTH_SHORT).show();
                    else
                    {
                        addToCart(name, price, description, category, periodic, rating,
                                username, city, days, time, numberOfRatings, timestamp, enteredLocation);
                        Intent intent2 = new Intent(IndividualServiceActivity.this, RequiredServicesActivity.class);
                        startActivity(intent2);
                    }
                }
            }
        });


    }

    public void addToCart(String name, float price, String description, String category, boolean periodic,
                          float rating, String username, String city, String days, String time,
                          float numberOfRatings, String timestamp, String enteredLocation){
        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("name", name);
        serviceData.put("price", price);
        serviceData.put("description", description);
        serviceData.put("category", category);
        serviceData.put("periodic", periodic);
        serviceData.put("rating", rating);
        serviceData.put("username", username);
        serviceData.put("city", city);
        serviceData.put("days", days);
        serviceData.put("time", time);
        serviceData.put("number_of_ratings", numberOfRatings);
        serviceData.put("customer", currentEmail);
        serviceData.put("timestamp", timestamp);
        serviceData.put("precise_location", enteredLocation);
        String documentName = name.replace(" ", "_") + "_" + username;
        storageDatabase.updateServiceData(documentName, serviceData);

    }
}
