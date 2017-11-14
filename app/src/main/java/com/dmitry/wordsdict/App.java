package com.dmitry.wordsdict;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.dmitry.wordsdict.rxbus.Events;
import com.dmitry.wordsdict.rxbus.RxEventBus;
//import com.sibedge.aarlib.ExampleLibrary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dmitry on 6/1/17.
 */
public class App extends Application {

//    private NetComponent mNetComponent;
    private RxEventBus bus;

//    public void initExampleLibrary() {
//        ExampleLibrary instance = new ExampleLibrary
//                .addSomeTitle("title")
//                .addSomeMessage("message")
//                .build();
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .initialData( realm -> {
//                        realm.createObject(WordModelRealm.class);
                })
                .schemaVersion(8)
                .migration(new Migration())
                .build();
//        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);

//        mNetComponent = DaggerNetComponent.builder()
//                .appModule(new AppModule(this))
//                .netModule(new NetModule("http://www.multitran.ru/"))
////                .netModule(new NetModule())
//                .build();
        
        initRxBus();
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

//    public NetComponent getNetComponent() {
//        return mNetComponent;
//    }
}