package com.revolut.currency.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.revolut.currency.model.Country;
import com.revolut.currency.repository.CountryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RateService extends Service {

    public final static String MY_ACTION = "MY_ACTION";
    private MutableLiveData<List<Country>> mutableLiveData;
    private CountryRepository countryRepository;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Timer timer = new Timer();

        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                mutableLiveData = null;
                mutableLiveData = getNewCurrencyMutableLiveData("EUR", "1");
                Intent intent = new Intent();
                intent.setAction(MY_ACTION);
                try {
                    Thread.sleep(2000);
                    if (mutableLiveData !=null ) {
                        if (mutableLiveData.getValue()!= null) {
                            intent.putExtra("DATAPASSED", new ArrayList<>(mutableLiveData.getValue()));
                            sendBroadcast(intent);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        timer.schedule(myTask, 0, 1000);

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }


    public MutableLiveData<List<Country>> getNewCurrencyMutableLiveData(String countryTag, String amount) {
        countryRepository = CountryRepository.getInstance();
       return countryRepository.getCountry(countryTag, amount);
    }


    @Override
    public void onCreate() {
        super.onCreate();
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
