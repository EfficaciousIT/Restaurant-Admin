package com.efficacious.e_smartsrestaurant.Dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.TableReport;
import com.efficacious.e_smartsrestaurant.Room.TableReportDatabase;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class ShowTableReportFragment extends Fragment {

    TableReportDatabase tableReportDatabase;
    CalendarView calendar;
    String Date;
    int total=0;
    List<TableReport> tableReports;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_table_report, container, false);

        setupLocalDB();

        Date = getArguments().getString("Date");

        tableReports = tableReportDatabase.dao_tableReport().getDateWiseReport(Date);
        ExtendedFloatingActionButton reportDetails = view.findViewById(R.id.reportDetails);

        int size = tableReports.size();
        if (size==0){
            LottieAnimationView lottieAnimationView = view.findViewById(R.id.emptyState);
            TextView textView = view.findViewById(R.id.emptyTxt);
            lottieAnimationView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            reportDetails.setVisibility(View.GONE);
        }else {
            for (int i=0;i<tableReports.size();i++){
                   total += tableReports.get(i).getTotal();
            }
            //total amount of day
            reportDetails.setText("Date : " + Date +"   Total Revenue : ₹" + total);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            TableReportAdapter adapter = new TableReportAdapter(tableReports);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }



        return view;
    }

    private void setupLocalDB(){
        tableReportDatabase = Room.databaseBuilder(getContext(), TableReportDatabase.class,"TableReport")
                .allowMainThreadQueries().build();
    }
}