package com.boc.bocsoft.mobile.bocmobile.buss.system.life;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件工具类
 * @author eyding
 */
public class FileUtils {

	/**
	 * 文件copy 
	 * @param ins
	 * @param outPath
	 * @return  copy成功返回true
	 */
	public static boolean copyFile(InputStream ins,String outPath){
		if(ins == null || outPath == null||outPath.length()==0){
			return false;
		}
		
		File mFile = new File(outPath);
		if(mFile.exists()){
			mFile.delete();
		}
		
		mFile.getParentFile().mkdirs();
		
		try {
		  return 	copyFile(ins, new FileOutputStream(outPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean copyFile(String inPath,String outPath){
		try {
			return copyFile(new FileInputStream(inPath), outPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean copyFile(InputStream ins,OutputStream os){
		if(ins == null || os == null){
			return false;
		}
		
		try{
			
			byte[] bs = new byte[512];
			int len = ins.read(bs);
			
			for(;;){
				if(len == -1){
					break;
				}
				os.write(bs, 0, len);
				len = ins.read(bs);
			}
			
			os.flush();
			
		}catch(Exception exp){
			exp.printStackTrace();
			return false;
		}finally{
			if(ins != null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * 保存字符串到文件
	 * @param savString
	 * @param filePath
	 */
	public static boolean saveString2File(String savString,String filePath){
		if(filePath == null){
			return false;
		}
		
		if(savString == null){
			savString = "";
		}
		
		
		FileOutputStream os = null;
		try {
			
		File mFile = new File(filePath);
		if(mFile.exists()){
			mFile.delete();
		}
		mFile.getParentFile().mkdirs();
		
		 os= new FileOutputStream(mFile);
		 os.write(savString.getBytes());
		
		 os.flush();
		
		} catch (Exception e) {
			return false;
		}finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					os= null;
					e.printStackTrace();
				}
				os= null;
			}
		}
		
		
		return true;
	}
	
	/**
	 * 从文件中读取数据
	 * @param path
	 * @return
	 */
	public static String readStringFromFile(String path){
	
		if(!isFileExist(path)){
			return "";
		}
		
		try {
			File file = new File(path);
			file.mkdirs();
			return readStringFromInputStream(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 从输入流读取数据
	 * @param ins
	 * @return
	 */
	public static String readStringFromInputStream(InputStream ins){
	
		if(ins == null){
			return "";
		}
		
		StringBuilder sBuilder = new StringBuilder();
		BufferedReader mReader = null;
		
		try {
			mReader  = new BufferedReader(new InputStreamReader(ins,"utf-8"));
			
			for(;;){
				String line = mReader.readLine();
				if(line==null){
					break;
				}
				sBuilder.append(line);
			}
			
			
		} catch (Exception e) {
			
		}finally{
			if(mReader != null){
				try {
					mReader.close();
					mReader = null;
				} catch (IOException e1) {
					mReader = null;
				}
			}else{
				try {
					ins.close();
				} catch (IOException e1) {
					ins = null;
				}
			}
		}
		
		return sBuilder.toString();
		
	}
	
	/**
	 * 解压缩
	 * @return
	 */
	public static boolean unZip(InputStream ins,String outDir) {
		
		mkDirs(outDir);
		
		ZipInputStream zipInputStream = new ZipInputStream(ins);
		
		try {
			ZipEntry nextEntry;
			for (;;) {
				nextEntry = zipInputStream.getNextEntry();
				if (nextEntry == null) {
					break;
				}
				if(nextEntry.isDirectory()){
					//TODO 不支持！！！！！！
					break;
				}
				
				File mFile = new File(outDir,nextEntry.getName());
				if(mFile.exists()){
					mFile.delete();
				}
				FileOutputStream os = new FileOutputStream(mFile);
				byte[] bs= new byte[512];
				int len = 0;
				for(;;){
					len = zipInputStream.read(bs);
					if(len<0){
						break;
					}
					os.write(bs,0,len);
				}
				os.flush();
				os.close();
			}
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				zipInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static boolean save2File(InputStream ins,String filePath){
		if(filePath == null){
			return true;
		}
		File mFile = new File(filePath);
		if(mFile.exists()){
			mFile.delete();
		}
		mFile.getParentFile().mkdirs();
		
		if(ins == null){
			return true;
		}
		
		try {
			FileOutputStream os = new FileOutputStream(mFile);
			byte[] bs = new byte[512];
			for(;;){
				int len  = ins.read(bs);
				if(len==-1){
					break;
				}
				os.write(bs,0,len);
			}
			os.flush();
			
			ins.close();
			os.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	private static void unZip(File zipFile,String outDir){
		mkDirs(outDir);
		
	}
	
	
	/**
	 * 构造目录
	 * @param dir
	 */
	public static void mkDirs(String dir){
		File file = new File(dir);
		if(file.exists() && !file.isDirectory() || !file.exists()){
			file.mkdirs();
		}
	}
	
	
	/**
	 * 文件是否存在
	 * @param path 文件路径
	 * @return
	 */
	public static boolean isFileExist(String path){
		if(path == null || path.length() == 0){
			return false;
		}
		
		File mFile = new File(path);
		
		return mFile.exists();
	}
	
	/**
	 * 获取的android 缓存路径  data/data /packagename.../
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getAndroidCacheDir(Context mContext){
	
		return mContext.getCacheDir().getPath();
	}

	public static String getAndroidCacheDir(Context context,String dir){
		return new File(context.getCacheDir(),dir).getPath();
	}
	
	/**
	 * D
	 * @return
	 */
	public static String getSDDir(){
		return Environment.getExternalStorageDirectory().getPath();
	}
}
