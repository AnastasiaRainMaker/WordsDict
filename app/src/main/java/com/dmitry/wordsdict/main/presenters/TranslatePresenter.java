package com.dmitry.wordsdict.main.presenters;

public interface TranslatePresenter {

    void onTranslateButtonClicked(String word);

    void onAdvancedTranslateButtonClicked(String word);

    void onDestroy();

}
