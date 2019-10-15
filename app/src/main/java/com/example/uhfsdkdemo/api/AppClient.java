package com.example.uhfsdkdemo.api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fengye on 2016/10/20.
 * email 1040441325@qq.com
 * Retrofit 客户端
 */
public class AppClient {

    public static AppVersionApi appVesionApi;

    public static AppVersionApi getAppVersionApi() {
        if (appVesionApi == null) {
            synchronized (AppClient.class) { //synchronized 线程锁，锁的是对象
                if (appVesionApi == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(2000, TimeUnit.SECONDS)
                            .writeTimeout(2000, TimeUnit.SECONDS)
                            .addNetworkInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request()
                                            .newBuilder()
                                            .addHeader("Connection", "close")
                                            .build();
                                    return chain.proceed(request);
                                }
                            }).build();
                    appVesionApi = new Retrofit.Builder()
                            .baseUrl("http://123.57.175.155:9120/")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
//                            .addConverterFactory(ScalarsConverterFactory.create())
                            .client(client)
                            .build()
                            .create(AppVersionApi.class);
                }
            }
        }
        return appVesionApi;
    }

    public static OkHttpClient getOkhttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .writeTimeout(2000, TimeUnit.SECONDS)
                .build();
        return client;
    }

}
