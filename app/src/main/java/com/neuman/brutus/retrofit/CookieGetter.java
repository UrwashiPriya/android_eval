package com.neuman.brutus.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.neuman.brutus.utils.Globals;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;


public class CookieGetter implements Interceptor {

    private Globals g;
    private Context context;

    public CookieGetter(Context context) {
        this.context = context;
        g = new Globals();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.clear();
            memes.apply();

            HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(g.PREF_COOKIES, new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.clear();
            memes.putStringSet(g.PREF_COOKIES, cookies).apply();
            memes.apply();
        }

        return originalResponse;
    }
}