package com.meetcity.moon.web.controller;


import com.meetcity.moon.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private RedisTemplate redisTemplate;



    @RequestMapping("/home/li")
    public String haha(){
        System.out.println("haha");
        return Constants.FILE_NAME;
    }
}
