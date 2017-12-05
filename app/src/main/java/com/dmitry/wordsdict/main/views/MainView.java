package com.dmitry.wordsdict.main.views;

public interface MainView {

    void showProgress();

    void hideProgress();

    void showTranslation(String translation, Boolean isAdvSearch);

    void showError(String text);

    void hideKeyBoard();

    void hideAdvancedButton();
}
