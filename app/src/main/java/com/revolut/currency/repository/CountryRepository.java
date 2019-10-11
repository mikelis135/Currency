package com.revolut.currency.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.revolut.currency.model.Country;
import com.revolut.currency.remote.Api;
import com.revolut.currency.remote.ApiConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryRepository {

    private static CountryRepository instance;
    private ArrayList<Country> dataSet = new ArrayList<>();
    private HashMap<String, String>  countryMap = new HashMap<>();
    private MutableLiveData<HashMap<String, String>> countryMutableLiveData = new MutableLiveData<>();

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

     public MutableLiveData<List<Country>> getCurrency(String country, final String amount){

         final int[] id = {0};
         currencyMutableLiveData = new MutableLiveData<>();

         rateApi.getRates(country).enqueue(new Callback<Object>() {
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

                                    String countryTag = countries.next();
                                    double rateDouble = Double.valueOf(ratesJson.get(countryTag).toString())/ Double.valueOf(amount);
                                    String rate = String.format(Locale.UK, "%.2f", rateDouble) ;
                                    id[0] = id[0] + 1;

                                    dataSet.add(new Country(id[0], countryTag,countryTag, rate));
                                }

                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                currencyMutableLiveData.postValue(dataSet);

               loadJSONFromAsset(dataSet);

                }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });


        return currencyMutableLiveData;
    }

    private void loadJSONFromAsset(final ArrayList arrayListCurrency) {

        countryApi.getCountries().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                try {
                    Gson gson = new Gson();

                    String json = gson.toJson(response.body());
                    JSONObject messageMapJson = new JSONObject(json);
                    if (messageMapJson != null) {
                        Iterator<String> keys = messageMapJson.keys();
                        while (keys.hasNext()) {
                            String countryTag = keys.next();
                            JSONObject ratesJson = messageMapJson.optJSONObject(countryTag);
                            String country = messageMapJson.get(countryTag).toString() ;
                            Log.d("okh",  countryTag +  " : " + country);
                            countryMutableLiveData.postValue(countryMap);
                            for (int i = 0; i < arrayListCurrency.size() ; i++) {
                                if (dataSet.get(0).getCurrencyName().equalsIgnoreCase(countryTag)){
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

                countryMutableLiveData.postValue(countryMap);

            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });

    }

    public void setCountry(){
         final OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
//                    .url("http://www.geognos.com/api/en/countries/info/"+FR+".json")
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(response.body().toString());
                }

            });

    }

    public MutableLiveData<List<Country>> getNewCurrency(String country, String amount){
        return getCurrency(country, amount);
    }

    public MutableLiveData<List<Country>> setAmount(String amount){

        for (int i = 0; i < dataSet.size(); i++) {

            Double rate = Double.valueOf(dataSet.get(i).getRate());
            try {
                dataSet.get(i).setRate(String.valueOf(Integer.valueOf(amount) * rate.intValue()));
            }catch (Exception e){
                dataSet.get(i).setRate("");
            }

        }

        currencyMutableLiveData = new MutableLiveData<>();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }

}
