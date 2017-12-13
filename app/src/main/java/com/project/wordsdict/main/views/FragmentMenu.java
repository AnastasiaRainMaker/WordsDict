package com.project.wordsdict.main.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.project.wordsdict.R;
import com.project.wordsdict.main.interactors.SaveWordInteractorImpl;
import com.project.wordsdict.main.presenters.TranslatePresenterImpl;
import com.project.wordsdict.main.interactors.TranslateWordInteractorImpl;

public class FragmentMenu extends Fragment implements MainView, View.OnClickListener {

    private TranslatePresenterImpl translatePresenter;
    private TextView translationEditText;
    private EditText wordEditText;
    private TextView wordTextView;
    private Button advancedTranslateButton;
    private ProgressDialog mProgressDialog;
    private String word;
    private MenuActivity mActivity;
    private Boolean advancedBtnShow = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MenuActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,
                container, false);

        initDialog();
        Button translateButton = view.findViewById(R.id.button_main_do_translate);
        translateButton.setOnClickListener(this);
        translationEditText = view.findViewById(R.id.trans_editText);
        wordEditText = view.findViewById(R.id.main_word_editText);
        wordTextView = view.findViewById(R.id.word_textview_hint);
        advancedTranslateButton = view.findViewById(R.id.advanced_search_button);
        advancedTranslateButton.setOnClickListener(this);
        advancedTranslateButton.setVisibility(View.GONE);
        translationEditText.setMovementMethod(new ScrollingMovementMethod());
        translatePresenter = new TranslatePresenterImpl(this,
                new TranslateWordInteractorImpl(getContext()),
                new SaveWordInteractorImpl());
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            advancedBtnShow = savedInstanceState.getBoolean("advancedBtnShow");
            word = savedInstanceState.getString("word");
            String translation = savedInstanceState.getString("translation");
            if (wordTextView != null) {
                wordTextView.setText(word);
            }
            if (translationEditText != null) {
                translationEditText.setText(translation);
            }
            if (advancedBtnShow) {
                advancedTranslateButton.setVisibility(View.VISIBLE);
            } else {
                advancedTranslateButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        translatePresenter.onDestroy();
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString("word", wordTextView.getText().toString());
        savedInstanceState.putString("translation", translationEditText.getText().toString());
        savedInstanceState.putBoolean("advancedBtnShow", advancedBtnShow);
        super.onSaveInstanceState(savedInstanceState);
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
        if (!isAdvSearch && !translation.equals(getResources().getString(R.string.entered_empty_str))) {
            advancedTranslateButton.setVisibility(View.VISIBLE);
            advancedBtnShow = true;
        }
        String textToSelect = word;
        translation = translation.replaceAll("\\w\\)", " ").replaceAll("[()|\\[\\]{}]", " ");
        translationEditText.setText(translation);

        // highlight the word
        int selTextIndex = translation.indexOf(textToSelect, 0);
        Spannable WordtoSpan = new SpannableString(translationEditText.getText());
        for (int ofs = 0; ofs < translation.length() && selTextIndex != -1; ofs = selTextIndex + 1) {
            selTextIndex = translation.indexOf(textToSelect, ofs);
            if (selTextIndex == -1)
                break;
            else {
                WordtoSpan.setSpan(
                        new BackgroundColorSpan(Color.parseColor("#FF4081")),
                        selTextIndex,
                        selTextIndex + textToSelect.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                wordTextView.setText(word);
                wordTextView.setTextSize(26);
                translationEditText.setLineSpacing(0.0f,1.0f);
                translationEditText.setText(WordtoSpan, TextView.BufferType.SPANNABLE);
                translationEditText.setBackgroundResource(R.color.background_main_color);
                translationEditText.setTextSize(19);

            }
        }
    }

    @Override
    public void showHint(String hint) {
        if (hint.length() > 0) {
            advancedTranslateButton.setVisibility(View.GONE);
            wordTextView.setText("Возможно Вы имели ввиду:");
            wordTextView.setTextSize(17);

            String[] arr = hint.split("; ");
            SpannableStringBuilder fullHint = new SpannableStringBuilder();
            for (String w : arr) {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        word = w;
                        translatePresenter.onAdvancedTranslateButtonClicked(word);
                        advancedBtnShow = false;
                    }
                };
                fullHint.append(w + ";" + "\r\r", clickableSpan, 0);
            }
            translationEditText.setText(fullHint);
            translationEditText.setTextSize(21);
            translationEditText.setLineSpacing(0.0f,1.8f);
            translationEditText.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            translationEditText.setText("Не найдено");
            advancedTranslateButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String text) {
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.error_dialog);
        ((TextView) dialog.findViewById(R.id.dialog_info)).setText(text);
        dialog.setTitle("Уведомление");
        Button okButton = dialog.findViewById(R.id.dialog_cancel);
        okButton.setOnClickListener(v-> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void hideKeyBoard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                translationEditText.setLineSpacing(0.0f,1.0f);
                translationEditText.setTextSize(21);
                if (wordTextView != null)
                wordTextView.setText(word);
                translationEditText.setBackgroundResource(R.color.white);
                wordTextView.setTextSize(26);
                wordEditText.setText("");
                break;
            }
            case R.id.advanced_search_button: {
                translatePresenter.onAdvancedTranslateButtonClicked(word);
                advancedBtnShow = false;
                if (wordTextView != null)
                    wordTextView.setText(word);
                wordEditText.setText("");
                break;
            }
        }
    }

}
