package com.project.wordsdict.main.presenters;

import android.content.Context;
import android.content.Intent;
import com.project.wordsdict.Constants;
import com.project.wordsdict.main.interactors.LearningInteractorImpl;
import com.project.wordsdict.main.views.LearningView;
import com.project.wordsdict.main.views.TranslationActivity;
import com.project.wordsdict.model.WordModelRealm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import io.realm.Realm;
import io.realm.RealmResults;

public class LearningPresenterImpl implements LearningPresenter {

    private LearningView learningView;
    private Realm mRealm;
    private RealmResults<WordModelRealm> resWords;
    private ArrayList<Integer> completedWords;
    private Random mRand;

    public LearningPresenterImpl(LearningView learningView, LearningInteractorImpl learningInteractorImpl) {
        this.learningView = learningView;
        mRealm = Realm.getDefaultInstance();
        resWords = learningInteractorImpl.findWords(mRealm);
        completedWords = new ArrayList<>();
        mRand = new Random();
    }

    @Override
    public RealmResults<WordModelRealm> getWords() {
        return resWords;
    }

    @Override
    public void onDestroy() {
        if (mRealm != null)
            mRealm.close();
    }

    @Override
    public void showError() {

    }

    @Override
    public void loadNextRandomWord() {
        if (completedWords.size() >= resWords.size()){
            learningView.setUpAllCompleted();
            return;
        }
        int randomIndex = mRand.nextInt((resWords.size()));
        if (isListContainsValue(completedWords, randomIndex)){
            loadNextRandomWord();
        } else {
            completedWords.add(randomIndex);
            WordModelRealm word = resWords.get(randomIndex);
            assert word != null;
            learningView.loadNextWord(
                    word.getWordName(),
                    "",
                    resWords.size(),
                    completedWords.size() - 1);
        }
    }

    @Override
    public void loadNextRandomWordFromAsset(Context context) {

        String word = "";
        String translation = "";
        Random rand = new Random();
        int target = rand.nextInt(4_000);
        int current = 0;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(Constants.ASSET_DICT_NAME), "Windows-1251"));
            String line = "";
            while (line != null){
                line = reader.readLine();
                if (current == target){
                    word = line.split(" - ")[0];
                    translation = line.split(" - ")[1];
                    break;
                } else {
                    current ++;
                }
            }
        } catch (Exception e) {
            learningView.showError("Ошибка чтения базы данных");
            e.printStackTrace();
        }

        learningView.loadNextWord(
                word,
                translation,
                resWords.size(),
                completedWords.size() - 1);

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isTranslationCorrect(String userValue, String word) {
        String correctTranslation = mRealm
                .where(WordModelRealm.class)
                .equalTo(Constants.REALM_WORD_NAME_KEY, word)
                .findFirst()
                .getWordTranslation();

        return truncateTranslation(correctTranslation).contains(userValue);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showTranslation(String word, Context mContext) {
        String translation = mRealm
                .where(WordModelRealm.class)
                .equalTo(Constants.REALM_WORD_NAME_KEY, word)
                .findFirst()
                .getWordTranslation();
        mContext.startActivity(new Intent(mContext, TranslationActivity.class)
                .putExtra("translation", translation)
                .putExtra("word", word));
    }

    @Override
    public int upWord(String word) {
        final int[] res = new int[1];
        mRealm.executeTransaction(realm -> {
            WordModelRealm rWord = realm
                    .where(WordModelRealm.class)
                    .equalTo(Constants.REALM_WORD_NAME_KEY, word)
                    .findFirst();
            assert rWord != null;
            res[0] = rWord.getWordPriority() + 1;
            rWord.setWordPriority(res[0]);
        });
        return res[0];
    }

    private String truncateTranslation(String value) {
        return value
                .replace(" ", "")
                .replace("oбщ", "");
    }

    private boolean isListContainsValue(ArrayList<Integer> completedWords, int randomIndex) {
        for ( Integer value: completedWords ) {
            if (value.equals(randomIndex))
                return true;
        }
        return false;
    }
}
