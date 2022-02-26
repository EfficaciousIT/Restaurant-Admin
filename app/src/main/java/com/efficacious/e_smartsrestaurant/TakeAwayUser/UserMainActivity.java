package com.efficacious.e_smartsrestaurant.TakeAwayUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.efficacious.e_smartsrestaurant.Base.SettingFragment;
import com.efficacious.e_smartsrestaurant.Kitchen.KitchenHomeFragment;
import com.efficacious.e_smartsrestaurant.Menu.MenuCategoryFragment;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments.HistoryFragment;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments.UserHomeFragment;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments.UserOrderFragment;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserMainActivity extends AppCompatActivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new UserHomeFragment();
                        break;
                    case R.id.order:
                        fragment = new UserOrderFragment();
                        break;
                    case R.id.history:
                        fragment = new HistoryFragment();
                        break;
                    case R.id.profile:
                        fragment = new UserProfileFragment();
                        break;
                }

                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserHomeFragment()).commit();

    }
}