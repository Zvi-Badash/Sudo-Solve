package com.zvibadash.sudosolve.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    static APIInterface client = null;

    private APIClient() {}
    public static APIInterface getClient() {
        if (client == null)
            client = new Retrofit.Builder()
                    .baseUrl(APIInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(APIInterface.class);

        return client;
    }
}
