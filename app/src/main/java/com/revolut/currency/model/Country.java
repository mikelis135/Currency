package com.revolut.currency.model;

import java.io.Serializable;
import java.util.List;

public class Country implements Serializable {

    private int id;
    private String currencyName;
    private String countryName;
    private List<String> rate;

    public Country(int id, String currencyName, String countryName, List<String> rate) {
        this.id = id;
        this.currencyName = currencyName;
        this.countryName = countryName;
        this.rate = rate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getRate() {
        return rate;
    }

    public void setRate(List<String> rate) {
        this.rate = rate;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
