package tv.kuainiu.modle.push;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jack on 2016/9/23.
 */
public class CustomVideo implements Parcelable {

    /**
     * id : 19
     * cat_id : 3
     * type : 2
     * title : 9-21看今日的大盘视频
     * thumb : http://img.iguxuan.com/uploadfile/2016/0920/t_20160920111208178.png
     * url :
     * user_id : 2
     * description : 大盘视频不错
     * inputtime : 1474428746
     * video_id : 5A64D9A01D1415539C33DC5901307461
     * support_num : 0
     * comment_num : 0
     * view_num : 0
     * is_official : 0
     * catname : 创业板
     * nickname : 用户f79kxd
     * avatar : http://img.iguxuan.com/uploadfile/avatar/default.png
     */

    private String id;
    private String cat_id;
    private int type;
    private String title;
    private String thumb;
    private String url;
    private String user_id;
    private String description;
    private long inputtime;
    private String video_id;
    private int support_num;
    private int is_support;
    private String comment_num;
    private String view_num;
    private String is_official;
    private String catname;
    private String nickname;
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getInputtime() {
        return inputtime;
    }

    public void setInputtime(long inputtime) {
        this.inputtime = inputtime;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public int getSupport_num() {
        return support_num;
    }

    public void setSupport_num(int support_num) {
        this.support_num = support_num;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

    public String getIs_official() {
        return is_official;
    }

    public void setIs_official(String is_official) {
        this.is_official = is_official;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.cat_id);
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeString(this.thumb);
        dest.writeString(this.url);
        dest.writeString(this.user_id);
        dest.writeString(this.description);
        dest.writeLong(this.inputtime);
        dest.writeString(this.video_id);
        dest.writeInt(this.support_num);
        dest.writeInt(this.is_support);
        dest.writeString(this.comment_num);
        dest.writeString(this.view_num);
        dest.writeString(this.is_official);
        dest.writeString(this.catname);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
    }

    public CustomVideo() {
    }

    protected CustomVideo(Parcel in) {
        this.id = in.readString();
        this.cat_id = in.readString();
        this.type = in.readInt();
        this.title = in.readString();
        this.thumb = in.readString();
        this.url = in.readString();
        this.user_id = in.readString();
        this.description = in.readString();
        this.inputtime = in.readLong();
        this.video_id = in.readString();
        this.support_num = in.readInt();
        this.is_support = in.readInt();
        this.comment_num = in.readString();
        this.view_num = in.readString();
        this.is_official = in.readString();
        this.catname = in.readString();
        this.nickname = in.readString();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<CustomVideo> CREATOR = new Parcelable.Creator<CustomVideo>() {
        @Override
        public CustomVideo createFromParcel(Parcel source) {
            return new CustomVideo(source);
        }

        @Override
        public CustomVideo[] newArray(int size) {
            return new CustomVideo[size];
        }
    };
}
