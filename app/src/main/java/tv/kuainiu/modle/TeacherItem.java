package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guxuan on 2016/3/8.
 */
public class TeacherItem implements Parcelable {


    /**
     * id : 1
     * catid : 28
     * title : 廖英强
     * url : http://www.iguxuan.com/index.php?m=content&c=teacher&a=index&teacherid=1
     * description : 用技术打开股市财富之门
     * thumb : http://img.iguxuan.com/statics/images/gx/TouXiang/LiaoYingQiang.jpg
     * banner : http://img.iguxuan.com/statics/images/gx/special/lyq.jpg
     * class : teacher
     * body_thumb : http://img.iguxuan.com/gx/public/img/teacher/body_thumb/lyq.jpg
     * vedio_count : 178
     * is_follow : 1
     * fans_count : 10
     * is_push : 0
     */

    public String id;
    public String catid;
    public String title;
    public String url;
    public String description;
    public String thumb;
    public String banner;
    @SerializedName("class") public String classX;
    public String body_thumb;
    public String vedio_count;
    public int is_follow;
    public int fans_count;
    public int is_push;

    @Override public String toString() {
        return "TeacherItem{" +
                "id='" + id + '\'' +
                ", catid='" + catid + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", thumb='" + thumb + '\'' +
                ", banner='" + banner + '\'' +
                ", classX='" + classX + '\'' +
                ", body_thumb='" + body_thumb + '\'' +
                ", vedio_count='" + vedio_count + '\'' +
                ", is_follow=" + is_follow +
                ", fans_count='" + fans_count + '\'' +
                ", is_push=" + is_push +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.catid);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.description);
        dest.writeString(this.thumb);
        dest.writeString(this.banner);
        dest.writeString(this.classX);
        dest.writeString(this.body_thumb);
        dest.writeString(this.vedio_count);
        dest.writeInt(this.is_follow);
        dest.writeInt(this.fans_count);
        dest.writeInt(this.is_push);
    }

    public TeacherItem() {
    }

    protected TeacherItem(Parcel in) {
        this.id = in.readString();
        this.catid = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.description = in.readString();
        this.thumb = in.readString();
        this.banner = in.readString();
        this.classX = in.readString();
        this.body_thumb = in.readString();
        this.vedio_count = in.readString();
        this.is_follow = in.readInt();
        this.fans_count = in.readInt();
        this.is_push = in.readInt();
    }

    public static final Parcelable.Creator<TeacherItem> CREATOR = new Parcelable.Creator<TeacherItem>() {
        @Override public TeacherItem createFromParcel(Parcel source) {
            return new TeacherItem(source);
        }

        @Override public TeacherItem[] newArray(int size) {
            return new TeacherItem[size];
        }
    };
}
