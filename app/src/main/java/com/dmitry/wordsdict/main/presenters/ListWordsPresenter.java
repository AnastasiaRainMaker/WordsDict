package com.dmitry.wordsdict.main.presenters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by dmitry on 6/3/17.
 */

public interface ListWordsPresenter {

    void onResume();

    void onDestroy();

    void showError();

    void toggleSelectionMode();

    void setUpRecyclerView(Activity activity, RecyclerView recycler);
}
