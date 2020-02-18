package com.neuman.brutus.retrofit;


import android.content.Context;
import android.preference.PreferenceManager;

import com.neuman.brutus.Home;
import com.neuman.brutus.utils.Globals;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class CookieSetter implements Interceptor {
    private Context context;
    private Globals g;

    public CookieSetter(Context context) {
        this.context = context;
        g = new Globals();
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(g.PREF_COOKIES, new HashSet<String>());

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}