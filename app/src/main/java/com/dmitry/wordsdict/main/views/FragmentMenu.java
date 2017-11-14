package com.dmitry.wordsdict.main.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmitry.wordsdict.R;
import com.dmitry.wordsdict.main.interactors.SaveWordInteractorImpl;
import com.dmitry.wordsdict.main.presenters.TranslatePresenterImpl;
import com.dmitry.wordsdict.main.interactors.TranslateWordInteractorImpl;

public class FragmentMenu extends Fragment implements MainView, View.OnClickListener {

    private ProgressBar progressBar;
    private TranslatePresenterImpl translatePresenter;
    private EditText translationEditText;
    private EditText wordEditText;
    private TextView wordTextView;
    private Button translateButton;
    private Button advancedTranslateButton;
    private ProgressDialog mProgressDialog;
    private String word;

//    @Inject
//    Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,
                container, false);
//        initSongoModule();
//        ((App) getApplication()).getNetComponent().inject(this);
        initDialog();
        translateButton = view.findViewById(R.id.button_main_do_translate);
        translateButton.setOnClickListener(this);
        progressBar = view.findViewById(R.id.progress_bar_main);
        translationEditText = view.findViewById(R.id.trans_editText);
        wordEditText = view.findViewById(R.id.main_word_editText);
        wordTextView = view.findViewById(R.id.word_textview_hint);
        advancedTranslateButton = view.findViewById(R.id.advanced_search_button);
        advancedTranslateButton.setOnClickListener(this);
        advancedTranslateButton.setVisibility(View.GONE);
        translatePresenter = new TranslatePresenterImpl(this,
                new TranslateWordInteractorImpl(getContext()),
                new SaveWordInteractorImpl());

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            word = savedInstanceState.getString("word");
        }
    }
//
//    private void initSongoModule() {
//        Songo songo = new Songo();
//        songo.runWebView("http://www.apxadtracking.net/iclk/redirect.php?code=254025&id=eU4UeWGam3jMIWuXeUo5KWo5mzjMIWuXeUJneG-0N-0N", this);
//    }


    @Override
    public void onResume() {
        super.onResume();
        if (wordTextView != null)
            wordTextView.setText(word);
        translatePresenter.onResume();
    }

    @Override
    public void onDestroy() {
        translatePresenter.onDestroy();
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("word", word);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void prepareView() {

    }

    @Override public void showProgress() {
        mProgressDialog.setMessage("Выполняется перевод.");
        mProgressDialog.show();
    }

    @Override public void hideProgress() {
        mProgressDialog.cancel();
    }

    @Override
    public void showTranslation(String translation, Boolean isAdvSearch) {
        if (!isAdvSearch && !translation.equals(getResources().getString(R.string.entered_empty_str)))
            advancedTranslateButton.setVisibility(View.VISIBLE);
        String mainText = translation;
        String textToSelect = word;
        translationEditText.setText(mainText);

        // highlight the word
        int selTextIndex = mainText.indexOf(textToSelect, 0);
        Spannable WordtoSpan = new SpannableString( translationEditText.getText() );
        for(int ofs = 0; ofs < mainText.length() && selTextIndex != -1; ofs = selTextIndex + 1)
        {
            selTextIndex = mainText.indexOf(textToSelect, ofs);
            if(selTextIndex == -1)
                break;
            else
            {
                WordtoSpan.
                        setSpan(
                                new BackgroundColorSpan(Color.parseColor("#FF4081")),
                                selTextIndex,
                                selTextIndex+textToSelect.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                translationEditText
                        .setText(WordtoSpan, TextView.BufferType.SPANNABLE);
                translationEditText.setBackgroundResource(R.color.background_main_color);
//                translationEditText.getBackground()
//                        .setColorFilter(Color.parseColor("#C5CAE9"), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public void setUpLearning() {

    }

    @Override
    public void setUpShowAll() {

    }

    @Override
    public void setError() {

    }

    @Override
    public void showError(String text) {
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.error_dialog);
        ((TextView) dialog.findViewById(R.id.dialog_info)).setText(text);
        dialog.setTitle("Уведомление");
        Button okButton = dialog.findViewById(R.id.dialog_cancel);
        okButton.setOnClickListener(v-> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void hideKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void hideAdvancedButton() {
        advancedTranslateButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_main_do_translate: {
                word = String.format("%s", wordEditText.getText());
                translatePresenter.onTranslateButtonClicked(word);
                if (wordTextView != null)
                    wordTextView.setText(word);
                wordEditText.setText("");
                break;
            }
            case R.id.advanced_search_button: {
                translatePresenter.onAdvancedTranslateButtonClicked(word);
                if (wordTextView != null)
                    wordTextView.setText(word);
                wordEditText.setText("");
                break;
            }
        }
    }

}
