package com.project.wordsdict.main.interactors;

import com.project.wordsdict.model.WordModelRealm;

import io.realm.Realm;
import io.realm.RealmResults;


public interface LearningInteractor {

    RealmResults<WordModelRealm> findWords(Realm mRealm);

}
