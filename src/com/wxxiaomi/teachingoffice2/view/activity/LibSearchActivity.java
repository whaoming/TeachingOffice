package com.wxxiaomi.teachingoffice2.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.BookInfo;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Search_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Search_Result;
import com.wxxiaomi.teachingoffice2.engine.LibraryEngineImpl;
import com.wxxiaomi.teachingoffice2.view.activity.base.BaseActivity;
import com.wxxiaomi.teachingoffice2.view.adapter.SearchResultColumnAdapter;
import com.wxxiaomi.teachingoffice2.view.custom.LoadMoreTextView;
import com.wxxiaomi.teachingoffice2.view.custom.LoadMoreTextView.LoadMoreTextViewListener;

public class LibSearchActivity extends BaseActivity {

	private EditText et_content;
	private Button btn_search;
	private Html_Lib_Search_Main main;
	private Html_Lib_Search_Result info;
	private ListView lv_lv;
	private SearchResultColumnAdapter adapter;
	private LoadMoreTextView lmtv;
	private TextView tv_nodata;

	@Override
	protected void initView() {
		showMingProcessDialog("正在加载数据...");
		setContentView(R.layout.activity_library_search);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		lv_lv = (ListView) findViewById(R.id.lv_lv);
		lmtv = new LoadMoreTextView(getLayoutInflater());
		lv_lv.addFooterView(lmtv.getRootView());
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
		initTitleBar();
	}

	@Override
	protected void initData() {
		
		getMain();
		lv_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BookInfo bookInfo = info.getColumns().get(position);
				Intent intent = new Intent(ct, LibBookInfoActivity.class);
				intent.putExtra("bookurl", bookInfo.getUrl().getBytes());
				startActivity(intent);
			}
		});
		lmtv.setMyClickListener(new LoadMoreTextViewListener() {
			@Override
			public void click(LoadMoreTextView loadMoreTextView) {
				/**
				 * 没有数据的时候不能调用 页数到了也不能调用下一页方法
				 */
				if (!(info.getColumns().size() == 0)
						&& (info.getCurrentPage() < info.getPageCount())) {
					getNextPageByNet();
					loadMoreTextView.setLoading();
				} else {

				}

			}
		});
	}

	private void getMain() {
		new AsyncTask<String, Void, ResponseData<Html_Lib_Search_Main>>() {
			@Override
			protected ResponseData<Html_Lib_Search_Main> doInBackground(
					String... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getLibSearchMain();
			}

			@Override
			protected void onPostExecute(
					ResponseData<Html_Lib_Search_Main> result) {
				if (result.isSuccess()) {
					main = result.getObj();
					closeMingDialog();
				} else {
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			info = null;
			adapter = null;
			showMingProcessDialog("正在查询");
			String content = et_content.getText().toString().trim();
			searchByNet(content);
			break;
		default:
			break;
		}
	}

	protected void getNextPageByNet() {
		new AsyncTask<Html_Lib_Search_Main, Void, ResponseData<Html_Lib_Search_Result>>() {
			@Override
			protected ResponseData<Html_Lib_Search_Result> doInBackground(
					Html_Lib_Search_Main... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getNextPage(info.getPageUrl(),
						info.getCurrentPage(), info.getUrl());
			}

			@Override
			protected void onPostExecute(
					ResponseData<Html_Lib_Search_Result> result) {
				if (result.isSuccess()) {
					processNextPageData(result.getObj());
				} else {

				}
				super.onPostExecute(result);
			}

		}.execute();

	}

	protected void processNextPageData(Html_Lib_Search_Result nextInfo) {
		info.getColumns().addAll(nextInfo.getColumns());
		info.setCurrentPage((info.getCurrentPage() + 1) + "");
		lmtv.setNormalText("已加载" + info.getCurrentPage() + "页，总共有"
				+ info.getPageCount() + "页");
		adapter.notifyDataSetChanged();
	}

	private void searchByNet(final String content) {
		new AsyncTask<Html_Lib_Search_Main, Void, ResponseData<Html_Lib_Search_Result>>() {
			@Override
			protected ResponseData<Html_Lib_Search_Result> doInBackground(
					Html_Lib_Search_Main... params) {
				LibraryEngineImpl impl = new LibraryEngineImpl();
				return impl.getSearchResult(main, content);
			}

			@Override
			protected void onPostExecute(
					ResponseData<Html_Lib_Search_Result> result) {
				if (result.isSuccess()) {
					info = result.getObj();
					processData();
				} else {

				}
				closeMingDialog();
				super.onPostExecute(result);
			}

		}.execute();

	}

	private void processData() {
		if (info.getColumns().size() == 0) {
			tv_nodata.setVisibility(View.VISIBLE);
			lv_lv.setVisibility(View.GONE);
		} else {
			tv_nodata.setVisibility(View.GONE);
			lv_lv.setVisibility(View.VISIBLE);
			lmtv.setNormalText("已加载" + info.getCurrentPage() + "页，总共有"
					+ info.getPageCount() + "页");
			if (adapter == null) {
				adapter = new SearchResultColumnAdapter(ct, info.getColumns());
				lv_lv.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}

		

	}

}
