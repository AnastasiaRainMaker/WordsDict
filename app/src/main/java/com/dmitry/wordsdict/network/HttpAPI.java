package com.dmitry.wordsdict.network;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by dmitry on 5/30/17.
 */

public interface HttpAPI {
    String BASE_URL = "";

//    @GET("c/M.exe")
//    Call<List<Post>> getMultitranTranslation(
//            @Query("s") String word
//    );

    @GET("c/M.exe")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ResponseBody> getMultitranTranslation(
            @Query("s") String word
    );



}
