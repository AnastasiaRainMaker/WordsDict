package com.dmitry.wordsdict.main.views;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
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

    private MenuActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MenuActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_words_list,
                container, false);
        RecyclerView recycler = view.findViewById(R.id.recycler_words_list);
        ListWordsPresenterImpl listWordsPresenterImpl = new ListWordsPresenterImpl(this,
                new ListWordsInteractorImpl());
        listWordsPresenterImpl.setUpRecyclerView(getActivity(), recycler);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        return view;
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
