package com.chinamworld.bocmbci.biz.infoserve.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 描述：登陆后请求回来的必读消息体
 * @author dl
 *
 */
public class NeedReadMessage implements Parcelable{

	/**消息id*/
	private String globalMsgId;
	/**消息归属的主题*/
	private String subject;
	/**消息内容*/
	private String content;
	public String getGlobalMsgId() {
		return globalMsgId;
	}
	public void setGlobalMsgId(String globalMsgId) {
		this.globalMsgId = globalMsgId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "NeedReadMessage [globalMsgId=" + globalMsgId + ", subject="
				+ subject + ", content=" + content + "]";
	}
	
	public static final Parcelable.Creator<NeedReadMessage> CREATOR=new Creator<NeedReadMessage>() {

		@Override
		public NeedReadMessage createFromParcel(Parcel source) {
			LogGloble.d(this.getClass().getSimpleName(), "createFromParcel");
			NeedReadMessage needReadMessage = new NeedReadMessage();
			needReadMessage.globalMsgId=source.readString();
			needReadMessage.subject=source.readString();
			needReadMessage.content=source.readString();
			return needReadMessage;
		}

		@Override
		public NeedReadMessage[] newArray(int size) {
			return new NeedReadMessage[size] ;
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		LogGloble.d(this.getClass().getSimpleName(), "writeToParcel");
		dest.writeString(globalMsgId);
		dest.writeString(subject);
		dest.writeString(content);
	}
	
	

}
