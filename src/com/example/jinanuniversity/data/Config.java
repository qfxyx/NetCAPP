package com.example.jinanuniversity.data;

public class Config {

	public static final String BAIDU_KEY = "43D397C31098B1754B6547D769ADA4FD8AEF5296";
	public static final String APPKEY = "JNUAPP-wlwhz";
	public static final String VER = "1";
	public static final String SECRET = "JNU$netc%APIy_(2#d^2+3@).D6D9)_&tC*QP-OM5kox333";

	public String DOMAIN_PORT = "http://"
			+ Service.getInstance().getServiceIp();

	/** 图片接口 */
	public final static String PICURL = "http://202.116.0.23/netcapi/images/";
	/** 登录 接口 */
	public String MARRYPIE_LOGIN = DOMAIN_PORT + "/mtLogin/login.do";
	/** 维护员明细 接口 */
	public String MTVWUSER = DOMAIN_PORT + "/mtVwUser/detail.do";
	/** 用户明细 接口 */
	public String USERDETAIL = DOMAIN_PORT + "/netcUser/detail.do";
	/** 用户明细 接口 */
	public String SAM3SYN = DOMAIN_PORT + "/netcUser/sam3Syn.do";
	/** 踢下线功能接口 */
	public String SAM3KICK = DOMAIN_PORT + "/netcUser/sam3Kick.do";
	/** 用户密码重置 接口 */
	public String PWDRESET = DOMAIN_PORT + "/netcUser/pwdReset.do";
	/** 信息发送 接口 */
	public String SEND = DOMAIN_PORT + "/mtMsg/send.do";
	/** 用户列表接口 接口 */
	public String USERLIST = DOMAIN_PORT + "/netcUser/list.do";
	/** 获取信息接口 */
	public String GETMESSAGE = DOMAIN_PORT + "/mtMsg/list.do";
	/** 信息明细接口 */
	public String MTMSGDETAILS = DOMAIN_PORT + "/mtMsg/detail.do";
	/** 获取维护单列表接口 */
	public String MTJOB = DOMAIN_PORT + "/mtJob/list.do";
	/** 历史维护单列表接口 */
	public String HISTORYJON = DOMAIN_PORT + "/mtHistoryJob/list.do";
	/** 获取维护单明细 */
	public String MTJOB_DETAIL = DOMAIN_PORT + "/mtJob/detail.do";
	/** 历史维护单明细接口 */
	public String HISTORYJON_DETAIL = DOMAIN_PORT + "/mtHistoryJob/detail.do";
	/** 新建信息接口 */
	public String NEWMSG = DOMAIN_PORT + "/mtMsg/create.do";
	/** 维护消单接口 */
	public String CLOSEJOB = DOMAIN_PORT + "/mtJob/close.do";
	/** 维护单延时接口 */
	public String MTJOB_DELAY = DOMAIN_PORT + "/mtJob/delay.do";
	/** 维护单转移接口 */
	public String MTJOB_PASS = DOMAIN_PORT + "/mtJob/pass.do";
	/** 维护单响应接口 */
	public String MTJOB_RESPOMSE = DOMAIN_PORT + "/mtJob/response.do";
	/** 用户物理地址修改接口 */
	public String MAC_UPDATE = DOMAIN_PORT + "/netcUser/macUpdate.do";
	/** 用户SAM3模版修改接口 */
	public String TEMPLATE = DOMAIN_PORT + "/netcUser/sam3Template.do";
	/** 用户物理地址解绑接口 */
	public String MAC_FREE = DOMAIN_PORT + "/netcUser/sam3MacFree.do";
	/** SAM3信息查询接口 */
	public String SAM3 = DOMAIN_PORT + "/netcUser/sam3Info.do";
	/** 用户SAM3认证日志接口 */
	public String SAM3LOGLIST = DOMAIN_PORT + "/netcUser/sam3LogList.do";
	/** SAM3上网明细接口 */
	public String SAM3DETAIL = DOMAIN_PORT + "/netcUser/sam3DetailList.do";
	/** 图片列表接口 */
	public String GETPICLIST = DOMAIN_PORT + "/mtPic/list.do";
	/** 图片明细接口 */
	public String GETPICDETAILS = DOMAIN_PORT + "/mtPic/detail.do";
	/** 注销登录接口 */
	public String LOGOUT = DOMAIN_PORT + "/mtLogin/logout.do";
}
