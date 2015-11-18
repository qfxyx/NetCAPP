package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.Activity.PhotoDetails;
import com.example.jinanuniversity.view.MyImageView;

public class PhotoListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;

	public PhotoListAdapter(Context context, List<Map<String, Object>> mList) {
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
			convertView = mInflater.inflate(R.layout.photolist_item, null);
			holder.tx_name = (TextView) convertView.findViewById(R.id.tx_name);
			holder.imgPhoto = (MyImageView) convertView
					.findViewById(R.id.img_photo);
			holder.re_list = (RelativeLayout) convertView
					.findViewById(R.id.re_list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tx_name.setText((String) mList.get(position).get("name"));
		String url = (String) mList.get(position).get("pic");
		holder.imgPhoto.setUrl(url, 100, 100);
		
		holder.re_list.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(context, PhotoDetails.class);
				intent.putExtra("picId", (String) mList.get(position).get("picId"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView tx_name;
		MyImageView imgPhoto;
		RelativeLayout re_list;
	}
}
