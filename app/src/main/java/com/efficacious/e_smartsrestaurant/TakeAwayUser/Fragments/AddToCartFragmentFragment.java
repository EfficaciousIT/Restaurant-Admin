package com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuData;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.Room.TableBookDatabase;
import com.efficacious.e_smartsrestaurant.Room.TakeAwayDatabase;
import com.efficacious.e_smartsrestaurant.Room.TakeAwayOrder;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

public class AddToCartFragmentFragment extends Fragment {

    String MenuName,Price,Category;
    int ResId;
    TextView foodNameTxt,foodPriceTxt;
    TextView btnAdd;
    ImageView btnMinus;
    TakeAwayDatabase takeAwayDatabase;
    MenuDatabase menuDatabase;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_cart_fragment, container, false);

        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);

        setUpDB();
        Bundle bundle = this.getArguments();

        if (bundle != null){
            MenuName = bundle.getString("MenuName");
            Price = bundle.getString("Price");
            Category = bundle.getString("Category");
            ResId = bundle.getInt("ResId");
        }

        btnAdd = view.findViewById(R.id.btnAdd);
        btnMinus = view.findViewById(R.id.btnMinus);
        foodNameTxt = view.findViewById(R.id.foodName);
        foodPriceTxt = view.findViewById(R.id.foodPrice);
        foodPriceTxt.setText(Price);
        foodNameTxt.setText(MenuName);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(btnAdd.getText().toString());
                int price = Integer.parseInt(Price);
                count++;
                int totalPrice = count*price;

                btnAdd.setText(String.valueOf(count));
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(btnAdd.getText().toString());
                if (count>1){
                    int price = Integer.parseInt(Price);
                    count--;
                    int totalPrice = count*price;
                    btnAdd.setText(String.valueOf(count));
                  }
            }
        });

        view.findViewById(R.id.btnAddToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qyt = Integer.parseInt(btnAdd.getText().toString());
                int RegisterId = sharedPreferences.getInt("RegisterId",0);

                MenuData menuData = new MenuData(0,Category,
                        MenuName,WebConstant.TAKEAWAY,RegisterId,0,Integer.valueOf(Price),qyt
                        ,null,WebConstant.TAKEAWAY, WebConstant.TAKEAWAY);
                menuDatabase.dao().menuDataList(menuData);
                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserHomeFragment())
                        .disallowAddToBackStack().commit();

            }
        });

        return view;
    }

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }
}