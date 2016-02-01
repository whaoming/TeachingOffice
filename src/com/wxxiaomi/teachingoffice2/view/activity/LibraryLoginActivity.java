package com.wxxiaomi.teachingoffice2.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_lib_Login;
import com.wxxiaomi.teachingoffice2.engine.LibraryEngineImpl;
import com.wxxiaomi.teachingoffice2.utils.SharePrefUtil;
import com.wxxiaomi.teachingoffice2.view.activity.base.BaseActivity;

public class LibraryLoginActivity extends BaseActivity {

	private EditText et_username;
	private EditText et_password;
	private EditText et_code;
	private Button btn_login;
	private ImageView iv_code;

	private Html_lib_Login main;
	private Bitmap codepic;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_library_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		et_code = (EditText) findViewById(R.id.et_code);
		iv_code = (ImageView) findViewById(R.id.iv_code);
		btn_login.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		getLoginBeanByNet();
	}

	private void getLoginBeanByNet() {
		new AsyncTask<String, Void, ResponseData<Html_lib_Login>>() {
			@Override
			protected ResponseData<Html_lib_Login> doInBackground(String... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getLibLoginPage2Bean();
			}

			@Override
			protected void onPostExecute(ResponseData<Html_lib_Login> result) {
				if(result.isSuccess()){
					processData(result.getObj());
				}else{
					
				}
				super.onPostExecute(result);
			}
		}.execute();
	}



	protected void processData(Html_lib_Login obj) {
		main = obj;
		getCodeByNet();
	}



	private void getCodeByNet() {
		new AsyncTask<Html_lib_Login, Void, ResponseData<Bitmap>>() {
			@Override
			protected ResponseData<Bitmap> doInBackground(Html_lib_Login... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getLoginCodePicBitmap(main);
			}

			@Override
			protected void onPostExecute(ResponseData<Bitmap> result) {
				if(result.isSuccess()){
					
					codepic = result.getObj();
					updateCode();
				}else{
					
				}
				super.onPostExecute(result);
			}
		}.execute(); 
		
	}

	protected void updateCode() {
		iv_code.setImageBitmap(codepic);
		
	}


	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			showMingProcessDialog("登录中");
			String username = et_username.getText().toString().trim();
			String password = et_password.getText().toString().trim();
			String code = et_code.getText().toString().trim();
			loginByNet(username,password,code);
			break;
		case R.id.iv_code:
			getCodeByNet();
			break;
		default:
			break;
		}

	}



	private void loginByNet(final String username, final String password, final String code) {
		new AsyncTask<Html_lib_Login, Void, ResponseData<Html_Lib_Main>>() {
			@Override
			protected ResponseData<Html_Lib_Main> doInBackground(Html_lib_Login... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.Login(main, username, password, code);
			}

			@Override
			protected void onPostExecute(ResponseData<Html_Lib_Main> result) {
				if(result.isSuccess()){
//					codepic = result.getObj();
					ConstantValue.isLibLogin = true;
					SharePrefUtil.saveString(ct, "libUserName", username);
					SharePrefUtil.saveString(ct, "libPassword", password);
					SharePrefUtil.saveBoolean(ct, "isRemLibLogin", true);
					Bundle bundle = new Bundle();
					Intent intent = new Intent();
					bundle.putSerializable("html_lib_main", result.getObj());
					intent.putExtra("value", bundle);
					setResult(1,intent);
					closeMingDialog();
					finish();
				}else{
//					Log.i("wang", result.getError());
					showLoginError(result.getError());
				}
				super.onPostExecute(result);
			}
		}.execute(); 
	}

	protected void showLoginError(String error) {
//		new MingEditDialog(ct, MingEditDialog.ERROR)
//		.setTitleText(error)
//		.setConfirmText("确定")
//		.show();
		showErrorDialog(error);
	}



	

}
