package com.project.wordsdict.main.interactors;

import android.app.Activity;

import com.project.wordsdict.Constants;
import com.project.wordsdict.main.adapters.RealmWordsListAdapter;
import com.project.wordsdict.model.WordModelRealm;
import io.realm.Realm;
import io.realm.Sort;

public class ListWordsInteractorImpl implements ListWordsInteractor {
    @Override
    public RealmWordsListAdapter setUpListWordsAdapter(Activity activity, Realm mRealm) {
        return new RealmWordsListAdapter(
                mRealm.where(WordModelRealm.class)
                        .notEqualTo(Constants.REALM_WORD_NAME_KEY, "")
                        .findAll().sort(
                                Constants.REALM_WORD_PRIORITY_KEY, Sort.DESCENDING,
                                Constants.REALM_WORD_DATE_KEY, Sort.DESCENDING),
                activity, mRealm);
    }
}
