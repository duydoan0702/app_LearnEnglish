package com.mobiai.app.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class DynamicUnitUtils {
    public static int convertDpToPixels(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().getDisplayMetrics()));}

    public static int convertPixelsToDp(int pixels) {
        return Math.round(pixels / Resources.getSystem().getDisplayMetrics().density);}

    public static int convertSpToPixels(float sp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, Resources.getSystem().getDisplayMetrics()));}

    public static int convertPixelsToSp(int pixels) {
        return Math.round(pixels / Resources.getSystem().getDisplayMetrics().density);}

    public static int convertDpToSp(float dp) {
        return Math.round(convertDpToPixels(dp) / (float) convertSpToPixels(dp));}
}