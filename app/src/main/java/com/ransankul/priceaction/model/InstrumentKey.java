package com.ransankul.priceaction.model;

public class InstrumentKey {

    private long id;

    private String stockName;
    private String tradingSymbol;
    private String instrumentKey;

    public InstrumentKey() {
    }

    public InstrumentKey(long id, String stockName, String tradingSymbol, String instrumentKey) {
        this.id = id;
        this.stockName = stockName;
        this.tradingSymbol = tradingSymbol;
        this.instrumentKey = instrumentKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getTradingSymbol() {
        return tradingSymbol;
    }

    public void setTradingSymbol(String tradingSymbol) {
        this.tradingSymbol = tradingSymbol;
    }

    public String getInstrumentKey() {
        return instrumentKey;
    }

    public void setInstrumentKey(String instrumentKey) {
        this.instrumentKey = instrumentKey;
    }
}
