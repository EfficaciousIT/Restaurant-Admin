package com.efficacious.e_smartsrestaurant.DeliveryBoy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.DeliveryBoyData;
import com.efficacious.e_smartsrestaurant.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeliveryBoyAdapter extends RecyclerView.Adapter<DeliveryBoyAdapter.ViewHolder>{

    List<DeliveryBoyData> deliveryBoyData;
    Context context;

    public DeliveryBoyAdapter(List<DeliveryBoyData> deliveryBoyData) {
        this.deliveryBoyData = deliveryBoyData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.deliveryBoyName.setText(deliveryBoyData.get(position).getName());
        holder.mobileNumber.setText(deliveryBoyData.get(position).getMobileNumber());
        String status = deliveryBoyData.get(position).getStatus();
        if (!TextUtils.isEmpty(status)){
            if (status.equalsIgnoreCase("true")){
                holder.status.setTextColor(Color.GREEN);
                holder.status.setText("Online");
            }else {
                holder.status.setText("Offline");
            }
        }else {
            holder.status.setText("Offline");
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure to delete ?");
                builder.setMessage("Permanently remove delivery boy.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("DeliveryBoy")
                                .document(deliveryBoyData.get(position).getMobileNumber()).delete();
                        deliveryBoyData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,deliveryBoyData.size());
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new DeliveryBoyDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("MobileNumber",deliveryBoyData.get(position).getMobileNumber());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                        .addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return deliveryBoyData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImg;
        ImageView btnDelete;
        TextView deliveryBoyName,status,mobileNumber;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            deliveryBoyName = itemView.findViewById(R.id.deliveryBoyName);
            status = itemView.findViewById(R.id.status);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
