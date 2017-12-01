package com.dmitry.wordsdict;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.dmitry.wordsdict.model.WordPair;
import com.dmitry.wordsdict.model.WordModelRealm;
import com.dmitry.wordsdict.rxbus.Events;
import com.dmitry.wordsdict.rxbus.RxEventBus;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;


public class App extends Application {

    private RxEventBus bus;
    public static RealmConfiguration realmConfig;
   // public static RealmConfiguration realmConfigDefault;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        realmConfig = new RealmConfiguration.Builder()
                .initialData( realm -> {
//                        realm.createObject(WordModelRealm.class);
                })
                .name("userWords.realm")
                .schemaVersion(8)
                .migration(new Migration())
                .modules(new MyWordModule())
                .build();

//        realmConfigDefault = new RealmConfiguration.Builder()
//                .assetFile("defaultWords.realm")
//                .name("defaultWords.realm")
//                .modules(new MyTranslationModule())
//                .schemaVersion(0)
//                .build();
        Realm.setDefaultConfiguration(realmConfig);
        initRxBus();

    }
    @RealmModule(classes = { WordModelRealm.class })
    private class MyWordModule {
    }
    @RealmModule(classes = { WordPair.class })
    public static class MyTranslationModule {
    }


    private void initRxBus() {
        bus = new RxEventBus();
        Log.d("before", "" + System.currentTimeMillis());
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                bus.send(new Events.Message("Hey I just took a nap, how are you!"));
            }
        }.start();
    }

    public RxEventBus bus() {
        return bus;
    }

}