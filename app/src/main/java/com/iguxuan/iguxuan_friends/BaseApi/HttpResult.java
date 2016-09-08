package com.iguxuan.iguxuan_friends.BaseApi;

/**
 * Created by jack on 2016/6/16.
 */
public class HttpResult<T> {
    //用来模仿resultCode和resultMessage
    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
