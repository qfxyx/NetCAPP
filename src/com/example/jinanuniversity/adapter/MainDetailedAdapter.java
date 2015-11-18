package com.example.jinanuniversity.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.Activity.MaintianDetailed;

public class MainDetailedAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private Context context;
	
	public MainDetailedAdapter(Context context, List<Map<String, Object>> mList) {
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
			convertView = mInflater.inflate(R.layout.detaile_item, null);
			holder.txtNum = (TextView) convertView.findViewById(R.id.num);
			holder.txtTime = (TextView) convertView
					.findViewById(R.id.time);
			holder.txtUserNum = (TextView) convertView.findViewById(R.id.user_num);
			holder.txtUser = (TextView) convertView.findViewById(R.id.user_name);
			holder.txtAddress = (TextView) convertView.findViewById(R.id.address);
			holder.txtQuyi = (TextView) convertView.findViewById(R.id.quyi);
			holder.txtHitch = (TextView) convertView.findViewById(R.id.hitch);
			holder.txtPhone = (TextView) convertView.findViewById(R.id.phone);
			holder.txtAccepter = (TextView) convertView.findViewById(R.id.accepter);
			holder.txtLeading = (TextView) convertView.findViewById(R.id.leading);
			holder.txtGroup = (TextView) convertView.findViewById(R.id.group);
			holder.txtResponser = (TextView) convertView.findViewById(R.id.responser);
			holder.txtResponser_time = (TextView) convertView.findViewById(R.id.responser_time);
			holder.txtDealer1 = (TextView) convertView.findViewById(R.id.dealer1);
			holder.txtDealTime1 = (TextView) convertView.findViewById(R.id.dealTime1);
			holder.txtDealResult1 = (TextView) convertView.findViewById(R.id.dealResult1);
			holder.txtDealer2 = (TextView) convertView.findViewById(R.id.dealer2);
			holder.txtDealTime2 = (TextView) convertView.findViewById(R.id.dealTime2);
			holder.txtDealResult = (TextView) convertView.findViewById(R.id.dealResult);
			holder.txtTrblCource = (TextView) convertView.findViewById(R.id.trblCource);
			holder.txtDealMethod = (TextView) convertView.findViewById(R.id.dealMethod);
			
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
		holder.txtNum.setText("维护单号："+(Integer)mList.get(position).get("Id"));
		holder.txtTime.setText("报修时间：" + (String)mList.get(position).get("accepteTime"));
		holder.txtUserNum.setText("用户编号：" + (String)mList.get(position).get("userId"));
		holder.txtUser.setText("用户姓名：" + (String)mList.get(position).get("name"));
		holder.txtAddress.setText("地址：" + (String)mList.get(position).get("address"));
		holder.txtQuyi.setText("区域：" + (String)mList.get(position).get("area"));
		holder.txtHitch.setText("故障描述：" + (String)mList.get(position).get("trblStatus"));
		holder.txtPhone.setText("电话：" + (String)mList.get(position).get("phone"));
		holder.txtAccepter.setText("受理人：" + (String)mList.get(position).get("accepter"));
		holder.txtLeading.setText("负责人：" + (String)mList.get(position).get("groupAdmin"));
		holder.txtGroup.setText("维护组：" + (String)mList.get(position).get("groupName"));
		holder.txtResponser.setText("响应人：" + (String)mList.get(position).get("responser"));
		holder.txtResponser_time.setText("响应时间：" + (String)mList.get(position).get("responseTime"));
		holder.txtDealer1.setText("第一处理人：" + (String)mList.get(position).get("dealer1"));
		holder.txtDealTime1.setText("第一处理时间：" + (String)mList.get(position).get("dealTime1"));
		holder.txtDealResult1.setText("第一处理结果：" + (String)mList.get(position).get("dealResult1"));
		holder.txtDealer2.setText("第二处理人：" + (String)mList.get(position).get("dealer2"));
		holder.txtDealTime2.setText("第二处理时间：" + (String)mList.get(position).get("dealTime2"));
		holder.txtDealResult.setText("处理结果：" + (String)mList.get(position).get("dealResult"));
		holder.txtTrblCource.setText("故障原因：" + (String)mList.get(position).get("trblCource"));
		holder.txtDealMethod.setText("解决方法：" + (String)mList.get(position).get("dealMethod"));
		
		
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
		TextView txtNum;//维护单号
		TextView txtTime;//保修时间
		TextView txtUserNum;//用户编号
		TextView txtUser;//用户姓名
		TextView txtAddress;//地址
		TextView txtQuyi;//区域
		TextView txtHitch;//故障描述
		TextView txtPhone;//电话
		TextView txtAccepter;//受理人
		TextView txtLeading ;
		TextView txtGroup ;
		TextView txtResponser ;
		TextView txtResponser_time ;
		TextView txtDealer1 ;
		TextView txtDealTime1 ;
		TextView txtDealResult1 ;
		TextView txtDealer2 ;
		TextView txtDealTime2 ;
		TextView txtDealResult ;
		TextView txtTrblCource ;
		TextView txtDealMethod ;
		
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
