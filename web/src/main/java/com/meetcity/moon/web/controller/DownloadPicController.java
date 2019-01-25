package com.meetcity.moon.web.controller;


import com.meetcity.moon.web.FileUtil;
import com.meetcity.moon.web.OkHttpUtil;
import okhttp3.Response;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@RestController
public class DownloadPicController {

    public int a = 75;
    public static Set set = new HashSet();

    @RequestMapping("/pic")
    public String test(String path) {
        if (path.contains("https://auto.tancdn.com/v1/images/") && !set.contains(path)) {
            System.out.println(path);
            Response r = OkHttpUtil.get(path);
            InputStream inputStream = r.body().byteStream();
            FileUtil.readStreamToFile(inputStream, "/Users/moon/Desktop/mytt/", a + ".jpg");
            set.add(path);
            a++;
        }


        return "ok";
    }
}
