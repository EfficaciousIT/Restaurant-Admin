package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAttendanceResponse {
    @SerializedName("GetAttendance")
    private List<GetAttendance> getAttendance = null;

    public List<GetAttendance> getGetAttendance() {
        return getAttendance;
    }

    public void setGetAttendance(List<GetAttendance> getAttendance) {
        this.getAttendance = getAttendance;
    }
}
