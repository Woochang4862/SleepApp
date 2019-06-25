package com.jeongwoochang.sleepapp.API;

import android.content.Context;
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.Response;
import timber.log.Timber;

import java.io.IOException;
import java.util.HashSet;

public class ReceivedCookiesInterceptor implements Interceptor {

    private SharedPreferencesHelper mDsp;

    public ReceivedCookiesInterceptor(Context context) {
        mDsp = new SharedPreferencesHelper(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        Timber.plant(new Timber.DebugTree());
        Timber.d(String.valueOf(!originalResponse.headers("Set-Cookie").isEmpty()));
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {

            HashSet<String> cookies = new HashSet<>(originalResponse.headers("Set-Cookie"));
            mDsp.putHashSet("cookies", cookies);
        }
        return originalResponse;
    }
}
