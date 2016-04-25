package com.udacity.nanodegree.popularmovies.webservice.retrofit;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RestClient {

    private static ApiInterface restClient;

    static {

        setupRestClient();
    }

    private RestClient() {}

    public static ApiInterface get() {

        return restClient;
    }

    private static void setupRestClient() {

        Retrofit retrofit = getRetrofitClient(AppUrls.BASE_URL);
        restClient = retrofit.create(ApiInterface.class);

    }

    public static Retrofit getRetrofitClient(String url) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.MINUTES);

        //client.setReadTimeout(30, TimeUnit.SECONDS);
        //client.setFollowRedirects(true);
        //client.setFollowSslRedirects(true);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        HttpLoggingInterceptor interceptorBody = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.interceptors().add(interceptor);
        client.interceptors().add(interceptorBody);

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
