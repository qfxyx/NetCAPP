package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import com.example.jinanuniversity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewMessageAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;
	
	public NewMessageAdapter(Context context, List<Map<String, Object>> mList) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.photolist_item, null);
			holder.txtName = (TextView) convertView
					.findViewById(R.id.tx_name);
			holder.imgPhoto = (ImageView) convertView.findViewById(R.id.img_photo);
			holder.reList = (RelativeLayout) convertView
					.findViewById(R.id.re_list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		System.out.println("name=="+(String)mList.get(position).get("name"));
		return convertView;
	}
	
	public class ViewHolder {
		TextView txtName;
		ImageView imgPhoto;
		RelativeLayout reList;
	} 

}
