package com.revolut.currency.repository;

import androidx.lifecycle.MutableLiveData;

import com.revolut.currency.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {

    private static CurrencyRepository instance;
    public ArrayList<Currency> dataSet = new ArrayList<>();
    private MutableLiveData<List<Currency>> currencyMutableLiveData;

    public static CurrencyRepository getInstance(){

        //singleton of Repository to prevent multiple instance of Repo
        if (instance == null){
            instance = new CurrencyRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Currency>> getCurrency(){
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
        dataSet.add(new Currency(1, "Lagos", "1" , "1.0"));
        dataSet.add(new Currency(2, "London", "1", "2.0"));
        dataSet.add(new Currency(3, "London", "1", "3.0"));
    }

}
