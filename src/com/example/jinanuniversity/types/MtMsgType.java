package com.example.jinanuniversity.types;

import android.os.Parcel;
import android.os.Parcelable;

public class MtMsgType implements IEasyType, Parcelable {

	private int id;
	private String sender;
	private String receiver;
	private String title;
	private String message;
	private String time;
	private boolean flag;
	private String memo;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(sender);
		out.writeString(receiver);
		out.writeString(title);
		out.writeString(message);
		out.writeString(time);
		out.writeString(memo);
	}

	private MtMsgType(Parcel in) {
		id = in.readInt();
		sender = in.readString();
		receiver = in.readString();
		title = in.readString();
		message = in.readString();
		time = in.readString();
		memo = in.readString();
	}

	private static Parcelable.Creator<MtMsgType> getCreator() {
		return CREATOR;
	}

	private final static Parcelable.Creator<MtMsgType> CREATOR = new Parcelable.Creator<MtMsgType>() {

		@Override
		public MtMsgType createFromParcel(Parcel source) {
			return new MtMsgType(source);
		}

		@Override
		public MtMsgType[] newArray(int size) {
			return new MtMsgType[size];
		}
	};

	public MtMsgType() {
		super();
	}

	public MtMsgType(int id, String sender, String receiver, String title,
			String message, String time, boolean flag, String memo) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.title = title;
		this.message = message;
		this.time = time;
		this.flag = flag;
		this.memo = memo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
