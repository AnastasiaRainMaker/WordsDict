package com.dmitry.wordsdict.main.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dmitry.wordsdict.Constants;
import com.dmitry.wordsdict.R;
import com.dmitry.wordsdict.model.WordModelRealm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.dmitry.wordsdict.Constants.REQ_CODE;

public class MenuActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    BottomNavigationView bottomNavigationView;

    private final int FILE_SELECT_CODE = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        applyBottomNavFont();
        mainToolbar = findViewById(R.id.toolbar_main);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //setUpExportButton();
                getUserPermission();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены, что хотите выйти?")
                .setCancelable(false)
                .setPositiveButton("Да", (dialog, id) -> {
                    if(android.os.Build.VERSION.SDK_INT >= 21){
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finishAndRemoveTask();
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                    }
                })
                .setNegativeButton("Нет", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        TextView textView = alert.findViewById(android.R.id.message);
        Button button1 = alert.getWindow().findViewById(android.R.id.button1);
        Button button2 = alert.getWindow().findViewById(android.R.id.button2);
        Typeface face=Typeface.createFromAsset(getAssets(), "custom_font3.ttf");
        assert textView != null;
        textView.setTypeface(face);
        button1.setTypeface(face);
        button2.setTypeface(face);

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

    private void getUserPermission(){

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE);
    }

    private void setUpExportButton() {
        String outputDir;
        File file;
        if (Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState())) {
            outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            file = new File(outputDir + Constants.EXPORT_FILE_NAME);
        } else {
            outputDir = Environment.getDataDirectory().toString();
            file = new File(outputDir + Constants.EXPORT_FILE_NAME);
        }
        if (outputDir != null) {
            try {
                String strOutput = getExportJson().toString();
                if (strOutput != null) {
                   if(!file.exists()) {
                       Writer output = new BufferedWriter(new FileWriter(file));
                       output.write(strOutput);
                       output.close();
                       Toast.makeText(getApplicationContext(), "Файл wordsDictExport.json сохранен в папке Download", Toast.LENGTH_LONG).show();
                   } else {
                       StringBuilder text = new StringBuilder();
                       try {
                           BufferedReader br = new BufferedReader(new FileReader(file));
                           String line;
                           while ((line = br.readLine()) != null) {
                               text.append(line);
                               text.append('\n');
                           }
                           br.close() ;
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       String newStrOutput = getExportJson().toString();
                       String newMergedOutput = text + newStrOutput;
                       String dedupedOutput = deDup(newMergedOutput);

                       Writer output = new BufferedWriter(new FileWriter(file));
                       output.write(dedupedOutput);
                       output.close();
                       Toast.makeText(getApplicationContext(), "Файл wordsDictExport.json сохранен в папке Download", Toast.LENGTH_LONG).show();
                   }
                } else {
                    Toast.makeText(getApplicationContext(), "Невозможно записать файл из пустого словаря", Toast.LENGTH_LONG).show();
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
    public String deDup(String s) {
        return s.replaceAll("(\\b\\w+\\b)-(?=.*\\b\\1\\b)", "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpExportButton();

                } else {
                    Toast.makeText(this,
                            this.getResources().getString(R.string.error_can_not_write_file),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private JSONArray getExportJson() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<WordModelRealm> words = mRealm.where(WordModelRealm.class)
                .notEqualTo(Constants.REALM_WORD_NAME_KEY, "")
                .findAll();
        if (words.size() > 0){
            return toJson(words);
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
