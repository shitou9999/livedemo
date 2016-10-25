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
     *
     * 	"id" : "1",
     "nickname" : "獠牙123",
     "avatar" : "http:\/\/img.iguxuan.com\/uploadfile\/avatar\/default.png",
     "slogan" : "信李治,得天下",
     "listorder" : "0",
     "fans_count" : "1",
     "is_follow" : 0
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

    public String nickname;
    public String avatar;
    public String slogan;
    public String listorder;
    public int is_living;
    public String[] tag_list;


    protected TeacherItem(Parcel in) {
        id = in.readString();
        catid = in.readString();
        title = in.readString();
        url = in.readString();
        description = in.readString();
        thumb = in.readString();
        banner = in.readString();
        classX = in.readString();
        body_thumb = in.readString();
        vedio_count = in.readString();
        is_follow = in.readInt();
        fans_count = in.readInt();
        is_push = in.readInt();
        nickname = in.readString();
        avatar = in.readString();
        slogan = in.readString();
        listorder = in.readString();
        is_living = in.readInt();
        tag_list = in.createStringArray();
    }

    public static final Creator<TeacherItem> CREATOR = new Creator<TeacherItem>() {
        @Override
        public TeacherItem createFromParcel(Parcel in) {
            return new TeacherItem(in);
        }

        @Override
        public TeacherItem[] newArray(int size) {
            return new TeacherItem[size];
        }
    };

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

    @Override public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(catid);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(description);
        parcel.writeString(thumb);
        parcel.writeString(banner);
        parcel.writeString(classX);
        parcel.writeString(body_thumb);
        parcel.writeString(vedio_count);
        parcel.writeInt(is_follow);
        parcel.writeInt(fans_count);
        parcel.writeInt(is_push);
        parcel.writeString(nickname);
        parcel.writeString(avatar);
        parcel.writeString(slogan);
        parcel.writeString(listorder);
    }


    public TeacherItem() {
    }



}
