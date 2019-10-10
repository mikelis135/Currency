package com.revolut.currency.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.revolut.currency.model.Countries;
import com.revolut.currency.model.Currency;
import com.revolut.currency.model.Rates;
import com.revolut.currency.remote.RateConfig;
import com.revolut.currency.remote.RateService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRepository {

    private static CurrencyRepository instance;
    public ArrayList<Currency> dataSet = new ArrayList<>();
    private MutableLiveData<List<Currency>> currencyMutableLiveData;
    private static RateService rateService;

    public static CurrencyRepository getInstance(){

        //singleton of Repository to prevent multiple instance of Repo
        if (instance == null){
            instance = new CurrencyRepository();
            rateService = RateConfig.getService();
        }
        return instance;
    }

     public void setRate(String country){

        rateService.getRates(country).enqueue(new Callback<Rates>() {
            @Override
            public void onResponse(Call<Rates> call, Response<Rates> response) {

//                if (response != null) {
//                    Rates rates = response.body();
//                    Log.d("okh", rates.toString());
//
//                    dataSet.set(new Currency(rates.getCountries().))
//                }

                    try {
                        Gson gson = new Gson();

                        String json = gson.toJson(response.body());
                        JSONObject messageMapJson = new JSONObject(json);
                        if (messageMapJson != null) {
                            Iterator<String> keys = messageMapJson.keys();
                            while (keys.hasNext()) {
                                String userId = keys.next();
                                Log.d("okh", "onResponse: "+ userId);
                                JSONObject eventToMessagesJson = messageMapJson.optJSONObject(userId);
                                if (eventToMessagesJson == null) {
                                    continue;
                                }
                                // Get the eventId --> message-list mapping
                                Map<String, List<String>> eventNameToMessages = new HashMap<String, List<String>>();
                                Iterator<String> eventNames = eventToMessagesJson.keys();
                                while (eventNames.hasNext()) {
                                    String eventName = eventNames.next();
                                    Log.d("okh",  eventToMessagesJson.get(eventName).toString());
                                    Log.d("okh", " "+ eventName);
                                }
                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                // Get subscriber event specs.


            @Override
            public void onFailure(Call<Rates> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });
    }

    public MutableLiveData<List<Currency>> getCurrency(){
        setRate("EUR");
        currencyMutableLiveData  = new MutableLiveData<>();

        if (dataSet.size() == 0) {
            setCurrency();
            currencyMutableLiveData.setValue(dataSet);
            return currencyMutableLiveData;
        }
        return currencyMutableLiveData;
    }

    public MutableLiveData<List<Currency>> getNewCurrency(int id, String name, String amount, String rate){

        dataSet.add(new Currency(id, name, amount, rate));

        //livedata object for setting new data
        currencyMutableLiveData = new MutableLiveData<>();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }

    public MutableLiveData<List<Currency>> setAmount(String amount){

        for (int i = 0; i < dataSet.size(); i++) {

            Double rate = Double.valueOf(dataSet.get(i).getRate());
            try {
                dataSet.get(i).setAmount(String.valueOf(Integer.valueOf(amount) * rate.intValue()));
            }catch (Exception e){
                dataSet.get(i).setAmount("");
            }

        }

        currencyMutableLiveData = new MutableLiveData<>();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }

    private void setCurrency() {
        dataSet.add(new Currency(1,  "Lagos", "1" , "1.0"));
        dataSet.add(new Currency(2, "London", "1", "2.0"));
        dataSet.add(new Currency(3, "London", "1", "3.0"));
    }

}
