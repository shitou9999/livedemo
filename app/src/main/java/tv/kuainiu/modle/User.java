package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

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
    private String session_id;
    private int is_teacher;


    public User() {
    }

    /**
     * @param area  Region code
     * @param phone Phone number
     * @param email Email address
     */
    public User(String area, String phone, String email) {
        this.area = area;
        this.phone = phone;
        this.email = email;
    }


    protected User(Parcel in) {
        user_id = in.readString();
        phone = in.readString();
        email = in.readString();
        realname = in.readString();
        idno = in.readString();
        area = in.readString();
        nickname = in.readString();
        province = in.readString();
        city = in.readString();
        qq = in.readString();
        gender = in.readString();
        birthday = in.readString();
        avatar = in.readString();
        follow_count = in.readInt();
        subscibe_count = in.readInt();
        allow_push = in.readInt();
        msg_num = in.readInt();
        fans_count = in.readInt();
        session_id = in.readString();
        is_teacher = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(realname);
        dest.writeString(idno);
        dest.writeString(area);
        dest.writeString(nickname);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(qq);
        dest.writeString(gender);
        dest.writeString(birthday);
        dest.writeString(avatar);
        dest.writeInt(follow_count);
        dest.writeInt(subscibe_count);
        dest.writeInt(allow_push);
        dest.writeInt(msg_num);
        dest.writeInt(fans_count);
        dest.writeString(session_id);
        dest.writeInt(is_teacher);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
}
