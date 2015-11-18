package com.example.jinanuniversity.types;

import java.util.ArrayList;

public class PicPageType {
	private ArrayList<Integer> idList = new ArrayList<Integer>(); // 图片ID
	private ArrayList<String> picList = new ArrayList<String>(); // 原图url位置
	private ArrayList<String> picThumbList= new ArrayList<String>(); // 图片略缩图url位置
	private ArrayList<String> PDatetimeList= new ArrayList<String>(); // 图片时间信息
	
	public PicPageType() {
		super();
	}

	public PicPageType(ArrayList<Integer> idList, ArrayList<String> picList,
			ArrayList<String> picThumbList, ArrayList<String> pDatetimeList) {
		super();
		this.idList = idList;
		this.picList = picList;
		this.picThumbList = picThumbList;
		this.PDatetimeList = pDatetimeList;
	}

	public void addIdList(int id){
		idList.add(id);
	}
	
	public void addPicList(String pic){
		picList.add(pic);
	}
	
	public void addPicThumbList(String picThumb){
		picThumbList.add(picThumb);
	}
	
	public void addPDatetimeList(String PDatetime){
		PDatetimeList.add(PDatetime);
	}
	
	public ArrayList<Integer> getIdList() {
		return idList;
	}

	public void setIdList(ArrayList<Integer> idList) {
		this.idList = idList;
	}

	public ArrayList<String> getPicList() {
		return picList;
	}

	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}

	public ArrayList<String> getPicThumbList() {
		////////////////////////////////////////////////////////////////////////////////////////////
		return picThumbList;
//		return picList;
	}

	public void setPicThumbList(ArrayList<String> picThumbList) {
		this.picThumbList = picThumbList;
	}

	public ArrayList<String> getPDatetimeList() {
		return PDatetimeList;
	}

	public void setPDatetimeList(ArrayList<String> pDatetimeList) {
		PDatetimeList = pDatetimeList;
	}
	

	
}