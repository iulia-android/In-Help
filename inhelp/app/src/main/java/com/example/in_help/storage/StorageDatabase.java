package com.example.in_help.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.example.in_help.category.CategoriesAdapter;
import com.example.in_help.R;
import com.example.in_help.service.ServiceAdapter;
import com.example.in_help.service.ServiceItem;
import com.example.in_help.service.ServicesActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;

public class StorageDatabase {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final CollectionReference servicesReference = db.collection("services");
    private final CollectionReference usersReference = db.collection("users");
    private final CollectionReference categoryReference = db.collection("categories");
    private final UserDatabase user = new UserDatabase();
    private final FirebaseAuth auth = user.getAuth();
    private ArrayList<String> data;

    public void insertNewUserData(final String emailString, final String name, final Integer ageNumber,
                                  final String countryString, final String townString, final String occupationString,
                                  final String descriptionString, final String preferencesString, final String offeredServicesString){
        Map<String, Object> user = new HashMap<>();
        user.put("email", emailString);
        user.put("name", name);
        user.put("age", ageNumber);
        user.put("country", countryString);
        user.put("town", townString);
        user.put("occupation", occupationString);
        user.put("description", descriptionString);
        user.put("preferences", preferencesString);
        user.put("offerServices", offeredServicesString);

        db.collection("users").document(emailString).set(user);

    }

    public void uploadService(final Context context, String nameOfService, float priceService,
                              String descriptionOfService, String categoryOfService,
                              boolean periodicBoolean, String username, Uri mImageUri, String city,
                              String days, String time, String timeStampFinal){

        String auxNameOfservice = nameOfService.replace(" ","_");

        StorageReference ref = storageReference.child("service_image/" + auxNameOfservice.toLowerCase()
                + "_" + username + ".png");

        ref.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       // Toast.makeText(context, "Image added.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                      //  Toast.makeText(context, "Error adding image.", Toast.LENGTH_SHORT).show();
                    }
                });

        Map<String, Object> serviceObject = new HashMap<>();
        serviceObject.put("name",nameOfService);
        serviceObject.put("price",priceService);
        serviceObject.put("description",descriptionOfService);
        serviceObject.put("category",categoryOfService);
        serviceObject.put("periodic",periodicBoolean);
        serviceObject.put("rating", 0);
        serviceObject.put("username", username);
        serviceObject.put("city", city);
        serviceObject.put("days", days);
        serviceObject.put("time", time);
        serviceObject.put("number_of_ratings", 0);
        serviceObject.put("customer", "No");
        serviceObject.put("timestamp", timeStampFinal);
        serviceObject.put("precise_location", "Location isn't set yet.");

        db.collection("services").document(auxNameOfservice + "_" + username).set(serviceObject);

        Intent intentCategory = new Intent(context, ServicesActivity.class);
        intentCategory.putExtra("category_from_add_service", categoryOfService);
        context.startActivity(intentCategory);
    }

    public void submitReview(final String name, final String email, final float rating){
        db.collection("services").whereEqualTo("username", email)
                .whereEqualTo("name", name).get() //usersReference.document(((String)userData.get("email"))).update(userData);
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                    DocumentSnapshot retrievedData = documentSnapshotList.get(0);
                    ServiceItem serviceItem = retrievedData.toObject(ServiceItem.class);

                    Map<String, Object> serviceData = new HashMap<>();
                    serviceData.put("rating", ( rating + serviceItem.getRating()*serviceItem.getNumber_of_ratings() ) / (serviceItem.getNumber_of_ratings() + 1));
                    serviceData.put("number_of_ratings", serviceItem.getNumber_of_ratings() + 1);
                    String auxNameOfservice = name.replace(" ","_");
                    System.out.println("===================");
                    System.out.println(auxNameOfservice + "_" + email);
                    db.collection("services").document(auxNameOfservice + "_" + email).update(serviceData);
                }
            }
        });
    }

    public void getCategoryImageFromStorage(final String imageName, final CategoriesAdapter.ViewHolder holder){
        storageReference.child(imageName).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(holder.getImage());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.getImage().setImageResource(R.drawable.no_image);
            }
        });
    }

    public void getServiceImageFromStorage(final String imageName, final ServiceAdapter.OfferredServiceHolder holder){
        storageReference.child(imageName).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(holder.getImageView());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    public void getServiceImageStorage(final String imageName, final ImageView imageView){
        storageReference.child(imageName).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    public FirestoreRecyclerOptions<ServiceItem> getSpecificServices(String receivedCategory){

        Query query = servicesReference.whereEqualTo("category", receivedCategory);
        return new FirestoreRecyclerOptions.Builder<ServiceItem>()
                .setQuery(query, ServiceItem.class)
                .build();
    }

    public FirestoreRecyclerOptions<ServiceItem> getSpecificServicesForTheCurrentUser(String user){

        Query query = servicesReference.whereEqualTo("username", user);
        return new FirestoreRecyclerOptions.Builder<ServiceItem>()
                .setQuery(query, ServiceItem.class)
                .build();
    }

    public FirestoreRecyclerOptions<ServiceItem> getSpecificRequiredServices(String customer){
        Query query = servicesReference.whereEqualTo("customer", customer);
            return new FirestoreRecyclerOptions.Builder<ServiceItem>()
                    .setQuery(query, ServiceItem.class)
                    .build();
    }


    public void updateUserData(Map<String, Object> userData){
       usersReference.document(((String)userData.get("email"))).update(userData);
    }

    public void updateServiceData(String documentName, Map<String, Object> serviceData){
        servicesReference.document(documentName).update(serviceData);
    }

    public FirestoreRecyclerOptions<ServiceItem> orderServicesAfter(
            String filterOption, String city,
            String category, boolean direction, boolean all){

        Query.Direction direction1;
        if( direction ) {
            direction1 = Query.Direction.ASCENDING;
        } else{
            direction1 = Query.Direction.DESCENDING;
        }

        Query query;
        if (all) {
            query = servicesReference.whereEqualTo("category", category).orderBy(filterOption, direction1);
        } else{
            query = servicesReference.whereEqualTo("category", category).whereEqualTo("city", city).orderBy(filterOption, direction1);
        }

        return new FirestoreRecyclerOptions.Builder<ServiceItem>()
                .setQuery(query, ServiceItem.class)
                .build();
    }

    public void updateNumberOfServices(String categoryName, Map<String, Object> categoryData){
        categoryReference.document(categoryName).update(categoryData);
    }
}
