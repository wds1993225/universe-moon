package com.meetcity.huobi.api;

import com.adobe.xmp.impl.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by moon on 2018/4/13 .
 * <p>
 * 火币pro的授权API
 */
public class AuthenticationUtil {


    public enum RequestMethod {     //配置列表追加方式
        GET("get"), POST("post");
        private final String text;

        RequestMethod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * 签名
     *
     * @param paramList 请求参数
     * @param method    请求方法
     * @param path      请求路径
     */
    public static String creatSign(List<String> paramList, RequestMethod method, String path) throws Exception {
        StringBuilder signBody = new StringBuilder("");
        if (method == RequestMethod.GET) {        //请求方法
            signBody.append("GET\n");
        } else {
            signBody.append("POST\n");
        }
        signBody.append("api.huobi.pro\n");     //访问地址
        signBody.append(path + "\n");             //访问路径
        Collections.sort(paramList);//排序

        StringBuilder paramSB = new StringBuilder("");//连起来
        for (int i = 0; i < paramList.size(); i++) {
            if (i != (paramList.size() - 1)) {
                paramSB.append(paramList.get(i) + "&");
            } else {
                paramSB.append(paramList.get(i) );
            }
        }
        //String urlParam = null;
        try {
            //urlParam = URLEncoder.encode(paramSB.toString(), "UTF-8");//url编码

        } catch (Exception e) {
            e.printStackTrace();
        }
        signBody.append(paramSB);
        System.out.println(signBody.toString());
        String signHma = HMACSHA256(signBody.toString().getBytes(), Constants.AccessKeyId.getBytes());//加密
        BASE64Encoder encoder = new BASE64Encoder();
        signHma = encoder.encode(signHma.getBytes("UTF-8"));//base64编码
        return signHma;
    }


    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"
     * 如果获取失败，返回null
     *
     * @return
     */
    public static String getUTCTimeStr() {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day);
        UTCTimeBuffer.append(" ").append(hour).append(":").append(minute);
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
            format.parse(UTCTimeBuffer.toString());
            return UTCTimeBuffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception{
        List<String> lis = new ArrayList<String>();
        lis.add("AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx");
        lis.add("order-id=1234567890");
        lis.add("SignatureMethod=HmacSHA256");
        lis.add("SignatureVersion=2");
        lis.add("Timestamp=2017-05-11T15%3A19%3A30");
        String a = creatSign(lis,RequestMethod.GET,"/v1/order/orders");
        System.out.println(a);
    }


    public static String HMACSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] b = mac.doFinal(data);
            BASE64Encoder encoder = new BASE64Encoder();
            String a  = encoder.encode(b);//base64编码
            System.out.println("");
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
