package com.soyoung.butler.network;

import com.soyoung.butler.login.MainListData;
import com.soyoung.butler.login.MainResult;
import com.soyoung.butler.register.RegisterData;
import com.soyoung.butler.register.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Soyoung on 16. 12. 28..
 */
public interface NetworkService {

    @POST("sign/in1")
    Call<MainResult> requestMain(@Body MainListData mainListDatas);
    //Call<MainResult> requestMain(@Part("email")RequestBody email,
     //                            @Part("password") RequestBody password);

    @POST("sign/up1")
    Call<RegisterResult> requestRegister(@Body RegisterData registerData);
}
