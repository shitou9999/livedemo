package tv.kuainiu.command.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by sirius on 2016/10/18.
 */

public class AppointmentRequestUtil {
    /**
     * 预约
     */
    public static void fetchList(int page, Context context, String type, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("type", StringUtils.replaceNullToEmpty(type, "1"));
        OKHttpUtils.getInstance().post(context, Api.live_appointment_list, ParamUtil.getParam(map), action);
    }

    public static void addAppointment(Context context, String teacher_id, String live_id, String room_id, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("teacher_id", StringUtils.replaceNullToEmpty(teacher_id));
        map.put("live_id", StringUtils.replaceNullToEmpty(live_id));
        map.put("room_id", StringUtils.replaceNullToEmpty(room_id));
        OKHttpUtils.getInstance().post(context, Api.add_live_appointment, ParamUtil.getParam(map), action);
    }

    public static void deleteAppointment(Context context, String live_id, Action action) {
        Map<String, String> map = new HashMap<>();
        map.put("live_id", StringUtils.replaceNullToEmpty(live_id));
        OKHttpUtils.getInstance().post(context, Api.del_live_appointment, ParamUtil.getParam(map), action);
    }
}
