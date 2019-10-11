package com.revolut.currency.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("/latest")
    Call<Object> getRates(@Query("base") String country);

    @GET("/names.json")
    Call<Object> getCountries();


}
