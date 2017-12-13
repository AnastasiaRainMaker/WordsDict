package com.project.wordsdict.main.presenters;

import com.project.wordsdict.main.interactors.SaveWordInteractorImpl;
import com.project.wordsdict.main.interactors.TranslateWordInteractor;
import com.project.wordsdict.main.views.MainView;
import io.reactivex.disposables.CompositeDisposable;

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
                mSubscriptions.add(translateWordInteractor.getHint(this, word));
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

    @Override
    public void onGetHintFinished(String hint, String word) {
        mainView.showHint(hint);

    }

    @Override
    public void onGetHintError(String text) {
        if (mainView != null) {
            mainView.showError(text);
            mainView.hideProgress();
        }
    }
}
