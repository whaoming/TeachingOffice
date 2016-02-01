package com.wxxiaomi.teachingoffice2.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo.UserInfoColumn;

public class OfficeUserInfoColumnAdapter extends BaseAdapter {

	BitmapUtils bitmapUtil;
	private Context context;
	private List<UserInfoColumn> list;
	private boolean isFromEdit;
	@SuppressLint("UseSparseArrays")
	// List<Map<Integer, String>> dataList = new HashMap<Integer, String>();
	private List<Map<String, String>> mData = new ArrayList<Map<String, String>>();
//	public Map<Integer, EditText> editorValue = new HashMap<Integer, EditText>();
//	public List<String> mData = new ArrayList<String>();

	public OfficeUserInfoColumnAdapter(Context ct,
			List<UserInfoColumn> columns, boolean isFromEdit) {
		this.context = ct;
		bitmapUtil = new BitmapUtils(context);
		this.list = columns;
		this.isFromEdit = isFromEdit;
		for (@SuppressWarnings("unused") UserInfoColumn cartBean : list) {
			mData.add(new HashMap<String, String>());
			
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		UserInfoColumn column = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.item_officeuserinfocolumn, null);
			holder.et_value = (EditText) convertView
					.findViewById(R.id.et_value);
			holder.tv_key = (TextView) convertView.findViewById(R.id.tv_key);
			holder.tv_value = (TextView) convertView
					.findViewById(R.id.tv_value);
			if(isFromEdit){
				class MyTextWatcher implements TextWatcher {
					public MyTextWatcher(ViewHolder holder) {
						mHolder = holder;
					}

					/**
					 * 这里其实是缓存了一屏数目的viewholder， 也就是说一屏能显示10条数据，那么内存中就会有10个viewholder
					 * 在这的作用是通过edittext的tag拿到对应的position，用于储存edittext的值
					 */
					private ViewHolder mHolder;

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!TextUtils.isEmpty(s.toString())) {
							// 当文本发生变化时，就保存值到list对应的position中
							if(mHolder.et_value.getTag()!=null){
							int position = (Integer) mHolder.et_value.getTag();
							mData.get(position).put("value", s.toString()); //
							}
						}
					}
				}
				holder.et_value.addTextChangedListener(new MyTextWatcher(holder));
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_key.setText(column.getKey());
		if(isFromEdit){
			holder.et_value.setVisibility(View.VISIBLE);
			holder.tv_value.setVisibility(View.GONE);
			holder.et_value.setTag(position);
			String value = mData.get(position).get("value");
			// 每次都存一下，用于在edittext中取到对应位置的edittext
//			editorValue.put(position, holder.et_value);
			
			if (!TextUtils.isEmpty(value)) {
				holder.et_value.setText(value);
			} else {
				holder.et_value.setText(column.getValue());
			}
		}else{
			holder.et_value.setVisibility(View.GONE);
			holder.tv_value.setVisibility(View.VISIBLE);
			holder.tv_value.setText(column.getValue());
		}
		
	
		
		
		
		return convertView;
	}

	static class ViewHolder {
		TextView tv_key;
		TextView tv_value;
		EditText et_value;
		// int postion;
	}

	// public List<EditText> getEts(){
	// return eds;
	// }

	// public interface updateListener {
	// void editText(String text, int position);
	// }
	// public
}
