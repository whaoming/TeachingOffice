package com.wxxiaomi.teachingoffice2.view.adapter;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.BookInfo;

public class SearchResultColumnAdapter extends BaseAdapter {

	private Context context;
	private List<BookInfo> list;

	public SearchResultColumnAdapter(Context ct,List<BookInfo> list) {
		this.context = ct;
		this.list = list;
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
		BookInfo column = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.item_lib_searchresult, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
			holder.tv_collectCount = (TextView) convertView.findViewById(R.id.tv_collectCount);
			holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
			holder.tv_borrow = (TextView) convertView.findViewById(R.id.tv_borrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(column.getName());
		holder.tv_info.setText("信息："+column.getAuthor());
		holder.tv_collectCount.setText("馆藏："+column.getCollectionCount());
		holder.tv_number.setText("索取号："+column.getNumber());
		holder.tv_borrow.setText("可借："+column.getIsBorrow());
		return convertView;
	}

	public class ViewHolder {
		TextView tv_name;
		TextView tv_info;
		TextView tv_collectCount;
		TextView tv_number;
		TextView tv_borrow;
	}

}
