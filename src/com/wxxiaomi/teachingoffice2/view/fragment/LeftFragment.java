package com.wxxiaomi.teachingoffice2.view.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.view.activity.LibSearchActivity;
import com.wxxiaomi.teachingoffice2.view.activity.LibraryLoginActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeLoginActivity;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity.LibMainChangeListener;
import com.wxxiaomi.teachingoffice2.view.activity.OfficeActivity.MainChangeListener;

@SuppressLint("InflateParams")
public class LeftFragment extends Fragment implements OnClickListener {

	private View rootView;
	private TextView tv_xm;
	private TextView tv_xh;
	private TextView tv_classForm;
	private TextView tv_personalInfo;
	private TextView tv_score;
	private TextView tv_eletive;
	private TextView tv_borrow;
	private TextView tv_borrowhistory;
	private TextView tv_liblogin;
	private TextView tv_officelogin;
	private TextView tv_lib_userinfo;
	private TextView tv_find;

	private Html_Main main;
	int currentId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_main, null);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tv_xm = (TextView) rootView.findViewById(R.id.tv_xm);
		tv_xh = (TextView) rootView.findViewById(R.id.tv_xh);
		tv_classForm = (TextView) rootView.findViewById(R.id.tv_classForm);
		tv_classForm.setOnClickListener(this);
		tv_personalInfo = (TextView) rootView
				.findViewById(R.id.tv_personalInfo);
		// tv_personalInfo.setPressed(true);
		tv_personalInfo.setOnClickListener(this);
		tv_score = (TextView) rootView.findViewById(R.id.tv_score);
		tv_score.setOnClickListener(this);
		tv_eletive = (TextView) rootView.findViewById(R.id.tv_eletive);
		tv_eletive.setOnClickListener(this);
		tv_borrow = (TextView) rootView.findViewById(R.id.tv_borrow);
		tv_borrow.setOnClickListener(this);
		tv_borrowhistory = (TextView) rootView
				.findViewById(R.id.tv_borrowhistory);
		tv_borrowhistory.setOnClickListener(this);
		tv_liblogin = (TextView) rootView.findViewById(R.id.tv_liblogin);
		tv_liblogin.setOnClickListener(this);
		tv_officelogin = (TextView) rootView.findViewById(R.id.tv_officelogin);
		tv_officelogin.setOnClickListener(this);
		tv_lib_userinfo = (TextView) rootView
				.findViewById(R.id.tv_lib_userinfo);
		tv_lib_userinfo.setOnClickListener(this);
		tv_find = (TextView) rootView.findViewById(R.id.tv_find);
		tv_find.setOnClickListener(this);
		if (getActivity() instanceof OfficeActivity) {
			OfficeActivity activity = (OfficeActivity) getActivity();
			activity.setMainChangeListener(new MainChangeListener() {
				@Override
				public void change(Html_Main mains) {
					main = mains;
					if (main != null) {
						if (ConstantValue.isOfficeLogin) {
							tv_xm.setText((main.getNumberAndName().split(" "))[1]);
							tv_xh = (TextView) rootView
									.findViewById(R.id.tv_xh);
							tv_xh.setText((main.getNumberAndName().split(" "))[0]);
							setButtonClickable();
							tv_officelogin.setVisibility(View.GONE);
						}
					}
				}
			});

			activity.setLibMainChangeListener(new LibMainChangeListener() {

				@Override
				public void change(Html_Lib_Main html_lib_main) {
					if (html_lib_main != null) {
						setButtonClickable();
						tv_liblogin.setVisibility(View.GONE);
					}

				}
			});
			main = activity.getMain();
			if (main != null) {
				if (ConstantValue.isOfficeLogin) {
					tv_officelogin.setVisibility(View.GONE);
					tv_xm.setText((main.getNumberAndName().split(" "))[1]);
					tv_xh = (TextView) rootView.findViewById(R.id.tv_xh);
					tv_xh.setText((main.getNumberAndName().split(" "))[0]);
				}
			}
		}
		setButtonClickable();

	}

	public void setButtonClickable() {
		if (ConstantValue.isOfficeLogin) {
			tv_classForm.setClickable(true);
			tv_personalInfo.setClickable(true);
			tv_score.setClickable(true);
			tv_eletive.setClickable(true);
			tv_classForm.setTextColor(Color.BLACK);
			tv_personalInfo.setTextColor(Color.BLACK);
			tv_score.setTextColor(Color.BLACK);
			tv_eletive.setTextColor(Color.BLACK);

		} else {
			tv_classForm.setClickable(false);
			tv_personalInfo.setClickable(false);
			tv_score.setClickable(false);
			tv_eletive.setClickable(false);
		}
		if (ConstantValue.isRemLibLogin) {
			tv_borrowhistory.setClickable(true);
			tv_borrow.setClickable(true);
			tv_lib_userinfo.setClickable(true);
			tv_borrowhistory.setTextColor(Color.BLACK);
			tv_borrow.setTextColor(Color.BLACK);
			tv_lib_userinfo.setTextColor(Color.BLACK);
			tv_liblogin.setVisibility(View.GONE);
		} else {
			tv_borrowhistory.setClickable(false);
			tv_borrow.setClickable(false);
			tv_lib_userinfo.setClickable(false);
		}
	}

	@Override
	public void onClick(View v) {
		/**
		 * 1.currentFragment == null 2.f == null == currentFragment
		 * 3.f!=currentFragment f一定==null
		 */
		Fragment f = null;
		int state = 0;
		if (v.getId() != currentId) {
			clearBackgroundColor();
			switch (v.getId()) {
			case R.id.tv_classForm:
				f = new ClassFormFragment();
				tv_classForm.setBackgroundColor(getResources().getColor(R.color.mygray));
				break;
			case R.id.tv_personalInfo:
				f = new PersonalInfoFragment();
				tv_personalInfo.setBackgroundColor(getResources().getColor(R.color.mygray));
				break;
			case R.id.tv_score:
				tv_score.setBackgroundColor(getResources().getColor(R.color.mygray));
				f = new ScoreInfoFragment();
				break;
			case R.id.tv_eletive:
				tv_eletive.setBackgroundColor(getResources().getColor(R.color.mygray));
				f = new ElectiveCourseFragment();
				break;
			case R.id.tv_lib:
			case R.id.tv_borrow:
				tv_borrow.setBackgroundColor(getResources().getColor(R.color.mygray));
				f = new LibBorrowedStateFragment();
				break;
			case R.id.tv_borrowhistory:
				tv_borrowhistory.setBackgroundColor(getResources().getColor(R.color.mygray));
				f = new LibBorrowedHistoryFragment();
				break;
			case R.id.tv_officelogin:
				state = 3;
				Intent intent = new Intent(getActivity(),
						OfficeLoginActivity.class);
				startActivityForResult(intent, 4);
				break;
			case R.id.tv_liblogin:
				state = 3;
				Intent intent1 = new Intent(getActivity(),
						LibraryLoginActivity.class);
				startActivityForResult(intent1, 1);
				break;
			case R.id.tv_lib_userinfo:
				tv_lib_userinfo.setBackgroundColor(getResources().getColor(R.color.mygray));
				f = new LibUserInfoFragment();
				break;
			case R.id.tv_find:
				tv_find.setBackgroundColor(getResources().getColor(R.color.mygray));
				state = 3;
				Intent intent2 = new Intent(getActivity(),
						LibSearchActivity.class);
				startActivity(intent2);
				break;
			default:
				break;
			}
			currentId = v.getId();
			switchFragment(f, state);
		} else {
			switchFragment(null, ConstantValue.CLICKTHESAMEFRAGMENT);
		}
	}

	private void clearBackgroundColor() {
		tv_classForm.setBackgroundColor(Color.WHITE);
		tv_personalInfo.setBackgroundColor(Color.WHITE);
		tv_score.setBackgroundColor(Color.WHITE);
		tv_eletive.setBackgroundColor(Color.WHITE);
		tv_borrow.setBackgroundColor(Color.WHITE);
		tv_borrowhistory.setBackgroundColor(Color.WHITE);
		tv_lib_userinfo.setBackgroundColor(Color.WHITE);
		tv_find.setBackgroundColor(Color.WHITE);
//		tv_lib_userinfo.setBackgroundColor(Color.WHITE);
		
	}

	private void switchFragment(Fragment f, int state) {
		if (getActivity() instanceof OfficeActivity) {
			OfficeActivity activity = (OfficeActivity) getActivity();
			activity.switchFragment(f, state);
		}
	}

}
