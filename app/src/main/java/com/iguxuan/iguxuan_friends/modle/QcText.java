package com.iguxuan.iguxuan_friends.modle;

/**
 *二维码扫描结果
 */
public class QcText {
    /**
     * type : qrcode_login
     * client_id : 00
     * connect_time : 00
     */

    private String type;
    private String client_id;
    private String connect_time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getConnect_time() {
        return connect_time;
    }

    public void setConnect_time(String connect_time) {
        this.connect_time = connect_time;
    }
}
