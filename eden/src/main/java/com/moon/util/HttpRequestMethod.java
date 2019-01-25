package com.moon.util;


/**
 * Http method
 */
public enum HttpRequestMethod {
    GET("GET"), POST("POST"), PUT("PUT");

    private final String text;

    HttpRequestMethod(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
