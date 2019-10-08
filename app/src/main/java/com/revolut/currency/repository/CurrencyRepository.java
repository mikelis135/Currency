package com.revolut.currency.repository;

import androidx.lifecycle.MutableLiveData;

import com.revolut.currency.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {

    private static CurrencyRepository instance;
    private ArrayList<Currency> dataSet = new ArrayList<>();
    MutableLiveData<List<Currency>> currencyMutableLiveData;

    public static CurrencyRepository getInstance(){

        //singleton of Repository to prevent multiple instance of Repo
        if (instance == null){
            instance = new CurrencyRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Currency>> getCurrency(){
        currencyMutableLiveData  = new MutableLiveData<>();

        if (dataSet.size() != 0) {

            //will come here henceforth for consecutive data
            currencyMutableLiveData.setValue(dataSet);
            return currencyMutableLiveData;
        }

        //will come here for first getting default values from default data
        setCurrency();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }

    private void setCurrency() {
        //static data
        dataSet.add(new Currency(1, "Lagos", ""));
        dataSet.add(new Currency(2, "London", ""));
        dataSet.add(new Currency(3, "London", ""));
    }

    public MutableLiveData<List<Currency>> getNewCurrency(int id, String name, String surname){

        dataSet.add(new Currency(id, name, surname));

        //livedata object for setting new data
        currencyMutableLiveData = new MutableLiveData<>();
        currencyMutableLiveData.setValue(dataSet);
        return currencyMutableLiveData;
    }
}
