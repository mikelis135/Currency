package com.revolut.currency.model;

public class Currency {

    private int id;
    private String currencyName;
    private String amount;
    private String rate;

    public Currency(int id, String name, String amount, String rate) {
        this.id = id;
        this.currencyName = name;
        this.amount = amount;
        this.rate = rate;
    }

    public String getName() {
        return currencyName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
