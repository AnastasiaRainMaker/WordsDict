package com.dmitry.wordsdict.main.views;

/**
 * Created by dmitry on 6/11/17.
 */

public interface LearningView {

    void prepareView();

    void showProgress();

    void hideProgress();

    void loadNextWord(String word, String translation, int countAll, int countDone);

    void setError();

    void showError(String text);

    void setUpAllCompleted();
}
