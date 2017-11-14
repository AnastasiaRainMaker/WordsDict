package com.dmitry.wordsdict.main.interactors;

import android.app.Activity;
import android.content.Context;

import com.dmitry.wordsdict.main.adapters.RealmWordsListAdapter;

import io.realm.Realm;

/**
 * Created by dmitry on 6/3/17.
 */

public interface ListWordsInteractor {

    RealmWordsListAdapter setUpListWordsAdapter(Activity activity, Realm mRealm);

}
