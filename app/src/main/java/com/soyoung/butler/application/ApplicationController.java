package com.soyoung.butler.application;

import android.app.Application;
import android.util.Log;
import com.tsengvn.typekit.Typekit;


import com.soyoung.butler.network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Soyoung on 16. 12. 28..
 */
public class ApplicationController extends Application{
    private static ApplicationController instance;

    private static String baseUrl = "http://52.78.14.160:8888";

    private NetworkService networkService;      //인터페이스 받을 부분

    public static ApplicationController getInstance() {
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;

        // TODO: 2016. 11. 21. 어플리케이션 초기 실행 시, retrofit을 사전에 build한다.
        buildService();

        /*Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NotoSansCJKkr_Medium.otf"))
                .addCustom1(Typekit.createFromAsset(this, "NotoSansCJKkr_Bold.otf"));*/
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);     //retrofit에 네트워크서비스 인터페이스를 알리기(적용)
    }
}
