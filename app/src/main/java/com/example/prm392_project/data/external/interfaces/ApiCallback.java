package com.example.prm392_project.data.external.interfaces;

public interface ApiCallback<T> {
    void onSuccess(T response);

    void onError(String message);
}
