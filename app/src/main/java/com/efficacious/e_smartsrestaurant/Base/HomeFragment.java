package com.efficacious.e_smartsrestaurant.Base;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.efficacious.e_smartsrestaurant.Billing.BillTableStatusFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderFragment;
import com.efficacious.e_smartsrestaurant.Menu.MenuCategoryFragment;
import com.efficacious.e_smartsrestaurant.NewOrder.CartFragment;
import com.efficacious.e_smartsrestaurant.NewOrder.NewOrderFragment;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String title;
    SharedPrefManger sharedPrefManger;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPrefManger = new SharedPrefManger(getContext());
        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        String UserName = sharedPreferences.getString(WebConstant.FIRST_NAME,null);
        String Role = sharedPreferences.getString(WebConstant.USER_TYPE,null);
        TextView userTxt = view.findViewById(R.id.userName);
        userTxt.setText("Hello, "+ UserName + " (" +Role+")");
        dayStatus(view);

        view.findViewById(R.id.newOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewOrderFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new MenuCategoryFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.existingOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExistingOrderFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new BillTableStatusFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPrefManger.logout();
                        startActivity(new Intent(getContext(),SplashScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();

            }
        });

        return view;
    }

    private void dayStatus(View view) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            title="Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            title="Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            title="Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            title="Good Night";
        }
        TextView textView = view.findViewById(R.id.text);
        textView.setText(title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.restaurant_logo)
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