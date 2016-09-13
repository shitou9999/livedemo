package tv.kuainiu.command.guide;


/**
 * 引导管理
 *
 * @author nanck on 2016/5/11.
 * @deprecated
 */
public class GuideManager {
    private int mVersionCode;
    private IGuideListener mGuideListener;

    private static GuideManager mInstance;

    public static synchronized GuideManager getInstance() {
        if (mInstance == null) {
            mInstance = new GuideManager();
        }
        return mInstance;
    }

    private GuideManager() {
    }

    public void setGuideListener(IGuideListener guideListener) {
        mGuideListener = guideListener;
    }

    public void setVersionCode(int versionCode) {
        mVersionCode = versionCode;
    }

    public void needLogin(boolean bool) {

    }

    public void update() {

    }
}
