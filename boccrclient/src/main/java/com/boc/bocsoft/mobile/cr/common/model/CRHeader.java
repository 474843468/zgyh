package com.boc.bocsoft.mobile.cr.common.model;

import com.boc.bocsoft.mobile.cr.common.global.CRGlobalConst;
import java.io.Serializable;

/**
 * CRHeader
 * @author Administrator
 *
 */
public class CRHeader implements Serializable{

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

	public CRHeader() {
		agent = CRGlobalConst.AGENT;
		version = CRGlobalConst.VERSION;
		device = CRGlobalConst.DEVICE;
		platform = CRGlobalConst.PLATFORM;
		plugins = CRGlobalConst.PLUGINS;
		page = CRGlobalConst.PAGE;
		local = CRGlobalConst.LOCAL;
		ext = CRGlobalConst.EXT;
		cipherType = CRGlobalConst.CIPHERTYPE;

	}
}
