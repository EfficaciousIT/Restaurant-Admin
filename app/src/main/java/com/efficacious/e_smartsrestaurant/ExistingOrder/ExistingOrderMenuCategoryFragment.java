package com.efficacious.e_smartsrestaurant.ExistingOrder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
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
import com.efficacious.e_smartsrestaurant.Menu.MenuCategoryAdapter;
import com.efficacious.e_smartsrestaurant.Model.CategoryResponse;
import com.efficacious.e_smartsrestaurant.Model.MenuCategoryDetail;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistingOrderMenuCategoryFragment extends Fragment {

    List<MenuCategoryDetail> menuCategoryDetails;
    ExistingOrderMenuCategoryAdapter adapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    EditText btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_existing_order_menu_category, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Existing Order");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Choose category");

        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            menuCategoryDetails = new ArrayList<>();
            categoryList();
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

        List<MenuCategoryDetail> temp = new ArrayList();

        for(MenuCategoryDetail menuCat: menuCategoryDetails){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(menuCat.getCatName().contains(text)){
                temp.add(menuCat);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
        if (temp.size() == 0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("Order Cancel")
                        .setMessage("Are you sure to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences = getActivity().getSharedPreferences(WebConstant.ORDER_ID_DATA_SHARED_PREF,0);
                                editor = sharedPreferences.edit();
                                editor.clear();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExistingOrderFragment())
                                        .disallowAddToBackStack().commit();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void categoryList() {
        try {
            Call<CategoryResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .categories("Category","1");

            call.enqueue(new Callback<CategoryResponse>() {
                @Override
                public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                    if (response.isSuccessful()){
                        menuCategoryDetails = response.body().getMenuCategoryDetails();
                        adapter = new ExistingOrderMenuCategoryAdapter(menuCategoryDetails);
                        recyclerView.setAdapter(adapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CategoryResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}