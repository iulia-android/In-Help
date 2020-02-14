package com.example.in_help.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.in_help.category.CategoriesActivity;
import com.example.in_help.R;
import com.example.in_help.service.OfferedServicesActivity;
import com.example.in_help.service.RequiredServicesActivity;
import com.example.in_help.storage.StorageDatabase;
import com.example.in_help.storage.UserDatabase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private final UserDatabase userDatabase = new UserDatabase();
    private final StorageDatabase storageDatabase = new StorageDatabase();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersReference = db.collection("users");
    private final UserDatabase user = new UserDatabase();
    private final FirebaseAuth auth = user.getAuth();
    private final FirebaseUser currenUser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getUserData();
        setListenters();
    }

    public void getUserData(){
            String currentEmail = currenUser.getEmail();
            System.out.println("mailll:" + currentEmail);
            usersReference.whereEqualTo("email", currentEmail).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()){
                                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                DocumentSnapshot retrievedData = documentSnapshotList.get(0);
                                UserData userData = retrievedData.toObject(UserData.class);
                                ArrayList<String> userInformation = new ArrayList<>();
                                userInformation.add(userData.getName());
                                userInformation.add(String.valueOf(userData.getAge()));
                                userInformation.add(userData.getCountry());
                                userInformation.add(userData.getTown());
                                userInformation.add(userData.getOccupation());
                                userInformation.add(userData.getDescription());
                                userInformation.add(userData.getPreferences());
                                userInformation.add(userData.getOfferServices());
                                fillUserInformation(userInformation);
                            }
                        }
                    });
    }

    public void fillUserInformation(ArrayList<String> userData){
        ((EditText)findViewById(R.id.edit_text_name)).setText(userData.get(0));
        System.out.println("EMAILLL: " + userData.get(0));
        ((ElegantNumberButton)findViewById(R.id.elegant_number_button_age)).setNumber(userData.get(1));
        ((EditText)findViewById(R.id.edit_text_country)).setText(userData.get(2));
        ((EditText)findViewById(R.id.edit_text_town)).setText(userData.get(3));
        ((EditText)findViewById(R.id.edit_text_occupation)).setText(userData.get(4));
        ((EditText)findViewById(R.id.edit_text_description)).setText(userData.get(5));
        ((EditText)findViewById(R.id.edit_text_preferences)).setText(userData.get(6));
        RadioGroup offerServicesGroup = findViewById(R.id.radio_group_offer_services);
        String offerServices = userData.get(7);
        if(offerServices.equals("Yes"))
            ((RadioButton)offerServicesGroup.getChildAt(1)).setChecked(true);
        else
            ((RadioButton)offerServicesGroup.getChildAt(0)).setChecked(true);
    }

    void updateUserData(String email){
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("name", ((EditText)findViewById(R.id.edit_text_name)).getText().toString());
        userData.put("age", Integer.parseInt(((ElegantNumberButton)findViewById(R.id.elegant_number_button_age)).getNumber()));
        userData.put("country", ((EditText)findViewById(R.id.edit_text_country)).getText().toString());
        userData.put("town", ((EditText)findViewById(R.id.edit_text_town)).getText().toString());
        userData.put("occupation", ((EditText)findViewById(R.id.edit_text_occupation)).getText().toString());
        userData.put("description", ((EditText)findViewById(R.id.edit_text_description)).getText().toString());
        userData.put("preferences", ((EditText)findViewById(R.id.edit_text_preferences)).getText().toString());
        int selectedId = ((RadioGroup)findViewById(R.id.radio_group_offer_services)).getCheckedRadioButtonId();
        String offeredServicesString = ((RadioButton)findViewById(selectedId)).getText().toString();
        userData.put("offerServices", offeredServicesString);
        storageDatabase.updateUserData(userData);
    }


    private void setListenters() {
        findViewById(R.id.linear_layout_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(AccountActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_user_services).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(AccountActivity.this, OfferedServicesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(AccountActivity.this, RequiredServicesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_apply_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData(currenUser.getEmail());
            }
        });

        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatabase.signOut(AccountActivity.this);
            }
        });

    }
}
