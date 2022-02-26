package com.efficacious.e_smartsrestaurant.DeliveryBoy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Model.DeliveryBoyData;
import com.efficacious.e_smartsrestaurant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddDeliveryBoyFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    List<DeliveryBoyData> deliveryBoyData;
    DeliveryBoyAdapter deliveryBoyAdapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_delivery_boy, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Delivery Boy");

        recyclerView = view.findViewById(R.id.recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        deliveryBoyData = new ArrayList<>();
        deliveryBoyAdapter = new DeliveryBoyAdapter(deliveryBoyData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), deliveryBoyAdapter.getItemCount());
        recyclerView.setAdapter(deliveryBoyAdapter);
        firebaseFirestore.collection("DeliveryBoy").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
//                        String Id = doc.getDocument().getId();
                        DeliveryBoyData mData = doc.getDocument().toObject(DeliveryBoyData.class);
                        deliveryBoyData.add(mData);
                        deliveryBoyAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        view.findViewById(R.id.btnAddDeliveryBoy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_delivery_boy);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button btnAddBoy = dialog.findViewById(R.id.btnAddBoy);
                ImageView btnCancel = dialog.findViewById(R.id.btnClose);
                EditText fullName = dialog.findViewById(R.id.fullName);
                EditText mobileNumber = dialog.findViewById(R.id.mobileNumber);
                ProgressBar progressBar = dialog.findViewById(R.id.loader);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnAddBoy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String deliveryBoyName = fullName.getText().toString().trim();
                        String mobileNo = mobileNumber.getText().toString().trim();

                        if (TextUtils.isEmpty(deliveryBoyName)){
                            fullName.setError("Empty field");
                        }else if (TextUtils.isEmpty(mobileNo)){
                            mobileNumber.setError("Empty field");
                        }else {
                            firebaseFirestore.collection("DeliveryBoy")
                                    .whereEqualTo("MobileNumber",mobileNo)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (!value.isEmpty()){
                                                btnAddBoy.setVisibility(View.GONE);
                                                progressBar.setVisibility(View.VISIBLE);
                                                HashMap<Object,String> map = new HashMap<>();
                                                map.put("Name",deliveryBoyName);
                                                map.put("MobileNumber","+91"+mobileNo);
                                                map.put("isVerify","false");
                                                map.put("TimeStamp", String.valueOf(System.currentTimeMillis()));
                                                map.put("Status", String.valueOf(false));
                                                map.put("FCMToken","");

                                                firebaseFirestore.collection("DeliveryBoy").document("+91"+mobileNo)
                                                        .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(getContext(), "Delivery boy added !!", Toast.LENGTH_SHORT).show();
                                                            btnAddBoy.setVisibility(View.VISIBLE);
                                                            progressBar.setVisibility(View.GONE);
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }else {
                                                btnAddBoy.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                mobileNumber.setError("Mobile number already register");
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });
        return view;
    }
}