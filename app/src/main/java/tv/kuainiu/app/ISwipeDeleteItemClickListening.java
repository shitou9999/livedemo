package tv.kuainiu.app;

import com.daimajia.swipe.SwipeLayout;

/**
 * Created by sirius on 2016/10/21.
 */

public interface ISwipeDeleteItemClickListening {
    public void delete(SwipeLayout swipeLayout, int position, Object object);
}
