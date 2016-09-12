package com.kuainiutv.app;

import android.content.res.ColorStateList;
import android.graphics.Color;

/**
 * Created by jack on 2016/9/6.
 */
public class Theme {
    private static String commonColor = "#FF0000";
    private static String commonNormalColor = "#616161";

    public static String getCommonColor() {
        return commonColor;
    }

    public static void setCommonColor(String _commonColor) {
        commonColor = _commonColor;
    }

    public static String getCommonNormalColor() {
        return commonNormalColor;
    }

    public static void setCommonNormalColor(String commonNormalColor) {
        Theme.commonNormalColor = commonNormalColor;
    }

    public static ColorStateList getColorSelectedStateList() {
        int[][] state = new int[2][];
        state[0] = new int[]{android.R.attr.state_selected};
        state[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(Theme.getCommonColor()), Color.parseColor(Theme.getCommonNormalColor())};
        return new ColorStateList(state, colors);
    }

    public static ColorStateList getColorCheckedStateList() {
        int[][] state = new int[2][];
        state[0] = new int[]{android.R.attr.state_checked};
        state[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(Theme.getCommonColor()), Color.parseColor("#ffffff")};
        return new ColorStateList(state, colors);
    }

    public static int[] getLoadingColor() {
        return new int[]{Color.parseColor(Theme.getCommonColor())};
    }
}
