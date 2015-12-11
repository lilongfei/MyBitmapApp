
package com.llf.mybitmapapp.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.llf.mybitmapapp.R;


/**
 * The base class for all the dialog
 * It achieve control the dialog width&height&closing&showing-SoftInput&hiding-SoftInput
 */
public abstract class DialogView {
    public Dialog dialog;
    protected Window window;
    public Context context;
    private int width = 0;
    private int height = 0;
    private int layoutResID;
    private DialogManager dialog_manager;
    private static InputMethodManager manager;

    public DialogView(Context context, int layoutResID) {
        dialog_manager = DialogManager.getInstance();
        this.context = context;
        dialog = new AlertDialog.Builder(context).create();
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_UP) {
                    closeDialog(false);
                }
                return false;
            }
        });
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeDialog(false);
            }
        });
        window = dialog.getWindow();
        this.layoutResID = layoutResID;
    }

    /**
     * set the layout and LayoutParams for the dialog and then show the dialog.
     * you must call this method in showDialog() at the first line;
     */
    protected void showBegin(boolean isNeedInputMethod) {
        dialog.setCancelable(false);
        dialog.show();
        window.setBackgroundDrawableResource(R.color.translucent);
        window.setContentView(layoutResID);
        setSettingWindowSize();
        if (isNeedInputMethod) {
            showInput(context);
        }
    }

    /**
     * add the dialog into the manager.
     * you must call this method in showDialog() at the last line;
     */
    protected void showEnd() {
        dialog_manager.addDialog(this);
    }

    /**
     * set the dialog's width and height
     */
    protected void setSettingWindowSize() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        WindowManager.LayoutParams lp = window.getAttributes();
        if (width > height) {
            lp.width = height * 7 / 8;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = width * 7 / 8;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        window.setAttributes(lp);
    }

    /**
     * the sub-class must come true this method
     * the first line code must call the method show(boolean isNeedInputMethod)
     * and then, user can get the view case by super.dialog.findViewById(int resID);
     */
    protected void showDialog() {
    }

    ;

    /**
     * before create a new dialog,the previous dialog will call this method first
     */
    protected void onPause() {
    }

    /**
     * the dialog will call this method when it get focus
     */
    protected void onResume() {
    }

    /**
     * close all the dialogs that have opened
     */
    public void closeAllDialog() {
        dialog_manager.closeAllDialog();
    }

    /**
     * if you want to manage DialogManager isManagerCalling = true but at most
     * time, isManagerCalling = false
     */
    protected void closeDialog(boolean isNeedRemoveDialog) {
        hideInput(context);
        dialog.cancel();
        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        if (!isNeedRemoveDialog) {
            dialog_manager.removeDialog(this);
        }
    }

    /**
     * show the soft input method
     */
    public void showInput(Context context) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * hide the soft input method
     */
    private void hideInput(Context context) {
        if (manager == null) {
            manager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if (dialog.getCurrentFocus() != null)
            manager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
