package com.ransankul.priceaction.model;

import java.util.Date;

public class OrderHistory {

    private Long id;

    private String stockSymbol;
    private String orderStatus;
    private Date orderTime;
    private String orderType;
    private String platform;
    private int quantity;
    private String userName;

    public OrderHistory() {
    }

    public OrderHistory(Long id, String stockSymbol, String orderStatus, Date orderTime,
                        String orderType, String platform, int quantity, String userName) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.platform = platform;
        this.quantity = quantity;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
