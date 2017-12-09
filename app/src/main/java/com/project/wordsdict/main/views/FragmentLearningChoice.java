package com.project.wordsdict.main.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.project.wordsdict.R;

public class FragmentLearningChoice extends Fragment {

    private MenuActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MenuActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_choice,
                container, false);
        Button randomWordButton = view.findViewById(R.id.learning_random_word_button);
        Button myDictButton = view.findViewById(R.id.learning_mydict_word_button);
        randomWordButton.setOnClickListener(v->setUpRandButton());
        myDictButton.setOnClickListener(v->setUpMyDictButton());
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
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

    private void prepareView() {
    }
}
