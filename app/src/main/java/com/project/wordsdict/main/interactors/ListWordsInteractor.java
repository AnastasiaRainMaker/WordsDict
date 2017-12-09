package com.project.wordsdict.main.interactors;

import android.app.Activity;
import com.project.wordsdict.main.adapters.RealmWordsListAdapter;
import io.realm.Realm;

public interface ListWordsInteractor {

    RealmWordsListAdapter setUpListWordsAdapter(Activity activity, Realm mRealm);

}
