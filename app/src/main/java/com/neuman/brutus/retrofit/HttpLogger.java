package com.neuman.brutus.retrofit;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;


public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private final String mTag;

    public HttpLogger(String tag) {
        mTag = tag;
    }

    @Override
    public void log(String message) {
        Log.d(mTag, message);
    }
}
