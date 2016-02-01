package com.wxxiaomi.teachingoffice2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.engine.OfficeEngineImpl;
import com.wxxiaomi.teachingoffice2.utils.SharePrefUtil;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.base.BaseActivity;

public class MainActivity extends BaseActivity{


	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
////		SharePrefUtil.getString("", key, defValue);
//		
//		Intent intent = new Intent(MainActivity.this,OfficeActivity.class);
//		intent.putExtra("isMainA", true);
//		startActivity(intent);
//		finish();
//	
//	}

	private TextView tv_info;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_first);
		tv_info = (TextView) findViewById(R.id.tv_info);
		checkLogin();
		Intent intent = new Intent(MainActivity.this,OfficeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 检查是否存在教务系统账号和图书馆账号
	 */
	private void checkLogin() {
		ConstantValue.isRemLibLogin = SharePrefUtil.getBoolean(ct, "isRemLibLogin", false);
		boolean isRemOfficeLogin = SharePrefUtil.getBoolean(ct, "isRemOfficeLogin", false);
		if(isRemOfficeLogin){
			tv_info.setText("正在登录中.....");
			String officeUserName = SharePrefUtil.getString(ct, "officeUserName", "");
			String officePassword = SharePrefUtil.getString(ct, "officePassword", "");
			LoginByNet(officeUserName,officePassword);
		}else{
			finish();
		}
		
		
		
	}

	private void LoginByNet(final String officeUserName, final String officePassword) {
		new AsyncTask<String, Void, ResponseData<Html_Main>>() {
			@Override
			protected ResponseData<Html_Main> doInBackground(String... params) {
				OfficeEngineImpl impl = new OfficeEngineImpl();
				Log.i("wang", "doInBackground");
				return impl.getOfficeMainHtml2BeanByOne(officeUserName,officePassword);
			}

			@Override
			protected void onPostExecute(ResponseData<Html_Main> result) {
				Intent intent = new Intent(ct,OfficeActivity.class);
				if (result.isSuccess()) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("info", result.getObj());
					intent.putExtra("value", bundle);
					intent.putExtra("isLoginSuccess", true);
					ConstantValue.isOfficeLogin = true;
				} else {
					intent.putExtra("isLoginSuccess", false);
				}
				startActivity(intent);
				finish();
				super.onPostExecute(result);
			}
		}.execute();// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
