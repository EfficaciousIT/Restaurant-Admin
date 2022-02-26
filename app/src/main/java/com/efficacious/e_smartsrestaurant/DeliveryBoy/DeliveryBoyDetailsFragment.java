package com.efficacious.e_smartsrestaurant.DeliveryBoy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class DeliveryBoyDetailsFragment extends Fragment {

    ImageView mProfileImg;
    TextView mUserName,mMobileNumber,mTotalOrders;

    FirebaseFirestore firebaseFirestore;
    String mobileNumber;
    Pie pie;
    AnyChartView anyChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_boy_details, container, false);

        mobileNumber = getArguments().getString("MobileNumber");
        mProfileImg = view.findViewById(R.id.profile);
        mUserName = view.findViewById(R.id.userName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        mTotalOrders = view.findViewById(R.id.totalOrder);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("DeliveryBoy").document(mobileNumber)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String name = task.getResult().getString("Name");
                    String moNumber = task.getResult().getString("MobileNumber");
                    String timeStamp = task.getResult().getString("TimeStamp");
                    mMobileNumber.setText(moNumber);
                    mUserName.setText(name);
                }
            }
        });

        firebaseFirestore.collection("Orders")
                .whereEqualTo("DeliveryBoyId",mobileNumber)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            int size = value.size();
                            mTotalOrders.setText("TOTAL ORDERS : " + String.valueOf(size));
                        }else {
                            TextView textView = view.findViewById(R.id.orderData);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });


        anyChartView = view.findViewById(R.id.pieChart);
        anyChartView.setProgressBar(view.findViewById(R.id.loader));

        pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        firebaseFirestore.collection("Orders").whereEqualTo("DeliveryBoyId",mobileNumber)
                .whereEqualTo("Status", "Complete order").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    data.add(new ValueDataEntry("Complete", value.size()));
                    loadData(data);
                }else {
                    data.add(new ValueDataEntry("Complete", 0));
                    loadData(data);
                }
            }
        });

        firebaseFirestore.collection("Orders").whereEqualTo("DeliveryBoyId",mobileNumber)
                .whereEqualTo("Status", "On the way").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    data.add(new ValueDataEntry("Pending", value.size()));
                    loadData(data);
                }else {
                    data.add(new ValueDataEntry("Pending", 0));
                    loadData(data);
                }
            }
        });

        firebaseFirestore.collection("Orders").whereEqualTo("DeliveryBoyId",mobileNumber)
                .whereEqualTo("Status", "Cancel").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    data.add(new ValueDataEntry("Cancel", value.size()));
                    loadData(data);
                }else {
                    data.add(new ValueDataEntry("Cancel", 0));
                    loadData(data);
                }
            }
        });



        pie.labels().position("outside");

//        pie.legend().title().enabled(true);
//        pie.legend().title()
//                .text("")
//                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);


        return view;
    }

    private void loadData(List<DataEntry> data) {
        int size = data.size();
        if (size>=3){
            pie.data(data);
            pie.title("Total order summery (by orders)");
        }
    }
}