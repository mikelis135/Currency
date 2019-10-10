package com.revolut.currency.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.revolut.currency.model.Currency;
import com.revolut.currency.remote.RateConfig;
import com.revolut.currency.remote.RateService;

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

     public MutableLiveData<List<Currency>> getCurrency(){

         final int[] id = {0};

        rateService.getRates("EUR").enqueue(new Callback<Object>() {
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
                                JSONObject eventToMessagesJson = messageMapJson.optJSONObject(userId);
                                if (eventToMessagesJson == null) {
                                    continue;
                                }

                                Map<String, List<String>> eventNameToMessages = new HashMap<String, List<String>>();
                                Iterator<String> eventNames = eventToMessagesJson.keys();

                                while (eventNames.hasNext()) {

                                    String eventName = eventNames.next();
                                    id[0] = id[0] + 1;

                                    dataSet.add(new Currency(id[0], eventName, eventToMessagesJson.get(eventName).toString()));
                                }

                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("okh", "onResponse: "+ t.getMessage());
            }
        });

         currencyMutableLiveData = new MutableLiveData<>();
         currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }

//    public MutableLiveData<List<Currency>> getCurrency(){
//
//        currencyMutableLiveData  = new MutableLiveData<>();
//
//        if (dataSet.size() == 0) {
////            setCurrency();
//            setRate("EUR");
//            currencyMutableLiveData.setValue(dataSet);
//            return currencyMutableLiveData;
//        }
//        return currencyMutableLiveData;
//    }

    public MutableLiveData<List<Currency>> getNewCurrency(int id, String name, String rate){

        dataSet.add(new Currency(id, name, rate));

        //livedata object for setting new data
        currencyMutableLiveData = new MutableLiveData<>();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
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

    private void setCurrency() {
        dataSet.add(new Currency(1,  "Lagos",  "1.0"));
        dataSet.add(new Currency(2, "London",  "2.0"));
        dataSet.add(new Currency(3, "London",  "3.0"));
    }

}
