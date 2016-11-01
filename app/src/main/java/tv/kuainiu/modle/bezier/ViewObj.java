package tv.kuainiu.modle.bezier;

import android.widget.ImageView;

/**
 * Created by sirius on 2016/11/1.
 */

public class ViewObj {
    private final ImageView red;

    public ViewObj(ImageView red){
        this.red = red;
    }

    public void setFabLoc(ViewPoint newLoc){
        red.setTranslationX(newLoc.x);
        red.setTranslationY(newLoc.y);
    }
}
