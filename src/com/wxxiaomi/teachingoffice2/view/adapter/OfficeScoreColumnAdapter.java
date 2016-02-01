package com.wxxiaomi.teachingoffice2.view.adapter;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wxxiaomi.teachingoffice2.R;
import com.wxxiaomi.teachingoffice2.bean.Score.ScoreColumn;

public class OfficeScoreColumnAdapter extends BaseAdapter {

	private Context context;
	private List<ScoreColumn> list;

	public OfficeScoreColumnAdapter(Context ct,List<ScoreColumn> list) {
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
		ScoreColumn column = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.item_office_score_column, null);
			holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
			holder.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_score.setText(column.getScore()+"");
		holder.tv_subject.setText(column.getClassname());

		return convertView;
	}

	public class ViewHolder {
		TextView tv_subject;
		TextView tv_score;
	}

}
