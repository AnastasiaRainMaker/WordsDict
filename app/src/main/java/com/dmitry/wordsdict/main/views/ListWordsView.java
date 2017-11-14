package com.dmitry.wordsdict.main.views;

/**
 * Created by dmitry on 6/3/17.
 */

public interface ListWordsView {

    void setUpMenu();

    void showProgress();

    void hideProgress();

    void showError(String text);

}
