package com.dmitry.wordsdict.main.interactors;

import android.content.Context;
import android.util.Log;
import com.dmitry.wordsdict.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static com.dmitry.wordsdict.Constants.MULTITRAN_CSS_SELECTOR;
import static com.dmitry.wordsdict.Constants.MULTITRAN_URL_RUS;
import static com.dmitry.wordsdict.Constants.TAG;

public class TranslateWordInteractorImpl implements TranslateWordInteractor {

    private Context mContext;

    public TranslateWordInteractorImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override public Disposable translateWord (final OnFinishedListener listener, String word) {
        if (word.length() > 0) {
            return Single.fromCallable(()-> Jsoup.connect(String.format(MULTITRAN_URL_RUS, word)).get())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            res -> listener.onTranslationFinished(res.body().select(MULTITRAN_CSS_SELECTOR).text(), word, true),
                            throwable -> listener.onTranslationError(throwable.getMessage())
                    );
        } else {
            return Single.fromCallable(()-> "Введено пустое значение")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            res -> listener.onTranslationFinished(res, word, true),
                            throwable -> {
                                listener.onTranslationError("Сервер временно недоступен.");
                                Log.d(TAG, throwable.getMessage());
                            }
                    );
        }

    }

    // not used yet
    @Override public Disposable translateWordRus(final OnFinishedListener listener, String word) {
        return Single.fromCallable(()-> Jsoup.connect(String.format(MULTITRAN_URL_RUS, word)).get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            Element elemBody = res.body();
                            String translation;
                            translation = elemBody.select(MULTITRAN_CSS_SELECTOR).text();
                            listener.onTranslationFinished(translation, word, true);
                        },
                        throwable -> listener.onTranslationError(throwable.getMessage())
                );
    }

    @Override public Disposable translateWordOffline(final OnFinishedListener listener, String word) {

        return Single.fromCallable(()-> {
            if (word.length() == 0) {
                return "Введено пустое значение";
            }
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(mContext.getAssets().open(Constants.ASSET_DICT_NAME_BIG), "Windows-1251"));
                String line = reader.readLine();
                String[] ss;
                while (line != null){
                    line = reader.readLine();
                    if (line != null){
                        ss = line.split(" - ");
                    } else {
                        ss = null;
                    }
                    if (
                            (ss != null && ss.length > 1) &&
                                    ((ss[0].replace(" ", "").equals(word)) ||
                                            (ss[1].replace(" ", "").equals(word)))){
                        return line.replace(" - ", "").replace(word, "");
                    }
                }
                return "Не найдено";
            } catch (IOException e) {
                return "Не найдено";
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            listener.onTranslationFinished(res, word, false);
                        },
                        throwable -> listener.onTranslationError("Не найдено")
                );
    }

}
