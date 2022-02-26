package com.efficacious.e_smartsrestaurant.Menu;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Model.MenuCategoryDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuListFragment extends Fragment {

    List<MenuDetail> menuDetail;
    MenuAdapter menuAdapter;
    RecyclerView recyclerView;
    String CategoryId;
    CheckInternetConnection checkInternetConnection;
    EditText btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Restaurant Menu");
        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            CategoryId = getArguments().getString("CategoryId");
            menuDetail = new ArrayList<>();
            if (!CategoryId.isEmpty()){
                menuList(CategoryId);
            }
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        btnSearch = view.findViewById(R.id.search);

        btnSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString(),view);
            }
        });

        return view;
    }

    private void filter(String text, View view) {
        ImageView imageView = view.findViewById(R.id.empty);
        TextView textView = view.findViewById(R.id.emptyTxt);

        List<MenuDetail> temp = new ArrayList();

        for(MenuDetail menuCat: menuDetail){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(menuCat.getMenuName().contains(text)){
                temp.add(menuCat);
            }
        }
        //update recyclerview
        menuAdapter.updateList(temp);
        if (temp.size() == 0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    private void menuList(String categoryId) {
        try {
            Call<MenuResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getMenu("CategoryDrp",categoryId,"1");

            call.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.isSuccessful()){
                        menuDetail = response.body().getMenuDetails();
                        menuAdapter = new MenuAdapter(menuDetail);
                        recyclerView.setAdapter(menuAdapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}