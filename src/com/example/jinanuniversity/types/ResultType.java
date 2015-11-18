package com.example.jinanuniversity.types;

public class ResultType{
	@Override
	public String toString() {
		return "ResultType [code=" + code + ", info=" + info + "]";
	}
	private int code;
	private String info;
	
	public ResultType() {
		super();
	}
	
	public ResultType(int code, String info) {
		super();
		this.code = code;
		this.info = info;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}