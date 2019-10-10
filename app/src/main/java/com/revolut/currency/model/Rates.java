
package com.revolut.currency.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rates {

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("rates")
    @Expose
    private Countries countries;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Countries getCountries() {
        return countries;
    }

    public void setCountries(Countries countries) {
        this.countries = countries;
    }

    String ratesToString(){
        return "";
    }

}
