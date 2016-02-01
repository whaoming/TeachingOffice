package com.wxxiaomi.teachingoffice2.view.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;


public class DialogUtils {
	
	
	/**
	 * 创建进度对话框
	 * 
	 * @param context
	 * @param content
	 * @return
	 */
	public static Dialog createProgressDialog(Context context, String content) {
		return new CustomProgressDialog(context, content);
	}
	/**
	 * Toast提示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast提示
	 * 
	 * @param context
	 * @param msgId
	 */
	public static void showToast(Context context, int msgId) {
		Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	/*
	 * public static Dialog createLoadingDialog(Context context, String msg) {
	 * LayoutInflater inflater = LayoutInflater.from(context); View v =
	 * inflater.inflate(R.layout.loading_dialog, null);// 得到加载view LinearLayout
	 * layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局 //
	 * main.xml中的ImageView ImageView spaceshipImage = (ImageView)
	 * v.findViewById(R.id.img); TextView tipTextView = (TextView)
	 * v.findViewById(R.id.tipTextView);// 提示文字 // 加载动画 Animation
	 * hyperspaceJumpAnimation =
	 * AnimationUtils.loadAnimation(context,R.anim.loading_animation); //
	 * 使用ImageView显示动画 spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	 * tipTextView.setText(msg);// 设置加载信息 Dialog loadingDialog = new
	 * Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
	 * //loadingDialog.setCancelable(false);// 不可以用“返回键”取消
	 * loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
	 * LinearLayout.LayoutParams.MATCH_PARENT,
	 * LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局 return loadingDialog; }
	 */

	/**
	 * Dialog
	 * 
	 * @param context
	 * @param iconId
	 * @param title
	 * @param message
	 * @param positiveBtnName
	 * @param negativeBtnName
	 * @param positiveBtnListener
	 * @param negativeBtnListener
	 * @return
	 */
	public static Dialog createConfirmDialog(
			Context context,
			int iconId,
			String title,
			String message,
			String positiveBtnName,
			String negativeBtnName,
			android.content.DialogInterface.OnClickListener positiveBtnListener,
			android.content.DialogInterface.OnClickListener negativeBtnListener) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		dialog = builder.create();
		return dialog;
	}

}
