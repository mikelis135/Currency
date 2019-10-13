package com.revolut.currency.model;

public class EventMesage {

    private String countryTag;
    private String amount;

    public EventMesage(String countryTag, String amount) {
        this.countryTag = countryTag;
        this.amount = amount;
    }

    public String getCountryTag() {
        return countryTag;
    }

    public void setCountryTag(String countryTag) {
        this.countryTag = countryTag;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
