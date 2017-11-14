package com.dmitry.wordsdict.main.presenters;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dmitry.wordsdict.main.adapters.DividerItemDecoration;
import com.dmitry.wordsdict.main.adapters.RealmWordsListAdapter;
import com.dmitry.wordsdict.main.interactors.ListWordsInteractorImpl;
import com.dmitry.wordsdict.main.views.ListWordsView;

import io.realm.Realm;

/**
 * Created by dmitry on 6/3/17.
 */

public class ListWordsPresenterImpl implements ListWordsPresenter {

    private ListWordsInteractorImpl listWordsInteractorImpl;
    private ListWordsView listWordsView;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    private Realm mRealm;
    private static final int SPAN_COUNT = 2;
    private RealmWordsListAdapter mAdapter;

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    public ListWordsPresenterImpl(ListWordsView listWordsView, ListWordsInteractorImpl listWordsInteractorImpl) {
        this.listWordsView = listWordsView;
        this.listWordsInteractorImpl = listWordsInteractorImpl;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        listWordsView = null;
        mRealm.close();
    }

    @Override
    public void showError() {

    }

    @Override
    public void toggleSelectionMode() {
//        mAdapter.enableSeletionMode(!mAdapter.inSeletionMode);
    }

    private void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType, RecyclerView mRecyclerView, Activity activity) {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(activity, SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(activity);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(activity);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void setUpRecyclerView(Activity activity, RecyclerView recycler) {
        mLayoutManager = new LinearLayoutManager(activity);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType, recycler, activity);
        mAdapter = listWordsInteractorImpl.setUpListWordsAdapter(activity, mRealm);
        if (mAdapter.getData().size() == 0) {
            listWordsView.showError("Словарь пуст. Для добавления слов воспользуйтесь быстрым переводом");
        }
        recycler.setAdapter(mAdapter);
        recycler.setHasFixedSize(true);
    }

}
