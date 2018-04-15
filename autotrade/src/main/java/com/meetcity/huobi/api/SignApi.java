package com.meetcity.huobi.api;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by moon on 2018/4/12 .
 */
public class SignApi {

    public static void keyPost(Map<String, String> paramMap, String path) {
        String method = "post";
        String timeStamp = AuthenticationUtil.getUTCTimeStr();
        List<String> paramList = new ArrayList<String>();
        paramList.add("AccessKeyId="+Constants.AccessKeyId);
        paramList.add("SignatureMethod=HmacSHA256");
        paramList.add("SignatureVersion=2");
        paramList.add("Timestamp="+timeStamp);


    }
}
