package com.chinamworld.bocmbci.biz.main;

import java.util.ArrayList;

import android.content.Context;

import com.chinamworld.bocmbci.widget.entity.ImageAndText;

public class Item {

	private String name;
	private int nameResourcesId;
	private int drawableResourcesId;
	private ItemPosition location;
	private ArrayList<ImageAndText> imageAndText;
	private boolean isPlugin = false;

	/**主菜单ID*/
	public final String MenuID;
//	public Item(Context context, int nameId, int resId, ItemPosition location) {
//		this(context, nameId, resId, location, false);
//	}
//
//	public Item(Context context, int nameId, int resId, ItemPosition location, boolean isPlugin) {
//		super();
//		this.name = context.getText(nameId).toString();
//		this.drawableResourcesId = resId;
//		this.location = location;
//		this.isPlugin = isPlugin;
//	}

	public Item(Context context, int nameId, int resId, ArrayList<ImageAndText> imageAndText, boolean isPlugin, String MenuID) {
		super();
		this.name = context.getText(nameId).toString();
		this.drawableResourcesId = resId;
		this.imageAndText = imageAndText;
		this.isPlugin = isPlugin;
		this.MenuID = MenuID;
	}

	public boolean isPlugin() {
		return isPlugin;
	}

	public void setPlugin(boolean isPlugin) {
		this.isPlugin = isPlugin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNameResourcesId() {
		return nameResourcesId;
	}

	public void setNameResourcesId(int nameResourcesId) {
		this.nameResourcesId = nameResourcesId;
	}

	public int getDrawableResourcesId() {
		return drawableResourcesId;
	}

	public void setDrawableResourcesId(int resId) {
		this.drawableResourcesId = resId;
	}

	public ItemPosition getLocation() {
		return location;
	}

	public void setLocation(ItemPosition location) {
		this.location = location;
	}

	public ArrayList<ImageAndText> getImageAndText() {
		return imageAndText;
	}

	public void setImageAndText(ArrayList<ImageAndText> imageAndText) {
		this.imageAndText = imageAndText;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[ name :" + this.name + ", resId :" + this.drawableResourcesId + ", location :" + this.location
				+ "]");
		return sb.toString();
	}

}
