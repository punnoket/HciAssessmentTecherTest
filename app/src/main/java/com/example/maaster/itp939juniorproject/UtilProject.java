package com.example.maaster.itp939juniorproject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 30/10/2559.
 */
public class UtilProject {

    public static boolean internetIsConnect(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        return i!=null && i.isConnected() && i.isAvailable();
    }

    public static void showErrorDialog(final Activity mActivity, String title, String desc, boolean b) {
        showErrorDialog(mActivity, title, desc, b);
    }
}
