package com.dmitry.wordsdict.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class TranslationModelRealm extends RealmObject {

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public String getEngTranslation() {
        return engTranslation;
    }

    public void setEngTranslation(String engTranslation) {
        this.engTranslation = engTranslation;
    }

    public String getRusTranslation() {
        return rusTranslation;
    }

    public void setRusTranslation(String rusTranslation) {
        this.rusTranslation = rusTranslation;
    }

    @PrimaryKey
    private long wordId;

    private String engTranslation;

    private String rusTranslation;


}
