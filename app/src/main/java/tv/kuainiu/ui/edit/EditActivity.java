package tv.kuainiu.ui.edit;


import android.os.Bundle;

import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.ui.activity.BaseActivity;


/**
 * Created by sirius on 2016/11/7.
 */

public class EditActivity extends BaseActivity {
    private static final int COLOR_REQUEST=1337;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
    }
//    @Override
//    public boolean pick(ColorPickerOperation op) {
//        Intent i=new Intent(this, ColorMixerActivity.class);
//
//        i.putExtra(ColorMixerActivity.TITLE, "Pick a Color");
//
//        if (op.hasColor()) {
//            i.putExtra(ColorMixerActivity.COLOR, op.getColor());
//        }
//
//        this.colorPickerOp=op;
//        startActivityForResult(i, COLOR_REQUEST);
//
//        return(true);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 Intent result) {
//        int color=0;
//
//        if (colorPickerOp!=null && requestCode==COLOR_REQUEST) {
//            if (resultCode== Activity.RESULT_OK) {
//                color=result.getIntExtra(ColorMixerActivity.COLOR, 0);
//            }
//
//            if (color==0) {
//                colorPickerOp.onPickerDismissed();
//            }
//            else {
//                colorPickerOp.onColorPicked(color);
//            }
//
//            colorPickerOp=null;
//        }
//    }
}
