package com.wxxiaomi.teachingoffice2.view.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.Score;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.engine.OfficeEngineImpl;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity.MainChangeListener;
import com.wxxiaomi.teachingoffice2.view.adapter.OfficeScoreAdapter;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class ScoreInfoFragment extends BaseFragment {

	private OfficeScoreAdapter adapter;
	private ExpandableListView lv_lv;
	private Html_Main fragmentData;
	private Score info;
	private LayoutInflater inflater;

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_scoreinfo, null);
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
					getScoreByNet();
				}
			});
		}
		getScoreByNet();
	}

	private void getScoreByNet() {
		new AsyncTask<String, Void, ResponseData<Score>>() {
			

			@Override
			protected  ResponseData<Score> doInBackground(String... params) {
				OfficeEngineImpl impl = new OfficeEngineImpl();
				return impl.getScore2Bean(fragmentData.getScoreUrl(), fragmentData.getFromUrl());
			}

			@Override
			protected void onPostExecute( ResponseData<Score> result) {
				if(result.isSuccess()){
					//获取成功
					info = result.getObj();
					processData();
				}else{
					//获取失败
					interFace.onFragmentCallback(ScoreInfoFragment.this, 0, null);
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	protected void processData() {
		if(adapter == null){
			adapter = new OfficeScoreAdapter(info.getColumns(),inflater);
			lv_lv.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
		
	}
	
}
