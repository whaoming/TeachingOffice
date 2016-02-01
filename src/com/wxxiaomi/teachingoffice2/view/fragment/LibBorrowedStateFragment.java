package com.wxxiaomi.teachingoffice2.view.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.BookBorrowedState;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BorrowedState;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.engine.LibraryEngineImpl;
import com.wxxiaomi.teachingoffice2.view.activity.LibBookInfoActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity.LibMainChangeListener;
import com.wxxiaomi.teachingoffice2.view.adapter.LibBorrowStateColumnAdapter;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class LibBorrowedStateFragment extends BaseFragment{

	/**
	 * 带过来的main的html页面的实体
	 */
	private ListView lv_listview;
	private Html_Lib_Main libMain;
	private Html_Lib_BorrowedState currentPageBean;
	private LibBorrowStateColumnAdapter adapter;
	
	
	
	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_borrowedstate, null);
		lv_listview = (ListView) view.findViewById(R.id.lv_lv);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		if (getActivity() instanceof OfficeActivity) {
			OfficeActivity activity = (OfficeActivity) getActivity();
			if(ConstantValue.isLibLogin){
				Log.i("wang", "ConstantValue.isLibLogin=true");
				libMain = activity.getLibMain();
				getBorrowedStateFormNet();
			}else{
				Log.i("wang", "ConstantValue.isLibLogin=false");
				interFace.onFragmentCallback(this, ConstantValue.LIBFIRSTNOLOGIN, null);
			}
			
			activity.setLibMainChangeListener(new LibMainChangeListener() {
				@Override
				public void change(Html_Lib_Main mains) {
//					ConstantValue.isLibLogin = true;
					libMain = mains;
					getBorrowedStateFormNet();
				}
			});
		}
		
		lv_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String url = currentPageBean.getColumns().get(position).getBookInfoUrl();
				Intent intent = new Intent(ct, LibBookInfoActivity.class);
				intent.putExtra("bookurl",url.getBytes());
				startActivity(intent);
			}
		});
		
	}
	
	protected void getBorrowedStateFormNet() {
		new AsyncTask<String, Void, ResponseData<Html_Lib_BorrowedState>>() {
			@Override
			protected ResponseData<Html_Lib_BorrowedState> doInBackground(String... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				ResponseData<Html_Lib_BorrowedState> borrowedState = impl.getBorrowedState(libMain.getCookie(), libMain.getBookBorrowedUrl(), "");
				return borrowedState;
			}

			@Override
			protected void onPostExecute(ResponseData<Html_Lib_BorrowedState> result) {
				if(result.isSuccess()){
					//获取成功
					currentPageBean = result.getObj();
					processData(currentPageBean.getColumns());
				}else{
					Log.i("wang", "获取失败,登录会话过期");
					//获取失败,登录会话过期
					interFace.onFragmentCallback(LibBorrowedStateFragment.this, ConstantValue.STATE_LIBOUTTIME, null);
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}

	protected void processData(List<BookBorrowedState> columns) {
		if(adapter == null){
			adapter = new LibBorrowStateColumnAdapter(ct, currentPageBean.getColumns());
			lv_listview.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
	}

	@Override      
    public boolean onOptionsItemSelected(MenuItem item) {      
        switch(item.getItemId()){      
        case R.id.add:  
            break;      
        }      
        return super.onOptionsItemSelected(item);      
    } 
}
