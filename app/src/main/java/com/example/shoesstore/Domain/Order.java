package com.example.shoesstore.Domain;

public class Order {
    private String orderId;
    private String gmail;
    private String orderDate;
    private String status;
    private double totalAmount;
    private String address;
    private  String phoneNumber;

    // Constructors, getters, and setters
    public Order() {}



    public Order(String orderId, String gmail, String orderDate, String status, double totalAmount, String address, String phoneNumber) {
        this.orderId = orderId;
        this.gmail = gmail;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.address = address;
        this.phoneNumber=phoneNumber;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String userId) {
        this.gmail = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
