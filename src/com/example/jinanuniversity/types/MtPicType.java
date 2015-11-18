package com.example.jinanuniversity.types;

import android.os.Parcel;
import android.os.Parcelable;

public class MtPicType implements IEasyType, Parcelable {

	private int id;
	private String picname;
	private String tag;
	private String samllurl;
	private String bigurl;
	private String detail;
	private String owner;
	private String time;
	private String memo1;
	private String memo2;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(picname);
		out.writeString(tag);
		out.writeString(samllurl);
		out.writeString(bigurl);
		out.writeString(detail);
		out.writeString(owner);
		out.writeString(time);
		out.writeString(memo1);
		out.writeString(memo2);
	}

	private MtPicType(Parcel in) {
		id = in.readInt();
		picname = in.readString();
		tag = in.readString();
		samllurl = in.readString();
		bigurl = in.readString();
		detail = in.readString();
		owner = in.readString();
		time = in.readString();
		memo1 = in.readString();
		memo2 = in.readString();
	}

	private static Parcelable.Creator<MtPicType> getCreator() {
		return CREATOR;
	}

	private final static Parcelable.Creator<MtPicType> CREATOR = new Parcelable.Creator<MtPicType>() {

		@Override
		public MtPicType createFromParcel(Parcel source) {
			return new MtPicType(source);
		}

		@Override
		public MtPicType[] newArray(int size) {
			return new MtPicType[size];
		}
	};

	public MtPicType() {
		super();
	}

	public MtPicType(int id, String picname, String tag, String samllurl,
			String bigurl, String detail, String owner, String time,
			String memo1, String memo2) {
		super();
		this.id = id;
		this.picname = picname;
		this.tag = tag;
		this.samllurl = samllurl;
		this.bigurl = bigurl;
		this.detail = detail;
		this.owner = owner;
		this.time = time;
		this.memo1 = memo1;
		this.memo2 = memo2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicname() {
		return picname;
	}

	public void setPicname(String picname) {
		this.picname = picname;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSamllurl() {
		return samllurl;
	}

	public void setSamllurl(String samllurl) {
		this.samllurl = samllurl;
	}

	public String getBigurl() {
		return bigurl;
	}

	public void setBigurl(String bigurl) {
		this.bigurl = bigurl;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "MtPicType [id=" + id + ", picname=" + picname + ", tag=" + tag
				+ ", samllurl=" + samllurl + ", bigurl=" + bigurl + ", detail="
				+ detail + ", owner=" + owner + ", time=" + time + ", memo1="
				+ memo1 + ", memo2=" + memo2 + "]";
	}
}
