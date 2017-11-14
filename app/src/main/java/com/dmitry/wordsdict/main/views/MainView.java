package com.dmitry.wordsdict.main.views;

import android.view.View;

/**
 * Created by dmitry on 5/28/17.
 */

public interface MainView {

    void prepareView();

    void showProgress();

    void hideProgress();

    void showTranslation(String translation, Boolean isAdvSearch);

    void setUpLearning();

    void setUpShowAll();

    void setError();

    void showError(String text);

    void hideKeyBoard();

    void hideAdvancedButton();
}
