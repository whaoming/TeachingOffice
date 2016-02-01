package com.wxxiaomi.teachingoffice2.view.activity.base;


import com.lidroid.xutils.view.annotation.ViewInject;
import com.wxxiaomi.mingdialog.dialog.MingEditDialog;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.utils.AppManager;
import com.wxxiaomi.teachingoffice2.view.utils.CustomProgressDialog;
import com.wxxiaomi.teachingoffice2.view.utils.DialogUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {

	protected Context ct;
//	protected QLApplication app;
	@ViewInject(R.id.loading_view)
	protected View loadingView;
	@ViewInject(R.id.ll_load_fail)
	protected LinearLayout loadfailView;
//	@ViewInject(R.id.btn_left)
//	protected Button leftBtn;
	protected ImageButton rightBtn;
//	protected ImageButton leftImgBtn;
	protected TextView leftImgBtn;
	protected ImageButton rightImgBtn;
	protected TextView titleTv;
	protected TextView rightbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		ct = this;
		initView();
		loadingView = findViewById(R.id.loading_view);
		loadfailView = (LinearLayout) findViewById(R.id.ll_load_fail);
		initData();
	}

	protected void initTitleBar() {
//		leftBtn = (Button) findViewById(R.id.btn_left);
//		rightBtn = (ImageButton) findViewById(R.id.btn_right);
		rightbutton = (TextView) findViewById(R.id.rightbutton);
//		if (leftBtn != null) {
//			leftBtn.setVisibility(View.GONE);
//		}
//		if (rightBtn != null) {
//			rightBtn.setVisibility(View.GONE);
//		}
//		leftImgBtn = (ImageButton) findViewById(R.id.imgbtn_left);
		leftImgBtn = (TextView) findViewById(R.id.activity_selectimg_back);
//		rightImgBtn = (ImageButton) findViewById(R.id.imgbtn_right);
		if (rightImgBtn != null) {
			rightImgBtn.setVisibility(View.INVISIBLE);
		}
//		if (leftImgBtn != null) {
//			leftImgBtn.setImageResource(R.drawable.back);
//		}
		titleTv = (TextView) findViewById(R.id.txt_title);
		if (leftImgBtn != null) {
			leftImgBtn.setOnClickListener(this);
		}
		if (rightBtn != null) {
			rightBtn.setOnClickListener(this);
//			rightbutton.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public void onClick(View v) {
		processClick(v);
		switch (v.getId()) {
		case R.id.activity_selectimg_back:
			this.finish();
			break;
		default:
			break;
		}
		

	}

//	protected void showToast(String msg) {
//		showToast(msg, 0);
//	}

//	protected void showToast(String msg, int time) {
//		CustomToast customToast = new CustomToast(ct, msg, time);
//		customToast.show();
//	}
	MingEditDialog pDialog;
	protected void showMingProcessDialog(String content){
		pDialog = new MingEditDialog(this,
				 MingEditDialog.PROGRESS).setTitleText(content);
			pDialog.show();
			pDialog.setCancelable(false);
	}
	protected void closeMingDialog(){
		if(pDialog!=null){
			pDialog.dismiss();
		}
	}
	protected void showErrorDialog(String error){
		if(pDialog!=null){
			pDialog.setTitleText(error)
			.setConfirmText("确定")
			.changeAlertType(MingEditDialog.ERROR);
		}else{
			pDialog = new MingEditDialog(ct, MingEditDialog.ERROR);
			pDialog.setTitleText(error);
			pDialog.show();
		}
	}

	
	protected CustomProgressDialog dialog;

	protected void showProgressDialog(String content) {
		if (dialog == null && ct != null) {
			dialog = (CustomProgressDialog) DialogUtils.createProgressDialog(ct,
					content);
		}
		dialog.show();
	}

	protected void closeProgressDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	public void showLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.VISIBLE);
	}

	public void dismissLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.INVISIBLE);
	}

	public void showLoadFailView() {
		if (loadingView != null) {
			loadingView.setVisibility(View.VISIBLE);
			loadfailView.setVisibility(View.VISIBLE);
		}

	}

	public void dismissLoadFailView() {
		if (loadingView != null)
			loadfailView.setVisibility(View.INVISIBLE);
	}

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void processClick(View v);

//	protected void loadData(HttpRequest.HttpMethod method, String url,
//			RequestParams params, RequestCallBack<String> callback) {
////		HttpUtils http = new HttpUtils();
////		http.configCurrentHttpCacheExpiry(0);
////
////		LogUtils.allowD = true;
////		if (params != null) {
////			if (params.getQueryStringParams() != null)
////				LogUtils.d(url + "?" + params.getQueryStringParams().toString());
////		} else {
////			params = new RequestParams();
////
////		}
////		if (0 == CommonUtil.isNetworkAvailable(ct)) {
////			showToast("加载失败，请检查网络！");
////		} else {
////			LogUtils.d(url);
////			http.send(method, url, params, callback);
////		}
//	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	public void initRight(String a){
		rightbutton.setOnClickListener(this);
		rightbutton.setVisibility(View.VISIBLE);
		rightbutton.setText(a);
	}

	
}
