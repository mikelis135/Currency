package com.revolut.currency.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.revolut.currency.model.Currency;
import com.revolut.currency.repository.CurrencyRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Currency>> currencyMutableLiveData;
    private CurrencyRepository currencyRepository;

    public void init() {
        if (currencyMutableLiveData == null) {
            currencyRepository = CurrencyRepository.getInstance();
            currencyMutableLiveData = currencyRepository.getCurrency();
        }
    }

    public LiveData<List<Currency>> getCurrencyMutableLiveData() {
        return currencyMutableLiveData;
    }

    public LiveData<List<Currency>> getNewCurrencyMutableLiveData(Currency Currency) {
        return currencyRepository.getNewCurrency(Currency.getId(), Currency.getName(), Currency.getAmount(), Currency.getRate());
    }

    public LiveData<List<Currency>> setNewCurrencyMutableLiveData(String amount) {
        return currencyRepository.setAmount(amount);
    }
}
