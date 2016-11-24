package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guxuan on 2016/3/12.
 */
public class User implements Parcelable {


    /**
     * user_id : 29
     * phone : 13127691195
     * email :
     * realname :
     * idno :
     * nickname :
     * province :
     * city :
     * qq  :
     * gender  :
     * birthday  : 0000-00-00
     * avatar  : null
     * follow_count  : 7
     * subscibe_count  : 0
     * allow_push  : 0
     */

    private String user_id;
    private String phone;
    private String email;
    private String realname;
    private String idno;
    private String area;
    private String nickname;
    private String province;
    private String city;
    private String qq;
    private String gender;
    private String birthday;
    private String avatar;
    private int follow_count;
    private int subscibe_count;
    private int allow_push;
    private int msg_num;
    private int fans_count;
    private int live_count;
    private int live_wait_count;
    private int appointment_count;

    private String session_id;
    private int is_teacher;
    private List<Permission> permission_list;
    private ThirdBind third_bind;



    public ThirdBind getThird_bind() {
        return third_bind;
    }

    public void setThird_bind(ThirdBind third_bind) {
        this.third_bind = third_bind;
    }

    public int getLive_count() {
        return live_count;
    }

    public void setLive_count(int live_count) {
        this.live_count = live_count;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public void setSubscibe_count(int subscibe_count) {
        this.subscibe_count = subscibe_count;
    }

    public void setAllow_push(int allow_push) {
        this.allow_push = allow_push;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRealname() {
        return realname;
    }

    public String getIdno() {
        return idno;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getQq() {
        return qq;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public int getSubscibe_count() {
        return subscibe_count;
    }

    public int getAllow_push() {
        return allow_push;
    }

    public int getMsg_num() {
        return msg_num;
    }

    public void setMsg_num(int msg_num) {
        this.msg_num = msg_num;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public int getLive_wait_count() {
        return live_wait_count;
    }

    public void setLive_wait_count(int live_wait_count) {
        this.live_wait_count = live_wait_count;
    }

    public int getAppointment_count() {
        return appointment_count;
    }

    public void setAppointment_count(int appointment_count) {
        this.appointment_count = appointment_count;
    }

    public List<Permission> getPermission_list() {
        return permission_list;
    }

    public void setPermission_list(List<Permission> permission_list) {
        this.permission_list = permission_list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.realname);
        dest.writeString(this.idno);
        dest.writeString(this.area);
        dest.writeString(this.nickname);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.qq);
        dest.writeString(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.avatar);
        dest.writeInt(this.follow_count);
        dest.writeInt(this.subscibe_count);
        dest.writeInt(this.allow_push);
        dest.writeInt(this.msg_num);
        dest.writeInt(this.fans_count);
        dest.writeInt(this.live_count);
        dest.writeInt(this.live_wait_count);
        dest.writeInt(this.appointment_count);
        dest.writeString(this.session_id);
        dest.writeInt(this.is_teacher);
        dest.writeList(this.permission_list);
        dest.writeParcelable(this.third_bind, flags);
    }

    public User() {
    }

    public User(String email, String phone, String area) {
        this.email = email;
        this.phone = phone;
        this.area = area;
    }

    public User(String area, String phone) {
        this.area = area;
        this.phone = phone;
    }

    protected User(Parcel in) {
        this.user_id = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.realname = in.readString();
        this.idno = in.readString();
        this.area = in.readString();
        this.nickname = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.qq = in.readString();
        this.gender = in.readString();
        this.birthday = in.readString();
        this.avatar = in.readString();
        this.follow_count = in.readInt();
        this.subscibe_count = in.readInt();
        this.allow_push = in.readInt();
        this.msg_num = in.readInt();
        this.fans_count = in.readInt();
        this.live_count = in.readInt();
        this.live_wait_count = in.readInt();
        this.appointment_count = in.readInt();
        this.session_id = in.readString();
        this.is_teacher = in.readInt();
        this.permission_list = new ArrayList<Permission>();
        in.readList(this.permission_list, Permission.class.getClassLoader());
        this.third_bind = in.readParcelable(ThirdBind.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
