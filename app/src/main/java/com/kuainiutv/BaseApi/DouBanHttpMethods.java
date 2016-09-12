package com.kuainiutv.BaseApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jack on 2016/6/16.
 */
public class DouBanHttpMethods {
    /**
     * 域名
     */
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    /**
     * 读取超时
     */
    public static final int READ_TIME_OUT = 5;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIME_OUT = 5;

    public Retrofit retrofit;

    private DouBanHttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    private static class SingletonHolder {
        private static final DouBanHttpMethods INSTANCE = new DouBanHttpMethods();
    }

    public static DouBanHttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
