package com.snc.zero.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.snc.zero.log.Logger;
import com.snc.zero.resource.ResourceUtil;

/**
 * Dialog Builder
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class DialogBuilder {
	private static final String TAG = DialogBuilder.class.getSimpleName();

	public static DialogBuilder with(Context context) {
		return new DialogBuilder((Activity) context);
	}

	public static DialogBuilder with(Activity activity) {
		return new DialogBuilder(activity);
	}

	private final Activity activity;
	private String title;
	private String message;
	private CharSequence positiveButtonText;
	private DialogInterface.OnClickListener positiveButtonListener;
	private CharSequence negativeButtonText;
	private DialogInterface.OnClickListener negativeButtonListener;
	private CharSequence neutralButtonText;
	private DialogInterface.OnClickListener neutralButtonListener;

	public DialogBuilder(Activity activity) {
		this.activity = activity;
	}

	public DialogBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	/*
	public DialogBuilder setTitle(int resId) {
		this.title = ResourceUtil.getString(activity, resId);
		return this;
	}
	 */

	public DialogBuilder setMessage(String message) {
		this.message = message;
		return this;
	}

	public DialogBuilder setMessage(int resId) {
		this.message = ResourceUtil.getString(activity, resId);
		return this;
	}

	public DialogBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
		this.positiveButtonText = text;
		this.positiveButtonListener = listener;
		return this;
	}
	public DialogBuilder setPositiveButton(int resId, DialogInterface.OnClickListener listener) {
		return setPositiveButton(ResourceUtil.getString(activity, resId), listener);
	}

	public DialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
		this.negativeButtonText = text;
		this.negativeButtonListener = listener;
		return this;
	}
	public DialogBuilder setNegativeButton(int resId, DialogInterface.OnClickListener listener) {
		return setNegativeButton(ResourceUtil.getString(activity, resId), listener);
	}

	@SuppressWarnings("UnusedReturnValue")
	public DialogBuilder setNeutralButton(CharSequence text, final DialogInterface.OnClickListener listener) {
		this.neutralButtonText = text;
		this.neutralButtonListener = listener;
		return this;
	}
	public DialogBuilder setNeutralButton(int resId, final DialogInterface.OnClickListener listener) {
		return setNeutralButton(ResourceUtil.getString(activity, resId), listener);
	}

	public void show() {
		try {
			Logger.e(TAG, "[Dialog Show] " + message);
			if (activity.isFinishing()) {
				Logger.e(TAG, "activity is finished.");
				return;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			if (null != title) {
				builder.setTitle(title);
			}
			if (null != message) {
				builder.setMessage(message);
			}
			if (null != positiveButtonListener) {
				builder.setPositiveButton(positiveButtonText, positiveButtonListener);
			}
			if (null != negativeButtonListener) {
				builder.setNegativeButton(negativeButtonText, negativeButtonListener);
			}
			if (null != neutralButtonListener) {
				builder.setNeutralButton(neutralButtonText, neutralButtonListener);
			}
			if (null == positiveButtonListener && null == negativeButtonListener && null == neutralButtonListener) {
				builder.setNegativeButton(android.R.string.ok, (dialog, which) -> {

				});
			}

			builder.setCancelable(false);

			Dialog dialog = builder.show();
			dialog.setCanceledOnTouchOutside(false);

		} catch (android.view.WindowManager.BadTokenException e) {
			Logger.e(activity.getClass().getSimpleName(), e);
		}
	}

}
