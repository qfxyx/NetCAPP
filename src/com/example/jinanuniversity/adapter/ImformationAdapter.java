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
import com.example.jinanuniversity.Activity.Message_text;

public class ImformationAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;

	public ImformationAdapter(Context context, List<Map<String, Object>> mList) {
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
			convertView = mInflater.inflate(R.layout.inforadapter_item, null);
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.tx_title);
			holder.txtTime = (TextView) convertView.findViewById(R.id.tx_time);
			holder.txtContent = (TextView) convertView
					.findViewById(R.id.tx_content);
			holder.txtsender = (TextView) convertView
					.findViewById(R.id.tx_sender);
			holder.rl_reply = (RelativeLayout) convertView
					.findViewById(R.id.rl_infor);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Boolean flag = (Boolean) mList.get(position).get("flag");
		if (flag) {
			convertView.setBackgroundResource(R.color.white);
		} else {
			convertView.setBackgroundResource(R.color.gray);
		}
		String messge = (String) mList.get(position).get("message");
		holder.txtContent.setText((messge.replaceAll("\r", "")).replaceAll("\n", ""));
		holder.txtTitle.setText((String) mList.get(position).get("title"));
		holder.txtsender.setText((String) mList.get(position).get("sender"));
		holder.txtTime.setText((String) mList.get(position).get("time"));

		holder.rl_reply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, Message_text.class);
				intent.putExtra("msgId",
						(String) mList.get(position).get("msgId"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView txtTitle;
		TextView txtTime;
		TextView txtContent;
		TextView txtsender;
		RelativeLayout rl_reply;
	}
}
