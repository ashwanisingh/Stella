package com.ns.networking.retrofit;


import android.content.Context;

import java.util.concurrent.TimeUnit;

import com.ns.networking.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Vignesh Prabhu on 28/09/15.
 */
public final class RetrofitAPICaller {


    private static RetrofitAPICaller sRetrofitApiCaller;

    private static Context sContext;

    private StellarApiService stellarApiService;

    private RetrofitAPICaller() {
        setupRetroAdapter();
    }

    public static RetrofitAPICaller getInstance(Context context) {
        sContext = context;
        if (sRetrofitApiCaller == null) {
            sRetrofitApiCaller = new RetrofitAPICaller();
        }
        return sRetrofitApiCaller;
    }

    private void setupRetroAdapter() {

        Retrofit digestRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(setTimeOut())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stellarApiService = digestRetrofit.create(StellarApiService.class);
    }

    public StellarApiService getStellarJetAPIs() {
        return stellarApiService;
    }

    private static OkHttpClient setTimeOut() {
        return new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
    }

}
