package com.efficacious.e_smartsrestaurant.Base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

public class SettingFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPrefManger sharedPrefManger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        sharedPrefManger = new SharedPrefManger(getContext());
        String first_name = sharedPreferences.getString(WebConstant.FIRST_NAME,null);
        String last_name = sharedPreferences.getString(WebConstant.LAST_NAME,null);
        String Role = sharedPreferences.getString(WebConstant.USER_TYPE,null);

        TextView userName = view.findViewById(R.id.userName);
        TextView role = view.findViewById(R.id.role);

        userName.setText(first_name + " " + last_name);
        role.setText(Role);



        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPrefManger.logout();
                        startActivity(new Intent(getContext(),SplashScreenActivity.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }
        });
        return view;
    }
}