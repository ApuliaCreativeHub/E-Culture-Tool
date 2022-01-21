package com.apuliacreativehub.eculturetool.ui.component;


import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class ModalBottomSheetUtil {

    public static final String TAG = "ModalBottomSheet";

    public static BottomSheetBehavior getBehavior(View view) {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setFitToContents(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        return bottomSheetBehavior;
    }
}