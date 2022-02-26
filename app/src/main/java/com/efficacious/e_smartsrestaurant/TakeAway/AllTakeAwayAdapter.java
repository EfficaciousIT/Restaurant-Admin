package com.efficacious.e_smartsrestaurant.TakeAway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ViewOrderFragment;
import com.efficacious.e_smartsrestaurant.Model.GetAllTakeAwayOrder;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AllTakeAwayAdapter extends RecyclerView.Adapter<AllTakeAwayAdapter.ViewHolder> {

    List<GetAllTakeAwayOrder> getAllTakeAwayOrders;
    Context context;
    CheckInternetConnection checkInternetConnection;
    FirebaseFirestore firebaseFirestore;

    public AllTakeAwayAdapter(List<GetAllTakeAwayOrder> getAllTakeAwayOrders) {
        this.getAllTakeAwayOrders = getAllTakeAwayOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.take_away_order_view,parent,false);
        context = parent.getContext();
        checkInternetConnection = new CheckInternetConnection(context);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.orderId.setText("#" + String.valueOf(getAllTakeAwayOrders.get(position).getOrderId()) + " Takeaway Order");
        firebaseFirestore.collection("TakeAway").document(getAllTakeAwayOrders.get(position).getOrderId().toString())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String DeliveryStatus = task.getResult().getString("DeliveryStatus");
                    holder.deliveryStatus.setText("DeliveryStatus : " + DeliveryStatus);
                }
            }
        });
        holder.btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new ViewTakeAwayOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("OrderId",Integer.parseInt(getAllTakeAwayOrders.get(position).getOrderId().toString()));
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getAllTakeAwayOrders.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnViewOrder;
        TextView orderId,deliveryStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnViewOrder = itemView.findViewById(R.id.btnViewOrder);
            orderId = itemView.findViewById(R.id.orderId);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);
        }
    }
}
