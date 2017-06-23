package com.boc.bocsoft.mobile.wfss.common.model;


import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;

import java.io.Serializable;

/**
 * CRHeader
 * @author Administrator
 *
 */
public class WFSSHeader implements Serializable{

	private String agent;
	private String version;
	private String device;
	private String platform;
	private String plugins;
	private String page;
	private String local;
	private String ext;
	private String cipherType;
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlugins() {
		return plugins;
	}
	public void setPlugins(String plugins) {
		this.plugins = plugins;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getCipherType() {
		return cipherType;
	}
	public void setCipherType(String cipherType) {
		this.cipherType = cipherType;
	}

	public WFSSHeader() {
		agent = WFSSGlobalConst.AGENT;
		version = WFSSGlobalConst.VERSION;
		device = WFSSGlobalConst.DEVICE;
		platform = WFSSGlobalConst.PLATFORM;
		plugins = WFSSGlobalConst.PLUGINS;
		page = WFSSGlobalConst.PAGE;
		local = WFSSGlobalConst.LOCAL;
		ext = WFSSGlobalConst.EXT;
		cipherType = WFSSGlobalConst.CIPHERTYPE;

	}
}
