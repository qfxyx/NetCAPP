package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.Activity.Main_inquire_detail;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MaintainInquireAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;
	
	public MaintainInquireAdapter(Context context, List<Map<String, Object>> mList) {
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
			convertView = mInflater.inflate(R.layout.inquireadapter_item, null);
			holder.user = (TextView) convertView
					.findViewById(R.id.te_user);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.handlePeople = (TextView) convertView
					.findViewById(R.id.handle_people);
			holder.handleTime = (TextView) convertView
			.findViewById(R.id.handle_time);
			holder.handlePeople2 = (TextView) convertView
			.findViewById(R.id.handle_people2);
			holder.handleTime2 = (TextView) convertView.findViewById(R.id.handle_time2);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			holder.cause = (TextView) convertView.findViewById(R.id.cause);
			holder.LayoutXQ = (RelativeLayout) convertView.findViewById(R.id.information);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.color.gray);
		}else{
			convertView.setBackgroundResource(R.color.white);
		}
		holder.user.setText("用户：" +(String)mList.get(position).get("userId")+ " " +(String)mList.get(position).get("name"));
		holder.address.setText("地址："+(String)mList.get(position).get("address"));
		holder.handlePeople.setText("处理人1："+(String)mList.get(position).get("dealer1"));
		holder.handleTime.setText("处理时间1:"+(String)mList.get(position).get("dealTime1"));
		holder.handlePeople2.setText("处理人2:"+(String)mList.get(position).get("dealer2"));
		holder.handleTime2.setText("处理时间2:"+(String)mList.get(position).get("dealTime2"));
		holder.status.setText("状态:"+(String)mList.get(position).get("dealResult"));
		holder.cause.setText("故障原因:"+(String)mList.get(position).get("jobLevel"));
		holder.LayoutXQ.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,Main_inquire_detail.class);
				intent.putExtra("id", (Integer) mList.get(position).get("id"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		TextView user;
		TextView address;
		TextView handlePeople;
		TextView handleTime;
		TextView handlePeople2;
		TextView handleTime2;
		TextView status;
		TextView cause;
		RelativeLayout LayoutXQ;
	} 

}
