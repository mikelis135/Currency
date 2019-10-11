package com.revolut.currency.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.revolut.currency.model.Currency;
import com.revolut.currency.repository.CurrencyRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RateService extends Service {

    private CurrencyRepository currencyRepository;
    public static String country;
    public static String amount;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                getNewCurrencyMutableLiveData();
            }
        };

        timer.schedule(myTask, 0, 1000);

        return START_NOT_STICKY;
    }

    public void getNewCurrencyMutableLiveData() {
        currencyRepository = CurrencyRepository.getInstance();
       currencyRepository.getNewCurrency(country, amount);
    }

    @Override
    public void onDestroy() {
        if(timer !=null) {
            timer.cancel();
        }
        stopService(new Intent(this, RateService.class));
        super.onDestroy();
    }

}
