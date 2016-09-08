package com.iguxuan.iguxuan_friends.modle;

/**
 * 评论消息
 */
public class ActivityMessage {

    /**
     * specify_datetime : 0000-00-00 00:00:00
     * activity_thumb :
     * jump_url : http://client.apitest.iguxuan.com//v2/message/activity_info?id=99
     * activity_id : 0
     * range_id : 0
     * activity_url :
     * status : 1
     * type : one
     * is_push : 1
     * content : 5555555555555555555
     * id : 99
     * is_collect : 0
     * activity_button_word :
     * title : 5555555555555
     * create_date : 1462503666
     * user_id : 36
     * is_read : 0
     * datetime : 2016-04-26 01:45:19
     * message_type : 2
     */

    private String specify_datetime;
    private String activity_thumb;
    private String jump_url_android;
    private String activity_id;
    private String range_id;
    private String activity_url;
    private String status;
    private String type;
    private String is_push;
    private String content;
    private String id;
    private String message_id;
    private int is_collect;
    private String activity_button_word;
    private String title;
    private int create_date;
    private String user_id;
    private int is_read;
    private String datetime;
    private String message_type;

    public String getSpecify_datetime() {
        return specify_datetime;
    }

    public void setSpecify_datetime(String specify_datetime) {
        this.specify_datetime = specify_datetime;
    }

    public String getActivity_thumb() {
        return activity_thumb;
    }

    public void setActivity_thumb(String activity_thumb) {
        this.activity_thumb = activity_thumb;
    }

    public String getJump_url() {
        return jump_url_android;
    }

    public void setJump_url(String jump_url) {
        this.jump_url_android = jump_url;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getRange_id() {
        return range_id;
    }

    public void setRange_id(String range_id) {
        this.range_id = range_id;
    }

    public String getActivity_url() {
        return activity_url;
    }

    public void setActivity_url(String activity_url) {
        this.activity_url = activity_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_push() {
        return is_push;
    }

    public void setIs_push(String is_push) {
        this.is_push = is_push;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public String getActivity_button_word() {
        return activity_button_word;
    }

    public void setActivity_button_word(String activity_button_word) {
        this.activity_button_word = activity_button_word;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCreate_date() {
        return create_date;
    }

    public void setCreate_date(int create_date) {
        this.create_date = create_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
