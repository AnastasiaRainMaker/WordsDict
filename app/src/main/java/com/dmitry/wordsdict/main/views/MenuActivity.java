package com.dmitry.wordsdict.main.views;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.TextView;
import android.widget.Toast;
import com.dmitry.wordsdict.Constants;
import com.dmitry.wordsdict.R;
import com.dmitry.wordsdict.model.WordModelRealm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MenuActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    BottomNavigationView bottomNavigationView;

    private final int FILE_SELECT_CODE = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        invalidateOptionsMenu();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        applyBottomNavFont();
        mainToolbar = findViewById(R.id.toolbar_main);
        applyToolbarFont();
        setSupportActionBar(mainToolbar);
        FragmentMenu fragment = new FragmentMenu();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, fragment)
                .commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    if (item.getItemId() == R.id.toolbar_my_dic) {
                       FragmentListWords fragmentListWords = new FragmentListWords();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.menu_frame, fragmentListWords)
                                .commit();
                    } else if (item.getItemId() == R.id.toolbar_tests){
                        FragmentLearningChoice fragmentLearningChoice = new FragmentLearningChoice();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.menu_frame, fragmentLearningChoice)
                                .commit();
                    }  else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.menu_frame, fragment)
                                .commit();
                    }
                    return true;
                });
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
    private void applyToolbarFont() {
        // The Toolbar widget doesn't provide a native way to set the appearance of
        // the text views. So we have to hack in to the view hierarchy here.
        for (int i = 0; i < mainToolbar.getChildCount(); i++) {
            View child = mainToolbar.getChildAt(i);
            if (child instanceof ActionMenuView) {
                ActionMenuView menu = (ActionMenuView) child;
                for (int j = 0; j < menu.getChildCount(); j++) {
                    View item = menu.getChildAt(j);
                    View smallItemText = item.findViewById(android.support.design.R.id.smallLabel);
                    if (smallItemText instanceof TextView) {
                        ((TextView) smallItemText).setTextAppearance(getApplicationContext(), R.style.MenuTextAppearance);
                    }
                    View largeItemText = item.findViewById(android.support.design.R.id.largeLabel);
                    if (largeItemText instanceof TextView) {
                        ((TextView) largeItemText).setTextAppearance(getApplicationContext(), R.style.MenuTextAppearance);
                    }
                }
            }
        }
    }
//
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

//    private void setUpLearningButton() {
//        startActivity(new Intent(MenuActivity.this, FragmentLearningChoice.class));
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Menu toolbarMenu = mainToolbar.getMenu();
        MenuItem toolbarMenuItem1 = toolbarMenu.findItem(R.id.action_about);
        MenuItem toolbarMenuItem2 = toolbarMenu.findItem(R.id.action_save);
        MenuItem toolbarMenuItem3 = toolbarMenu.findItem(R.id.action_send);

//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/custom_font3.ttf");
//        SpannableStringBuilder title1 = new SpannableStringBuilder(getApplicationContext().getString(R.string.about));
//        title1.setSpan(custom_font, 0, title1.length(), 0);
//        toolbarMenuItem1.setTitle(title1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                setUpExportButton();
                return true;

            case R.id.action_send:
                setUpLoadFromFile();
                return true;

            case R.id.action_about:
                setUpAbout();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setUpAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    private void setUpLoadFromFile() {
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d(Constants.TAG, "File Uri: " + uri.toString());
                    String path = getPath(this, uri);
                    Log.d(Constants.TAG, "File Path: " + path);
                    if (path != null) {
                        loadFromFile(path);
                    } else {
                        Toast.makeText(this,
                                this.getResources().getString(R.string.error_can_not_get_file),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFromFile(String path) {
        File file = new File(path);
        try (Realm mRealm = Realm.getDefaultInstance()) {
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jAr = new JSONArray(json);
            final int[] newWordsCount = {0};
            for (int i = 0; i < jAr.length(); i++) {
                JSONObject obj = (JSONObject) jAr.get(i);
                mRealm.executeTransaction(realm -> {
                    WordModelRealm word = new WordModelRealm();
                    try {
                        String wordName = obj.getString(Constants.REALM_WORD_NAME_KEY);
                        if (realm.where(WordModelRealm.class)
                                .equalTo(Constants.REALM_WORD_NAME_KEY, wordName)
                                .count() == 0) {
                            word.setWordFrequency(obj.getInt(Constants.REALM_WORD_FREQ_KEY));
                            word.setWordName(wordName);
                            word.setWordTranslation(obj.getString(Constants.REALM_WORD_TRANSLATION_KEY));
                            word.setWordId(getRealmNextId(word));
                            realm.insert(word);
                            newWordsCount[0]++;
                            Log.d(Constants.TAG, String.format("The word %1$s imported", wordName));
                        } else {
                            Log.d(Constants.TAG, String.format("Error while importing the word: %1$s", wordName));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
            Toast.makeText(getApplicationContext(),
                    String.format(this.getResources().getString(R.string.import_accomplished), newWordsCount[0]),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    String.format(this.getResources().getString(R.string.error_reading_file), path),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public String getPath(Context context, Uri uri){
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor != null ? cursor.getColumnIndexOrThrow("_data") : 0;
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.error_can_not_get_file),
                        Toast.LENGTH_SHORT).show();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private void setUpExportButton() {
        File outputDir;
        if (Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState())) {
            outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else {
            outputDir = Environment.getDataDirectory();
        }
        if (outputDir != null) {
            try {
                String strOutput = getExportJson();
                if (strOutput != null) {
                    File file = new File(outputDir, Constants.EXPORT_FILE_NAME);
                    Writer output = new BufferedWriter(new FileWriter(file));
                    output.write(getExportJson());
                    output.close();
                    Toast.makeText(getApplicationContext(), String.format("Saved at %s", file.getAbsoluteFile()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No data to save", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this,
                        this.getResources().getString(R.string.error_can_not_write_file),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,
                    this.getResources().getString(R.string.error_can_not_write_file),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String getExportJson() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<WordModelRealm> words = mRealm.where(WordModelRealm.class)
                .notEqualTo(Constants.REALM_WORD_NAME_KEY, "")
                .findAll();
        if (words.size() > 0){
            return toJson(words).toString();
        }
        return null;
    }

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

    public <T extends WordModelRealm> JSONArray toJson(RealmResults<T> items){
        JSONArray jArray = new JSONArray();
        for (T item: items) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(Constants.REALM_WORD_NAME_KEY, item.getWordName());
                obj.put(Constants.REALM_WORD_FREQ_KEY, item.getWordFrequency());
                obj.put(Constants.REALM_WORD_TRANSLATION_KEY, item.getWordTranslation());
                obj.put(Constants.REALM_WORD_DATE_KEY, item.getCreationDate());
                obj.put(Constants.REALM_WORD_PRIORITY_KEY, item.getWordPriority());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jArray.put(obj);
        }
        return jArray;
    }

}
