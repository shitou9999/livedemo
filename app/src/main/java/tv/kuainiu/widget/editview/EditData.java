package tv.kuainiu.widget.editview;

import android.graphics.Bitmap;
import android.text.Spanned;

import java.io.Serializable;

/**
 * Created by sirius on 2016/11/8.
 */

public class EditData implements Serializable {
    public Spanned inputStr;
    public String imagePath;
    public Bitmap bitmap;
}
