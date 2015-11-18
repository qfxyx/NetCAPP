package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jinanuniversity.R;

public class UserDetailAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;
	
	public UserDetailAdapter(Context context, List<Map<String, Object>> mList) {
		this.mInflater = LayoutInflater.from(context);
		this.mList = mList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.userdetail_item, null);
			
			holder.userId = (TextView) convertView.findViewById(R.id.userId);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			holder.level = (TextView) convertView.findViewById(R.id.level);
			holder.userFlag = (TextView) convertView.findViewById(R.id.userFlag);
			holder.userAddress = (TextView) convertView.findViewById(R.id.userAddress);
			holder.userphone = (TextView) convertView.findViewById(R.id.userphone);
			holder.officeName = (TextView) convertView.findViewById(R.id.officeName);
			holder.email = (TextView) convertView.findViewById(R.id.email);
			holder.servIp = (TextView) convertView.findViewById(R.id.servIp);
			holder.servAbroad = (TextView) convertView.findViewById(R.id.servAbroad);
			holder.openDate = (TextView) convertView.findViewById(R.id.openDate);
			holder.fromDate = (TextView) convertView.findViewById(R.id.fromDate);
			holder.toDate = (TextView) convertView.findViewById(R.id.toDate);
			holder.activeFlag = (TextView) convertView.findViewById(R.id.activeFlag);
			holder.mac = (TextView) convertView.findViewById(R.id.mac);
			holder.ip = (TextView) convertView.findViewById(R.id.ip);
			holder.gateway = (TextView) convertView.findViewById(R.id.gateway);
			holder.mask = (TextView) convertView.findViewById(R.id.mask);
			holder.sam3Tempelate = (TextView) convertView.findViewById(R.id.sam3Tempelate);
//			holder.txtPhone = (TextView) convertView.findViewById(R.id.pho);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		System.out.println("userId=="+(String)mList.get(position).get("userId"));
		holder.userId.setText("用户编号：" + (String)mList.get(position).get("userId"));
		holder.userName.setText("用户姓名：" + (String)mList.get(position).get("userName"));
		holder.level.setText("用户等级：" + (String)mList.get(position).get("level"));
		holder.userFlag.setText("用户类型：" + (String)mList.get(position).get("usrFlag"));
		holder.userAddress.setText("用户住址：" + (String)mList.get(position).get("building") + "+" +
				(String)mList.get(position).get("roomNo"));
		holder.userphone.setText("电话号码：" + (String)mList.get(position).get("userphone"));
		holder.officeName.setText("单位：" + (String)mList.get(position).get("officeName"));
		holder.email.setText("邮箱：" + (String)mList.get(position).get("email"));
		holder.servIp.setText("是否开通有线网：" + (Boolean)mList.get(position).get("servIp"));
		holder.servAbroad.setText("是否开通直通IP：" + (Boolean)mList.get(position).get("servAbroad"));
		holder.openDate.setText("有线网开户日期：" + (String)mList.get(position).get("openDate"));
		holder.fromDate.setText("有线网起始日期：" + (String)mList.get(position).get("fromDate"));
		holder.toDate.setText("有线网结束日期：" + (String)mList.get(position).get("toDate"));
		holder.activeFlag.setText("是否到期：" + (Boolean)mList.get(position).get("activeFlag"));
		holder.mac.setText("地址：" + (String)mList.get(position).get("mac"));
		holder.ip.setText("IP：" + (String)mList.get(position).get("ip"));
		holder.gateway.setText("网关：" + (String)mList.get(position).get("gateway"));
		holder.mask.setText("掩码：" + (String)mList.get(position).get("mask"));
		holder.sam3Tempelate.setText("Sam3模块：" + (String)mList.get(position).get("sam3Tempelate"));
		return convertView;
	}
	
	public class ViewHolder {
		TextView userId ;
		TextView userName ;
		TextView level ;
		TextView userFlag ;
		TextView userAddress ;
		TextView userphone ;
		TextView officeName ;
		TextView email ;
		TextView servIp ;
		TextView servAbroad ;
		TextView openDate ;
		TextView fromDate ;
		TextView toDate ;
		TextView activeFlag ;
		TextView mac ;
		TextView ip ;
		TextView gateway ;
		TextView mask ;
		TextView sam3Tempelate ;
	} 
}
