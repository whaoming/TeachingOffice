package com.wxxiaomi.teachingoffice2.view.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo.UserInfoColumn;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class LibUserInfoFragment extends BaseFragment{

	/**
	 * 带过来的main的html页面的实体
	 */
//	private Html_Main fragmentData;
//	
//	private ListView lv_listview;
//	
//	private OfficeUserInfoColumnAdapter adapter;
//	
//	private OfficeUserInfo info;
//	private TextView text;
//	private Html_Lib_Main libMain;
	
	
	
	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_lib_userinfo, null);
		setHasOptionsMenu(true);
//		lv_listview = (ListView) view.findViewById(R.id.lv_listview);
//		text = (TextView) view.findViewById(R.id.text);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		if (getActivity() instanceof OfficeActivity) {
//			OfficeActivity activity = (OfficeActivity) getActivity();
//			libMain = activity.getLibMain();
		}
		
//		text.setText(libMain.getUserInfo().toString());
		dismissLoadingView();
//		getPersonalInfoFromNet();
		
	}
	
	@Override      
    public boolean onOptionsItemSelected(MenuItem item) {      
        switch(item.getItemId()){      
        case R.id.add:  
//        	updateUserInfo();
//        	Log.i("wang", "aaa");
//        	List<EditText> ets = adapter.getEts();
//        	for(EditText et : ets){
//        		Log.i("wang", et.getText()+"");
//        	}
//        	List<UserInfoColumn> list = new ArrayList<UserInfoColumn>();
//        	for(UserInfoColumn column : info.getColumns()){
//        		if(column.isEdit()){
//        			list.add(column);
//        		}
//        	}
//        	Intent intent = new Intent(ct,OfficeUserInfoEditActivity.class);
//        	Bundle bund = new Bundle();
//    		bund.putSerializable("info", (Serializable) list);
//    		intent.putExtra("value", bund);
//    		startActivity(intent);
            break;      
        }      
        return super.onOptionsItemSelected(item);      
    } 

//	private void updateUserInfo() {
//		final List<UserInfoColumn> editColumns = new ArrayList<UserInfoColumn>();;
//		for(UserInfoColumn column : info.getColumns()){
//			if(column.isEdit()){
////				Log.i("wang", column.getKey()+":"+column.getValue());
//				editColumns.add(column);
//			}
//		}
//		new AsyncTask<String, Void, ResponseData<Html_OfficeUserInfo>>() {
//			@Override
//			protected ResponseData<Html_OfficeUserInfo> doInBackground(String... params) {
//				OfficeEngineImpl impl = new OfficeEngineImpl();
//				return impl.updateOfficeUserInfo(fragmentData.getPersonalInfoUrl(), fragmentData.getFromUrl(), editColumns,info.get__VIEWSTATE());
//			}
//
//			@Override
//			protected void onPostExecute(ResponseData<Html_OfficeUserInfo> result) {
//				if(result.isSuccess()){
//					//获取成功
//					info = result.getObj();
//					processData(info.getColumns());
//				}else{
//					//获取失败
//				}
//				super.onPostExecute(result);
//			}
//		}.execute();
//		
//		
//	}

//	private void getPersonalInfoFromNet() {
//		new AsyncTask<String, Void, ResponseData<OfficeUserInfo>>() {
//			@Override
//			protected ResponseData<OfficeUserInfo> doInBackground(String... params) {
//				OfficeEngineImpl impl = new OfficeEngineImpl();
//				return impl.getOfficePersonalInfo2Bean(fragmentData.getPersonalInfoUrl(), fragmentData.getFromUrl());
//			}
//
//			@Override
//			protected void onPostExecute(ResponseData<OfficeUserInfo> result) {
//				if(result.isSuccess()){
//					//获取成功
//					info = result.getObj();
//					processData(info.getColumns());
//				}else{
//					//获取失败
//				}
//				super.onPostExecute(result);
//			}
//		}.execute();
//		
//	}

	protected void processData(List<UserInfoColumn> columns) {
//		if(adapter == null){
//			adapter = new OfficeUserInfoColumnAdapter(ct, columns,false);
//			lv_listview.setAdapter(adapter);
//		}else{
//			adapter.notifyDataSetChanged();
//		}
		dismissLoadingView();
	}

//	@Override
//	public void editText(String text, int position) {
//		Log.i("wang", "text="+text+"--position="+position);
//		info.getColumns().get(position).setValue(text);
//		
//	}


	
}
