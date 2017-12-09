package com.project.wordsdict;
import android.app.Application;
import com.project.wordsdict.model.WordPair;
import com.project.wordsdict.model.WordModelRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("userWords.realm")
                .schemaVersion(8)
                .migration(new Migration())
                .modules(new MyWordModule())
                .build();

        Realm.setDefaultConfiguration(realmConfig);

    }
    @RealmModule(classes = { WordModelRealm.class })
    private class MyWordModule {
    }
    @RealmModule(classes = { WordPair.class })
    public static class MyTranslationModule {
    }

}