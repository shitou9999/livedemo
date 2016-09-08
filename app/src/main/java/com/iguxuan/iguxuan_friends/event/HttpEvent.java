package com.iguxuan.iguxuan_friends.event;


import com.iguxuan.iguxuan_friends.modle.cons.Action;

import org.json.JSONObject;

/**
 * @author nanck 2016/3/10.
 */
public class HttpEvent {
    protected Action action;
    protected int code;
    protected String msg;
    private JSONObject data;

    public HttpEvent(Action action, int code, JSONObject data, String msg) {
        this.action = action;
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public HttpEvent(Action action, int code, JSONObject data) {
        this.action = action;
        this.code = code;
        this.data = data;
    }

    public HttpEvent(Action action, int code, String msg) {
        this.action = action;
        this.code = code;
        this.msg = msg;
    }

    public HttpEvent(Action action, int code) {
        this.action = action;
        this.code = code;
    }

    public HttpEvent(int code, JSONObject data) {
        this.code = code;
        this.data = data;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public Action getAction() {
        return action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public JSONObject getData() {
        return data;
    }
}
