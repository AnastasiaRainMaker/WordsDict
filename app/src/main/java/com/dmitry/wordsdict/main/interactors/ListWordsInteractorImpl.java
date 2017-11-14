package com.dmitry.wordsdict.main.interactors;

import android.app.Activity;

import com.dmitry.wordsdict.Constants;
import com.dmitry.wordsdict.main.adapters.RealmWordsListAdapter;
import com.dmitry.wordsdict.model.WordModelRealm;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by dmitry on 6/3/17.
 */

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
