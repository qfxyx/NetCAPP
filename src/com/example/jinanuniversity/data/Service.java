package com.example.jinanuniversity.data;

/**
 * 服务器地址
 */
public class Service {
	
	private static Service service;
	private String serviceIp;
	public String JSESSIONID = null;
	
	public static Service getInstance() {
		if (null == service) {
			synchronized (Service.class) {
				service = new Service();
			}
		}
		return service;
	}

	private Service() {
//		serviceIp = "netcapi1.jnu.edu.cn";
		serviceIp = "netcapi1.jnu.edu.cn";
	}

	public String getServiceIp() {
		return serviceIp;
	}

	public void setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
	}

	public String getJSESSIONID() {
		return JSESSIONID;
	}

	public void setJSESSIONID(String jSESSIONID) {
		JSESSIONID = jSESSIONID;
	}
}
