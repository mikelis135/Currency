package com.revolut.currency.model;

public class Currency {

    private int id;
    private String currencyName;
    private String rate;

    public Currency(int id, String name,String rate) {
        this.id = id;
        this.currencyName = name;
        this.rate = rate;
    }

    public String getName() {
        return currencyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
