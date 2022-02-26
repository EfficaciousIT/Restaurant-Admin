package com.efficacious.e_smartsrestaurant.Model;

public class TableData {

    int tableImg;
    String tableNo;
    int peopleCount;

    public TableData(TableData[] tableData) {
    }

    public TableData(int tableImg, String tableNo, int peopleCount) {
        this.tableImg = tableImg;
        this.tableNo = tableNo;
        this.peopleCount = peopleCount;
    }

    public int getTableImg() {
        return tableImg;
    }

    public void setTableImg(int tableImg) {
        this.tableImg = tableImg;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }
}
