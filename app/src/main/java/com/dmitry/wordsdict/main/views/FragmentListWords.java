package com.dmitry.wordsdict.main.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dmitry.wordsdict.R;
import com.dmitry.wordsdict.main.interactors.ListWordsInteractorImpl;
import com.dmitry.wordsdict.main.presenters.ListWordsPresenterImpl;

public class FragmentListWords extends Fragment implements ListWordsView {

    private RecyclerView recycler;
    private ListWordsPresenterImpl listWordsPresenterImpl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_words_list,
                container, false);
        recycler = view.findViewById(R.id.recycler_words_list);
        listWordsPresenterImpl = new ListWordsPresenterImpl(this,
                new ListWordsInteractorImpl());
        listWordsPresenterImpl.setUpRecyclerView(getActivity(), recycler);

        return view;
    }

    @Override
    public void setUpMenu() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String text) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.error_dialog);
        ((TextView) dialog.findViewById(R.id.dialog_info)).setText(text);
        Button okButton = dialog.findViewById(R.id.dialog_cancel);
        okButton.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();
    }

}
