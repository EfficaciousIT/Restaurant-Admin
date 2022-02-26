package com.efficacious.e_smartsrestaurant.Base;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Attendance.EmployListFragment;
import com.efficacious.e_smartsrestaurant.Attendance.MarkAttendanceFragment;
import com.efficacious.e_smartsrestaurant.Billing.BillDetailFragment;
import com.efficacious.e_smartsrestaurant.Billing.BillTableStatusFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.DeliveryBoy.AddDeliveryBoyFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderFragment;
import com.efficacious.e_smartsrestaurant.Kitchen.KitchenHomeFragment;
import com.efficacious.e_smartsrestaurant.Menu.MenuCategoryFragment;
import com.efficacious.e_smartsrestaurant.Model.GetTableReport;
import com.efficacious.e_smartsrestaurant.Model.GetTableReportResponse;
import com.efficacious.e_smartsrestaurant.Model.UpdateToken;
import com.efficacious.e_smartsrestaurant.NewOrder.NewOrderFragment;
import com.efficacious.e_smartsrestaurant.NewOrder.TableStatusFragment;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.TableReport;
import com.efficacious.e_smartsrestaurant.Room.TableReportDatabase;
import com.efficacious.e_smartsrestaurant.TakeAway.TakeAwayFragment;
import com.efficacious.e_smartsrestaurant.TakeAway.TakeAwayOrderHistoryFragment;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Fragment selectFragment;
    SharedPrefManger sharedPrefManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefManger = new SharedPrefManger(getApplicationContext());

        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OPEN,R.string.CLOSE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = getApplicationContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        int EmployId = sharedPreferences.getInt(WebConstant.EMPLOYEE_ID,0);
        String FirstName = sharedPreferences.getString(WebConstant.FIRST_NAME,null);
        String LastName = sharedPreferences.getString(WebConstant.LAST_NAME,null);
        String Role = sharedPreferences.getString(WebConstant.USER_TYPE,null);

        View view = nav.inflateHeaderView(R.layout.header_layout);
        CircleImageView profile = view.findViewById(R.id.profileImg);
        TextView name = view.findViewById(R.id.userName);
        TextView role = view.findViewById(R.id.role);
        name.setText(FirstName + " " + LastName);
        role.setText(Role);
//        Picasso.get().load(ProfileUrl).into(profile);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        try {
                            UpdateToken updateToken = new UpdateToken(EmployId,token);
                            Call<ResponseBody> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .updateToken("update",updateToken);

                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        }catch (Exception e){}
                    }
                });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                removeColor(nav);

                item.setChecked(true);

                switch (item.getItemId()){
                    case R.id.dashBoard:
                        selectFragment = new DashboardFragment();
                        getSupportActionBar().setTitle("Dashboard");
                        item.setChecked(true);
                        break;
                    case R.id.newOrder:
                        selectFragment = new NewOrderFragment();
                        getSupportActionBar().setTitle("New Order");
                        break;
                    case R.id.existingOrder:
                        selectFragment = new ExistingOrderFragment();
                        getSupportActionBar().setTitle("Existing Order");
                        break;
                    case R.id.menu:
                        selectFragment = new MenuCategoryFragment();
                        getSupportActionBar().setTitle("Restaurant Menu");
                        break;
                    case R.id.bill:
                        selectFragment = new BillTableStatusFragment();
                        getSupportActionBar().setTitle("Bill");
                        break;
                    case R.id.tableStatus:
                        selectFragment = new TableStatusFragment();
                        getSupportActionBar().setTitle("Table Status");
                        break;
                    case R.id.takeAway:
                        selectFragment = new TakeAwayFragment();
                        getSupportActionBar().setTitle("Take Away");
                        break;
                    case R.id.attendance:
                        selectFragment = new EmployListFragment();
                        break;
                    case R.id.deliveryBoy:
                        selectFragment = new AddDeliveryBoyFragment();
                        break;
                    case R.id.setting:
                        selectFragment = new SettingFragment();
                        getSupportActionBar().setTitle("Setting");
                        break;
                    case R.id.logout:
                        logout();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).addToBackStack(null).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();


        //notification intent extra data
        String flag = getIntent().getStringExtra("flag");
//        if (!TextUtils.isEmpty(flag)){
//            Log.d(TAG, "flag : " + flag);
//            if (flag.equalsIgnoreCase(WebConstant.BILL)){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BillTableStatusFragment())
//                        .addToBackStack(null).commit();
//            }else if (flag.equalsIgnoreCase(WebConstant.WAITER_DISPATCH)){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExistingOrderFragment())
//                        .addToBackStack(null).commit();
//            }else if (flag.equalsIgnoreCase(WebConstant.TAKEAWAY_DISPATCH)){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BillTableStatusFragment())
//                        .addToBackStack(null).commit();
//            }else if (flag.equalsIgnoreCase(WebConstant.KITCHEN_NEW_ORDER)){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new KitchenHomeFragment())
//                        .addToBackStack(null).commit();
//            }else if (flag.equalsIgnoreCase(WebConstant.TAKEAWAY_HISTORY)){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TakeAwayOrderHistoryFragment())
//                        .addToBackStack(null).commit();
//            }else if (flag.equalsIgnoreCase(WebConstant.TAKEAWAY_NEW_ORDER)){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TakeAwayFragment())
//                        .addToBackStack(null).commit();
//            }
//        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPrefManger.logout();
                startActivity(new Intent(MainActivity.this,SplashScreenActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
        

    }


    private void removeColor(NavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }

}