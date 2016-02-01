package com.wxxiaomi.teachingoffice2.view.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo.UserInfoColumn;
import com.wxxiaomi.teachingoffice2.view.activity.base.BaseActivity;
import com.wxxiaomi.teachingoffice2.view.adapter.OfficeUserInfoColumnAdapter;

public class OfficeUserInfoEditActivity extends BaseActivity {

	private ListView lv_lv;
	private OfficeUserInfoColumnAdapter adapter;
	private List<UserInfoColumn> info;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initView() {
		setContentView(R.layout.activity_office_userinfo_edit);
		initTitleBar();
		initRight("完成");
		lv_lv = (ListView) findViewById(R.id.lv_lv);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (List<UserInfoColumn>) bund.getSerializable("info");
	}



	@Override
	protected void initData() {
		if(info != null){
			adapter = new OfficeUserInfoColumnAdapter(ct, info, true);
		}
		lv_lv.setAdapter(adapter);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.rightbutton:
			//发送到服务器
			
			break;

		default:
			break;
		}

	}

}
