package com.dmitry.wordsdict.main.interactors;

import com.dmitry.wordsdict.model.WordModelRealm;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dmitry on 6/11/17.
 */

public interface LearningInteractor {

    RealmResults<WordModelRealm> findWords(Realm mRealm);

}
