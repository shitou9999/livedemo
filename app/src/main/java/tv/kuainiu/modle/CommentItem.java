package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 评论内容
 */
public class CommentItem implements Parcelable {


    /**
     * source_show : 来自android端
     * model :
     * phone : 18321706682
     * support : 1
     * nickname : 龙猫
     * is_teacher : 0
     * app_version :
     * parent_user_id : 0
     * avatar : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/avatar/201609/4_84748.jpg
     * content : 不错
     * id : 1
     * parent_user_name : null
     * source : android
     * create_date : 1474179177
     * parent_comment_content :
     * is_support : 0
     * user_id : 4
     * parent_is_teacher : 0
     * parent_comment_id : 0
     * system_version :
     */

    private String source_show;
    private String model;
    private String phone;
    private int support;
    private String nickname;
    private int is_teacher;
    private String app_version;
    private String parent_user_id;
    private String avatar;
    private String content;
    private String id;
    private String parent_user_name;
    private String source;
    private String create_date;
    private String parent_comment_content;
    private int is_support;
    private String user_id;
    private int parent_is_teacher;
    private String parent_comment_id;
    private String system_version;

    protected CommentItem(Parcel in) {
        source_show = in.readString();
        model = in.readString();
        phone = in.readString();
        support = in.readInt();
        nickname = in.readString();
        is_teacher = in.readInt();
        app_version = in.readString();
        parent_user_id = in.readString();
        avatar = in.readString();
        content = in.readString();
        id = in.readString();
        source = in.readString();
        create_date = in.readString();
        parent_comment_content = in.readString();
        is_support = in.readInt();
        user_id = in.readString();
        parent_is_teacher = in.readInt();
        parent_comment_id = in.readString();
        system_version = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source_show);
        dest.writeString(model);
        dest.writeString(phone);
        dest.writeInt(support);
        dest.writeString(nickname);
        dest.writeInt(is_teacher);
        dest.writeString(app_version);
        dest.writeString(parent_user_id);
        dest.writeString(avatar);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(source);
        dest.writeString(create_date);
        dest.writeString(parent_comment_content);
        dest.writeInt(is_support);
        dest.writeString(user_id);
        dest.writeInt(parent_is_teacher);
        dest.writeString(parent_comment_id);
        dest.writeString(system_version);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String parent_user_id) {
        this.parent_user_id = parent_user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getParent_user_name() {
        return parent_user_name;
    }

    public void setParent_user_name(String parent_user_name) {
        this.parent_user_name = parent_user_name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getParent_comment_content() {
        return parent_comment_content;
    }

    public void setParent_comment_content(String parent_comment_content) {
        this.parent_comment_content = parent_comment_content;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getParent_is_teacher() {
        return parent_is_teacher;
    }

    public void setParent_is_teacher(int parent_is_teacher) {
        this.parent_is_teacher = parent_is_teacher;
    }

    public String getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(String parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }

    public String getSystem_version() {
        return system_version;
    }

    public void setSystem_version(String system_version) {
        this.system_version = system_version;
    }
}
