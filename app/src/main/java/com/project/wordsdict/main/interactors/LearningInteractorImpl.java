package com.project.wordsdict.main.interactors;

import com.project.wordsdict.Constants;
import com.project.wordsdict.model.WordModelRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public class LearningInteractorImpl implements LearningInteractor {
    @Override
    public RealmResults<WordModelRealm> findWords(Realm mRealm) {
        return mRealm.where(WordModelRealm.class)
                .notEqualTo(Constants.REALM_WORD_NAME_KEY, "")
                .findAll();
    }
}
