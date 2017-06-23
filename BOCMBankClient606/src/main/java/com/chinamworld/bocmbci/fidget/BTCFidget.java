package com.chinamworld.bocmbci.fidget;
/**
 * 飞聚的实体Bean
 * @author xby
 */
public class BTCFidget {
	/*private String id;
	private String orc;
	private String name;
	private String version;
	private String md5;
	private String url;
	private String indexmd5;
	// add by xxh 2012-1-5 增加三个字段，中行标识，全国标识，下标
	private String owned;
	private String nationwide;
	private String index;
	*//**图片索引 add xby*//*
	private int drawable =-1;
	

	

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

	public BTCFidget(String id, String orc, String version, String name,
			String url, String md5, String indexmd5, String owned) {
		this.id = id;
		this.orc = orc;
		this.version = version;
		this.name = name;
		this.url = url;
		this.md5 = md5;
		this.indexmd5 = indexmd5;
		this.owned = owned;
	}
	
	public BTCFidget(String name,int drawable){
		this.name= name;
		this.drawable = drawable;
	}

	public String getOwned() {
		return owned;
	}

	public String getNationwide() {
		return nationwide;
	}

	public String getIndex() {
		return index;
	}

	public void setNationwide(String nationwide) {
		this.nationwide = nationwide;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	// end

	public String getMD5() {
		return md5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getID() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getVersion() {
		return version;
	}

	public String getOrc() {
		return orc;
	}

	public String getIndexMd5() {
		return indexmd5;
	}

	public boolean equals(BTCFidget o) {
		return o.getID().equals(id);
	}*/
}
