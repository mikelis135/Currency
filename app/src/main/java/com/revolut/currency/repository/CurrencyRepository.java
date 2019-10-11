package com.revolut.currency.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.revolut.currency.model.Currency;
import com.revolut.currency.remote.RateApi;
import com.revolut.currency.remote.RateConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRepository {

    private static CurrencyRepository instance;
    public ArrayList<Currency> dataSet = new ArrayList<>();
    private MutableLiveData<List<Currency>> currencyMutableLiveData;
    private static RateApi rateApi;

    public static CurrencyRepository getInstance(){

        //singleton of Repository to prevent multiple instance of Repo
        if (instance == null){
            instance = new CurrencyRepository();
            rateApi = RateConfig.getService();
        }
        return instance;
    }

     public MutableLiveData<List<Currency>> getCurrency(String country, final String amount){

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

                                    String country = countries.next();
                                    double rateDouble = Double.valueOf(ratesJson.get(country).toString())/ Double.valueOf(amount);
                                    String rate = String.format(Locale.UK, "%.2f", rateDouble) ;
                                    id[0] = id[0] + 1;

                                    dataSet.add(new Currency(id[0], country, rate));
                                }

                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                currencyMutableLiveData.postValue(dataSet);

                }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });



        return currencyMutableLiveData;
    }

    public MutableLiveData<List<Currency>> getNewCurrency(String country, String amount){
        return getCurrency(country, amount);
    }

    public MutableLiveData<List<Currency>> setAmount(String amount){

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
