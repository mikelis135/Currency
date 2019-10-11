package com.revolut.currency.remote;

public class ApiConfig {

    public static Api getRateService() {
        return RetrofitRateClient.getClient().create(Api.class);
    }

    public static Api getCountryService() {
        return RetrofitCountryClient.getClient().create(Api.class);
    }

}