package com.dmitry.wordsdict.main.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dmitry.wordsdict.R;
import com.dmitry.wordsdict.main.interactors.LearningInteractorImpl;
import com.dmitry.wordsdict.main.interactors.SaveWordInteractorImpl;
import com.dmitry.wordsdict.main.presenters.LearningPresenter;
import com.dmitry.wordsdict.main.presenters.LearningPresenterImpl;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class LearningActivity extends AppCompatActivity implements LearningView {

    private LearningPresenter learningPresenter;
    private TextView wordTextView;
    private TextView doneXfromYTextView;
    private TextView TextViewHint;
    private EditText translationEditText;
    private int taskType;
    private SaveWordInteractorImpl saveWordInteractor;
    private String translation;
    BottomNavigationViewEx bottomNavigationView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_learning);
        taskType = getIntent().getIntExtra("type", 0);
        learningPresenter = new LearningPresenterImpl(this,
                new LearningInteractorImpl());
        bottomNavigationView = findViewById(R.id.bottom_navigation_learning);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            itemView.setChecked(false);
            bottomNavigationView.enableAnimation(false);
            bottomNavigationView.enableShiftingMode(false);
            bottomNavigationView.enableItemShiftingMode(false);
        }
        applyBottomNavFont();
        wordTextView = findViewById(R.id.learning_word_textview);
        translationEditText = findViewById(R.id.learning_translationedittext);
        doneXfromYTextView = findViewById(R.id.learning_donexfromy_textview);
        TextViewHint = findViewById(R.id.learning_textView_hint);
        if (taskType == 1){
            doneXfromYTextView.setVisibility(View.GONE);
            Menu menu = bottomNavigationView.getMenu();
            MenuItem upWordItem = menu.findItem(R.id.learning_up_word_item);
            upWordItem.setTitle("Добавить в мой словарь");
            upWordItem.setOnMenuItemClickListener(item -> {
                setUpAddToDictButton();
                saveWordInteractor = new SaveWordInteractorImpl();
                return true;
            });

        } else {
            Menu menu = bottomNavigationView.getMenu();
            MenuItem upWordItem = menu.findItem(R.id.learning_up_word_item);
            upWordItem.setOnMenuItemClickListener(item -> {
                setUpUpWordButton();
                return true;
            });
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    if (item.getItemId() == R.id.learning_show_translation_item) {
                        setUpHintButton();
                    } else if (item.getItemId() == R.id.learning_skip_item){
                        setUpSkipButton();
                    }  else if (item.getItemId() == R.id.learning_check_item) {
                        setUpCheckButton();
                    } else {
                        setUpUpWordButton();
                    }
                    return true;
                });


        prepareView();
    }

    private void applyBottomNavFont() {
        // The BottomNavigationView widget doesn't provide a native way to set the appearance of
        // the text views. So we have to hack in to the view hierarchy here.
        for (int i = 0; i < bottomNavigationView.getChildCount(); i++) {
            View child = bottomNavigationView.getChildAt(i);
            if (child instanceof BottomNavigationMenuView) {
                BottomNavigationMenuView menu = (BottomNavigationMenuView) child;
                for (int j = 0; j < menu.getChildCount(); j++) {
                    View item = menu.getChildAt(j);
                    View smallItemText = item.findViewById(android.support.design.R.id.smallLabel);
                    if (smallItemText instanceof TextView) {
                        ((TextView) smallItemText).setTextAppearance(getApplicationContext(), R.style.BottomNavTextAppearance);
                    }
                    View largeItemText = item.findViewById(android.support.design.R.id.largeLabel);
                    if (largeItemText instanceof TextView) {
                        ((TextView) largeItemText).setTextAppearance(getApplicationContext(), R.style.BottomNavTextAppearance);
                    }
                }
            }
        }
    }
    private void setUpAddToDictButton() {
        String word = wordTextView.getText() != null ? wordTextView.getText().toString() : "";
        final Dialog dialog = new Dialog(LearningActivity.this);
        dialog.setContentView(R.layout.add_word_dialog);
        ((TextView) dialog.findViewById(R.id.dialog_info)).setText(String.format("Введите перевод для %s", word));
        Button okButton = dialog.findViewById(R.id.dialog_add_word_ok);
        okButton.setOnClickListener(v->{
            EditText et = dialog.findViewById(R.id.dialog_add_word_edittext);
            if (et.getText() != null && et.getText().toString().length() > 0){
                saveWordInteractor.saveWordToDB(et.getText().toString(), word);
                showError(String.format("Слово '%s' добавлено в словарь", word));
                dialog.dismiss();
            } else {
                showError(String.format("Значение не может быть пустым", word));
            }
        });
        dialog.show();

    }

    private void setUpSkipButton() {
        if (taskType == 1) {
            learningPresenter.loadNextRandomWordFromAsset(getApplicationContext());
        } else {
            learningPresenter.loadNextRandomWord();
        }
    }

    private void setUpUpWordButton() {
        String word = wordTextView.getText() != null ? wordTextView.getText().toString() : "";
        if (word.length() > 0) {
            showError(String.format("Приоритет слова '%s' поднят", word));
        }
        learningPresenter.upWord(word);
    }

    private void setUpHintButton() {
        String word = wordTextView.getText() != null ? wordTextView.getText().toString() : "";
        if (taskType == 1) {
            startActivity(new Intent(this, TranslationActivity.class)
                    .putExtra("translation", translation)
                    .putExtra("word", word));
        } else {
            if (word.length() > 0)
                learningPresenter.showTranslation(word, this);
        }
    }

    private void setUpCheckButton() {
        String userValue = translationEditText.getText() != null
                ? translationEditText.getText().toString()
                : null;
        if (
                (taskType == 1 && userValue != null && translation != null && userValue.length() > 0 && translation.replace(" ", "").contains(userValue)) ||
                        (taskType == 0 && userValue != null && userValue.length() > 0 && learningPresenter
                .isTranslationCorrect(userValue, wordTextView.getText().toString()))){
            runWordAnimation(wordTextView, true);
        } else {
            runWordAnimation(wordTextView, false);
            Toast toast = Toast.makeText(this,
                    getResources().getString(R.string.learning_wrong_translation),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        learningPresenter.onDestroy();
    }

    @Override
    public void prepareView() {
        if (learningPresenter.getWords().size() == 0 && taskType == 0) {
            disableView();
        } else {
            setUpLearning();
        }
    }

    private void setUpLearning() {
        if (taskType == 1) {
            learningPresenter.loadNextRandomWordFromAsset(getApplicationContext());
        } else {
            learningPresenter.loadNextRandomWord();
        }
    }

    private void disableView() {
        translationEditText.setVisibility(View.GONE);
        doneXfromYTextView.setVisibility(View.GONE);
        TextViewHint.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadNextWord(String word, String translation, int countAll, int countDone) {
        this.translation = translation;
        translationEditText.setText("");
        wordTextView.setText(word == null ? "" : word);
        doneXfromYTextView.setText(
                String.format(this.getResources().getString(R.string.doneXfromY), countDone, countAll));
    }

    private void runWordAnimation(TextView tv, Boolean success)
    {
        Animation a;
        if (success) {
            a = AnimationUtils.loadAnimation(this, R.anim.learning_correct_anim);
        } else {
            a = AnimationUtils.loadAnimation(this, R.anim.learning_falure_anim);
        }
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv.setBackgroundColor(success
                        ? getResources().getColor(R.color.green)
                        : getResources().getColor(R.color.red));
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setBackgroundColor(Color.TRANSPARENT);
                if (success)
                    if (taskType == 1) {
                        learningPresenter.loadNextRandomWordFromAsset(getApplicationContext());
                    } else {
                        learningPresenter.loadNextRandomWord();
                    }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void setError() {

    }

    @Override
    public void showError(String text) {
        final Dialog dialog = new Dialog(LearningActivity.this);
        dialog.setContentView(R.layout.error_dialog);
        ((TextView) dialog.findViewById(R.id.dialog_info)).setText(text);
        Button okButton = dialog.findViewById(R.id.dialog_cancel);
        okButton.setOnClickListener(v->dialog.dismiss());
        dialog.show();
    }

    @Override
    public void setUpAllCompleted() {
        Toast.makeText(this, getResources().getString(R.string.learning_completed), Toast.LENGTH_SHORT).show();
        finish();
    }

}
