package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.Activity.MaintianDetailed;

public class MaintainAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;
	private String dealResult;

	public MaintainAdapter(Context context, List<Map<String, Object>> mList) {
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
			convertView = mInflater
					.inflate(R.layout.maintainadapter_item, null);
			holder.txtTime = (TextView) convertView.findViewById(R.id.tx_time);
			holder.txtSession = (TextView) convertView
					.findViewById(R.id.tx_session);
			holder.txtUser = (TextView) convertView.findViewById(R.id.tx_user);
			holder.txtAddress = (TextView) convertView
					.findViewById(R.id.tx_address);
			holder.rLayout = (RelativeLayout) convertView
					.findViewById(R.id.relativelayout);
			holder.txtrblStatus = (TextView) convertView
					.findViewById(R.id.tx_trblStatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		dealResult = (String) mList.get(position).get("dealResult");
		if (dealResult.equals("未处理")) {
			convertView.setBackgroundResource(R.color.gray);
		} else {
			convertView.setBackgroundResource(R.color.white);
		}
		holder.txtUser.setText("用户："
				+ (String) mList.get(position).get("userId") + "\t\t"
				+ (String) mList.get(position).get("name"));
		holder.txtAddress.setText("地址："
				+ (String) mList.get(position).get("address"));
		holder.txtTime.setText("下单时间："
				+ (String) mList.get(position).get("accepteTime"));
		holder.txtSession.setText("状态："
				+ (String) mList.get(position).get("dealResult"));
		holder.txtrblStatus.setText("故障描述: "
				+ mList.get(position).get("trblStatus").toString());

		if ((Integer) mList.get(position).get("id") != null) {
			holder.rLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, MaintianDetailed.class);
					intent.putExtra("id",
							(Integer) mList.get(position).get("id"));
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	public class ViewHolder {
		TextView txtTime;// 下单时间
		TextView txtSession;// 状态
		TextView txtUser;// 用户
		TextView txtAddress;// 地址
		TextView txtrblStatus;// 故障描述
		RelativeLayout rLayout;
	}
}
