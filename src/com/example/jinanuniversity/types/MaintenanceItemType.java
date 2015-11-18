package com.example.jinanuniversity.types;


import android.os.Parcel;
import android.os.Parcelable;


public class MaintenanceItemType implements IEasyType, Parcelable{

	private int id;
	private String userId;
	private String name;
	private String jobLevel;
	private String dealResult;
	private String address;
	private String dealer1;
	private String dealTime1;
	private String dealer2;
	private String dealTime2;
	private String trblCource;
	

	public String getTrblCource() {
		return trblCource;
	}

	public void setTrblCource(String trblCource) {
		this.trblCource = trblCource;
	}

	public MaintenanceItemType() {
		super();
	}

	public MaintenanceItemType(String userId,String name, String jobLevel,
			String delResult, int id, String address, String dealer1, String dealTime1,
			String dealer2, String dealTime2, String trblCource) {
		super();
		this.userId = userId;
		this.name = name;
		this.jobLevel = jobLevel;
		this.dealResult = dealResult;
		this.id = id;
		this.address = address;
		this.dealer1 = dealer1;
		this.dealTime1 = dealTime1;
		this.dealer2 = dealer2;
		this.dealTime2 = dealTime2;
		this.trblCource = trblCource;
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


	public static Parcelable.Creator<MaintenanceItemType> getCreator() {
		return CREATOR;
	}

	private MaintenanceItemType(Parcel in) {
		userId = in.readString();
		name = in.readString();
		jobLevel = in.readString();
		dealResult = in.readString();
		id = in.readInt();
		address = in.readString();
		dealer1 = in.readString();
		dealTime1 = in.readString();
		dealer2 = in.readString();
		dealTime2 = in.readString();
		trblCource = in.readString();
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
		out.writeString(dealer1);
		out.writeString(dealTime1);
		out.writeString(dealer2);
		out.writeString(dealTime2);
		out.writeString(trblCource);
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

	public String getDealer1() {
		return dealer1;
	}

	public void setDealer1(String dealer1) {
		this.dealer1 = dealer1;
	}

	public String getDealTime1() {
		return dealTime1;
	}

	public void setDealTime1(String dealTime1) {
		this.dealTime1 = dealTime1;
	}

	public String getDealer2() {
		return dealer2;
	}

	public void setDealer2(String dealer2) {
		this.dealer2 = dealer2;
	}

	public String getDealTime2() {
		return dealTime2;
	}

	public void setDealTime2(String dealTime2) {
		this.dealTime2 = dealTime2;
	}

	public static final Parcelable.Creator<MaintenanceItemType> CREATOR = new Parcelable.Creator<MaintenanceItemType>() {
		@Override
		public MaintenanceItemType createFromParcel(Parcel in) {
			return new MaintenanceItemType(in);
		}

		@Override
		public MaintenanceItemType[] newArray(int size) {
			return new MaintenanceItemType[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
