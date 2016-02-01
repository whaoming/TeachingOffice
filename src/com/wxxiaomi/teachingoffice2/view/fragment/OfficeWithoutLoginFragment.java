package com.wxxiaomi.teachingoffice2.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.view.MenuItem;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeLoginActivity;
import com.wxxiaomi.teachingoffice2.view.fragment.base.BaseFragment;

public class OfficeWithoutLoginFragment extends BaseFragment {

	private Button btn_login;
	
	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_office_withoutlogin, null);
		setHasOptionsMenu(true);
		btn_login = (Button) view.findViewById(R.id.btn_login);		
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent(ct,OfficeLoginActivity.class);
				startActivityForResult(intent, 4);
			}
		});
		return view;
	}
	
	
	
	 @Override      
	    public boolean onOptionsItemSelected(MenuItem item) {      
	        switch(item.getItemId()){      
	        case R.id.add:  
	            break;      
	        }      
	        return super.onOptionsItemSelected(item);      
	    } 

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

}
