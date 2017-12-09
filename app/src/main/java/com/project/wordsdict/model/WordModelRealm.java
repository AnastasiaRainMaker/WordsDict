package com.project.wordsdict.model;

import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WordModelRealm extends RealmObject {

    @PrimaryKey
    private long wordId;

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordTranslation() {
        return wordTranslation;
    }

    public void setWordTranslation(String wordTranslation) {
        this.wordTranslation = wordTranslation;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getWordFrequency() {
        return wordFrequency;
    }

    public void setWordFrequency(int wordFrequency) {
        this.wordFrequency = wordFrequency;
    }

    private String wordName;

    private String wordTranslation;

    private Date creationDate;

    public boolean isSelected() {
        if (selected == null)
            return false;
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private Boolean selected = false;

    private int wordFrequency;

    public Integer getWordPriority() {
        if (wordPriority == null)
            return 0;
        return wordPriority;
    }

    public void setWordPriority(Integer wordPriority) {
        this.wordPriority = wordPriority;
    }

    private Integer wordPriority;

}
