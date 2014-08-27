package com.lamp.lostfound.bean;

import cn.bmob.v3.BmobObject;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * 创建失物对象
 * @author xyzj9_000
 *
 */
public class Lost extends BmobObject implements Parcelable {
	private String title;//标题
	private String describe;//描述
	private String phone;//手机号
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public static final Parcelable.Creator<Lost> CREATOR = new Creator<Lost>() {
		
		@Override
		public Lost[] newArray(int size) {
			return new Lost[size];
		}
		
		@Override
		public Lost createFromParcel(Parcel source) {
			Lost lost = new Lost();
			lost.setObjectId(source.readString());
			lost.setTitle(source.readString());
			lost.setDescribe(source.readString());
			lost.setPhone(source.readString());
			lost.setCreatedAt(source.readString());
			return lost;
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getObjectId());
		dest.writeString(title);
		dest.writeString(phone);
		dest.writeString(describe);
		dest.writeString(getCreatedAt());
	}
	
	
	
}
