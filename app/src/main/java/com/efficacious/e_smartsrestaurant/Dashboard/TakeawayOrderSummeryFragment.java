package com.efficacious.e_smartsrestaurant.Dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efficacious.e_smartsrestaurant.R;


public class TakeawayOrderSummeryFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_takeaway_order_summery, container, false);

        return view;
    }
}