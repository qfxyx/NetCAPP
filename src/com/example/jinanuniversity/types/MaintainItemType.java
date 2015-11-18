package com.example.jinanuniversity.types;

import android.os.Parcel;
import android.os.Parcelable;

public class MaintainItemType implements IEasyType, Parcelable {

	private String userId;
	private String name;
	private String jobLevel;
	private String dealResult;
	private int id;
	private String address;
	private String accepteTime;
	private String trblStatus;

	public String getTrblStatus() {
		return trblStatus;
	}

	public void setTrblStatus(String trblStatus) {
		this.trblStatus = trblStatus;
	}

	public MaintainItemType() {
		super();
	}

	public MaintainItemType(String userId, String name, String jobLevel,
			String delResult, int id, String address, String accepteTime) {
		super();
		this.userId = userId;
		this.name = name;
		this.jobLevel = jobLevel;
		this.dealResult = dealResult;
		this.id = id;
		this.address = address;
		this.accepteTime = accepteTime;
	}

	public String getDealResult() {
		return dealResult;
	}

	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Parcelable.Creator<MaintainItemType> getCreator() {
		return CREATOR;
	}

	private MaintainItemType(Parcel in) {
		userId = in.readString();
		name = in.readString();
		jobLevel = in.readString();
		dealResult = in.readString();
		id = in.readInt();
		address = in.readString();
		accepteTime = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		// TODO Auto-generated method stub
		out.writeString(userId);
		out.writeString(name);
		out.writeString(jobLevel);
		out.writeString(dealResult);
		out.writeInt(id);
		out.writeString(address);
		out.writeString(accepteTime);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccepteTime() {
		return accepteTime;
	}

	public void setAccepteTime(String accepteTime) {
		this.accepteTime = accepteTime;
	}

	public static final Parcelable.Creator<MaintainItemType> CREATOR = new Parcelable.Creator<MaintainItemType>() {
		@Override
		public MaintainItemType createFromParcel(Parcel in) {
			return new MaintainItemType(in);
		}

		@Override
		public MaintainItemType[] newArray(int size) {
			return new MaintainItemType[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
