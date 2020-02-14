package com.example.in_help.service;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.in_help.R;
import com.example.in_help.storage.StorageDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ServiceAdapter extends FirestoreRecyclerAdapter<ServiceItem, ServiceAdapter.OfferredServiceHolder> {

    private StorageDatabase storageDatabase = new StorageDatabase();
    private OnItemClickListener listener;

    ServiceAdapter(@NonNull FirestoreRecyclerOptions<ServiceItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final OfferredServiceHolder holder, int position, @NonNull ServiceItem model) {
        holder.textViewNameService.setText(model.getName());
        String price = Float.toString(model.getPrice());
        holder.textViewPriceService.setText(price);
        holder.ratingBar.setRating(model.getRating());

        final String auxNameOfservice = model.getName().replace(" ","_");
        final String imageName = "service_image/" + auxNameOfservice.toLowerCase() + "_" + model.getUsername() + ".png";
        final String customerName = model.getCustomer();
        if(!customerName.equals("No")){
            holder.textViewDisponibility.setText("Service is unavailable.");
            holder.textViewDisponibility.setTextColor(Color.RED);
        }

        storageDatabase.getServiceImageFromStorage(imageName, holder);
    }

    @NonNull
    @Override
    public OfferredServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_cardview, parent, false);
        return new OfferredServiceHolder(v);
    }

    @Override
    public void updateOptions(@NonNull FirestoreRecyclerOptions<ServiceItem> options) {
        super.updateOptions(options);
    }

    public class OfferredServiceHolder extends RecyclerView.ViewHolder{
        TextView textViewNameService;
        TextView textViewPriceService;
        TextView textViewDisponibility;
        RatingBar ratingBar;
        ImageView imageView;

        OfferredServiceHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameService = itemView.findViewById(R.id.text_view_my_services_name);
            textViewPriceService = itemView.findViewById(R.id.text_view_price);
            ratingBar = itemView.findViewById(R.id.rating_bar_service);
            imageView = itemView.findViewById(R.id.image_view_service_image);
            textViewDisponibility = itemView.findViewById(R.id.text_view_disponibility);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
