package com.hobai;

import java.sql.SQLException;

import com.hobai.util.FileUtil;
import com.hobai.util.StringUtil;

public class Table2Dao {
	public static final String LINE = "\r\n";//换行符
    public static final String TAB = "\t";//制表符
    public static void main(String[] args) {
    	String tableName="";
    	String packetPath ="";
    	String daopath=""+packetPath+"\\dao";
    	try {
			generateIDao(tableName, packetPath, daopath);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public static void generateIDao( String tableName,String packetPath,String daopath) throws SQLException {
    	 
    	 StringBuffer sb = new StringBuffer();
    	 String className=StringUtil.getClassName(tableName);
    	 sb.append("package"+ packetPath+".dao;");
         sb.append(LINE);  
    	 sb.append("import"+packetPath+".entity."+className+";");
    	 sb.append(LINE);  
    	 sb.append("public interface I"+className+"Dao  extends IBaseDao<"+className+"> {");
    	 sb.append("}");
    	 className="I"+className+"Dao";
    	 //System.out.println(sb.toString());
         FileUtil.toFile(daopath,className, sb.toString());
         System.out.println("IDao生成成功："+" 路径："+daopath);

    }
    //添加dao层
    public static void generateDao( String tableName,String packetPath,String daopath) throws SQLException {
    	//读取到字段注释
    	 StringBuffer sb = new StringBuffer();
    	 String className=StringUtil.getClassName(tableName);
    	 sb.append("package"+ packetPath+".dao.impl;");
         sb.append(LINE);  
         sb.append("import org.springframework.stereotype.Repository;");
         sb.append(LINE);
         sb.append("import "+ packetPath+".dao.BaseDao;");
         sb.append(LINE);  
         sb.append("import "+ packetPath+".dao.I"+className+"Dao;");
         sb.append(LINE);  
    	 sb.append("import "+ packetPath+".entity."+className+";");
    	 sb.append(LINE); 
    	 sb.append("@Repository");
    	 sb.append(LINE); 
    	 sb.append("public class "+className+"Dao extends BaseDao<"+className+"> implements I"+className+"Dao {");
    	 sb.append("}");
    	 className=className+"Dao";
    	// System.out.println(sb.toString());
         FileUtil.toFile(daopath+"\\impl",className, sb.toString());
         System.out.println("Dao生成成功："+" 路径："+daopath+"\\impl");

    }
}
