package com.efficacious.e_smartsrestaurant.Model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LoginResponse {
    @SerializedName("LoginDetails")
    List<LoginDetail> loginDetails;

    public LoginResponse(List<LoginDetail> loginDetails) {
        this.loginDetails = loginDetails;
    }

    public List<LoginDetail> getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(List<LoginDetail> loginDetails) {
        this.loginDetails = loginDetails;
    }
}
