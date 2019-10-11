package com.revolut.currency.viewmodel;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.revolut.currency.model.Currency;
import com.revolut.currency.remote.RateService;
import com.revolut.currency.repository.CurrencyRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Currency>> currencyMutableLiveData;
    private CurrencyRepository currencyRepository;

    public void init() {
        if (currencyMutableLiveData == null) {
            currencyRepository = CurrencyRepository.getInstance();
            currencyMutableLiveData = currencyRepository.getCurrency("EUR", "10");
        }
        else{
            return;
        }
    }

    public LiveData<List<Currency>> getCurrencyMutableLiveData() {
        return currencyMutableLiveData;
    }

    public LiveData<List<Currency>> getNewCurrencyMutableLiveData(String country, String amount) {
        return currencyRepository.getNewCurrency(country, amount);
    }

    public LiveData<List<Currency>> setNewCurrencyMutableLiveData(String amount) {
        return currencyRepository.setAmount(amount);
    }
}
