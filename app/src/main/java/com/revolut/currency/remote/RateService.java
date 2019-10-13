package com.revolut.currency.remote;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.revolut.currency.MainActivity;
import com.revolut.currency.model.Country;
import com.revolut.currency.model.EventMesage;
import com.revolut.currency.repository.CountryRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RateService extends Service {

    private CountryRepository countryRepository;
    Timer timer;
    String countryTag, amount;
    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onEvent(EventMesage eventMesage){
        countryTag = eventMesage.getCountryTag();
        amount = eventMesage.getAmount();
        Log.d("okh", "eventMessage: "+ eventMesage.getCountryTag() +" "+ eventMesage.getAmount());
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Timer timer = new Timer();

        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                countryTag = intent.getStringExtra("countryTag");
                amount = intent.getStringExtra("amount");
                Log.d("okh", "onStartCommand: " + countryTag + " " + amount);

                getNewCurrencyMutableLiveData(countryTag, amount);
            }
        };

        timer.schedule(myTask, 0, 1000);

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        EventBus.getDefault().register(this);
        context = this;
        super.onStart(intent, startId);
    }


    public MutableLiveData<List<Country>> getNewCurrencyMutableLiveData(String countryTag, String amount) {
        countryRepository = CountryRepository.getInstance();
       return countryRepository.getCountry(countryTag, amount);
    }

    @Override
    public void onDestroy() {
        if(timer !=null) {
            timer.cancel();
            timer = null;
        }
        stopService(new Intent(this, RateService.class));
        super.onDestroy();
    }

}
