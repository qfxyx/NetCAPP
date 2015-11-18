package com.example.jinanuniversity.util;

import java.io.IOException;

import android.util.Log;

import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.error.IEasyCredentialsException;
import com.example.jinanuniversity.error.IEasyException;
import com.example.jinanuniversity.error.IEasyParseException;

public class IEasy {
	private static final String TAG="IEasy";
	private IEasyHttpApiV1 mIEasyV1;
	private Config config;

	public IEasy(IEasyHttpApiV1 httpApi) {
		mIEasyV1 = httpApi;
		config = new Config();
	}

	// 验证码登陆验证
	public String invitationCodeLogin(String appkey, String timestamp,
			String sign, String ver, String account, String password) {
		String url = config.MARRYPIE_LOGIN;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.invitationCodeLogin(url, appkey, timestamp, sign,
					ver, account, password);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 获取信息 */
	public String getMessage(String appkey, String sign, String timestamp,
			String ver, String account, String pageNo) {
		String url = config.GETMESSAGE;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getMessage(url, appkey, sign, timestamp, ver,
					account, pageNo);
			return flag;
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 用户SAM3认证日志 */
	public String getsam3LogList(String appkey, String sign, String timestamp,
			String ver, String account, String userId, String startTime,
			String endTime, String pageNo) {
		String url = config.SAM3LOGLIST;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getsam3LogList(url, appkey, sign, timestamp, ver,
					account, userId, startTime, endTime, pageNo);
			return flag;
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/** SAM3上网明细 */
	public String getsam3DetailList(String appkey, String sign, String timestamp,
			String ver, String account, String userId, String startTime,
			String endTime, String pageNo) {
		String url = config.SAM3DETAIL;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getsam3LogList(url, appkey, sign, timestamp, ver,
					account, userId, startTime, endTime, pageNo);
			return flag;
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 发送 */
	public String sendLive(String sign, String timestamp, String account,
			String sender, String receiver, String title, String message,
			String memo) {
		String url = config.SEND;
		String flag = "noEffect";
		try {
			flag = mIEasyV1
					.sendLive(url, Config.APPKEY, sign, timestamp, Config.VER,
							account, sender, receiver, title, message, memo);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 获取维护单列表 */
	public String getMainList(String sign, String appkey, String timestamp,
			String ver, String account, String name, String ruleList,
			String pageNo) {
		String url = config.MTJOB;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getMainList(url, sign, appkey, timestamp, ver,
					account, name, ruleList, pageNo);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 历史维护单列表接口 */
	public String getMain_inquireList(String sign, String appkey,
			String timestamp, String ver, String account, String name,
			String ruleList, String pageNo) {
		String url = config.HISTORYJON;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getMain_inquireList(url, sign, appkey, timestamp,
					ver, account, name, ruleList, pageNo);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 获取维护单明细 */
	public String getMtJob(String sign, String appkey, String timestamp,
			String ver, String account, String jobId) {
		String url = config.MTJOB_DETAIL;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getMtJob(url, sign, appkey, timestamp, ver,
					account, jobId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 获取历史维护单明细 */
	public String getHistoryJob(String sign, String appkey, String timestamp,
			String ver, String account, String historyJobId) {
		String url = config.HISTORYJON_DETAIL;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getHistoryJob(url, sign, appkey, timestamp, ver,
					account, historyJobId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 获取新建信息 */
	public String getmtMsg(String sign, String appkey, String timestamp,
			String ver, String account) {
		String url = config.NEWMSG;
		String flag = "noEffect";
		try {
			flag = mIEasyV1
					.getmtMsg(url, sign, appkey, timestamp, ver, account);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 维护单消单 */
	public String closeJob(String appkey, String sign, String timestamp,
			String ver, String account, String jobId, String troubleReason,
			String dealResult, String dealMethod) {
		String url = config.CLOSEJOB;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.closeJob(url, appkey, sign, timestamp, ver,
					account, jobId, troubleReason, dealResult, dealMethod);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/** 注销登录 */
	public String cancel(String appkey, String sign, String timestamp,
			String ver, String account) {
		String url = config.LOGOUT;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.cancel(url, appkey, sign, timestamp, ver,
					account);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 维护单延时接口 */
	public String getDelayed(String appkey, String sign, String timestamp,
			String ver, String account, String jobId, String delayReason) {
		String url = config.MTJOB_DELAY;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getDelayed(url, appkey, sign, timestamp, ver,
					account, jobId, delayReason);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 维护单请求支援接口 */
	public String getPass(String appkey, String sign, String timestamp,
			String ver, String account, String jobId, String passReason,
			String passMemo) {
		String url = config.MTJOB_PASS;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getPass(url, appkey, sign, timestamp, ver, account,
					jobId, passReason, passMemo);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 维护单响应 */
	public String response(String appkey, String sign, String timestamp,
			String ver, String account, String jobId) {
		String url = config.MTJOB_RESPOMSE;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.response(url, appkey, sign, timestamp, ver,
					account, jobId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 维护员明细 */
	public String getVwUser(String sign, String timestamp, String account,
			String adminId) {
		String url = config.MTVWUSER;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getVwUser(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, adminId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 用户列表 */
	public String getUserList(String sign, String timestamp, String account,
			String ruleList, String pageNo, String queryPwd) {
		String url = config.USERLIST;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getUserList(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, ruleList, pageNo, queryPwd);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 用户明细 用户密码重置
	 * */
	public String getUserDetail(String url, String sign, String timestamp,
			String account, String userId) {
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getUserDetail(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, userId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 用户物理地址修改
	 * */
	public String macUpdate(String sign, String timestamp, String account,
			String userId, String mac) {
		String url = config.MAC_UPDATE;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.macUpdate(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, userId, mac);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 用户SAM3模版修改
	 */
	public String template(String sign, String timestamp, String account,
			String userId, String template) {
		String url = config.TEMPLATE;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.template(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, userId, template);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 图片列表
	 */
	public String getPicList(String sign, String timestamp, String account,
			String building, String pageNo) {
		String url = config.GETPICLIST;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getPicList(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, building, pageNo);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 图片明细
	 */
	public String getPicDetails(String sign, String timestamp, String account,
			String picId ) {
		String url = config.GETPICDETAILS;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getPicDetails(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, picId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 信息明细
	 */
	public String getMsgDetails(String sign, String timestamp, String account,
			String msgId ) {
		String url = config.MTMSGDETAILS;
		String flag = "noEffect";
		try {
			flag = mIEasyV1.getMsgDetails(url, Config.APPKEY, sign, timestamp,
					Config.VER, account, msgId);
		} catch (IEasyCredentialsException e) {
			e.printStackTrace();
		} catch (IEasyParseException e) {
			e.printStackTrace();
		} catch (IEasyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
