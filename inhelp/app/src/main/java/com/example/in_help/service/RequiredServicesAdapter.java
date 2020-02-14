package com.example.in_help.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.in_help.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequiredServicesAdapter extends FirestoreRecyclerAdapter<ServiceItem, RequiredServicesAdapter.RequiredServicesHolder>{

    public RequiredServicesAdapter(@NonNull FirestoreRecyclerOptions<ServiceItem> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull RequiredServicesHolder holder, int position, @NonNull ServiceItem model) {
        holder.serviceName.setText("Service: " + model.getName());
        holder.serviceCategory.setText("Category: " + model.getCategory());
        holder.offerdBy.setText(model.getUsername());
        String currentSchedule;
        if(model.isPeriodic()){
            currentSchedule = "This service is periodic.";
            currentSchedule += "\nAvailable by:\n    " + model.getDays() + "\n     " + model.getTime();
        }
        else{
            currentSchedule =  "This service is not periodic.";
            currentSchedule += "\nAvailable on:\n    " + model.getDays() + "\n     " + model.getTime();
        }
        if( !model.isPeriodic() ){
            currentSchedule +=  " " + model.getTimestamp();
        }
        holder.schedule.setText(currentSchedule);
        holder.rating.setRating(model.getRating());
        holder.price.setText(Float.toString(model.getPrice()) + " Lei");

    }

    @NonNull
    @Override
    public RequiredServicesAdapter.RequiredServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.required_service_cardview, parent, false);
        return new RequiredServicesAdapter.RequiredServicesHolder(v);
    }


    public class RequiredServicesHolder extends RecyclerView.ViewHolder{
        TextView serviceName;
        TextView serviceCategory;
        TextView offerdBy;
        TextView schedule;
        RatingBar rating;
        TextView price;

        public RequiredServicesHolder(@NonNull View itemView){
            super(itemView);

            serviceName = itemView.findViewById(R.id.text_view_service_name);
            serviceCategory = itemView.findViewById(R.id.text_view_service_category);
            offerdBy =  itemView.findViewById(R.id.text_view_offered_by_replace);
            schedule = itemView.findViewById(R.id.text_view_service_schedule_replace);
            rating = itemView.findViewById(R.id.rating_bar_service);
            price = itemView.findViewById(R.id.text_view_service_price_replace);
        }
    }
}