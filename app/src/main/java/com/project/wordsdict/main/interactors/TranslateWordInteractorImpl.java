package com.project.wordsdict.main.interactors;

import android.content.Context;
import android.util.Log;
import com.project.wordsdict.App;
import com.project.wordsdict.model.WordPair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import static com.project.wordsdict.Constants.MULTITRAN_CSS_SELECTOR;
import static com.project.wordsdict.Constants.MULTITRAN_CSS_SELECTOR_HINT;
import static com.project.wordsdict.Constants.MULTITRAN_URL_RUS;
import static com.project.wordsdict.Constants.TAG;

public class TranslateWordInteractorImpl implements TranslateWordInteractor {

    private Realm mRealm;
    private RealmConfiguration realmConfigDefault = new RealmConfiguration.Builder()
            .assetFile("defaultWords.realm")
            .name("defaultWords.realm")
            .modules(new App.MyTranslationModule())
            .schemaVersion(0)
            .build();

    public TranslateWordInteractorImpl(Context mContext) {
    }

    @Override
    public Disposable translateWord (final OnFinishedListener listener, String word) {
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

    @Override
    public Disposable translateWordRus(final OnFinishedListener listener, String word) {
        return Single.fromCallable(()-> Jsoup.connect(String.format(MULTITRAN_URL_RUS, word)).get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            Element elemBody = res.body();
                            String translation = elemBody.select(MULTITRAN_CSS_SELECTOR).text();
                            listener.onTranslationFinished(translation, word, true);
                        },
                        throwable -> listener.onTranslationError(throwable.getMessage())
                );
    }

    @Override
    public Disposable getHint(final OnFinishedListener listener, String word) {
        if (word.length() > 0) {
            return Single.fromCallable(()-> Jsoup.connect(String.format(MULTITRAN_URL_RUS, word)).get())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            res -> {
                                Element elemBody = res.body();
                                String hint = elemBody.select(MULTITRAN_CSS_SELECTOR_HINT).text();
                                listener.onGetHintFinished(hint, word);
                            },
                            throwable -> listener.onGetHintError(throwable.getMessage())
                    );
        } else {
            return Single.fromCallable(()-> "Введено пустое значение")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            res -> listener.onGetHintFinished(res, word),
                            throwable -> {
                                listener.onGetHintError("Сервер временно недоступен.");
                                Log.d(TAG, throwable.getMessage());
                            }
                    );
        }
    }


    @Override
    public Disposable translateWordOffline(final OnFinishedListener listener, String word) {
        return Single.fromCallable(()-> {
            if (word.length() == 0) {
                return "Введено пустое значение";
            }
             try {
                 mRealm = Realm.getInstance(realmConfigDefault);
                 RealmResults<WordPair> result = mRealm.where(WordPair.class)
                                                      .contains("wordPair", word)
                                                      .findAll();
                 String res = "";
                 if (result.size() > 0){
                     for (WordPair resultItem: result) {
                         String wPair = resultItem.getWordPair();
                         String[] newWPair = wPair.split(" - ");
                         if (newWPair[0].replace(" ","").equals(word) && newWPair[0].replace(" ","").length() == word.length()){
                             res += newWPair[1].replace(" ","");
                             res += "\n";
                         } else if(newWPair[1].replace(" ","").equals(word) && newWPair[1].replace(" ","").length() == word.length()){
                             res += newWPair[0].replace(" ","");
                             res += "\n";
                         }
                     }
                 }
                 return res.substring(0, res.length() - 1);
            } catch (Exception e) {
                return "Не найдено";
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> listener.onTranslationFinished(res, word, false),
                        throwable -> listener.onTranslationError("Не найдено")
                );
    }

}
