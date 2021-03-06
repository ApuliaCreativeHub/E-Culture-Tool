package com.apuliacreativehub.eculturetool.ui.component;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class Utils {
    public final static String SCHEME_ANDROID_RESOURCE = "android.resource";
    public final static String DRAWABLE_URI_BASE_PATH = SCHEME_ANDROID_RESOURCE + "://com.apuliacreativehub.eculturetool/drawable/";

    public static boolean checkConnection(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("pathFromUri", "getRealPathFromURI Exception : " + e);
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
