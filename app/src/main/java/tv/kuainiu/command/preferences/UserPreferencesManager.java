package tv.kuainiu.command.preferences;


import tv.kuainiu.IGXApplication;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.modle.User;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.SecurityUtils;

/**
 * @author nanck on 2016/6/3.
 */
public class UserPreferencesManager {
    /**
     * Region code
     */
    public static final String AREA = "area";
    /**
     * Phone number
     */
    public static final String PHONE = "phone";
    /**
     * Email address
     */
    public static final String EMAIL = "email";


    public static void putUserExtraInfo(User user) {
        if (user != null) {
            PreferencesUtils.putString(IGXApplication.getInstance(), AREA, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getArea()));
            PreferencesUtils.putString(IGXApplication.getInstance(), PHONE, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getPhone()));
            PreferencesUtils.putString(IGXApplication.getInstance(), EMAIL, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getEmail()));
        }
    }

    public static User getUserExtraInfo() {
        String area = PreferencesUtils.getString(IGXApplication.getInstance(), AREA);
        String phone = PreferencesUtils.getString(IGXApplication.getInstance(), PHONE);
        String email = PreferencesUtils.getString(IGXApplication.getInstance(), EMAIL);
        return new User(SecurityUtils.DESUtil.de(Api.PUBLIC_KEY, area),
                SecurityUtils.DESUtil.de(Api.PUBLIC_KEY, phone), SecurityUtils.DESUtil.de(Api.PUBLIC_KEY, email));
    }
}
