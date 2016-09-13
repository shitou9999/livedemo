package tv.kuainiu.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import tv.kuainiu.R;

/**
 * Created by jack on 2016/5/10.
 */
public class CheckCodePicker {
    private static boolean isShowTipAccountConflict = false;

    public static void selectorCheckCode(Context context, final IDialogButtonOnClickListener iDialogButtonOnClickListener) {
        if (isShowTipAccountConflict) {
            return;
        }
        isShowTipAccountConflict = true;
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder.withTitle(null).withMessage(null).withDialogColor(context.getResources().getColor(R.color.white_color)).isCancelableOnTouchOutside(false);
        dialogBuilder.setCustomView(R.layout.layout_selector_check_code, context);

        LinearLayout buttonMessage = (LinearLayout) dialogBuilder.findViewById(R.id.ll_message);
        LinearLayout buttonCall = (LinearLayout) dialogBuilder.findViewById(R.id.ll_call);

        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialogButtonOnClickListener.message();
                dialogBuilder.dismiss();
            }
        });
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialogButtonOnClickListener.call();
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                isShowTipAccountConflict = false;
            }
        });
        dialogBuilder.show();
    }


    public interface IDialogButtonOnClickListener {
        void message();

        void call();
    }
}
