package com.hobai;

import java.io.File;
import java.sql.SQLException;


public class GenerateAll {
	public static final String dburl = "127.0.0.1:1521";
    public static final String dbname = "XE";
    public static final String user = "u02";
    public static final String pwd = "u02"; 
    //工程源文件目录
    public static final String workspacepath="E:\\Desktop\\";
    //包路径
    public static final String packetPath="com.zrcx";
    //entity 包全称
    public static final String pakege="com.zrcx.entity";
    //mapper生成目标
    public static final String mapperFileDir=workspacepath+"src\\"+packetPath+"\\entity\\";
    //class生成目标
    public static final String classpath=workspacepath+"src\\"+packetPath+"\\entity\\";
    //dao生成目标目录
    public static final String daopath=workspacepath+"src\\"+packetPath+"\\dao\\";
    //表列表
    public static final String[] tableNames={"T_4S_DEPT"};
  
	public static void main(String[] args) {
		//创建entity文件夹
				File entityPath = new File(classpath);  
		        if (!entityPath.exists()) {  
		        	entityPath.mkdirs();// 目录不存在的情况下，创建目录  
		        }  
		    	//创建dao文件夹
		File daoPath = new File(daopath);  
        if (!daoPath.exists()) {  
        	daoPath.mkdirs(); 
        }  
    	//创建dao\\impl文件夹
        File daoImplPath = new File(daopath+"\\impl");   
        if (!daoImplPath.exists()) {  
        	daoImplPath.mkdirs();
        }  
		for(int i=0;i<tableNames.length;i++){
			String tableName=tableNames[i];
		try {
			Table2Dao.generateIDao(tableName, packetPath, daopath);
			Table2Dao.generateDao(tableName,  packetPath, daopath);
			Table2Batisxml.generateMapperFile(dburl, dbname, user, pwd, tableName, mapperFileDir, packetPath);
			Table2Pojo.generateClassFlie(dburl, dbname, user, pwd, pakege, tableName, classpath);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		}
	}

}
