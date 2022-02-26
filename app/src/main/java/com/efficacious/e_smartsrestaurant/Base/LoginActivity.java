package com.efficacious.e_smartsrestaurant.Base;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Kitchen.KitchenMainActivity;
import com.efficacious.e_smartsrestaurant.Model.LoginResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.LoginForTakeAwayActivity;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.UserMainActivity;
import com.efficacious.e_smartsrestaurant.WaiterRole.WaiterMainActivity;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Dialog dialog;
    SharedPrefManger sharedPrefManger;
    SharedPreferences sharedPreferences;
    CheckInternetConnection checkInternetConnection;
    ProgressDialog progressDialog;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkInternetConnection = new CheckInternetConnection(this);
        sharedPrefManger = new SharedPrefManger(getApplicationContext());
        sharedPreferences = this.getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
        progressDialog = new ProgressDialog(this);

        findViewById(R.id.btnCustomerLoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginForTakeAwayActivity.class));
            }
        });
        dialog = new Dialog(this);
        findViewById(R.id.btnRestaurantLoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.login_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                EditText username = dialog.findViewById(R.id.username);
                EditText password = dialog.findViewById(R.id.password);
                Button btnLogin = dialog.findViewById(R.id.btnLogin);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String UserName = username.getText().toString().trim();
                            String Password = password.getText().toString().trim();

                            if (userType==null){
                                Toast.makeText(getApplicationContext(), "Select role", Toast.LENGTH_SHORT).show();
                            }else if (UserName.isEmpty()){
                                username.setError("Username");
                            }else if (Password.isEmpty()){
                                password.setError("Password");
                            }else {
                                Call<LoginResponse> call = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .login("Login",UserName,Password);

                                call.enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                        LoginResponse loginResponse = response.body();
                                        int error = response.code();
                                        try {
                                            if (response.isSuccessful()){
                                                sharedPrefManger.saveUser(loginResponse.getLoginDetails());
                                                progressDialog.setMessage("Wait a moment..");
                                                progressDialog.setCanceledOnTouchOutside(false);
                                                progressDialog.show();
                                                selectActivity();
                                            }else if (error==500){
                                                Toast.makeText(getApplicationContext(),"Internal server error !!", Toast.LENGTH_SHORT).show();
                                            }
                                        }catch (Exception e){
                                            Toast.makeText(getApplicationContext(), "Invalid or Check credentials", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ImageView btnClose = dialog.findViewById(R.id.btnCancel);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                RadioButton manager = dialog.findViewById(R.id.manager);
                manager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.managerImg);
                        circleImageView.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.waiterImg);
                        circleImageView1.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.chefImg);
                        circleImageView2.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView3 = dialog.findViewById(R.id.question);
                        circleImageView3.setVisibility(View.INVISIBLE);
                        userType = "Manager";
                        sharedPrefManger.logout();
                    }
                });

                RadioButton waiter = dialog.findViewById(R.id.waiter);
                waiter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.managerImg);
                        circleImageView.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.waiterImg);
                        circleImageView1.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.chefImg);
                        circleImageView2.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView3 = dialog.findViewById(R.id.question);
                        circleImageView3.setVisibility(View.INVISIBLE);
                        userType = "Waitter";
                        sharedPrefManger.logout();
                    }
                });

                RadioButton kitchen = dialog.findViewById(R.id.kitchen);
                kitchen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.managerImg);
                        circleImageView.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.waiterImg);
                        circleImageView1.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.chefImg);
                        circleImageView2.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView3 = dialog.findViewById(R.id.question);
                        circleImageView3.setVisibility(View.INVISIBLE);
                        userType = "Kitchen";
                        sharedPrefManger.logout();
                    }
                });
            }
        });

    }

    private void selectActivity() {
        sharedPreferences = getApplicationContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        String role = sharedPreferences.getString(WebConstant.USER_TYPE,null);

        if (role.equalsIgnoreCase(userType)){
            if (role.equalsIgnoreCase("Manager")){
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

            }else if (role.equalsIgnoreCase("Waitter")){
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, WaiterMainActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
            }else if (role.equalsIgnoreCase("Kitchen")){
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, KitchenMainActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Invalid or Check credentials", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("No Internet Connection");
            alert.setMessage("Connect to WIFI or Turn on mobile data.");
            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alert.show();

        }else {
            if (sharedPrefManger.isLoggedIn()){
                sharedPreferences = getApplicationContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
                String role = sharedPreferences.getString(WebConstant.USER_TYPE,null);

                    if (role.equalsIgnoreCase("Manager")){
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

                    }else if (role.equalsIgnoreCase("Waitter")){
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, WaiterMainActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    }else if (role.equalsIgnoreCase("Kitchen")){
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, KitchenMainActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    }
            }else {
                if (sharedPreferences.getBoolean(WebConstant.LOGGED,false)){
                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                    finish();
                }
            }
        }

    }
}