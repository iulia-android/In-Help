package com.example.in_help.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.in_help.R;
import com.example.in_help.service.RequiredServicesActivity;
import com.example.in_help.service.OfferedServicesActivity;
import com.example.in_help.user.AccountActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<Integer> numberOfServices = new ArrayList<>();
    private final CollectionReference categoryReference = db.collection("categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setListenters();
        initializeCategoriesData();
    }


    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_categories);
        CategoriesAdapter adapter = new CategoriesAdapter(categoryNames, numberOfServices, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setLists(ArrayList<String> categoryNames, ArrayList<Integer> numberOfServices){
        this.categoryNames.addAll(categoryNames);
        this.numberOfServices.addAll(numberOfServices);
        initializeRecyclerView();
    }

    private void initializeCategoriesData() {
        categoryReference.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()){
                                ArrayList<String> categoryNames = new ArrayList<>();
                                ArrayList<Integer> numberOfServices = new ArrayList<>();
                                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot documentSnapshot: documentSnapshotList){
                                    CategoryData categoryData = documentSnapshot.toObject(CategoryData.class);
                                    categoryNames.add(categoryData.getCategoryName());
                                    numberOfServices.add(categoryData.getNumberOfServices());
                                }
                                setLists(categoryNames, numberOfServices);
                            }
                        }
                    });
    }

    private void setListenters() {
        findViewById(R.id.linear_layout_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(CategoriesActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_user_services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(CategoriesActivity.this, OfferedServicesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(CategoriesActivity.this, RequiredServicesActivity.class);
                startActivity(intent);
            }
        });
    }
}
