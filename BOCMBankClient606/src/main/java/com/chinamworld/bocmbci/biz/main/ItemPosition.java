package com.chinamworld.bocmbci.biz.main;

import android.view.View;

/**
 * item位置
 * 
 * @author Administrator
 */
public class ItemPosition {

	private int page;
	private int position;
	private int currentPage;
	private int currentPosition;
	private int visibility;
	
	

	public ItemPosition(int page, int position) {
		this(page, page, position, position);
	}

	public ItemPosition(int page, int currentPage, int position, int currentPosition, int visibility) {
		super();
		this.page = page;
		this.currentPage = currentPage;
		this.position = position;
		this.currentPosition = currentPosition;
		this.visibility = visibility;
	}

	public ItemPosition(int page, int currentPage, int position, int currentPosition) {
		this(page, currentPage, position, currentPosition, View.VISIBLE);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public ItemPosition createItemLocation(int location, int row, int column) {
		int page = location / (row * column);
		int position = location % (row * column);
		return new ItemPosition(page, page, position, position);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[page :" + page + ", currentPage :" + this.currentPage + ", position:" + position
				+ ", currentPosition :" + currentPosition + ", visibility :" + this.visibility + "]");
		return sb.toString();
	}

}
