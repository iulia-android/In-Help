package com.example.in_help.category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.in_help.R;
import com.example.in_help.service.AddingServiceActivity;
import com.example.in_help.service.ServicesActivity;
import com.example.in_help.storage.StorageDatabase;
import com.example.in_help.user.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private StorageDatabase storageDatabase = new StorageDatabase();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference servicesReference = db.collection("services");
    private ArrayList<String> categoryNames;
    private ArrayList<Integer> noOfServices;
    private Context context;

    CategoriesAdapter(final ArrayList<String> categoryNames, final ArrayList<Integer> noOfServices,
                      final Context context) {
        this.categoryNames = categoryNames;
        this.noOfServices = noOfServices;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesAdapter.ViewHolder holder, final int position) {
        holder.categoryName.setText(categoryNames.get(position));
        String imageName = "category_image/" + categoryNames.get(position).toLowerCase()
                .replace(" ", "_") + ".png";
        storageDatabase.getCategoryImageFromStorage(imageName, holder);
        updateCategories(holder, position);

        holder.categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(context, ServicesActivity.class);
                intent.putExtra("category", categoryNames.get(position));

                AddingServiceActivity.choosedCategory = categoryNames.get(position);
                context.startActivity(intent);
            }
        });
    }

    public void updateCategories(@NonNull final CategoriesAdapter.ViewHolder holder, final int position){
        servicesReference.whereEqualTo("category", categoryNames.get(position)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int numberOfServices = 0;
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            numberOfServices = documentSnapshotList.size();
                        }
                        System.out.println("============");
                        System.out.println(numberOfServices);
                        Map<String, Object> categoryData = new HashMap<>();
                        categoryData.put("categoryName", categoryNames.get(position));
                        categoryData.put("numberOfServices", numberOfServices);
                        storageDatabase.updateNumberOfServices(categoryNames.get(position), categoryData);
                        String servicesNumber = "Number of services: " + numberOfServices;
                        holder.numberOfServices.setText(servicesNumber);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView categoryName;
        private TextView numberOfServices;
        private LinearLayout categoryCard;

         ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view_category);
            categoryName = itemView.findViewById(R.id.text_view_category_name);
            numberOfServices = itemView.findViewById(R.id.text_view_number_of_services);
            categoryCard = itemView.findViewById(R.id.linear_layout_category);
        }

        public ImageView getImage() {
            return image;
        }
    }
}
