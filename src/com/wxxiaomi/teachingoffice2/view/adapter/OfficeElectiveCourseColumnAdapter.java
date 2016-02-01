package com.wxxiaomi.teachingoffice2.view.adapter;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.OfficeElectiveCourse.ElectiveCourseColumn;

public class OfficeElectiveCourseColumnAdapter extends BaseAdapter {

	private Context context;
	private List<ElectiveCourseColumn> list;

	public OfficeElectiveCourseColumnAdapter(Context ct,List<ElectiveCourseColumn> list) {
		this.context = ct;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
//		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ElectiveCourseColumn column = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.item_lib_searchresult, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(column.getName());
		return convertView;
	}

	public class ViewHolder {
		TextView tv_name;
	}

}
