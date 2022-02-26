package com.efficacious.e_smartsrestaurant.DeliveryBoy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efficacious.e_smartsrestaurant.Model.DeliveryBoyData;
import com.efficacious.e_smartsrestaurant.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SelectDeliveryBoyFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    List<DeliveryBoyData> deliveryBoyData;
    SelectDeliveryBoyAdapter deliveryBoyAdapter;
    RecyclerView recyclerView;
    String OrderId,TotalBill,UserName,MobileNumber,RegisterId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_delivery_boy, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Delivery Boy");

        Bundle bundle = this.getArguments();
        if(bundle != null){
            OrderId = bundle.getString("OrderId");
            TotalBill = bundle.getString("TotalBill");
            UserName = bundle.getString("UserName");
            MobileNumber = bundle.getString("MobileNumber");
            RegisterId = bundle.getString("RegisterId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        deliveryBoyData = new ArrayList<>();
        deliveryBoyAdapter = new SelectDeliveryBoyAdapter(deliveryBoyData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), deliveryBoyAdapter.getItemCount());
        recyclerView.setAdapter(deliveryBoyAdapter);
        firebaseFirestore.collection("DeliveryBoy").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
//                        String Id = doc.getDocument().getId();
                        DeliveryBoyData mData = doc.getDocument().toObject(DeliveryBoyData.class).withId(OrderId,TotalBill,RegisterId,UserName,MobileNumber);
                        deliveryBoyData.add(mData);
                        deliveryBoyAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}