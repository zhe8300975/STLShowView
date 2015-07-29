package com.example.zhaowencong.showstlview.othershow;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.View;

import com.example.zhaowencong.showstlview.R;
import com.example.zhaowencong.showstlview.util.Log;

/**
 * 展示文件列表
 * 
 */
public class FileListDialog extends Activity implements View.OnClickListener, OnClickListener, DialogInterface.OnKeyListener {
	private Context context = null;
	private File currentPath;
	private File[] dialogFileList;
	private int selectedIndex = -1;
	private OnFileListDialogListener listener = null;
	private boolean isDirectorySelect = false;
	private String title;
	private String extFilter;

	/**
	 * 
	 * @return selected file name
	 */
	public String getSelectedFileName() {
		String ret = "";
		if (selectedIndex >= 0) {
			ret = dialogFileList[selectedIndex].getName();
		}
		return ret;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param isDirectorySelect
	 * @param title
	 * @param extFilter
	 */
	public FileListDialog(Context context, boolean isDirectorySelect, String title, String extFilter) {
		this.isDirectorySelect = isDirectorySelect;
		this.title = title;
		this.extFilter = extFilter;
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// save current position
		selectedIndex = which;
		if ((dialogFileList == null) || (listener == null)) {
		} else {
			File file = dialogFileList[which];

			if (file.isDirectory() && !isDirectorySelect) {
				// is a directory: display file list-up again.
				show(file.getAbsolutePath());
			} else {
				// file selected. call the event listener
				listener.onClickFileList(file);
			}
		}
	}

	/**
	 * Display the file chooser dialog
	 * 
	 * @param path
	 */
	public void show(String path) {

		try {
			currentPath = new File(path);
			dialogFileList = new File(path).listFiles();
			if (dialogFileList == null) {
				// NG
				if (listener != null) {
					listener.onClickFileList(null);
				}
			} else {
				List<String> list = new ArrayList<String>();
				List<File> fileList = new ArrayList<File>();
				// create file list
				Arrays.sort(dialogFileList, new Comparator<File>() {

					@Override
					public int compare(File object1, File object2) {
						return object1.getName().toLowerCase().compareTo(object2.getName().toLowerCase());
					}
				});
				for (File file : dialogFileList) {
					if (!file.canRead()) {
						continue;
					}
					String name = null;
					if (file.isDirectory()) {
						if (!file.getName().startsWith(".")) {
							name = file.getName() + File.separator;
						}
					} else {
						if (file.getName().toLowerCase().endsWith(extFilter.toLowerCase())) {
							name = file.getName();
						}
					}
					if (name != null) {
						list.add(name);
						fileList.add(file);
					}
				}

				dialogFileList = fileList.toArray(dialogFileList);

				// Build file chooser dialog
				Builder dialog = new Builder(context).setTitle(title).setItems(list.toArray(new String[] {}), this).setOnKeyListener(this).setNeutralButton(R.string.close_dialog, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// finish the dialog
						listener.onClickFileList(null);
						dialog.dismiss();
					}
				});
				if (currentPath.getParentFile() != null) {
					dialog = dialog.setPositiveButton(R.string.parent_directory, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							File fileParent = currentPath.getParentFile();
							if (fileParent != null) {
								show(fileParent.getAbsolutePath());
								dialog.dismiss();
							} else {
								// Already the root directory: finish dialog.
								listener.onClickFileList(null);
								dialog.dismiss();
							}

						}
					});
				}
				dialog.show();
			}
		} catch (SecurityException se) {
			Log.e(se);
		} catch (Exception e) {
			Log.e(e);
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			File fileParent = currentPath.getParentFile();
			if (fileParent != null) {
				show(fileParent.getAbsolutePath());
				dialog.dismiss();
			} else {
				// Already the root directory: finish dialog.
				listener.onClickFileList(null);
				dialog.dismiss();
			}

			return true;
		}
		return false;
	}

	public void setOnFileListDialogListener(OnFileListDialogListener listener) {
		this.listener = listener;
	}

	public interface OnFileListDialogListener {
		public void onClickFileList(File file);
	}

}
