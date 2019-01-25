package com.moon.Exception;

public class HttpLoaderException extends RuntimeException {

    public HttpLoaderException(String errorMsg) {
        super(errorMsg);
    }

    public HttpLoaderException(String errorMsg, Exception e) {
        super(errorMsg, e);
    }

}
