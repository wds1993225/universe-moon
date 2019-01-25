package com.meetcity.moon.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/home/li")
    public String haha() {
        System.out.println("haha");
        //return Constants.FILE_NAME;
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "moonNo4")
    public void nihao(String name, String pwd) {
        System.out.println("name:" + name);
        System.out.println("pwd:" + pwd);
    }
}
