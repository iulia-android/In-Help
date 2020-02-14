package com.example.in_help.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.in_help.storage.StorageDatabase;
import com.example.in_help.user.AccountActivity;
import com.example.in_help.category.CategoriesActivity;
import com.example.in_help.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OfferedServicesActivity extends AppCompatActivity {

    private StorageDatabase storageDatabase = new StorageDatabase();
    private OfferedServicesAdapter offeredServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_services);

        setListenters();
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        String username = "noUser";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            username = user.getEmail();
        }

        FirestoreRecyclerOptions<ServiceItem> options = storageDatabase.getSpecificServicesForTheCurrentUser(username);

        offeredServiceAdapter = new OfferedServicesAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view__offered_services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(offeredServiceAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                offeredServiceAdapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setListenters() {
        findViewById(R.id.linear_layout_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(OfferedServicesActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(OfferedServicesActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_user_services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(OfferedServicesActivity.this, OfferedServicesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(OfferedServicesActivity.this, RequiredServicesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        offeredServiceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        offeredServiceAdapter.stopListening();
    }
}