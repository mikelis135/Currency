package com.revolut.currency.remote;

public class RateConfig {

    private static final String BASE_URL = "https://revolut.duckdns.org/";
    private static final String BASE_COUNTRY = "EUR";

    public static RateApi getService() {
        return RetrofitClient.getClient(BASE_URL).create(RateApi.class);
    }

}
