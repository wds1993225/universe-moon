package com.moon.Exception;


/**
 * http exception
 */
public class HttpUtilException extends Exception {

    private String errorMsg;

    public HttpUtilException(String errorMsg) {
        super(errorMsg);
    }
}
