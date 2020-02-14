package com.example.in_help.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.in_help.user.AccountActivity;
import com.example.in_help.category.CategoriesActivity;
import com.example.in_help.R;
import com.example.in_help.storage.StorageDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private StorageDatabase storageDatabase = new StorageDatabase();
    private ServiceAdapter serviceAdapter;
    private String receivedCategory;
    private Spinner spinnerFilterService;
    private RecyclerView recyclerView;
    private Spinner getSpinnerFilterCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        setUpSpinner();
        setUpSpinnerCity();

        Bundle bundle = getIntent().getExtras();
        String bundleString1 = null;
        String bundleString2 = null;
        String bundleString3 = null;
        String bundleString4 = null;

        if (bundle != null && bundle.getString("category") != null) {
            bundleString1 = bundle.getString("category");
        }
        if( bundle != null && bundle.getString("category_from_add_service") != null ){
            bundleString2 = bundle.getString("category_from_add_service");
        }
        if( bundle != null && bundle.getString("category_from_add_service_back_button") != null ){
            bundleString3 = bundle.getString("category_from_add_service_back_button");
        }
        if( bundle != null && bundle.getString("category_from_selected_service_back_button") != null ){
            bundleString3 = bundle.getString("category_from_selected_service_back_button");
        }

        if( bundleString1 != null ) {
            receivedCategory = bundleString1;
        }
        if ( bundleString2 != null ) {
            receivedCategory = bundleString2;
        }
        if ( bundleString3 != null ) {
            receivedCategory = bundleString3;
        }
        if ( bundleString4 != null ) {
            receivedCategory = bundleString4;
        }

        setUpRecyclerView();

        setListenters();
    }

    public void setUpSpinner(){

        spinnerFilterService = findViewById(R.id.spinner_filter);
        List<String> filters = getFilters();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filters);
        spinnerFilterService.setAdapter(adapter);
        spinnerFilterService.setOnItemSelectedListener(this);
    }

    public void setUpSpinnerCity(){

        getSpinnerFilterCity = findViewById(R.id.spinner_city);
        List<String> filters = getCity();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filters);
        getSpinnerFilterCity.setAdapter(adapter);
        getSpinnerFilterCity.setOnItemSelectedListener(this);
    }

    private List<String> getFilters(){
        List<String> filters = new ArrayList<>();

        filters.add("Cheapest price");
        filters.add("Highest price");
        filters.add("Most popular");
        filters.add("Products A-Z");
        filters.add("Products Z-A");

        return filters;
    }

    private List<String> getCity(){
        List<String> filters = new ArrayList<>();

        filters.add("All");
        filters.add("Alba Iulia");
        filters.add("Blaj");
        filters.add("Brasov");
        filters.add("Cluj Napoca");
        filters.add("Craiova");
        filters.add("Deva");
        filters.add("Iasi");
        filters.add("Ramnicu Valcea");
        filters.add("Reghin");
        filters.add("Sibiu");
        filters.add("Sighisoara");
        filters.add("Turda");
        filters.add("Targu Mures");
        filters.add("Oradea");
        filters.add("Piatra Neamt");

        return filters;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemFilter = String.valueOf(spinnerFilterService.getSelectedItem());
        String selectedItemCity = String.valueOf(getSpinnerFilterCity.getSelectedItem()).toLowerCase();
        String filterOption = "rating";
        boolean direction = true;
        boolean all = true;

        if ( selectedItemFilter.equals("Most popular") ){
            filterOption = "rating";
        } else if ( selectedItemFilter.equals("Cheapest price") || selectedItemFilter.equals("Highest price") ){
            filterOption = "price";
        } else if ( selectedItemFilter.equals("Products A-Z") || selectedItemFilter.equals("Products Z-A") ){
            filterOption = "name";
        }

        if ( selectedItemFilter.equals("Products Z-A") || selectedItemFilter.equals("Highest price") ){
            direction = false;
        }

        if ( !selectedItemCity.equals("all")){
            all = false;
        }

        FirestoreRecyclerOptions<ServiceItem> options = storageDatabase.orderServicesAfter(filterOption, selectedItemCity, receivedCategory, direction, all);
        serviceAdapter.updateOptions(options);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setUpRecyclerView() {

        FirestoreRecyclerOptions<ServiceItem> options = storageDatabase.getSpecificServices(receivedCategory);

        serviceAdapter = new ServiceAdapter(options);
        recyclerView = findViewById(R.id.recycler_view_services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serviceAdapter);

        serviceAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                ServiceItem serviceItem = documentSnapshot.toObject(ServiceItem.class);

                Intent intent = new Intent(ServicesActivity.this, IndividualServiceActivity.class);
                intent.putExtra("service_item_name", serviceItem.getName());
                intent.putExtra("service_item_description", serviceItem.getDescription());
                intent.putExtra("service_item_price", serviceItem.getPrice());
                intent.putExtra("service_item_periodic", serviceItem.isPeriodic());
                intent.putExtra("service_item_username", serviceItem.getUsername());
                intent.putExtra("service_item_city", serviceItem.getCity());
                intent.putExtra("service_item_rating", serviceItem.getRating());
                intent.putExtra("service_item_category", serviceItem.getCategory());
                intent.putExtra("service_item_days", serviceItem.getDays());
                intent.putExtra("service_item_time", serviceItem.getTime());
                intent.putExtra("service_item_nr_of_ratings", serviceItem.getNumber_of_ratings());
                intent.putExtra("service_item_nr_of_timestamp", serviceItem.getTimestamp());
                intent.putExtra("service_customer_name", serviceItem.getCustomer());
                intent.putExtra("precise_location", serviceItem.getPrecise_location());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        serviceAdapter.stopListening();
    }


    private void setListenters() {
        findViewById(R.id.floating_action_button_add_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(ServicesActivity.this, AddingServiceActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(ServicesActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(ServicesActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_user_services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(ServicesActivity.this, OfferedServicesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(ServicesActivity.this, RequiredServicesActivity.class);
                startActivity(intent);
            }
        });
    }
}
