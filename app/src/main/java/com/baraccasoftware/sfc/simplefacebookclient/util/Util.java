package com.baraccasoftware.sfc.simplefacebookclient.util;

import android.os.Build;

/**
 * Created by angelomoroni on 26/12/14.
 */
public class Util {

    public static boolean versionIsOlderThanLollipop(){
        return (Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP);
    }
}
