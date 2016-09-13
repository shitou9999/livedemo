package tv.kuainiu.event;


import tv.kuainiu.modle.User;

/**
 * @author nanck 2016/3/12.
 */
public class UserEvent {
    private int code;
    private String msg;
    private User user;

    public UserEvent(int code, String msg, User user) {
        this.code = code;
        this.msg = msg;
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public User getUser() {
        return user;
    }
}
