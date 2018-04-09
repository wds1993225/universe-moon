package com.meetcity.moon.example;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by moon on 2018/3/13 .
 */
@Slf4j
public class MoonIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println("");
        Response response = chain.proceed(request);
        System.out.println("");
        return response;
    }
}
