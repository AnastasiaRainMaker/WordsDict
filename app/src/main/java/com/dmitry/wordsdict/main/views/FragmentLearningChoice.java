package com.dmitry.wordsdict.main.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.dmitry.wordsdict.R;

public class FragmentLearningChoice extends Fragment {


    private Button myDictButton;
    private Button randomWordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_choice,
                container, false);
        randomWordButton = view.findViewById(R.id.learning_random_word_button);
        myDictButton = view.findViewById(R.id.learning_mydict_word_button);
        randomWordButton.setOnClickListener(v->setUpRandButton());
        myDictButton.setOnClickListener(v->setUpMyDictButton());

//        showHello(myDictButton);
//        showHello(randomWordButton);
        prepareView();
        return  view;
    }

    private void setUpMyDictButton() {
        Intent intent = new Intent(getContext(), LearningActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
    }

    private void setUpRandButton() {
        Intent intent = new Intent(getContext(), LearningActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

//    private void showHello(Button view) {
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        Random rand = new Random();
//        rand.nextBoolean();
//        TranslateAnimation animation = new TranslateAnimation(rand.nextBoolean() ? -width : width, 0, 0, 0);
//        animation.setDuration(1000);
//        view.startAnimation(animation);
//    }

    private void prepareView() {
    }
}
