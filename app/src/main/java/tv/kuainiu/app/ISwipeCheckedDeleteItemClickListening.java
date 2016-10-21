package tv.kuainiu.app;

/**
 * Created by sirius on 2016/10/21.
 */

public interface ISwipeCheckedDeleteItemClickListening extends ISwipeDeleteItemClickListening {
    public void selected(Object object);

    public void unelected(Object object);
}
