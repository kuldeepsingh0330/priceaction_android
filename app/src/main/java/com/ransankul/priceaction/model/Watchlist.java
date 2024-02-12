package com.ransankul.priceaction.model;

import androidx.annotation.Nullable;

public class Watchlist {

    private String id;
    private String stockName;
    private String stockExchangeName;
    private String currentPrice;
    private String changeInPrice;
    private String instrumentKey;

    public Watchlist() {
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Watchlist watchlist = (Watchlist) obj;
        return this.id.equals(watchlist.getId());
    }

    public Watchlist(String id, String stockName, String stockExchangeName, String currentPrice, String changeInPrice, String instrumentKey) {
        this.id = id;
        this.stockName = stockName;
        this.stockExchangeName = stockExchangeName;
        this.currentPrice = currentPrice;
        this.changeInPrice = changeInPrice;
        this.instrumentKey = instrumentKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockExchangeName() {
        return stockExchangeName;
    }

    public void setStockExchangeName(String stockExchangeName) {
        this.stockExchangeName = stockExchangeName;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getChangeInPrice() {
        return changeInPrice;
    }

    public void setChangeInPrice(String changeInPrice) {
        this.changeInPrice = changeInPrice;
    }


    public String getInstrumentKey() {
        return instrumentKey;
    }

    public void setInstrumentKey(String instrumentKey) {
        this.instrumentKey = instrumentKey;
    }

    @Override
    public String toString() {
        return "Watchlist{" +
                "id=" + id +
                ", stockName='" + stockName + '\'' +
                ", stockExchangeName='" + stockExchangeName + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", changeInPrice='" + changeInPrice + '\'' +
                '}';
    }
}
