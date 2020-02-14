package com.example.in_help.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.in_help.R;
import com.example.in_help.category.CategoriesActivity;
import com.example.in_help.storage.StorageDatabase;
import com.example.in_help.user.AccountActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RequiredServicesActivity extends AppCompatActivity {

    private StorageDatabase storageDatabase = new StorageDatabase();
    private RequiredServicesAdapter requiredServicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_services);

        setListenters();
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        String userName = "noUser";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            userName = user.getEmail();
        }

        FirestoreRecyclerOptions<ServiceItem> options = storageDatabase.getSpecificRequiredServices(userName);
        requiredServicesAdapter = new RequiredServicesAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view__required_services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(requiredServicesAdapter);
    }


    private void setListenters() {
        findViewById(R.id.linear_layout_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(RequiredServicesActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(RequiredServicesActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_user_services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(RequiredServicesActivity.this, OfferedServicesActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        requiredServicesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requiredServicesAdapter.stopListening();
    }
}
