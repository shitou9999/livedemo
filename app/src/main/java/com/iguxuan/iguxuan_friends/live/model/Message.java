package com.iguxuan.iguxuan_friends.live.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/8/2.
 */
public class Message implements Parcelable {
    /**
     * publish_timestamp : 2342342342
     * head_photo : https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0uXVq6BUimO2Jx8VQJrkjpsi68kILPMkWO8nX61zg4a6t_V5bBw
     * nickname : angel
     * message_type : 2
     * message_content : 写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。
     * message_image : http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1
     * comment_count : 456
     * like_count : 764
     * like_before : 0
     * type : 1
     */

    private long publish_timestamp;
    private String head_photo;
    private String nickname;
    private int message_type;
    private String message_content;
    private String message_image;
    private int comment_count;
    private int like_count;
    private int like_before;
    private int type;

    public long getPublish_timestamp() {
        return publish_timestamp;
    }

    public void setPublish_timestamp(long publish_timestamp) {
        this.publish_timestamp = publish_timestamp;
    }

    public String getHead_photo() {
        return head_photo;
    }

    public void setHead_photo(String head_photo) {
        this.head_photo = head_photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_image() {
        return message_image;
    }

    public void setMessage_image(String message_image) {
        this.message_image = message_image;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getLike_before() {
        return like_before;
    }

    public void setLike_before(int like_before) {
        this.like_before = like_before;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override public String toString() {
        return "Message{" +
                "comment_count=" + comment_count +
                ", publish_timestamp=" + publish_timestamp +
                ", head_photo='" + head_photo + '\'' +
                ", nickname='" + nickname + '\'' +
                ", message_type=" + message_type +
                ", message_content='" + message_content + '\'' +
                ", message_image='" + message_image + '\'' +
                ", like_count=" + like_count +
                ", like_before=" + like_before +
                ", type=" + type +
                '}';
    }


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.publish_timestamp);
        dest.writeString(this.head_photo);
        dest.writeString(this.nickname);
        dest.writeInt(this.message_type);
        dest.writeString(this.message_content);
        dest.writeString(this.message_image);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.like_count);
        dest.writeInt(this.like_before);
        dest.writeInt(this.type);
    }

    public Message() {
    }

    protected Message(Parcel in) {
        this.publish_timestamp = in.readLong();
        this.head_photo = in.readString();
        this.nickname = in.readString();
        this.message_type = in.readInt();
        this.message_content = in.readString();
        this.message_image = in.readString();
        this.comment_count = in.readInt();
        this.like_count = in.readInt();
        this.like_before = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
