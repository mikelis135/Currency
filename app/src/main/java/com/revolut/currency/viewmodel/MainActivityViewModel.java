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

    public LiveData<List<Country>> init() {
        if (currencyMutableLiveData == null) {
            countryRepository = CountryRepository.getInstance();
            currencyMutableLiveData = countryRepository.getCountry("EUR", "10");
            return  currencyMutableLiveData;
        }
        else{
            return currencyMutableLiveData;
        }
    }

    public LiveData<List<Country>> getCurrencyMutableLiveData(String countryTag, String amount) {
        currencyMutableLiveData = countryRepository.getCountry(countryTag, amount);
        return currencyMutableLiveData;
    }

}
