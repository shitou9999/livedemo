package com.iguxuan.iguxuan_friends.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/4/6.
 */
public class CommentItem implements Parcelable {

    /**
     * id : 768
     * commentid : content_54-3053-1
     * userid : 41
     * creat_at : 1463549003
     * content : 后悔
     * reply_content :
     * reply_teacher_id : 0
     * reply_datetime : 0000-00-00 00:00:00
     * support : 0
     * nickname : nanck哈哈哈
     * avatar : http://img.iguxuan.com/uploadfile/avatar/41_23375.jpg
     * phone : 13127691195
     * parent_user_id : 41
     * parent_user_name : nanck哈哈哈
     * parent_comment_id : 767
     * parent_comment_content : <strong>//@nanck哈哈哈：</strong>ii想几句话
     * source : c
     * model : c
     * system_version : c
     * app_version : c
     * is_teacher : 0
     * parent_is_teacher : 0
     * is_support : 0
     */

    private String id;
    private String commentid;
    private String userid;
    private long creat_at;
    private String content;
    private String reply_content;
    private String reply_teacher_id;
    private String reply_datetime;
    private int support;
    private String nickname;
    private String avatar;
    private String phone;
    private String parent_user_id;
    private String parent_user_name;
    private String parent_comment_id;
    private String parent_comment_content;
    private String source;
    private String source_show;
    private String model;
    private String system_version;
    private String app_version;
    private int is_teacher;
    private int parent_is_teacher;
    private int is_support;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getCreat_at() {
        return creat_at;
    }

    public void setCreat_at(long creat_at) {
        this.creat_at = creat_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_teacher_id() {
        return reply_teacher_id;
    }

    public void setReply_teacher_id(String reply_teacher_id) {
        this.reply_teacher_id = reply_teacher_id;
    }

    public String getReply_datetime() {
        return reply_datetime;
    }

    public void setReply_datetime(String reply_datetime) {
        this.reply_datetime = reply_datetime;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String parent_user_id) {
        this.parent_user_id = parent_user_id;
    }

    public String getParent_user_name() {
        return parent_user_name;
    }

    public void setParent_user_name(String parent_user_name) {
        this.parent_user_name = parent_user_name;
    }

    public String getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(String parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }

    public String getParent_comment_content() {
        return parent_comment_content;
    }

    public void setParent_comment_content(String parent_comment_content) {
        this.parent_comment_content = parent_comment_content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getSource_show() {
        return source_show;
    }

    public void setSource_show(String source_show) {
        this.source_show = source_show;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystem_version() {
        return system_version;
    }

    public void setSystem_version(String system_version) {
        this.system_version = system_version;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public int getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public int getParent_is_teacher() {
        return parent_is_teacher;
    }

    public void setParent_is_teacher(int parent_is_teacher) {
        this.parent_is_teacher = parent_is_teacher;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }


    @Override public String toString() {
        return "CommentItem{" +
                "app_version='" + app_version + '\'' +
                ", id='" + id + '\'' +
                ", commentid='" + commentid + '\'' +
                ", userid='" + userid + '\'' +
                ", creat_at=" + creat_at +
                ", content='" + content + '\'' +
                ", reply_content='" + reply_content + '\'' +
                ", reply_teacher_id='" + reply_teacher_id + '\'' +
                ", reply_datetime='" + reply_datetime + '\'' +
                ", support=" + support +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", parent_user_id='" + parent_user_id + '\'' +
                ", parent_user_name='" + parent_user_name + '\'' +
                ", parent_comment_id='" + parent_comment_id + '\'' +
                ", parent_comment_content='" + parent_comment_content + '\'' +
                ", source='" + source + '\'' +
                ", source_show='" + source_show + '\'' +
                ", model='" + model + '\'' +
                ", system_version='" + system_version + '\'' +
                ", is_teacher=" + is_teacher +
                ", parent_is_teacher=" + parent_is_teacher +
                ", is_support=" + is_support +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.commentid);
        dest.writeString(this.userid);
        dest.writeLong(this.creat_at);
        dest.writeString(this.content);
        dest.writeString(this.reply_content);
        dest.writeString(this.reply_teacher_id);
        dest.writeString(this.reply_datetime);
        dest.writeInt(this.support);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.phone);
        dest.writeString(this.parent_user_id);
        dest.writeString(this.parent_user_name);
        dest.writeString(this.parent_comment_id);
        dest.writeString(this.parent_comment_content);
        dest.writeString(this.source);
        dest.writeString(this.source_show);
        dest.writeString(this.model);
        dest.writeString(this.system_version);
        dest.writeString(this.app_version);
        dest.writeInt(this.is_teacher);
        dest.writeInt(this.parent_is_teacher);
        dest.writeInt(this.is_support);
    }

    public CommentItem() {
    }

    protected CommentItem(Parcel in) {
        this.id = in.readString();
        this.commentid = in.readString();
        this.userid = in.readString();
        this.creat_at = in.readLong();
        this.content = in.readString();
        this.reply_content = in.readString();
        this.reply_teacher_id = in.readString();
        this.reply_datetime = in.readString();
        this.support = in.readInt();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.phone = in.readString();
        this.parent_user_id = in.readString();
        this.parent_user_name = in.readString();
        this.parent_comment_id = in.readString();
        this.parent_comment_content = in.readString();
        this.source = in.readString();
        this.source_show = in.readString();
        this.model = in.readString();
        this.system_version = in.readString();
        this.app_version = in.readString();
        this.is_teacher = in.readInt();
        this.parent_is_teacher = in.readInt();
        this.is_support = in.readInt();
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override public CommentItem createFromParcel(Parcel source) {
            return new CommentItem(source);
        }

        @Override public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };
}
