package com.moon.util;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

public class MyX509TrustManager extends X509ExtendedTrustManager {
	
	public static final MyX509TrustManager INSTANCE = new MyX509TrustManager();
	
	private MyX509TrustManager(){}
	
    public void checkClientTrusted(X509Certificate[] chain, String authType,Socket socket)throws CertificateException {

    }
    public void checkClientTrusted(X509Certificate[]chain,String authType,SSLEngine engine)throws CertificateException {

    }
    public void checkClientTrusted(X509Certificate[]chain,String authType)throws CertificateException {

    }
    public void checkServerTrusted(X509Certificate[] chain,String authType,Socket socket)throws CertificateException{

    }
    public void checkServerTrusted(X509Certificate[]chain,String authType,SSLEngine engine)throws CertificateException{

    }
    public void checkServerTrusted(X509Certificate[] chain,String authType)throws CertificateException{

    }
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
}