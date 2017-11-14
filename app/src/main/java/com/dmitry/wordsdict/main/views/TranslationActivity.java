package com.dmitry.wordsdict.main.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.widget.TextView;

import com.dmitry.wordsdict.R;

/**
 * Created by dmitry on 6/3/17.
 */

public class TranslationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_translation);
        TextView translationView = (TextView) findViewById(R.id.word_translation_text);

        String mainText = getIntent().getStringExtra("translation");
        String textToSelect = getIntent().getStringExtra("word");
        translationView.setText(mainText);

        int selTextIndex = mainText.indexOf(textToSelect, 0);
        Spannable WordtoSpan = new SpannableString( translationView.getText() );
        for(int ofs = 0; ofs < mainText.length() && selTextIndex != -1; ofs = selTextIndex + 1)
        {
            selTextIndex = mainText.indexOf(textToSelect, ofs);
            if(selTextIndex == -1)
                break;
            else
            {
                WordtoSpan.
                        setSpan(
                                new BackgroundColorSpan(0xFFFFFF00),
                                selTextIndex,
                                selTextIndex+textToSelect.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                translationView
                        .setText(WordtoSpan, TextView.BufferType.SPANNABLE);
            }
        }

    }

}
