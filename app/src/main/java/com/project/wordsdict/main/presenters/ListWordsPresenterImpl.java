package com.project.wordsdict.main.presenters;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.project.wordsdict.main.adapters.DividerItemDecoration;
import com.project.wordsdict.main.adapters.RealmWordsListAdapter;
import com.project.wordsdict.main.interactors.ListWordsInteractorImpl;
import com.project.wordsdict.main.views.ListWordsView;

import io.realm.Realm;

public class ListWordsPresenterImpl implements ListWordsPresenter {

    private ListWordsInteractorImpl listWordsInteractorImpl;
    private RealmWordsListAdapter mAdapter;
    private ListWordsView listWordsView;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    private Realm mRealm;
    private static final int SPAN_COUNT = 2;

    private LayoutManagerType mCurrentLayoutManagerType;
    private RecyclerView.LayoutManager mLayoutManager;

    public ListWordsPresenterImpl(ListWordsView listWordsView, ListWordsInteractorImpl listWordsInteractorImpl) {
        this.listWordsView = listWordsView;
        this.listWordsInteractorImpl = listWordsInteractorImpl;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void showError() {

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

    @SuppressWarnings("ConstantConditions")
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

    public void finishMenu(){
        if (mAdapter != null) {
            mAdapter.menuFinish();
        }
    }

}
