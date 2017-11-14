package com.dmitry.wordsdict.main.presenters;

import com.dmitry.wordsdict.main.interactors.SaveWordInteractorImpl;
import com.dmitry.wordsdict.main.interactors.TranslateWordInteractor;
import com.dmitry.wordsdict.main.views.MainView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dmitry on 5/28/17.
 */

public class TranslatePresenterImpl implements TranslatePresenter, TranslateWordInteractor.OnFinishedListener {

    private MainView mainView;
    private TranslateWordInteractor translateWordInteractor;
    private SaveWordInteractorImpl saveWordInteractor;
    private CompositeDisposable mSubscriptions;

    public TranslatePresenterImpl(MainView mainView, TranslateWordInteractor translateWordInteractor, SaveWordInteractorImpl saveWordInteractor) {
        this.mainView = mainView;
        this.translateWordInteractor = translateWordInteractor;
        this.saveWordInteractor = saveWordInteractor;
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void onResume() {
//        if (mainView != null) {
//            mainView.showProgress();
//        }
    }

    @Override
    public void onTranslateButtonClicked(String word) {
        if (mainView != null) {
            mainView.showProgress();
            if (word.length() > 0){
                mSubscriptions.add(translateWordInteractor.translateWordOffline(this, word));
            } else {
                onTranslationFinished("", "", false);
            }
            mainView.hideKeyBoard();
        }
    }

    @Override
    public void onAdvancedTranslateButtonClicked(String word) {
        if (mainView != null) {
            mainView.showProgress();
            mSubscriptions.add(translateWordInteractor.translateWord(this, word));
            mainView.hideKeyBoard();
            mainView.hideAdvancedButton();
        }
    }

    @Override
    public void onDestroy() {
        mainView = null;
        if (mSubscriptions != null && !mSubscriptions.isDisposed())
            mSubscriptions.dispose();
    }

    public MainView getMainView() {
        return mainView;
    }

    @Override
    public void onTranslationFinished(String translation, String word, Boolean isAdvSearch) {
        if (mainView != null) {
            mainView.hideProgress();
            if (translation.length() > 0 && word.length() > 0) {
                if (!translation.equals("Нет перевода") && !translation.equals("Не найдено")){
                    saveWordInteractor.saveWordToDB(translation, word);
                }
                mainView.showTranslation(translation, isAdvSearch);
            } else {
                mainView.showTranslation("Нет перевода", isAdvSearch);
            }
        }
    }

    @Override
    public void onTranslationError(String text) {
        if (mainView != null) {
            mainView.showError(text);
            mainView.hideProgress();
        }
    }
}
