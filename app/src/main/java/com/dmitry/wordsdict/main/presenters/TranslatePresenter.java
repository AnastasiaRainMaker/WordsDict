package com.dmitry.wordsdict.main.presenters;

/**
 * Created by dmitry on 5/28/17.
 */

public interface TranslatePresenter {

    void onResume();

    void onTranslateButtonClicked(String word);

    void onAdvancedTranslateButtonClicked(String word);

    void onDestroy();

}
