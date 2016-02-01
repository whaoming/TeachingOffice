package com.wxxiaomi.teachingoffice2.view.custom;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wxxiaomi.teachingoffice2.R;

public class LoadMoreTextView{

	LayoutInflater inflater;
	private View view;
	private LoadMoreTextViewListener listener;
	private FrameLayout fl_normal;
	private TextView tv_normal;
	
	private FrameLayout fl_loading;
//	private TextView tv_loading;
	private boolean isLoading;
	
	
	public LoadMoreTextView(LayoutInflater inflater){
		this.inflater = inflater;
		init();
	}

	@SuppressLint("InflateParams")
	private void init() {
		view = inflater.inflate(R.layout.item_textview_loadmore, null);
		fl_normal = (FrameLayout) view.findViewById(R.id.fl_normal);
		fl_loading = (FrameLayout) view.findViewById(R.id.fl_loading);
		tv_normal = (TextView) view.findViewById(R.id.tv_normal);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isLoading){
					listener.click(LoadMoreTextView.this);
				}
			}
		});
	}
	
	public void setLoading() {
		isLoading = true;
		fl_normal.setVisibility(View.GONE);
		fl_loading.setVisibility(View.VISIBLE);
	}
	public void setNormal() {
		isLoading = false;
		fl_normal.setVisibility(View.VISIBLE);
		fl_loading.setVisibility(View.GONE);
	}
	
	public View getRootView(){
		return view;
	}
	public void setMyClickListener(LoadMoreTextViewListener listener){
		this.listener = listener;
	}
	
	public interface LoadMoreTextViewListener{
		void click(LoadMoreTextView loadMoreTextView);
	}
	public void setVisble(boolean isVisable){
		if(!isVisable){
			view.setVisibility(View.GONE);
		}
	}
	
	public void setNormalText(String text){
		setNormal();
		tv_normal.setText(text);
	}
}
