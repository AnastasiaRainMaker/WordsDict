package com.project.wordsdict.main.views;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.project.wordsdict.R;
import com.project.wordsdict.main.interactors.ListWordsInteractorImpl;
import com.project.wordsdict.main.presenters.ListWordsPresenterImpl;

public class FragmentListWords extends Fragment implements ListWordsView {

    private MenuActivity mActivity;
    private ListWordsPresenterImpl listWordsPresenterImpl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MenuActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_words_list,
                container, false);
        RecyclerView recycler = view.findViewById(R.id.recycler_words_list);
        listWordsPresenterImpl = new ListWordsPresenterImpl(this,
                new ListWordsInteractorImpl());
        listWordsPresenterImpl.setUpRecyclerView(mActivity, recycler);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listWordsPresenterImpl.finishMenu();
    }

    @Override
    public void showError(String text) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.error_dialog);
        ((TextView) dialog.findViewById(R.id.dialog_info)).setText(text);
        Button okButton = dialog.findViewById(R.id.dialog_cancel);
        okButton.setOnClickListener(v-> dialog.dismiss());
        dialog.show();
    }
}
