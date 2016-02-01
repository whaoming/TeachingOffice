package com.wxxiaomi.teachingoffice2.view.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo.UserInfoColumn;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.engine.OfficeEngineImpl;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeUserInfoEditActivity;
import com.wxxiaomi.teachingoffice2.view.adapter.OfficeUserInfoColumnAdapter;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class PersonalInfoFragment extends BaseFragment{

	/**
	 * 带过来的main的html页面的实体
	 */
	private Html_Main fragmentData;
	
	private ListView lv_listview;
	
	private OfficeUserInfoColumnAdapter adapter;
	
	private OfficeUserInfo info;
	
	
	
	
	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_personalinfo, null);
		setHasOptionsMenu(true);
		lv_listview = (ListView) view.findViewById(R.id.lv_listview);
		
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		if (getActivity() instanceof OfficeActivity) {
			OfficeActivity activity = (OfficeActivity) getActivity();
			fragmentData = activity.getMain();
		}
		getPersonalInfoFromNet();
		
	}
	
	@Override      
    public boolean onOptionsItemSelected(MenuItem item) {      
        switch(item.getItemId()){      
        case R.id.add:  
        	List<UserInfoColumn> list = new ArrayList<UserInfoColumn>();
        	for(UserInfoColumn column : info.getColumns()){
        		if(column.isEdit()){
        			list.add(column);
        		}
        	}
        	Intent intent = new Intent(ct,OfficeUserInfoEditActivity.class);
        	Bundle bund = new Bundle();
    		bund.putSerializable("info", (Serializable) list);
    		intent.putExtra("value", bund);
    		startActivity(intent);
            break;      
        }      
        return super.onOptionsItemSelected(item);      
    } 


	private void getPersonalInfoFromNet() {
		new AsyncTask<String, Void, ResponseData<OfficeUserInfo>>() {
			@Override
			protected ResponseData<OfficeUserInfo> doInBackground(String... params) {
				OfficeEngineImpl impl = new OfficeEngineImpl();
				return impl.getOfficePersonalInfo2Bean(fragmentData.getPersonalInfoUrl(), fragmentData.getFromUrl());
			}

			@Override
			protected void onPostExecute(ResponseData<OfficeUserInfo> result) {
				if(result.isSuccess()){
					//获取成功
					info = result.getObj();
					processData(info.getColumns());
				}else{
					//获取失败
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	protected void processData(List<UserInfoColumn> columns) {
		if(adapter == null){
			adapter = new OfficeUserInfoColumnAdapter(ct, columns,false);
			lv_listview.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
	}


	
}
