package com.wxxiaomi.teachingoffice2.view.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.OfficeElectiveCourse;
import com.wxxiaomi.teachingoffice2.bean.OfficeElectiveCourse.ElectiveCourseColumn;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.engine.OfficeEngineImpl;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity.MainChangeListener;
import com.wxxiaomi.teachingoffice2.view.adapter.OfficeElectiveCourseAdapter;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class ElectiveCourseFragment extends BaseFragment {

	/**
	 * 带过来的main的html页面的实体
	 */
	private Html_Main fragmentData;
	private OfficeElectiveCourse info;
//	private OfficeElectiveCourseColumnAdapter adapter;
	private OfficeElectiveCourseAdapter adapter;
	private ExpandableListView lv_lv;
	LayoutInflater inflater;
	
	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_electivecourse, null);
		lv_lv = (ExpandableListView) view.findViewById(R.id.lv_lv);
		lv_lv.setGroupIndicator(null);
		this.inflater = inflater;
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		if (getActivity() instanceof OfficeActivity) {
			OfficeActivity activity = (OfficeActivity) getActivity();
			fragmentData = activity.getMain();
			activity.setMainChangeListener(new MainChangeListener() {
				@Override
				public void change(Html_Main main) {
					fragmentData = main;
					getElectiveCourseFromNet();
				}
			});
		}
		getElectiveCourseFromNet();
	}

	private void getElectiveCourseFromNet() {
		Log.i("wang", "getElectiveCourseFromNet()");
		new AsyncTask<String, Void, ResponseData<OfficeElectiveCourse>>() {
			@Override
			protected ResponseData<OfficeElectiveCourse> doInBackground(String... params) {
				OfficeEngineImpl impl = new OfficeEngineImpl();
				return impl.getOfficeElectiveCourse2Bean(fragmentData.getElectiveCourseUrl(), fragmentData.getFromUrl());
			}

			@Override
			protected void onPostExecute(ResponseData<OfficeElectiveCourse> result) {
				if(result.isSuccess()){
					//获取成功
//					Log.i("wang", "fragment-result.isSuccess()=true");
					info = result.getObj();
					processData(info.getColumns());
				}else{
					//获取失败,登录会话过期
//					Log.i("wang", "fragment-result.isSuccess()=false");
					interFace.onFragmentCallback(ElectiveCourseFragment.this, 0, null);
				}
				super.onPostExecute(result);
			}
		}.execute();
	}
	

	protected void processData(List<ElectiveCourseColumn> columns) {
		if(adapter == null){
			adapter = new OfficeElectiveCourseAdapter(ct,inflater, info.getColumns());
//			adapter = new OfficeElectiveCourseColumnAdapter(ct, info.getColumns());
			lv_lv.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
	}
	
}
