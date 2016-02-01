package com.wxxiaomi.teachingoffice2.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.engine.OfficeEngineImpl;
import com.wxxiaomi.teachingoffice2.utils.SharePrefUtil;
import com.wxxiaomi.teachingoffice2.view.activity.base.BaseActivity;

public class OfficeLoginActivity extends BaseActivity {

	private EditText et_username;
	private EditText et_password;
	private Button btn_login;

//	private Html_Main main;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_office_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgressDialog("正在登录");
				String username = et_username.getText().toString().trim();
				String password = et_password.getText().toString().trim();
				LoginToNet(username, password);
			}
		});
	}

	protected void LoginToNet(final String username, final String password) {
		new AsyncTask<String, Void, ResponseData<Html_Main>>() {
			@Override
			protected ResponseData<Html_Main> doInBackground(String... params) {
				OfficeEngineImpl impl = new OfficeEngineImpl();
				return impl.getOfficeMainHtml2BeanByOne(username, password);
			}

			@Override
			protected void onPostExecute(ResponseData<Html_Main> result) {
				if(result.isSuccess()){
					processData(result.getObj());
				}else{
					
				}
				
				super.onPostExecute(result);
			}
		}.execute();

	}

	protected void processData(Html_Main result) {
		/**
		 *  Intent intent =  new Intent(MainActivity.this,LibMainActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("info", login_response.getD_main());
			intent.putExtra("value", bund);
			startActivity(intent);
					
			Intent intent = getIntent();
			Bundle bund = intent.getBundleExtra("value");
			info = (D_LibMain) bund.getSerializable("info");
		 */
		/**
		 *  String result = String.valueOf(sum);  
                Intent data = new Intent();  
                data.putExtra("result", result);  
  
                setResult(0x11, data);  
                finish();  
		 */
		
//		Intent intent = new Intent(ct,OfficeActivity.class);
//		Bundle bund = new Bundle();
//		bund.putSerializable("info", result);
//		intent.putExtra("value", bund);
//		startActivity(intent);
//		finish();
		SharePrefUtil.saveBoolean(ct, "isRemOfficeLogin", true);
		SharePrefUtil.saveString(ct, "officeUserName", result.getUsername());
		SharePrefUtil.saveString(ct, "officePassword", result.getPassword());
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("result", result);
		intent.putExtra("value", bundle);
		setResult(4, intent);
		closeProgressDialog();
		finish();
		ConstantValue.isOfficeLogin = true;
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
