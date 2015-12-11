package com.llf.mybitmapapp.dialog;

import java.util.ArrayList;

/**
 * Manage all dialogs that is alive
 * DialogManager can add and remove the dialog,and control the method onResume()&onPause() called.
 * */
public class DialogManager
{
	private static ArrayList<DialogView> dialog_array = new ArrayList<DialogView>();
	
	private DialogManager()
	{
	}
	
	private static DialogManager dialog_manager;
	
	public static DialogManager getInstance()
	{
		if (dialog_manager == null)
		{
			dialog_manager = new DialogManager();
		}
		return dialog_manager;
	}
	
	/**
	 * Before add a dialog,call the previous dialog's onPause().
	 * After add the dialog,call its onResume();
	 * */
	public void addDialog(DialogView dialog)
	{
		if (dialog_array.size() > 0)
		{
			dialog_array.get(dialog_array.size() - 1).onPause();
		}
		dialog_array.add(dialog);
		dialog_array.get(dialog_array.size() - 1).onResume();
	}
	
	/**
	 * Before remove a dialog,call its onPause().
	 * After remove the dialog,call the previous dialog's onResume();
	 * */
	public void removeDialog(DialogView dialog)
	{
		if (dialog_array.contains(dialog))
		{
			dialog.onPause();
			dialog_array.remove(dialog);
			if (dialog_array.size() > 0)
			{
				dialog_array.get(dialog_array.size() - 1).onResume();
			}
		}
	}
	
	public static int getDialogCount()
	{
		return dialog_array.size();
	}
	
	/**
	 * Close all the dialogs
	 * */
	public static void closeNewestDialog(int index)
	{
		dialog_array.get(index).closeDialog(true);
		dialog_array.remove(index);
	}
	
	/**
	 * Close all the dialogs
	 * */
	public void closeAllDialog()
	{
		for (int i = getDialogCount() - 1; i > -1; i--)
		{
			dialog_array.get(i).closeDialog(true);
			dialog_array.remove(i);
		}
	}
	
	public static DialogView getTopDialog()
	{
		return dialog_array.get(dialog_array.size() - 1);
	}
}
