package com.example.safecarrier;

public interface RetrofitCallback<T> {
    void onResponseSuccess(int code, T receivedData);
}
