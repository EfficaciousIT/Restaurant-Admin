package com.efficacious.e_smartsrestaurant.Dashboard;

import static com.airbnb.lottie.L.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Attendance.EmployListAdapter;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.EmployeeDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetAllTakeAwayOrder;
import com.efficacious.e_smartsrestaurant.Model.GetEmployeeDetail;
import com.efficacious.e_smartsrestaurant.Model.GetTableReport;
import com.efficacious.e_smartsrestaurant.Model.GetTableReportResponse;
import com.efficacious.e_smartsrestaurant.Model.TakeAwayOrderResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.TableReport;
import com.efficacious.e_smartsrestaurant.Room.TableReportDatabase;
import com.efficacious.e_smartsrestaurant.TakeAway.AllTakeAwayAdapter;
import com.efficacious.e_smartsrestaurant.TakeAway.TakeAwayOrderHistoryFragment;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
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
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    CheckInternetConnection checkInternetConnection;
    List<GetTableReport> getTableReports;
    List<GetAllTakeAwayOrder> allTakeAwayOrders;
    TextView totalRevenueTxt,totalOrderTxt,totalTakeawayTxt,totalRegisterUserTxt;
    int TotalRevenue=0;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("DashBoard");

        getTableReports = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        checkInternetConnection = new CheckInternetConnection(getContext());
        totalRevenueTxt = view.findViewById(R.id.totalRevenue);
        totalOrderTxt = view.findViewById(R.id.totalOrder);
        totalTakeawayTxt = view.findViewById(R.id.takeAwayOrder);
        totalRegisterUserTxt = view.findViewById(R.id.totalRegisterUser);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

//        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.horizontalCalender)
//                .range(startDate, endDate)
//                .datesNumberOnScreen(5)
//                .build();
//
//        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//            @Override
//            public void onDateSelected(Calendar calendar, int position) {
//                int day = calendar.get(Calendar.DATE);
//                int month = calendar.get(Calendar.MONTH);
//                int year = calendar.get(Calendar.YEAR);
//            }
//
//            @Override
//            public void onCalendarScroll(HorizontalCalendarView calendarView,
//                                         int dx, int dy) {
//
//            }
//
//            @Override
//            public boolean onDateLongClicked(Calendar date, int position) {
//                return true;
//            }
//        });

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            getTotalRevenue();
            getTakeAwayOrderCount();
            getTotalRegisterUserCount();
        }

        view.findViewById(R.id.takeAway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new TakeAwayOrderHistoryFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

//        view.findViewById(R.id.takeAwayOrderSummery).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new TakeawayOrderSummeryFragment())
//                        .addToBackStack(null).commit();
//            }
//        });

        return view;
    }

    private void getTotalRegisterUserCount() {

        firebaseFirestore.collection("UserData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                                totalRegisterUserTxt.setText("#" + String.valueOf(count));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getTakeAwayOrderCount() {
        firebaseFirestore.collection("TakeAway")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                                totalTakeawayTxt.setText("#" + String.valueOf(count));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void getTotalRevenue() {
        try {
            Call<GetTableReportResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getTableReport("select","1");
            call.enqueue(new Callback<GetTableReportResponse>() {
                @Override
                public void onResponse(Call<GetTableReportResponse> call, Response<GetTableReportResponse> response) {
                    if (response.isSuccessful()){
                        getTableReports = response.body().getGetTableReport();
                        totalOrderTxt.setText("#" + String.valueOf(getTableReports.size()));
                        for (int i=0;i<getTableReports.size();i++){
                            TotalRevenue += getTableReports.get(i).getTotal();
                            totalRevenueTxt.setText("₹ " + String.valueOf(TotalRevenue));
                        }
                        TotalRevenue=0;
                    }
                }

                @Override
                public void onFailure(Call<GetTableReportResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure to exit ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                getActivity().moveTaskToBack(true);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}