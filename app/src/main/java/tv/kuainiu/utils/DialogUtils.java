package tv.kuainiu.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import tv.kuainiu.R;
import tv.kuainiu.ui.MainActivity;

/**
 * Created by jack on 2016/5/10.
 */
public class DialogUtils {
    private static boolean isShowTipAccountConflict = false;
    private static boolean isShowTipAccountConflict2 = false;

    public static void TipAccountConflict(Context context, String content, final IDialogButtonOnClickListener iDialogButtonOnClickListener) {
        if (isShowTipAccountConflict) {
            return;
        }
        isShowTipAccountConflict = true;
        showOffLineTip(context, content, iDialogButtonOnClickListener);
    }
    public static void TipAccountConflictMainActivity(Context context, String content, final IDialogButtonOnClickListener iDialogButtonOnClickListener) {
        if (MainActivity.isPause) {
            return;
        }
        if (isShowTipAccountConflict) {
            return;
        }
        showOffLineTip(context, content, iDialogButtonOnClickListener);
    }

    private static void showOffLineTip(Context context, String content, final IDialogButtonOnClickListener iDialogButtonOnClickListener) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder.withTitle(null).withMessage(null).withDialogColor(context.getResources().getColor(R.color.white_color)).isCancelableOnTouchOutside(false);
        dialogBuilder.setCustomView(R.layout.dialog_push_message, context);
        TextView tv_dialog_title = (TextView) dialogBuilder.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setVisibility(View.GONE);
        TextView pushMessageContent = (TextView) dialogBuilder.findViewById(R.id.tv_push_message_content);
        TextView pushMessageCancel = (TextView) dialogBuilder.findViewById(R.id.tv_pushMessageCancel);
        pushMessageCancel.setVisibility(View.GONE);
        TextView pushMessageRead = (TextView) dialogBuilder.findViewById(R.id.tv_pushMessageRead);
        pushMessageRead.setText("确定");
        View v_dialog_line = dialogBuilder.findViewById(R.id.v_dialog_line);
        v_dialog_line.setVisibility(View.GONE);
        pushMessageContent.setText(content);
        pushMessageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                iDialogButtonOnClickListener.cancel();
            }
        });
        pushMessageRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                iDialogButtonOnClickListener.sure();
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowTipAccountConflict = false;
            }
        });
        dialogBuilder.show();
    }


    public static void TipInitApp(Context context, String content, final IDialogButtonOnClickListener iDialogButtonOnClickListener) {
        if (isShowTipAccountConflict) {
            return;
        }
        isShowTipAccountConflict = true;
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder.withTitle(null).withMessage(null).withDialogColor(context.getResources().getColor(R.color.white_color)).isCancelableOnTouchOutside(false);
        dialogBuilder.setCustomView(R.layout.dialog_push_message, context);
        TextView tv_dialog_title = (TextView) dialogBuilder.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setVisibility(View.GONE);
        TextView pushMessageContent = (TextView) dialogBuilder.findViewById(R.id.tv_push_message_content);
        TextView pushMessageCancel = (TextView) dialogBuilder.findViewById(R.id.tv_pushMessageCancel);
        pushMessageCancel.setVisibility(View.GONE);
        TextView pushMessageRead = (TextView) dialogBuilder.findViewById(R.id.tv_pushMessageRead);
        pushMessageRead.setText("确定");
        View v_dialog_line = dialogBuilder.findViewById(R.id.v_dialog_line);
        pushMessageContent.setText(content);
        pushMessageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                iDialogButtonOnClickListener.cancel();
            }
        });
        pushMessageRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                iDialogButtonOnClickListener.sure();
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowTipAccountConflict = false;
            }
        });
        dialogBuilder.show();
    }

    public interface IDialogButtonOnClickListener {
        void cancel();

        void sure();
    }
}
