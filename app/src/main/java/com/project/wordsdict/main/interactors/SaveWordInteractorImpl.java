package com.project.wordsdict.main.interactors;

import com.project.wordsdict.Constants;
import com.project.wordsdict.model.WordModelRealm;
import java.util.Date;
import io.realm.Realm;
import io.realm.RealmObject;


public class SaveWordInteractorImpl implements SaveWordInteractor{

    private  <T extends RealmObject> int getRealmNextId(T realmObj) {
        Realm mRealm = Realm.getDefaultInstance();
        Number currentIdNum = mRealm.where(realmObj.getClass()).max(Constants.REALM_WORD_ID_KEY);
        mRealm.close();
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    @Override
    public void saveWordToDB(String translation, String word) {
        Realm mRealm = Realm.getDefaultInstance();
        if (mRealm.where(WordModelRealm.class).equalTo(Constants.REALM_WORD_NAME_KEY, word).count() == 0) {
            WordModelRealm rWord = new WordModelRealm();
            rWord.setWordName(word);
            rWord.setWordId(getRealmNextId(rWord));
            rWord.setCreationDate(new Date());
            rWord.setWordTranslation(translation);
            rWord.setWordFrequency(1);
            mRealm.executeTransaction(realm -> realm.insert(rWord));
        } else {
            mRealm.executeTransaction(realm -> {
                WordModelRealm riWord = realm.where(WordModelRealm.class).equalTo(Constants.REALM_WORD_NAME_KEY, word).findFirst();
                assert riWord != null;
                //riWord.setWordFrequency(riWord.getWordFrequency() + 1);
                riWord.setCreationDate(new Date());
                String oldTranslation = riWord.getWordTranslation();
                if (oldTranslation != null  && oldTranslation.length() < translation.length())
                    riWord.setWordTranslation(translation); // update translation if needed
            });
        }
    }
}
