package com.project.wordsdict.main.views;

public interface LearningView {

    void prepareView();

    void loadNextWord(String word, String translation, int countAll, int countDone);

    void showError(String text);

    void setUpAllCompleted();
}
