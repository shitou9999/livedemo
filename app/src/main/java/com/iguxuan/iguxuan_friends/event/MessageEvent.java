package com.iguxuan.iguxuan_friends.event;

/**
 * Created by jack on 2016/4/18.
 */
public class MessageEvent {
    private String alert;
    private String extras;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public MessageEvent(String alert, String extras) {
        this.alert = alert;
        this.extras = extras;
    }
}
