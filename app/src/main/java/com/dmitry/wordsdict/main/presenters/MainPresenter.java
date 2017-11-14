package com.dmitry.wordsdict.main.presenters;

/**
 * Created by dmitry on 5/28/17.
 */

public interface MainPresenter {

    void onResume();

    void onTranslateItemClicked(String text);

    void onLearningButtonClicked();

    void onShowAllButtonClicked();

    void onDestroy();

}
