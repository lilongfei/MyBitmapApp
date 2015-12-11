package com.llf.mybitmapapp.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by burke.li on 10/12/2015.
 */
public class ToastUtils {
    private static Toast toast;

    public static void toastMessage(Context context, int resID){
        if(toast == null){
            toast = Toast.makeText(context, context.getString(resID), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(context.getString(resID));
        toast.show();
    }

    public static void toastMessage(Context context, String mes){
        if(toast == null){
            toast = Toast.makeText(context, mes, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(mes);
        toast.show();
    }
}
