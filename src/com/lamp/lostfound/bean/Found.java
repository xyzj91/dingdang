package com.lamp.lostfound.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.bmob.v3.BmobObject;
/**
 * 招领对象
 * @author xyzj9_000
 *
 */
public class Found extends BmobObject implements Parcelable {
	private String title;//标题
	private String describe;//描述
	private String phone;//联系手机
	
	
	
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
	
	 public static final Parcelable.Creator<Found> CREATOR = new Creator<Found>() {
		
		@Override
		public Found[] newArray(int size) {
			return new Found[size];
		}
		
		@Override
		public Found createFromParcel(Parcel source) {
			Found f = new Found();
			f.setObjectId(source.readString());
			f.setTitle(source.readString());
			f.setDescribe(source.readString());
			f.setPhone(source.readString());
			f.setCreatedAt(source.readString());
			return f;
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
		dest.writeString(describe);
		dest.writeString(phone);
		dest.writeString(getCreatedAt());
		
	}

}
