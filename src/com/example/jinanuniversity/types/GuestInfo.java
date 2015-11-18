package com.example.jinanuniversity.types;

import android.os.Parcel;
import android.os.Parcelable;

public class GuestInfo implements IEasyType, Parcelable {

	private String authCode;
	private int code ;
	private String info;
	private int Wid;
	private String address;
	private String marry_time;
//	private BigDecimal s;
	private String man;
	private String invitation_content;//请柬 内容
	private String longitude;
	private String latitude;
	private String appPic;
	private String story;
	private String loginTime;
	
	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
	
	public String getAppPic() {
		return appPic;
	}

	public void setAppPic(String appPic) {
		this.appPic = appPic;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getInvitation_content() {
		return invitation_content;
	}

	public void setInvitation_content(String invitation_content) {
		this.invitation_content = invitation_content;
	}

	public GuestInfo(String authCode, int code, String info, int wid) {
		super();
		this.authCode = authCode;
		this.code = code;
		this.info = info;
		Wid = wid;
	}
	
	private GuestInfo(Parcel in) {
		authCode = in.readString();
		code = in.readInt();
		info = in.readString();
		Wid = in.readInt();
	}
	
	public static final Parcelable.Creator<GuestInfo> CREATOR = new Parcelable.Creator<GuestInfo>() {
		@Override
		public GuestInfo createFromParcel(Parcel in) {
			return new GuestInfo(in);
		}

		@Override
		public GuestInfo[] newArray(int size) {
			return new GuestInfo[size];
		}
	};
	
	public GuestInfo() {
		super();
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
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
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMarry_time() {
		return marry_time;
	}
	
	public void setMarry_time(String marry_time) {
		this.marry_time = marry_time;
	}
	public int getWid() {
		return Wid;
	}

	public void setWid(int wid) {
		Wid = wid;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(authCode);
		out.writeInt(code);
		out.writeString(info);
		out.writeInt(Wid);
	}

	public String getMan() {
		return man;
	}

	public void setMan(String man) {
		this.man = man;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}
}
