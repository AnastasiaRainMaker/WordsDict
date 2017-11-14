package com.dmitry.wordsdict.main.interactors;

import com.dmitry.wordsdict.Constants;
import com.dmitry.wordsdict.model.WordModelRealm;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by dmitry on 6/11/17.
 */

public class LearningInteractorImpl implements LearningInteractor {
    @Override
    public RealmResults<WordModelRealm> findWords(Realm mRealm) {
        return mRealm.where(WordModelRealm.class)
                .notEqualTo(Constants.REALM_WORD_NAME_KEY, "")
                .findAll();
    }
}
