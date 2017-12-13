package com.project.wordsdict.main.views;

public interface MainView {

    void showProgress();

    void hideProgress();

    void showTranslation(String translation, Boolean isAdvSearch);

    void showHint(String hint);

    void showError(String text);

    void hideKeyBoard();

    void hideAdvancedButton();
}
