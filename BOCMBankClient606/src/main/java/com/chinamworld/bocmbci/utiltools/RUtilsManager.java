package com.chinamworld.bocmbci.utiltools;

import java.lang.reflect.Field;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.log.LogGloble;




public class RUtilsManager extends BaseRUtil {

	public RUtilsManager()
	{
		Instance = this;
	}
	
	@Override
	public int getID(String idName) {
		String[] str = idName.split("\\.");
	    try {
	    	Class<?>[] classes = null;
	    	if(idName.contains("android.R"))
	    	{
	    		classes = android.R.class.getDeclaredClasses();
	    		String tmp = idName.replace("android.", "");
	    		str = tmp.split("\\.");
	    	}
	    	else {
	    		classes = R.class.getDeclaredClasses();
	    	}
			Class<?> rClass = null;
			for(Class<?> c  : classes){
				if(c.getName().endsWith(str[1])){
					rClass = c;
					break;
				}	
			}
			Field field = rClass.getField(str[2]);
			return (Integer)field.get(rClass);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	    LogGloble.e("BaseRUtils","未找到指定的ID:" + idName);
	    return -1;
	}

	@Override
	public int[] getArrayID(String idName) {
		String[] str = idName.split("\\.");
	    try {
	    	Class<?>[] classes;
	    	if(idName.contains("android.R"))
	    	{
	    		classes = android.R.class.getDeclaredClasses();
	    		String tmp = idName.replace("android.", "");
	    		str = tmp.split("\\.");
	    	}
	    	else {
	    		classes = R.class.getDeclaredClasses();
	    	}
			Class<?> rClass = null;
			for(Class<?> c  : classes){
				if(c.getName().endsWith(str[1])){
					rClass = c;
					break;
				}	
			}
			Field field = rClass.getField(str[2]);	
			return (int[])field.get(rClass);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	    LogGloble.e("BaseRUtils","未找到指定的ID:" + idName);
	    return null;
	}


}
