package com.neuman.brutus.Retrofit;

import android.content.Context;

import com.neuman.brutus.Utils.Globals;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Context context;
    private static Client client;
    private Interface interface_;
    private Globals g = new Globals();

    public static Interface getService(Context context) {
        if (client == null) {
            client = new Client(context);
        }

        return client.interface_;
    }

    private Client(Context context) {
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(g.API_BASE)
                .client(getOkHttpClient(new HttpLogger("Retrofit")))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        interface_ = retrofit.create(Interface.class);
    }

    private static OkHttpClient getOkHttpClient(HttpLoggingInterceptor.Logger logger) {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();

        // set conn timeout & read timeout
        mBuilder.connectTimeout(2000, TimeUnit.SECONDS);
        mBuilder.readTimeout(3000, TimeUnit.SECONDS);

        // add logging for all requests in our debug builds
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        mBuilder.cache(null);

        // config builder
        mBuilder.addInterceptor(logging).build();
        mBuilder.addInterceptor(new CookieSetter(context));
        mBuilder.addInterceptor(new CookieGetter(context));

        // build
        return mBuilder.build();
    }
}