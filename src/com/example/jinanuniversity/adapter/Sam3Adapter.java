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

public class Sam3Adapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;
	
	public Sam3Adapter(Context context, List<Map<String, Object>> mList) {
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
			convertView = mInflater.inflate(R.layout.san3_item, null);
			
			holder.sam3Name = (TextView) convertView.findViewById(R.id.te_name);
			holder.sam3Address = (TextView) convertView.findViewById(R.id.address);
			holder.sam3Group = (TextView) convertView.findViewById(R.id.group);
			holder.sam3Tempelate = (TextView) convertView.findViewById(R.id.tempelate);
			holder.sam3Mac = (TextView) convertView.findViewById(R.id.mac);
			holder.sam3Power = (TextView) convertView.findViewById(R.id.power);
			holder.sam3Online = (TextView) convertView.findViewById(R.id.status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.sam3Name.setText("姓名：" + (String)mList.get(position).get("name"));
		holder.sam3Address.setText("用户住址：" + (String)mList.get(position).get("address"));
		holder.sam3Group.setText("group：" + (String)mList.get(position).get("group"));
		holder.sam3Tempelate.setText("Sam3模块：" + (String)mList.get(position).get("template"));
		holder.sam3Mac.setText("MAC：" + (String)mList.get(position).get("mac"));
		holder.sam3Power.setText("power：" + (String)mList.get(position).get("power"));
		holder.sam3Online.setText("是否在线：" + (String)mList.get(position).get("online"));
		System.out.println("name=="+(String)mList.get(position).get("name"));
		return convertView;
	}
	
	public class ViewHolder {
		TextView sam3Name ;
		TextView sam3Address ;
		TextView sam3Group ;
		TextView sam3Tempelate ;
		TextView sam3Mac ;
		TextView sam3Power ;
		TextView sam3Online ;
	} 
}
