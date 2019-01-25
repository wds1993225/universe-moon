package com.meetcity.moon.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by moon on 2018/2/28 .
 */

@RestController
public class UACController {

    @RequestMapping("/moon/logininfo")
    private String loginInfo(){
        return "";
    }

    @RequestMapping("/moon/sms")
    private String sms(String smsNumber){
        return "";

    }
}
