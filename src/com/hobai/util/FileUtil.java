package com.hobai.util;

import java.io.File;
import java.io.FileOutputStream;
/**
 * 
 * @Title: FileUtil.java
 * @Package com.util
 * @Description: 文件保存处理工具类(将字符串保存为文件)
 * @author Hobai Riku
 * @date 2016年7月13日 上午11:49:34
 * @version 1.0
 */
public class FileUtil {
	public static void toFile(String path,String fileName,String content){
		//保存到文件里
		try {
			File file = 
		    new File(path+"\\" + fileName+".java");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes("UTF-8"));
			fos.flush();
			fos.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
