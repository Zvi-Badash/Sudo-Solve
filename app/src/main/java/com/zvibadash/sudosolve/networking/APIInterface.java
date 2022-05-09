package com.zvibadash.sudosolve.networking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIInterface {
    String API_KEY = "QPETl3iMdkIpejgC5ElMDfzuZez66fPzDnMoZ7tAGKxFdwXgcsKNqSidrNa0r4BdpoXJtHYL8piCQAqHgXBElpqgvtO7pWXzEsTE";
    String BASE_URL = "http://192.168.1.31:5000/";

    @GET("/check_connection")
    @Headers({
            "Content-Type: application/json",
            "Auth: " + API_KEY
    })
    Call<ResponseCheckConnection> isConnected();

    @GET("generate")
    @Headers({
            "Content-Type: application/json",
            "Auth: " + API_KEY
    })
    Call<ResponseGenerated> generate(@Query("level") DifficultyLevel level);

    @POST("solve")
    @Headers({
            "Content-Type: application/json",
            "Auth: " + API_KEY
    })
    Call<ResponseSolved> solve(@Body RequestSolve board);

    @POST("identify")
    @Headers({
            "Content-Type: application/json",
            "Auth: " + API_KEY
    })
    Call<ResponseIdentify> identify(@Body RequestIdentify board);
}
