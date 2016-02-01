package com.wxxiaomi.teachingoffice2.view.fragment;

import com.actionbarsherlock.view.MenuItem;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClassFormFragment extends BaseFragment {

	
	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.text, null);
		setHasOptionsMenu(true);
		TextView tv = (TextView) view.findViewById(R.id.test);
		tv.setText(ClassFormFragment.class.getSimpleName());
		Button btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		return view;
	}
	
	
	
	 @Override      
	    public boolean onOptionsItemSelected(MenuItem item) {      
	        switch(item.getItemId()){      
	        case R.id.add:  
	        	Log.i("wang", "aaa");
	            break;      
	        }      
	        return super.onOptionsItemSelected(item);      
	    } 

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResume() {
		super.onResume();
	}

//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.text, null);//����fragment��layout
//		TextView tv = (TextView) view.findViewById(R.id.test);
//		tv.setText(Fragment1.class.getSimpleName());
//		
//		return view;
//	}
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//	}
	
}
