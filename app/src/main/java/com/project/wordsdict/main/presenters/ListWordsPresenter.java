package com.project.wordsdict.main.presenters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

public interface ListWordsPresenter {

    void showError();

    void setUpRecyclerView(Activity activity, RecyclerView recycler);
}
