package com.revolut.currency.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.revolut.currency.model.Country;
import com.revolut.currency.repository.CountryRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Country>> currencyMutableLiveData;
    private CountryRepository countryRepository;

    public void init() {
        if (currencyMutableLiveData == null) {
            countryRepository = CountryRepository.getInstance();
            currencyMutableLiveData = countryRepository.getCurrency("EUR", "10");
        }
        else{
            return;
        }
    }

    public LiveData<List<Country>> getCurrencyMutableLiveData() {
        return currencyMutableLiveData;
    }

    public LiveData<List<Country>> getNewCurrencyMutableLiveData(String country, String amount) {
        return countryRepository.getNewCurrency(country, amount);
    }

    public LiveData<List<Country>> setNewCurrencyMutableLiveData(String amount) {
        return countryRepository.setAmount(amount);
    }
}
