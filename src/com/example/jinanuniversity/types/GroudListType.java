package com.example.jinanuniversity.types;

import android.os.Parcel;
import android.os.Parcelable;

public class GroudListType implements IEasyType, Parcelable {

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	private String account;
	private String name;

	public GroudListType() {
		super();
	}

	public GroudListType(String account, String name) {
		super();
		this.account = account;
		this.name = name;
	}

	public static Parcelable.Creator<GroudListType> getCreator() {
		return CREATOR;
	}

	private GroudListType(Parcel in) {
		account = in.readString();
		name = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		// TODO Auto-generated method stub
		out.writeString(account);
		out.writeString(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static final Parcelable.Creator<GroudListType> CREATOR = new Parcelable.Creator<GroudListType>() {
		@Override
		public GroudListType createFromParcel(Parcel in) {
			return new GroudListType(in);
		}

		@Override
		public GroudListType[] newArray(int size) {
			return new GroudListType[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
