package com.krupet.error.exceptions;

public enum ServiceErrorCode {
    BAD_REQUEST("bad_request");

    private String key;

    ServiceErrorCode(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
