package com.revolut.currency.remote;

public class RateConfig {

    private static final String BASE_URL = "https://revolut.duckdns.org/";

    public static RateService getService() {
        return RetrofitClient.getClient(BASE_URL).create(RateService.class);
    }

}
