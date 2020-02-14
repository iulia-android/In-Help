package com.example.in_help.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.in_help.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OfferedServicesAdapter extends FirestoreRecyclerAdapter<ServiceItem, OfferedServicesAdapter.OfferedServicesHolder> {

    public OfferedServicesAdapter(@NonNull FirestoreRecyclerOptions<ServiceItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OfferedServicesHolder holder, int position, @NonNull ServiceItem model) {
        holder.textViewOfferedServicesName.setText("Service: " + model.getName());
        holder.textViewOfferedServicesCategory.setText("Category: " + model.getCategory());
        String schedule = "";
        if( model.isPeriodic() ){
            schedule += "Periodic" + '\n' + model.getDays() + '\n' + model.getTime();
        } else {
            schedule += "Not periodic" + '\n' + model.getDays().trim() + '\n' + model.getTime() + " " + model.getTimestamp();
        }
        holder.textViewOfferedServicesSchedule.setText(schedule);
        holder.ratingBarOfferedServices.setRating(model.getRating());
        holder.textViewCustomerLocation.setText(model.getPrecise_location());
        holder.textViewOfferedServicesCustomer.setText(model.getCustomer());
    }

    @NonNull
    @Override
    public OfferedServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offered_service_cardview, parent, false);
        return new OfferedServicesHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class OfferedServicesHolder extends RecyclerView.ViewHolder{
        TextView textViewOfferedServicesName;
        TextView textViewOfferedServicesCategory;
        TextView textViewOfferedServicesCustomer;
        TextView textViewOfferedServicesSchedule;
        TextView textViewCustomerLocation;
        RatingBar ratingBarOfferedServices;

        public OfferedServicesHolder(@NonNull View itemView) {
            super(itemView);

            textViewOfferedServicesName = itemView.findViewById(R.id.text_view_my_services_name);
            textViewOfferedServicesCategory = itemView.findViewById(R.id.text_view_my_services_category);
            textViewOfferedServicesCustomer = itemView.findViewById(R.id.text_view_my_services_customer_replace);
            textViewOfferedServicesSchedule = itemView.findViewById(R.id.text_view_my_services_customer_schedule_replace);
            textViewCustomerLocation = itemView.findViewById(R.id.text_view_my_service_customer_location_replace);
            ratingBarOfferedServices = itemView.findViewById(R.id.rating_bar_my_services);
        }
    }
}
