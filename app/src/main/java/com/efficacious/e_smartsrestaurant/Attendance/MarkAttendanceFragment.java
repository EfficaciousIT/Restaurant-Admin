package com.efficacious.e_smartsrestaurant.Attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Model.GetAttendance;
import com.efficacious.e_smartsrestaurant.Model.GetAttendanceResponse;
import com.efficacious.e_smartsrestaurant.Model.InsertAttendance;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.roomorama.caldroid.CaldroidFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkAttendanceFragment extends Fragment {

    String eName,eMobile,date,attendance;
    CheckInternetConnection checkInternetConnection;
    int empId;
    Button btnMarkAttendance;
    Calendar calendar;
    TextView selectDate;
    Button btnSubmit;
    RadioButton present,absent,leave;
    RelativeLayout relativeLayout;
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    List<GetAttendance> attendanceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Attendance");

        eName = getArguments().getString("eName");
        eMobile = getArguments().getString("eMobile");
        empId = Integer.valueOf(getArguments().getString("empId"));
        checkInternetConnection = new CheckInternetConnection(getContext());
        btnMarkAttendance = view.findViewById(R.id.btnMarkAttendance);
//        calendarView = view.findViewById(R.id.calenderView);
        present = view.findViewById(R.id.present);
        absent = view.findViewById(R.id.absent);
        leave = view.findViewById(R.id.leave);

        selectDate = view.findViewById(R.id.date);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        relativeLayout = view.findViewById(R.id.relativeLayout);

        attendanceList = new ArrayList<>();

        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);

        mCaldroidFragment.setArguments(args);

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            try {
                Call<GetAttendanceResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getAttendance("select",String.valueOf(1),String.valueOf(empId));

                call.enqueue(new Callback<GetAttendanceResponse>() {
                    @Override
                    public void onResponse(Call<GetAttendanceResponse> call, Response<GetAttendanceResponse> response) {
                        attendanceList = response.body().getGetAttendance();
                        setAttendanceOnCalender(attendanceList);
                        try {
                            getChildFragmentManager().beginTransaction().replace(R.id.cal_container,mCaldroidFragment)
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(Call<GetAttendanceResponse> call, Throwable t) {

                    }
                });
            }catch (Exception e){

            }
        }

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                        month+=1;
//                        date = day + "/" + month + "/" + year;
                        relativeLayout.setVisibility(View.VISIBLE);
//                        selectDate.setText(date);
//                    }
//                },year,month,dayOfMonth);
//                datePickerDialog.show();
            }
        });

        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendance = "P";
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendance = "A";
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendance = "L";
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String date = selectDate.getText().toString();
                if (!TextUtils.isEmpty(attendance)){
                    if (!checkInternetConnection.isConnectingToInternet()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Internet not Available !!");
                        builder.setNegativeButton("Close", null);
                        builder.show();
                    }else {
                        try {
                            InsertAttendance insertAttendance = new InsertAttendance(empId,attendance,1);
                            Call<ResponseBody> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .insertAttendance("insert",insertAttendance);

                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(getContext(), "Attendance mark successfully !!", Toast.LENGTH_SHORT).show();
                                        relativeLayout.setVisibility(View.GONE);
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new EmployListFragment())
                                                .addToBackStack(null).commit();
                                    }
                                }


                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(), "Api Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }catch (Exception e){
                            Toast.makeText(getContext(), "Internal Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(getContext(), "Select option", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
               getFragmentManager().beginTransaction().replace(R.id.fragment_container,new EmployListFragment())
                       .addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setAttendanceOnCalender(List<GetAttendance> attendanceList) {
        int day = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        for (int i=0;i<attendanceList.size();i++){
            String inputString2 = attendanceList.get(i).getIntime();
            String inputString1 = myFormat.format(date);
            try {
                Date date1 = null;
                try {
                    date1 = myFormat.parse(inputString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = myFormat.parse(inputString2);
                //Calculating number of days from two dates
                long diff = date2.getTime() - date1.getTime();
                long datee = diff / (1000 * 60 * 60 * 24);
                //Converting long type to int type
                day = (int) datee;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, day);
            Date holidayDay = cal.getTime();
            ColorDrawable bgToday;
           try {
               if (attendanceList.get(i).getStatus().contentEquals("P")) {
                   bgToday = new ColorDrawable(Color.GREEN);
               } else {
                   bgToday = new ColorDrawable(Color.RED);
               }
               mCaldroidFragment.setBackgroundDrawableForDate(bgToday, holidayDay);
           }catch (Exception e){

           }
        }
    }
}