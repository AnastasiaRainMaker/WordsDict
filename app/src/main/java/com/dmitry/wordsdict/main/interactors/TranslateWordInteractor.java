package com.dmitry.wordsdict.main.interactors;

import io.reactivex.disposables.Disposable;

/**
 * Created by dmitry on 5/28/17.
 */

public interface TranslateWordInteractor {

    Disposable translateWordRus (OnFinishedListener listener, String word);

    Disposable translateWordOffline(OnFinishedListener listener, String word);

    interface OnFinishedListener {
        void onTranslationFinished(String translation, String word, Boolean isAdvSearch);
        void onTranslationError(String text);
    }

    Disposable translateWord(OnFinishedListener listener, String word);

}
