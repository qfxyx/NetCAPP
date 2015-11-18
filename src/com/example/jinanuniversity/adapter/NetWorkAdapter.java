package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import com.example.jinanuniversity.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NetWorkAdapter extends BaseAdapter {

	private static final String TAG = "adapter.NetWorkAdapter";
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;

	public NetWorkAdapter(Context context, List<Map<String, Object>> mList) {
		this.context = context;
		this.mInflater = LayoutInflater.from(this.context);
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dailyrecord_item, null);
			holder.txtMseeage = (TextView) convertView
					.findViewById(R.id.message);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtMseeage.setText((String) mList.get(position).get(
				"sam3DetailList"));
		return convertView;
	}

	public class ViewHolder {
		TextView txtMseeage;
	}

}
