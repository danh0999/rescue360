package com.example.prm392_project.data.external.response;

public class BaseResp<T> {
    private int code;
    private String message;
    private T data;

    public BaseResp(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}