package com.dmitry.wordsdict.main.presenters;

import android.content.Context;

import com.dmitry.wordsdict.model.WordModelRealm;

import io.realm.RealmResults;

/**
 * Created by dmitry on 6/11/17.
 */

public interface LearningPresenter {

    void onResume();

    RealmResults<WordModelRealm> getWords();

    void onDestroy();

    void showError();

    void loadNextRandomWord();

    void loadNextRandomWordFromAsset(Context context);

    boolean isTranslationCorrect(String userValue, String word);

    void showTranslation(String word, Context mContext);

    int upWord(String word);
}
