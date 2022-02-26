package com.efficacious.e_smartsrestaurant.TakeAway;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efficacious.e_smartsrestaurant.DeliveryBoy.DeliveryBoyAdapter;
import com.efficacious.e_smartsrestaurant.Model.DeliveryBoyData;
import com.efficacious.e_smartsrestaurant.Model.TakeAwayOrderData;
import com.efficacious.e_smartsrestaurant.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TakeAwayOrderHistoryFragment extends Fragment {

    List<TakeAwayOrderData> takeAwayOrderData;
    TakeAwayHistoryOrderAdapter takeAwayHistoryAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_away_order_history, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Takeaway history");

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        takeAwayOrderData = new ArrayList<>();
        takeAwayHistoryAdapter = new TakeAwayHistoryOrderAdapter(takeAwayOrderData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), takeAwayHistoryAdapter.getItemCount());
        recyclerView.setAdapter(takeAwayHistoryAdapter);
        firebaseFirestore.collection("Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
//                        String Id = doc.getDocument().getId();
                        TakeAwayOrderData mData = doc.getDocument().toObject(TakeAwayOrderData.class);
                        takeAwayOrderData.add(mData);
                        takeAwayHistoryAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}