package com.project.wordsdict.main.interactors;

import io.reactivex.disposables.Disposable;

public interface TranslateWordInteractor {

    Disposable translateWordRus (OnFinishedListener listener, String word);

    Disposable translateWordOffline(OnFinishedListener listener, String word);

    interface OnFinishedListener {
        void onTranslationFinished(String translation, String word, Boolean isAdvSearch);
        void onTranslationError(String text);
        void onGetHintFinished(String hint, String word);
        void onGetHintError(String text);
    }

    Disposable translateWord(OnFinishedListener listener, String word);

    Disposable getHint (OnFinishedListener listener, String word);

}
