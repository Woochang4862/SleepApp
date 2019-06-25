package com.jeongwoochang.sleepapp.API;

import android.content.Context;
import android.util.Log;
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import java.io.IOException;
import java.util.HashSet;

public class AddCookiesInterceptor implements Interceptor {
    private SharedPreferencesHelper mDsp;

    public AddCookiesInterceptor(Context context){
        mDsp = new SharedPreferencesHelper(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = mDsp.getHashSet("cookies", new HashSet<String>());

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Timber.tag("OkHttp").v("Adding Header: %s", cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}
