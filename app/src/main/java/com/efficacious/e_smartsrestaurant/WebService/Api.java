package com.efficacious.e_smartsrestaurant.WebService;

import com.efficacious.e_smartsrestaurant.Model.BillDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.BillUpdateDetails;
import com.efficacious.e_smartsrestaurant.Model.CategoryResponse;
import com.efficacious.e_smartsrestaurant.Model.CustomerDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.EmployeeDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.GetAttendanceResponse;
import com.efficacious.e_smartsrestaurant.Model.GetBillForTableResponse;
import com.efficacious.e_smartsrestaurant.Model.GetFCMTokenResponse;
import com.efficacious.e_smartsrestaurant.Model.GetTableReportResponse;
import com.efficacious.e_smartsrestaurant.Model.GetUserDetailResponse;
import com.efficacious.e_smartsrestaurant.Model.GetUserWiseTakeAwayOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.InsertAttendance;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.LoginResponse;
import com.efficacious.e_smartsrestaurant.Model.MenuResponse;
import com.efficacious.e_smartsrestaurant.Model.OrderDetails;
import com.efficacious.e_smartsrestaurant.Model.RegisterCustomerDetails;
import com.efficacious.e_smartsrestaurant.Model.RestaurantDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.TableStatusResponse;
import com.efficacious.e_smartsrestaurant.Model.TakeAwayOrderIdResponse;
import com.efficacious.e_smartsrestaurant.Model.TakeAwayOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderIdResponse;
import com.efficacious.e_smartsrestaurant.Model.UpdateStatusDetails;
import com.efficacious.e_smartsrestaurant.Model.UpdateToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api{
    @GET("Login")
    Call<LoginResponse>login(
            @Query("Command") String command,
            @Query("Username") String username,
            @Query("Password") String password
    );

    @GET("Category")
    Call<CategoryResponse> categories(
            @Query("Command") String command,
            @Query("Res_Id") String resId
    );

    @GET("Menu")
    Call<MenuResponse> getMenu(
            @Query("Command") String command,
            @Query("Cat_Id") String catId,
            @Query("Res_Id") String resId
    );

    @GET("TableStatus")
    Call<TableStatusResponse> getTableStatus(
            @Query("Command") String command,
            @Query("Res_Id") String ResId
    );

    @POST("TakeOrder")
    Call<ResponseBody> getOrder(
            @Query("Command") String command,
            @Body TakeOrderDetail takeOrderDetail
            );

    //get order id API
    @GET("TakeOrder")
    Call<TakeOrderIdResponse> getOrderId(
            @Query("Command") String command,
            @Query("ResId") String ResId
    );

    @POST("OrderDetails")
    Call<ResponseBody> getOrderDetails(
            @Query("Command") String command,
            @Body OrderDetails orderDetails
            );

    @GET("KitchenOrder")
    Call<KitchenNewOrderResponse> getKitchenNewOrder(
            @Query("Command") String command,
            @Query("ResId") String resId
    );

    @GET("ExistingOrder")
    Call<ExistingOrderResponse> getExistingOrder(
            @Query("Command") String command,
            @Query("ResId") String resId,
            @Query("EmployeeId") String employeeId
    );

    @GET("ExistingOrderDetails")
    Call<ExistingOrderDetailsResponse> getExistingOrderDetails(
            @Query("Command") String command,
            @Query("ResId") String resId,
//            @Query("EmployeeId") String employeeId,
            @Query("OrderId") String orderId
    );

    @POST("UpdateOrderStatus")
    Call<ResponseBody> updateStatus(
            @Query("Command") String command,
            @Body UpdateStatusDetails updateStatusDetails
            );

    @GET("TableBill")
    Call<GetBillForTableResponse> getTableBill(
            @Query("Command") String command,
            @Query("ResId") String resId
    );

    @GET("BillDetails")
    Call<BillDetailsResponse> getBillDetails(
            @Query("Command") String command,
            @Query("orderId") String orderId
    );

    @POST("CloseBill")
    Call<ResponseBody> updateBillDetails(
            @Query("Command") String command,
            @Body BillUpdateDetails billUpdateDetails
    );

    @GET("RestaurantDetails")
    Call<RestaurantDetailsResponse> getRestaurantDetails(
            @Query("Command") String command,
            @Query("ResId") String resId
    );

    @POST("CustomerRegister")
    Call<ResponseBody> registerUser(
            @Query("Command") String command,
            @Body RegisterCustomerDetails registerCustomerDetails
            );

    @GET("Customer")
    Call<CustomerDetailsResponse> getCustomerDetails(
      @Query("Command") String command,
      @Query("Res_id") String resId,
      @Query("MobileNo") String mobileNo
    );

    @GET("GetTakeAwayOrder")
    Call<TakeAwayOrderIdResponse> getTakeAwayOrderId(
            @Query("Command") String command,
            @Query("Res_Id") String resId,
            @Query("TimeStamp") String timeStamp
    );

    @GET("GetAllTakeAwayOrder")
    Call<TakeAwayOrderResponse> getAllTakeAwayOrder(
            @Query("Command") String command,
            @Query("Res_Id") String resId
    );

    @GET("TableReport")
    Call<GetTableReportResponse> getTableReport(
            @Query("Command") String command,
            @Query("Res_Id") String resId
    );

    @GET("Employee")
    Call<EmployeeDetailsResponse> getEmployDetails(
            @Query("Command") String command,
            @Query("Res_id") String resId
    );

    @POST("Attendance")
    Call<ResponseBody> insertAttendance(
            @Query("Command") String command,
            @Body InsertAttendance insertAttendance
    );

    @GET("GetAttendance")
    Call<GetAttendanceResponse> getAttendance(
            @Query("Command") String command,
            @Query("Res_id") String resId,
            @Query("Employee_Id") String empId
    );

    @GET("UserWiseTakeAwayOrder")
    Call<GetUserWiseTakeAwayOrderResponse> getOrderHistory(
            @Query("Command") String command,
            @Query("ResId") String ResId,
            @Query("RegisterId") String RegisterId
    );

    @POST("UpdateFCM")
    Call<ResponseBody> updateToken(
            @Query("Command") String command,
            @Body UpdateToken updateToken
    );

    @GET("GetFCM")
    Call<GetFCMTokenResponse> getFCMToken(
            @Query("Command") String command,
            @Query("Res_Id") String ResId,
            @Query("Status") String Status
    );

    @GET("Customer")
    Call<GetUserDetailResponse> getUserDetails(
            @Query("Command") String command,
            @Query("Res_id") String resId,
            @Query("MobileNo") String mobileNo
    );
}
