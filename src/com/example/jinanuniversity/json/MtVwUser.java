package com.example.jinanuniversity.json;

import java.io.Serializable;
import java.util.Date;

public class MtVwUser implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String account;
	private String password;
	private String name;
	private String department;
	private String address;
	private String phone;
	private String shortphone;
	private String email;
	private String qq;
	private String area;
	private String groupname;
	private String bulidings; 
	private String role;
	private String property;
	
	private String querypwd;
	private String memo1;
	private String memo2;
	private int code;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "NetcUser [account=" + account + ", password=" + password
				+ ", name=" + name + ", department=" + department
				+ ", address=" + address + ", phone=" + phone + ", shortphone="
				+ shortphone + ", email=" + email + ", qq=" + qq + ", area="
				+ area + ", groupname=" + groupname + ", bulidings="
				+ bulidings + ", role=" + role + ", property=" + property
				+ ", querypwd=" + querypwd + ", memo1=" + memo1 + ", memo2="
				+ memo2 + "]";
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getShortphone() {
		return shortphone;
	}
	public void setShortphone(String shortphone) {
		this.shortphone = shortphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getBulidings() {
		return bulidings;
	}
	public void setBulidings(String bulidings) {
		this.bulidings = bulidings;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getQuerypwd() {
		return querypwd;
	}
	public void setQuerypwd(String querypwd) {
		this.querypwd = querypwd;
	}
	public String getMemo1() {
		return memo1;
	}
	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}
	public String getMemo2() {
		return memo2;
	}
	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}
	

}
