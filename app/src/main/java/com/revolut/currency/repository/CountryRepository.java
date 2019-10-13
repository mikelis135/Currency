package com.revolut.currency.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.revolut.currency.model.Country;
import com.revolut.currency.remote.Api;
import com.revolut.currency.remote.ApiConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryRepository {

    private static CountryRepository instance;
    private ArrayList<Country> dataSet = new ArrayList<>();
    private HashMap<String, String>  countryMap = new HashMap<>();
    private MutableLiveData<HashMap<String, String>> countryMutableLiveData = new MutableLiveData<>();
    private boolean isGotAmount = false;

    private MutableLiveData<List<Country>> currencyMutableLiveData;
    private static Api rateApi, countryApi;

    public static CountryRepository getInstance(){

        //singleton of Repository to prevent multiple instance of Repo
        if (instance == null){
            instance = new CountryRepository();
            countryApi = ApiConfig.getCountryService();
            rateApi = ApiConfig.getRateService();
        }
        return instance;
    }

     public MutableLiveData<List<Country>> getCountry(String country, final String amount){

         currencyMutableLiveData = new MutableLiveData<>();

         rateApi.getRates(country).enqueue(new Callback<Object>() {

             int id = 0;
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                    try {
                        Gson gson = new Gson();

                        String json = gson.toJson(response.body());
                        JSONObject messageMapJson = new JSONObject(json);
                        if (messageMapJson != null) {
                            Iterator<String> keys = messageMapJson.keys();
                            while (keys.hasNext()) {
                                String userId = keys.next();
                                Log.d("okh", "onResponse: "+ userId);
                                
                                JSONObject ratesJson = messageMapJson.optJSONObject(userId);
                                if (ratesJson == null) {
                                    continue;
                                }

                                Iterator<String> countries = ratesJson.keys();

                                while (countries.hasNext()) {
                                    List<String> rate  = new ArrayList<>();
                                    String countryTag = countries.next();
                                    double rateDouble = Double.valueOf(ratesJson.get(countryTag).toString());
                                    rate.add(String.format(Locale.UK, "%.2f", rateDouble));
                                    rate.add(String.format(Locale.UK, "%.2f", rateDouble));

                                    dataSet.add(new Country(id, countryTag,countryTag, rate));
                                    id++;

                                }

                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                currencyMutableLiveData.postValue(dataSet);

               getCountryNames(dataSet);

            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });


        return currencyMutableLiveData;
    }

    private void getCountryNames(final ArrayList arrayListCurrency) {

        countryApi.getCountries().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response != null) {
                    try {
                        Gson gson = new Gson();

                        String json = gson.toJson(response.body());
                        JSONObject messageMapJson = new JSONObject(json);
                        if (messageMapJson != null) {
                            Iterator<String> keys = messageMapJson.keys();
                            while (keys.hasNext()) {
                                String countryTag = keys.next();
                                JSONObject ratesJson = messageMapJson.optJSONObject(countryTag);
                                String country = messageMapJson.get(countryTag).toString();
                                countryMutableLiveData.postValue(countryMap);
                                for (int i = 0; i < arrayListCurrency.size(); i++) {
                                    String tag = dataSet.get(i).getCurrencyName().substring(0, 2);
                                    if (tag.equalsIgnoreCase(countryTag)) {
                                        dataSet.get(i).setCountryName(country);
                                    }
                                }

                                if (ratesJson == null) {
                                    continue;
                                }

                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    currencyMutableLiveData.postValue(dataSet);
                    countryMutableLiveData.postValue(countryMap);
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });

    }

    public MutableLiveData<List<Country>> getNewCurrency(String country, String amount){
        return getCountry(country, amount);
    }

    public MutableLiveData<List<Country>> setAmount(String countryTag, String amount){

//        for (int i = 0; i < dataSet.size(); i++) {
//            double newRate = Double.valueOf(dataSet.get(i).getRate().get(0)) * Double.valueOf(amount);
//            String amountString = String.format(Locale.UK, "%.2f", newRate) ;
//            rate.add(0, amountString);
//            dataSet.get(i).setRate(rate);
//
//        }

        currencyMutableLiveData = new MutableLiveData<>();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }


}
