package com.project.wordsdict.model;

import io.realm.RealmObject;

public class WordPair extends RealmObject {

    public String getWordPair() {
        return wordPair;
    }

    public void setWordPair(String wordPair) {
        this.wordPair = wordPair;
    }

    private String wordPair;


}
