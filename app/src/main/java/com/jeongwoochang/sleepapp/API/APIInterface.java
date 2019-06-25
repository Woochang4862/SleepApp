package com.jeongwoochang.sleepapp.API;

import com.jeongwoochang.sleepapp.util.data.*;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;

public interface APIInterface {

    @Multipart
    @POST("/register")
    Call<RegisterRes> register(@PartMap HashMap<String, RequestBody> registerParm);

    @POST("/login")
    Call<LoginRes> login(@Query(value = "id", encoded = true) String id, @Query(value = "pw", encoded = true) String pw);

    @Multipart
    @POST("/writeboard")
    Call<LoginRes> writeBoard(@PartMap HashMap<String, RequestBody> boardParm);

    @GET("/getboards")
    Call<BoardRes> getBoards();

    @FormUrlEncoded
    @POST("/clicklike")
    Call<LoginRes> clickLike(@Field("no") String no);

    @FormUrlEncoded
    @POST("/unclicklike")
    Call<LoginRes> unclickLike(@Field("no") String no);

    @GET("/getasmr")
    Call<YTASMRRes> getASMR(@Query("page") int page);

    @GET("/getasmr")
    Call<YTASMRRes> getASMR(@Query(value = "token", encoded = true) String token);

    @GET("/getwhitenoise")
    Call<YTASMRRes> getWhiteNoise(@Query("page") int page);

    @GET("/getwhitenoise")
    Call<YTASMRRes> getWhiteNoise(@Query(value = "token", encoded = true) String token);

    @GET("/getuser")
    Call<UserRes> getUser(@Query(value = "id", encoded = true) String id);
}
