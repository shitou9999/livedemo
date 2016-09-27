package tv.kuainiu.ui.liveold;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import tv.kuainiu.command.dao.LiveSubscribeDao;
import tv.kuainiu.ui.liveold.model.LiveInfoEntry;
import tv.kuainiu.modle.Remind;
import tv.kuainiu.utils.DebugUtils;

/**
 * 直播提醒通知工具
 * @author nanck on 2016/7/22.
 */
public class RemindUtils {
    public static final int UN_REMIND = 0;
    public static final int REMIND = 1;


    // XXX
    public static void translate(LinkedList<Remind> reminds, List<LiveInfoEntry> liveInfoEntries) {
        if (liveInfoEntries == null || liveInfoEntries.isEmpty()
                || reminds == null || reminds.isEmpty()) {
            return;
        }


        for (Remind remind : reminds) {
            for (LiveInfoEntry entry : liveInfoEntries) {
                if (remind.getId().equals(entry.getId())) {
                    entry.setRemindFlag(REMIND);
                }
            }
        }

    }

    // XXX
    public static void translate(Context context, List<LiveInfoEntry> liveInfoEntries) {
        LinkedList<Remind> reminds = new LiveSubscribeDao(context).fetchReminds();
        DebugUtils.dd("For db list : " + reminds.toString());
        translate(reminds, liveInfoEntries);
    }

}
