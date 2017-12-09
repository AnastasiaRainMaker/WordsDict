package com.project.wordsdict.main.presenters;

import android.content.Context;
import com.project.wordsdict.model.WordModelRealm;
import io.realm.RealmResults;

public interface LearningPresenter {

    RealmResults<WordModelRealm> getWords();

    void onDestroy();

    void showError();

    void loadNextRandomWord();

    void loadNextRandomWordFromAsset(Context context);

    boolean isTranslationCorrect(String userValue, String word);

    void showTranslation(String word, Context mContext);

    int upWord(String word);
}
