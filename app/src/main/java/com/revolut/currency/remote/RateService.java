package com.revolut.currency.remote;

import com.revolut.currency.model.Rates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RateService {

    @GET("/latest")
    Call<Rates> getRates(@Query("base") String country);

}
