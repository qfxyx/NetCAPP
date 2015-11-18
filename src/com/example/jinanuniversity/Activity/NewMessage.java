package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.NewMessageListParser;
import com.example.jinanuniversity.types.GroudListType;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.google.gson.Gson;

/**
 * 消息发送
 * @author Junbin
 */
public class NewMessage extends Activity implements OnClickListener {

	private EditText edt_title;
	private EditText edt_cont;
	private Button bt_empty, bt_send, bt_my_send, bt_my_all_send, bt_back;
	private ProgressBar message_refresh_progress;
	private PreferencesHelper preferencesHelper;
	private String account, timestamp, title, message;
	private int code;
	private String otherList = "otherList"; // 其他成员列表
	private String groudList = "groudList"; // 本组成员列表
	private String leaderList = "leaderList"; // 机动成员列表
	private Map<String, String> map = new HashMap<String, String>();
	private List<String> list = new ArrayList<String>();
	private ProgressDialog mProgressDialog;
	private ExAdapter adapter;
	private ExpandableListView exList;
	private List<Map<String, String>> groupData = new ArrayList<Map<String, String>>(); // 组别成员
	private List<List<Map<String, String>>> memberData = new ArrayList<List<Map<String, String>>>(); // 小组成员
	private static final String ISCHECKED = "isChecked"; // 记录小组成员是否选中 0 是 1否
	private static final String GROUP = "group";
	private static final String MEMBER = "member";
	private static final String ACCOUNT = "account";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newmessage);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		map.put(groudList, "本组成员");
		map.put(leaderList, "机动成员");
		map.put(otherList, "其他成员");

		initView();
		new getmtJobList().execute();
	}

	private void initView() {
		message_refresh_progress = (ProgressBar) findViewById(R.id.message_refresh_progress);
		exList = (ExpandableListView) findViewById(R.id.list);
		exList.setGroupIndicator(null);// 不设置大组指示器图标，因为我们自定义设置了
		exList.setDivider(null);// 设置图片可拉伸的
		adapter = new ExAdapter(NewMessage.this);
		exList.setAdapter(adapter);

		edt_title = (EditText) findViewById(R.id.edt_title);
		edt_cont = (EditText) findViewById(R.id.edt_cont);

		bt_empty = (Button) findViewById(R.id.bt_empty);
		bt_send = (Button) findViewById(R.id.bt_send);
		bt_my_send = (Button) findViewById(R.id.bt_my_send);
		bt_my_all_send = (Button) findViewById(R.id.bt_my_all_send);
		bt_back = (Button) findViewById(R.id.bt_back);

		bt_empty.setOnClickListener(this);
		bt_send.setOnClickListener(this);
		bt_my_send.setOnClickListener(this);
		bt_my_all_send.setOnClickListener(this);
		bt_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		List<String> list;
		switch (v.getId()) {
		case R.id.bt_send:
			sendMessage(NewMessage.this.list, "发送");
			break;

		case R.id.bt_my_send:
			list = new ArrayList<String>();
			for (int i = 0; i < memberData.get(0).size(); i++) {
				list.add(memberData.get(0).get(i).get(MEMBER));
			}
			sendMessage(list, "本组发送");
			break;

		case R.id.bt_my_all_send:
			list = new ArrayList<String>();
			for (int i = 0; i < groupData.size(); i++) {
				for (int j = 0; j < memberData.get(i).size(); j++) {
					list.add(memberData.get(i).get(j).get(MEMBER));
				}
			}
			sendMessage(list, "全部发送");
			break;

		case R.id.bt_empty:
			edt_cont.setText("");
			break;

		case R.id.bt_back:
			this.finish();
			break;
		}
	}

	/** 发送消息 */
	private void sendMessage(List<String> list, String memo) {
		if (check()) { // 检查内容
			if (list.size() > 0 && list != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<String> receivers = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++) {
					receivers.add(list.get(i));
				}
				map.put("receiverList", receivers);
				Gson gson = new Gson();
				String gsonString = gson.toJson(map);
				new sendInfo().execute(gsonString, memo); // 后台发送消息
			} else {
				Toast.makeText(this, "请选择发送对象!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/** 检查输入 */
	private boolean check() {
		title = edt_title.getText().toString().trim();
		message = edt_cont.getText().toString().trim();
		if (title.length() == 0) {
			Toast.makeText(getApplicationContext(), "请填写标题", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (message.length() == 0) {
			Toast.makeText(getApplicationContext(), "请填写内容", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	/**
	 * 异步加载数据
	 */
	private class getmtJobList extends AsyncTask<Void, Void, Integer> {
		List<GroudListType> other;
		List<GroudListType> group;
		List<GroudListType> leader;

		@Override
		protected Integer doInBackground(Void... params) {
			String re = getData();
			System.out.println("re==" + re);
			NewMessageListParser ww = new NewMessageListParser(re);
			code = ww.getCode();
			other = ww.praserList(otherList);
			group = ww.praserList(groudList);
			leader = ww.praserList(leaderList);
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			message_refresh_progress.setVisibility(View.GONE);
			if (result == 0) {
				AddGroup(group, groudList);
				AddGroup(leader, leaderList);
				AddGroup(other, otherList);
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			message_refresh_progress.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取数据
	 */
	private String getData() {
		timestamp = ParamsManager.getTime();
		String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
				+ timestamp + Config.VER + account);
		IEasy ieasy = new IEasy(new IEasyHttpApiV1());
		String re = "";
		re = ieasy
				.getmtMsg(sign, Config.APPKEY, timestamp, Config.VER, account);
		return re;
	}

	public void AddGroup(List<GroudListType> list, String name) {
		if (list != null && list.size() > 0) {
			// 增加组别
			Map<String, String> GroupMap = new HashMap<String, String>();
			GroupMap.put(GROUP, map.get(name));
			groupData.add(GroupMap);

			// 增加该组成员
			List<Map<String, String>> memBerList = new ArrayList<Map<String, String>>();
			try {
				for (int i = 0; i < list.size(); i++) {
					Map<String, String> memBerMap = new HashMap<String, String>();
					memBerMap.put(MEMBER, list.get(i).getName());
					memBerMap.put(ACCOUNT, list.get(i).getAccount());
					memBerMap.put(ISCHECKED, "1");
					memBerList.add(memBerMap);
				}
				memberData.add(memBerList);
			} catch (Exception e) {
				System.out.println("NewMessage-->增加成员出错!");
			}
		}
	}

	/** 后台线程执行消息发送 */
	public class sendInfo extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String receiver = params[0];
			String memo = params[1];
			String timestamp = ParamsManager.getTime();
			String sender = preferencesHelper.getString("name", "");

			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account + sender
					+ receiver + title + message + memo);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.sendLive(sign, timestamp, account, sender,
					receiver, title, message, memo);
			System.out.println("ddd==" + re);

			NewMessageListParser ww = new NewMessageListParser(re);
			int code = ww.getCode();
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dismissProgressDialog();
			if (result == 0) {
				Toast.makeText(getApplicationContext(), "发送成功",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "发送失败",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
	}

	// 关键代码是这个可扩展的listView适配器
	class ExAdapter extends BaseExpandableListAdapter {
		Context context;

		public ExAdapter(Context context) {
			super();
			this.context = context;
		}

		// 得到大组成员的view
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.member_group_item, null);
			}

			TextView title = (TextView) view.findViewById(R.id.group_name);
			title.setText(getGroup(groupPosition).toString());// 设置大组成员名称

			ImageView image = (ImageView) view.findViewById(R.id.tubiao);// 是否展开大组的箭头图标
			if (isExpanded) {// 大组展开时
				image.setBackgroundResource(R.drawable.btn_open);
			} else {
				// 大组合并时
				image.setBackgroundResource(R.drawable.btn_close);
			}
			return view;
		}

		// 得到大组成员的id
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 得到大组成员名称
		public Object getGroup(int groupPosition) {
			return groupData.get(groupPosition).get(GROUP).toString();
		}

		// 得到大组成员总数
		public int getGroupCount() {
			return groupData.size();
		}

		// 得到小组成员的view
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.member_member_item, null);
			}
			final String name = memberData.get(groupPosition)
					.get(childPosition).get(MEMBER).toString();
			final CheckBox checkBox = (CheckBox) view
					.findViewById(R.id.member_box);
			checkBox.setText(name);

			String isChecked = memberData.get(groupPosition).get(childPosition)
					.get(ISCHECKED);
			if (isChecked == "0") {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}

			checkBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isChecked = checkBox.isChecked();
					if (isChecked) {
						list.add(name);
						memberData.get(groupPosition).get(childPosition)
								.put(ISCHECKED, "0");
					} else {
						list.remove(name);
						memberData.get(groupPosition).get(childPosition)
								.put(ISCHECKED, "1");
					}
				}
			});

			ImageButton img_detail = (ImageButton) view
					.findViewById(R.id.img_detail);
			img_detail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(context, mtVwUserDetail.class);
					intent.putExtra("adminId", memberData.get(groupPosition)
							.get(childPosition).get(ACCOUNT));
					startActivity(intent);
				}
			});
			return view;
		}

		// 得到小组成员id
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 得到小组成员的名称
		public Object getChild(int groupPosition, int childPosition) {
			return memberData.get(groupPosition).get(childPosition).get(MEMBER)
					.toString();
		}

		// 得到小组成员的数量
		public int getChildrenCount(int groupPosition) {
			return memberData.get(groupPosition).size();
		}

		public boolean hasStableIds() {
			return true;
		}

		// 得到小组成员是否被选择
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("发送中，请稍候...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
		}
		mProgressDialog.show();
		return mProgressDialog;
	}

	private void dismissProgressDialog() {
		try {
			mProgressDialog.dismiss();
		} catch (IllegalArgumentException e) {
		}
	}
}
