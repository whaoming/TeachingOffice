package com.wxxiaomi.teachingoffice2.view.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.wxxiaomi.mingdialog.dialog.MingEditDialog;
import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.BookBorrowedState;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BorrowHistory;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.engine.LibraryEngineImpl;
import com.wxxiaomi.teachingoffice2.view.activity.LibBookInfoActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity.LibMainChangeListener;
import com.wxxiaomi.teachingoffice2.view.adapter.LibBorrowHistoryColumnAdapter;
import com.wxxiaomi.teachingoffice2.view.custom.LoadMoreTextView;
import com.wxxiaomi.teachingoffice2.view.custom.LoadMoreTextView.LoadMoreTextViewListener;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class LibBorrowedHistoryFragment extends BaseFragment implements
		OnClickListener {

	/**
	 * 带过来的main的html页面的实体
	 */
	//
	private ListView lv_listview;
	private Html_Lib_Main libMain;
	private Html_Lib_BorrowHistory currentPageBean;
	private LibBorrowHistoryColumnAdapter adapter;
	private int hasLoadPage = 0;
	private LoadMoreTextView tv;

	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_borrowedhistory, null);
		setHasOptionsMenu(true);
		lv_listview = (ListView) view.findViewById(R.id.lv_lv);
		tv = new LoadMoreTextView(inflater);
		lv_listview.addFooterView(tv.getRootView());
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		tv.setMyClickListener(new LoadMoreTextViewListener() {
			
			@Override
			public void click(LoadMoreTextView loadMoreTextView) {
				loadMoreTextView.setLoading();
				getNextPageByNet();
			}
		});
		
		/**
		 * if(ConstantValue.isLibLogin){
				libMain = activity.getLibMain();
				getBorrowedStateFormNet();
			}else{
				interFace.onFragmentCallback(this, ConstantValue.LIBFIRSTNOLOGIN, null);
			}
		 */
		
		if (getActivity() instanceof OfficeActivity) {
			OfficeActivity activity = (OfficeActivity) getActivity();
			if(ConstantValue.isLibLogin){
				libMain = activity.getLibMain();
				getBorrowedHistoryFormNet();
			}else{
				interFace.onFragmentCallback(this, ConstantValue.LIBFIRSTNOLOGIN, null);
			}
			
			activity.setLibMainChangeListener(new LibMainChangeListener() {
				@Override
				public void change(Html_Lib_Main mains) {
					libMain = mains;
					getBorrowedHistoryFormNet();
				}
			});
		}
		
		lv_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			BookBorrowedState column = currentPageBean.getColumns().get(position);
			Intent intent = new Intent(ct, LibBookInfoActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("info", column);
//			intent.putExtra("value", bundle);
			intent.putExtra("bookurl", column.getBookInfoUrl().getBytes());
			startActivity(intent);
			
			}
		});

	}

	protected void getBorrowedHistoryFormNet() {
		new AsyncTask<String, Void, ResponseData<Html_Lib_BorrowHistory>>() {
			@Override
			protected ResponseData<Html_Lib_BorrowHistory> doInBackground(
					String... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getBorrowHistory(libMain.getCookie(),
						libMain.getBorrowHistoryUrl());
			}

			@Override
			protected void onPostExecute(
					ResponseData<Html_Lib_BorrowHistory> result) {
				if (result.isSuccess()) {
					// 获取成功
					currentPageBean = result.getObj();
					processData(currentPageBean.getColumns());

				} else {
					// 获取失败,登录会话过期
					interFace.onFragmentCallback(
							LibBorrowedHistoryFragment.this,
							ConstantValue.STATE_LIBOUTTIME, null);
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

	protected void processData(List<BookBorrowedState> columns) {
		hasLoadPage = hasLoadPage + 1;
		tv.setNormal();
		if (adapter == null) {
			adapter = new LibBorrowHistoryColumnAdapter(ct,
					currentPageBean.getColumns());
			lv_listview.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		dismissLoadingView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.tv_more:
//			getNextPageByNet();
//			break;

		default:
			break;
		}

	}

	private void getNextPageByNet() {
		if (currentPageBean.getNextPageUrl() == "") {
			new MingEditDialog(ct).setTitleText("已经到底了")
			.setConfirmText("确定")
			.show();
			tv.setVisble(false);
		} else {
			new AsyncTask<String, Void, ResponseData<Html_Lib_BorrowHistory>>() {
				@Override
				protected ResponseData<Html_Lib_BorrowHistory> doInBackground(
						String... params) {
					LibraryEngineImpl impl = new LibraryEngineImpl();
					return impl.getBorrowHistory(libMain.getCookie(),
							currentPageBean.getNextPageUrl());
				}

				@Override
				protected void onPostExecute(
						ResponseData<Html_Lib_BorrowHistory> result) {
					if (result.isSuccess()) {
						// 获取成功
						currentPageBean.getColumns().addAll(
								result.getObj().getColumns());
						currentPageBean.setNextPageUrl(result.getObj()
								.getNextPageUrl());
						processData(currentPageBean.getColumns());
					} else {
						// 获取失败,登录会话过期
						interFace.onFragmentCallback(
								LibBorrowedHistoryFragment.this,
								ConstantValue.STATE_LIBOUTTIME, null);
					}
					super.onPostExecute(result);
				}
			}.execute();
		}
	}

}
