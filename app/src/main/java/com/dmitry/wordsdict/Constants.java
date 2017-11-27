package com.dmitry.wordsdict;

/**
 * Created by dmitry on 6/3/17.
 */

public final class Constants {
    public static final String MULTITRAN_CSS_SELECTOR = "body > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(1) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > table:nth-child(5)";
    public static final String MULTITRAN_URL = "http://www.multitran.ru/c/M.exe?s=%s";
    public static final String MULTITRAN_URL_RUS = "https://www.multitran.ru/c/m.exe?l1=2&l2=1&s=%s";
    public static final String EXPORT_FILE_NAME = "/wordsDictExport.json/";
    public static String TAG = "WORDS_DICT";
    public static String REALM_WORD_NAME_KEY = "wordName";
    public static String REALM_WORD_TRANSLATION_KEY = "wordTranslation";
    public static String REALM_WORD_FREQ_KEY = "wordFrequency";
    public static String REALM_WORD_PRIORITY_KEY = "wordPriority";
    public static String REALM_WORD_ID_KEY = "wordId";
    public static String REALM_WORD_DATE_KEY = "creationDate";
    public static String REALM_WORD_SELECTED_KEY = "selected";
    public static java.lang.String ASSET_DICT_NAME_BIG = "bigEn.txt";
    public static java.lang.String ASSET_DICT_NAME = "endict.txt";
    public static final int REQ_CODE = 235;

}
