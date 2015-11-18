package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.Activity.UserDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;

	public UserListAdapter(Context context, List<Map<String, Object>> mList) {
		this.mInflater = LayoutInflater.from(context);
		this.mList = mList;
		this.context = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.userlist_item, null);
			holder.txtName = (TextView) convertView.findViewById(R.id.tx_name);
			holder.txtFlag = (TextView) convertView.findViewById(R.id.txt_flag);
			holder.txtNumber = (TextView) convertView
					.findViewById(R.id.tv_number);
			holder.txtIp = (TextView) convertView.findViewById(R.id.tv_ip);
			holder.txtAddress = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.reList = (RelativeLayout) convertView
					.findViewById(R.id.reList);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.color.gray);
		}else{
			convertView.setBackgroundResource(R.color.white);
		}
		holder.txtNumber.setText("编号："
				+ (String) mList.get(position).get("userId"));
		holder.txtName.setText("姓名："
				+ (String) mList.get(position).get("userName"));
		holder.txtFlag.setText("类型 ："
				+ (String) mList.get(position).get("usrFlag"));
		holder.txtIp.setText("IP：" + (String) mList.get(position).get("ip"));
		holder.txtAddress.setText("地址："
				+ (String) mList.get(position).get("building"));
		holder.reList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserDetailActivity.class);
				intent.putExtra("userId",(String) mList.get(position).get("userId"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView txtName;
		TextView txtFlag;
		TextView txtNumber;
		TextView txtIp;
		TextView txtAddress;
		RelativeLayout reList;
	}
}
