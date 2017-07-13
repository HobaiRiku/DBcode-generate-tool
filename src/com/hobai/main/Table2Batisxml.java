package com.hobai.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hobai.entity.CTableProperty;
import com.hobai.util.DbconnUtil;
import com.hobai.util.StringUtil;
/**
 * 
 * @Title: Table2Batisxml.java
 * @Package com
 * @Description: Mybatis映射文件生成
 * @author Hobai Riku
 * @date 2016年7月13日 上午11:34:06
 * @version 1.0
 */
public class Table2Batisxml {
	
	public static String NEWLINE = "\n";
	public static String INDENT_1 = "\t";
	public static String INDENT_2 = "\t\t";
	public static String INDENT_3 = "\t\t\t";
	public static String INDENT_4 = "\t\t\t\t";
	public static String ENCODE = "UTF-8";
    //数据库连接配置
    public static final String dburl = "127.0.0.1:1521";
    public static final String dbname = "XE";
    public static final String user = "u01";
    public static final String pwd = "u01";

	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		//实体名称（小写）
		String name ="user";
    	//表名称
    	String tableName = "t_"+name;
    	String className = name.substring(0, 1).toUpperCase()+name.substring(1);
		String sequenceName = "seq_t_"+name;
		String packetPath = "com.zrcx";
		//生成文件存放路径
		String mapperFileDir = "E:\\Desktop\\";
    	Connection con = getConnection();
    	//生成实体
       	generateEntity(con,tableName);
        //生成xml
		generateMapperXMLFile(mapperFileDir,con,tableName,sequenceName,packetPath,className);
		
	}
	
   /**
    * 
    * @Description: 获取数据库连接
    * @return
    * @throws ClassNotFoundException
    * @throws SQLException   
    * Connection  
    * @throws
    * @author Hobai Riku
    * @date 2016年7月13日 上午11:40:58
    */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
       return DbconnUtil.getConnection(dburl, dbname, user, pwd, DbconnUtil.ORACLE);
    }
	
    /**
     * 
     * @Description: 创建实体类
     * @param con
     * @param tableName   
     * void  
     * @throws
     * @author Hobai Riku
     * @date 2017年6月13日 上午11:41:18
     */
	public static void generateEntity(Connection con,String tableName) {

		OutputStreamWriter osw = null;
		List<CTableProperty> cTablePropertyList = null;
		try {
			cTablePropertyList = getTableStructByCon(con,tableName);

			for (int i = 0, listSize = cTablePropertyList.size(); i < listSize; i++) {
				CTableProperty cTableProperty = cTablePropertyList.get(i);
				System.out.println("private " + cTableProperty.getFieldTypeName() + " " + cTableProperty.getFieldName() + ";");
				System.out.println("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * @Description: 生成mybatis配置文件
	 * @param mapperFileDir
	 * @param con
	 * @param tableName
	 * @param sequenceName
	 * @param packetPath
	 * @param tName   
	 * void  
	 * @throws
	 * @author Hobai Riku
	 * @date 2017年7月13日 上午11:41:50
	 */
	public static void generateMapperXMLFile(String mapperFileDir ,Connection con,
		String tableName,String sequenceName,String packetPath,String tName) {

		String primaryKeyColumnName = "ID";
		String primaryKeyFieldTypeName = "java.lang.Long";

		String primaryKeyFieldName = StringUtil.toVariableName(primaryKeyColumnName);
		//String entityName = StringUtil.toVariableName(tableName);
		String entityName = tName;
		//String tName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
		String resultMapId = "rs"+tName;
		//String mapperPath = packetPath + ".dao.mapper." + tName +"Mapper";
		String mapperPath =  tName +"Mapper";
		String classPath = packetPath + ".entity." + tName;
		String fileFullPath = mapperFileDir + tName + "Mapper.xml";
		
		OutputStreamWriter osw = null;
		List<CTableProperty> cTablePropertyList = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(new File(fileFullPath)), ENCODE);
			//获取所有的列对象
			cTablePropertyList = getTableStructByCon(con,tableName);

			osw.write("" + "<?xml version=\"1.0\" encoding=\"" + ENCODE + "\"?>" + NEWLINE);
			osw.write("" + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">" + NEWLINE);
			osw.write("" + "<mapper namespace=\"" + mapperPath + "\">" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			
			osw.write(INDENT_1 + "<resultMap id=\"" + resultMapId + "\" type=\"" + classPath + "\">" + NEWLINE);
			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
				CTableProperty cTableProperty = cTablePropertyList.get(i);
				osw.write(INDENT_2 + "<result property=\"" + cTableProperty.getFieldName() + "\" column=\"" + cTableProperty.getColumnName() + "\" />" + NEWLINE);
			}
			osw.write(INDENT_1 + "</resultMap>" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------

			osw.write(INDENT_1 + "<sql id=\"sql_column_items\">" + NEWLINE);
			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
				CTableProperty cTableProperty = cTablePropertyList.get(i);
				if(i == listSize-1){
					osw.write(INDENT_2 + cTableProperty.getColumnName() + NEWLINE);
				}else {
					osw.write(INDENT_2 + cTableProperty.getColumnName() + ", " + NEWLINE);
				}
			}
			osw.write(INDENT_1 + "</sql>" + NEWLINE);

			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			
/*			osw.write(INDENT_1 + "<select id=\"findAll\" resultMap=\"" + resultMapId + "\">" + NEWLINE);
			osw.write(INDENT_2 + "select  <include refid=\"sql_column_items\"/> from " + tableName + NEWLINE);
			osw.write(INDENT_1 + "</select>" + NEWLINE);*/
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			
			osw.write(INDENT_1 + "<select id=\"findById\" resultMap=\"" + resultMapId + "\" parameterType=\"" + primaryKeyFieldTypeName + "\">" + NEWLINE);
			osw.write(INDENT_2 + "select  <include refid=\"sql_column_items\"/> from " + tableName + NEWLINE);
			osw.write(INDENT_2 + "<where>" + NEWLINE);
			osw.write(INDENT_3 + primaryKeyColumnName + " = #{" + primaryKeyFieldName + "}" + NEWLINE);
			osw.write(INDENT_2 + "</where>" + NEWLINE);
			osw.write(INDENT_1 + "</select>" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			
/*			osw.write(INDENT_1 + "<select id=\"findPage\" resultMap=\"" + resultMapId + "\">" + NEWLINE);
			osw.write(INDENT_2 + "select  <include refid=\"sql_column_items\"/> from " + tableName + NEWLINE);
			osw.write(INDENT_2 + "<where>" + NEWLINE);
//			osw.write(INDENT_3 + "1 = 1" + NEWLINE);
			
			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
				CTableProperty cTableProperty = cTablePropertyList.get(i);

				String entityFieldName = entityName + "." + cTableProperty.getFieldName();
				if ("java.lang.String".equalsIgnoreCase((cTableProperty.getFieldTypeName()))) {
					osw.write(INDENT_3 + "<if test=\"" + entityName + " != null and " + entityFieldName + " != null and " + entityFieldName + " != '' \">  " + NEWLINE);
					osw.write(INDENT_4 + "and " + cTableProperty.getColumnName() + " = #{" + entityFieldName + "}" + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);
					
				} else if ("java.util.Date".equalsIgnoreCase((cTableProperty.getFieldTypeName()))) {

					osw.write(INDENT_3 + "<if test=\""+cTableProperty.getFieldName() +"Begin != null\" >" + NEWLINE);
					osw.write(INDENT_4 + "<![CDATA[" + NEWLINE);
					osw.write(INDENT_4 + "and "+cTableProperty.getColumnName()+" >= #{"+cTableProperty.getFieldName()+"Begin} " + NEWLINE);
					osw.write(INDENT_4 + "]]> " + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);

					osw.write(INDENT_3 + "<if test=\""+cTableProperty.getFieldName() +"End != null\" > " + NEWLINE);
					osw.write(INDENT_4 + "<![CDATA[" + NEWLINE);
					osw.write(INDENT_4 + "and "+cTableProperty.getColumnName()+" <= #{"+cTableProperty.getFieldName()+"End}" + NEWLINE);
					osw.write(INDENT_4 + "]]> " + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);

				}else {
					osw.write(INDENT_3 + "<if test=\"" + entityName + " != null and " + entityFieldName + " != null \">  " + NEWLINE);
					osw.write(INDENT_4 + "and " + cTableProperty.getColumnName() + " = #{" + entityFieldName + "}" + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);
				}
			}
			osw.write(INDENT_2 + "</where>" + NEWLINE);

			osw.write(INDENT_2 + "<if test=\"order != null and order != '' \">  " + NEWLINE);
			osw.write(INDENT_3 + "order by ${order} ${sort}" + NEWLINE);
			osw.write(INDENT_2 + "</if>" + NEWLINE);
			
			osw.write(INDENT_1 + "</select>" + NEWLINE);*/

			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
//
//			osw.write(INDENT_1 + "<select id=\"findList\" resultMap=\"" + resultMapId + "\" parameterType=\"" + classPath + "\">" + NEWLINE);
//			osw.write(INDENT_2 + "select  <include refid=\"sql_column_items\"/> from " + tableName + NEWLINE);
//			osw.write(INDENT_2 + "<where>" + NEWLINE);
////			osw.write(INDENT_3 + "1 = 1" + NEWLINE);
//			
//			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
//				CTableProperty cTableProperty = cTablePropertyList.get(i);
//
//				if ("java.lang.String".equalsIgnoreCase((cTableProperty
//						.getFieldTypeName()))) {
//					osw.write(INDENT_3 + "<if test=\"" + cTableProperty.getFieldName() + " != null and " + cTableProperty.getFieldName() + " != '' \">  " + NEWLINE);
//					osw.write(INDENT_4 + "and " + cTableProperty.getColumnName() + " = #{" + cTableProperty.getFieldName() + "}" + NEWLINE);
//					osw.write(INDENT_3 + "</if>" + NEWLINE);
//					
//				}else {
//					osw.write(INDENT_3 + "<if test=\"" + cTableProperty.getFieldName() + " != null \">  " + NEWLINE);
//					osw.write(INDENT_4 + "and " + cTableProperty.getColumnName() + " = #{" + cTableProperty.getFieldName() + "}" + NEWLINE);
//					osw.write(INDENT_3 + "</if>" + NEWLINE);
//				}
//			}
//			
//			osw.write(INDENT_2 + "</where>" + NEWLINE);
//			osw.write(INDENT_1 + "</select>" + NEWLINE);

			osw.write(INDENT_1 + "<select id=\"findList\" resultMap=\"" + resultMapId + "\">" + NEWLINE);
			osw.write(INDENT_2 + "select  <include refid=\"sql_column_items\"/> from " + tableName + NEWLINE);
			osw.write(INDENT_2 + "<where>" + NEWLINE);
//			osw.write(INDENT_3 + "1 = 1" + NEWLINE);
			
			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
				CTableProperty cTableProperty = cTablePropertyList.get(i);

				String entityFieldName = entityName + "." + cTableProperty.getFieldName();
				if ("java.lang.String".equalsIgnoreCase((cTableProperty.getFieldTypeName()))) {
					osw.write(INDENT_3 + "<if test=\"" + entityName + " != null and " + entityFieldName + " != null and " + entityFieldName + " != '' \">  " + NEWLINE);
					osw.write(INDENT_4 + "and " + cTableProperty.getColumnName() + " = #{" + entityFieldName + "}" + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);
					
				} else if ("java.util.Date".equalsIgnoreCase((cTableProperty.getFieldTypeName()))) {

					osw.write(INDENT_3 + "<if test=\""+cTableProperty.getFieldName() +"Begin != null\" >" + NEWLINE);
					osw.write(INDENT_4 + "<![CDATA[" + NEWLINE);
					osw.write(INDENT_4 + "and "+cTableProperty.getColumnName()+" >= #{"+cTableProperty.getFieldName()+"Begin} " + NEWLINE);
					osw.write(INDENT_4 + "]]> " + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);

					osw.write(INDENT_3 + "<if test=\""+cTableProperty.getFieldName() +"End != null\" > " + NEWLINE);
					osw.write(INDENT_4 + "<![CDATA[" + NEWLINE);
					osw.write(INDENT_4 + "and "+cTableProperty.getColumnName()+" <= #{"+cTableProperty.getFieldName()+"End}" + NEWLINE);
					osw.write(INDENT_4 + "]]> " + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);

				}else {
					osw.write(INDENT_3 + "<if test=\"" + entityName + " != null and " + entityFieldName + " != null \">  " + NEWLINE);
					osw.write(INDENT_4 + "and " + cTableProperty.getColumnName() + " = #{" + entityFieldName + "}" + NEWLINE);
					osw.write(INDENT_3 + "</if>" + NEWLINE);
				}
			}
			osw.write(INDENT_2 + "</where>" + NEWLINE);

			osw.write(INDENT_2 + "<if test=\"order != null and order != '' \">  " + NEWLINE);
			osw.write(INDENT_3 + "order by ${order} ${sort}" + NEWLINE);
			osw.write(INDENT_2 + "</if>" + NEWLINE);
			
			osw.write(INDENT_1 + "</select>" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			
			String insertColumnNames = "";
			String insertFieldNames = "";
			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
				CTableProperty cTableProperty = cTablePropertyList.get(i);
				String columnName = cTableProperty.getColumnName();
				String fieldName = cTableProperty.getFieldName();
				String columnTypeName = cTableProperty.getColumnTypeName();

				insertColumnNames += INDENT_3 + columnName;
				
				if (columnName.equalsIgnoreCase(primaryKeyColumnName) && sequenceName != null && sequenceName != "") {
					insertFieldNames += INDENT_3 + sequenceName + ".nextval";
				}else if(columnName.equalsIgnoreCase("CREATE_DATE")){
					insertFieldNames += INDENT_3 + "sysdate";
				}else {
					String jdbcType = null;
					if(columnTypeName.equalsIgnoreCase("VARCHAR2")) {
						jdbcType = "VARCHAR";
					}else if(columnTypeName.equalsIgnoreCase("NUMBER")) {
						jdbcType = "NUMERIC";
					}else if(columnTypeName.equalsIgnoreCase("DATE")) {
						jdbcType = "TIMESTAMP";
					}
					insertFieldNames +=  INDENT_3 + "#{" + fieldName + ", jdbcType="+jdbcType+"}";
				}
				
				if(i != listSize - 1 ){
					insertColumnNames += ", " + NEWLINE;
					insertFieldNames += ", " + NEWLINE;
				}
				
			}
			
			osw.write(INDENT_1 + "<insert id=\"add\" parameterType=\"" + classPath + "\">" + NEWLINE);
			osw.write(INDENT_2 + "insert into " + tableName + " " + NEWLINE);
			osw.write(INDENT_3 + "(" + NEWLINE + insertColumnNames + NEWLINE + INDENT_3 + ") " + NEWLINE);
			osw.write(INDENT_2 + "values" + NEWLINE);
			osw.write(INDENT_3 + "(" + NEWLINE + insertFieldNames + NEWLINE + INDENT_3 + ")" + NEWLINE);
			osw.write(INDENT_1 + "</insert>" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			osw.write(INDENT_1 + "<update id=\"update\" parameterType=\"" + classPath + "\">" + NEWLINE);
			osw.write(INDENT_2 + "update " + tableName + NEWLINE);
			osw.write(INDENT_3 + " <set> " + NEWLINE);
			
			for(int i=0, listSize=cTablePropertyList.size(); i<listSize; i++){
				CTableProperty cTableProperty = cTablePropertyList.get(i);
				String columnName = cTableProperty.getColumnName();
				String fieldName = cTableProperty.getFieldName();
				String columnTypeName = cTableProperty.getColumnTypeName();
				if (columnName.equalsIgnoreCase(primaryKeyColumnName)) {
					continue;
				}
				String jdbcType = null;
				if(columnTypeName.equalsIgnoreCase("VARCHAR2")) {
					jdbcType = "VARCHAR";
				}else if(columnTypeName.equalsIgnoreCase("NUMBER")) {
					jdbcType = "NUMERIC";
				}else if(columnTypeName.equalsIgnoreCase("DATE")) {
					jdbcType = "TIMESTAMP";
				}
				osw.write(INDENT_4 + "<if test=\" " + fieldName + " != null \">" + columnName + " = #{" + fieldName + ", jdbcType="+jdbcType+"}, </if>" + NEWLINE);
			}
			osw.write(INDENT_4 + primaryKeyColumnName + " = #{" + primaryKeyFieldName + "}" + NEWLINE);
			
			osw.write(INDENT_3 + "<where>" + NEWLINE);
			osw.write(INDENT_4 + primaryKeyColumnName + " = #{" + primaryKeyFieldName + "}" + NEWLINE);
			osw.write(INDENT_3 + "</where>" + NEWLINE);
			osw.write(INDENT_2 + "</set>" + NEWLINE);
			osw.write(INDENT_1 + "</update>" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			osw.write(INDENT_1 + "<delete id=\"deleteById\" parameterType=\"" + primaryKeyFieldTypeName + "\">" + NEWLINE);
			osw.write(INDENT_2 + "delete from " + tableName + NEWLINE);
			osw.write(INDENT_2 + "<where>" + NEWLINE);
			osw.write(INDENT_3 + primaryKeyColumnName + " = #{" + primaryKeyFieldName + "}" + NEWLINE);
			osw.write(INDENT_2 + "</where>" + NEWLINE);
			osw.write(INDENT_1 + "</delete>" + NEWLINE);
			
			// ------------------------------------------
			osw.write(INDENT_1 + "" + NEWLINE);
			// ------------------------------------------
			
			osw.write("" + "</mapper>" + NEWLINE);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(osw != null) {
					osw.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("创建成功，XML在目录" + mapperFileDir);
	}
	
	/**
	 * 获取数据表结构, 返回属性名为键, 属性类型为值的Map, eg:<"userName", "java.lang.String">
	 * @Description: TODO
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException   
	 * List<CTableProperty>  
	 * @throws
	 * @author Hobai Riku
	 * @date 2017年6月13日 上午11:44:38
	 */
	public static List<CTableProperty> getTableStructByCon(Connection conn,String tableName) 
			throws SQLException, ClassNotFoundException {

		List<CTableProperty> cTablePropertyList = new ArrayList<CTableProperty>();
		
		//Connection connection = conn;
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;

//		String tableName = MappingConvertor.toTableName(entityClass.getSimpleName());
//		String sqlString = "select * from " + tableName;
		String sqlString = "select * from " + tableName;
		
		// 查询表
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		String url = "jdbc:oracle:thin:@10.10.11.170:1521:orcl";
//		connection = DriverManager.getConnection(url, "ivr", "ivr123");
//		String url = "jdbc:oracle:thin:@192.168.241.76:1521:sm";
//		String url = "jdbc:oracle:thin:@172.16.27.204:1521:orcl";
//		connection = DriverManager.getConnection(url, "hbsxt", "hbsxt123");
//		String url = "jdbc:oracle:thin:@172.16.27.204:1521:orcl";
//		connection = DriverManager.getConnection(url, "dzgk", "dzgk123");
//		connection = DriverManager.getConnection(url, "tosp", "tosp123");
//		connection = DriverManager.getConnection(url, "ivr2013", "ivr123");

		pstmt = (PreparedStatement) conn.prepareStatement(sqlString);
		pstmt.execute(); 
		rsmd = (ResultSetMetaData) pstmt.getMetaData();
		
		for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

			CTableProperty cTableProperty = new CTableProperty();
			// 数字长度大小
			int precision = rsmd.getPrecision(i);
			
			//System.out.println("precision=" + precision);
			
			cTableProperty.setPrecision(precision);
			int scale = rsmd.getScale(i);
			
			cTableProperty.setScale(scale);
			//System.out.println("scale=" + scale);
			// 属性名
			String fieldName = rsmd.getColumnName(i);
			//System.out.println("fieldName=" + fieldName);
			// 属性类型名
			String fieldTypeName = rsmd.getColumnTypeName(i);
			cTableProperty.setColumnName(fieldName);
			cTableProperty.setColumnTypeName(fieldTypeName);
			// 转换（把下划线去掉，后面的字母大写）
			fieldName = StringUtil.toVariableName(fieldName);
			if(fieldTypeName.equalsIgnoreCase("VARCHAR2")) {
				fieldTypeName = "java.lang.String";
			}else if(fieldTypeName.equalsIgnoreCase("NUMBER")) {
				//判断有没有小数点
				if (rsmd.getScale(i) > 0) {
					if (precision > 7) {
						fieldTypeName = "java.lang.Double";
					} else {
						fieldTypeName = "java.lang.Float";
					}
				} else {
					if (precision > 5) {// 长整形
						fieldTypeName = "java.lang.Long";
					} else {
						fieldTypeName = "java.lang.Integer";
					}
				}
			}else if(fieldTypeName.equalsIgnoreCase("DATE")) {
				fieldTypeName = "java.util.Date";
			}
			cTableProperty.setFieldName(fieldName);
			cTableProperty.setFieldTypeName(fieldTypeName);
			cTablePropertyList.add(cTableProperty);
//			System.out.println(fieldTypeName + "  " + fieldName+"	" + precision);			
		}
		return cTablePropertyList;
	}
	
	/**
	 * 
	 * @Description: 获取类结构, 返回属性名为键, 属性类型为值的Map, eg:<"userName", "java.lang.String">
	 * @param entityClass
	 * @return
	 * @throws ClassNotFoundException   
	 * List<CTableProperty>  
	 * @throws
	 * @author Hobai Riku
	 * @date 2016年7月13日 上午11:45:17
	 */
	public static List<CTableProperty> getClassStruct(Class<?> entityClass) throws ClassNotFoundException {

		List<CTableProperty> cTablePropertyList = new ArrayList<CTableProperty>();
		
		Field[] fieldArray = entityClass.getDeclaredFields();
		
		for (int i = 0; i < fieldArray.length; i++) {
			CTableProperty cTableProperty = new CTableProperty();
			
			String fieldName = fieldArray[i].getName();
			String fieldTypeName = fieldArray[i].getType().getName();
			
			cTableProperty.setFieldName(fieldName);
			cTableProperty.setFieldTypeName(fieldTypeName);

			// 转换
			fieldName = StringUtil.toColumnName(fieldName);
			if(fieldTypeName.equalsIgnoreCase("java.lang.String")) {
				fieldTypeName = "VARCHAR2";
			}else if(fieldTypeName.equalsIgnoreCase("java.lang.Long")||
					fieldTypeName.equalsIgnoreCase("java.lang.Integer")||
					fieldTypeName.equalsIgnoreCase("java.lang.Float")) {
				fieldTypeName = "NUMBER";
			}else if(fieldTypeName.equalsIgnoreCase("java.util.Date")) {
				fieldTypeName = "DATE";
			}
			cTableProperty.setColumnName(fieldName);
			cTableProperty.setColumnTypeName(fieldTypeName);

			cTablePropertyList.add(cTableProperty);
			
			System.out.println("#{" + cTableProperty.getFieldName() + "},");
//			System.out.println("" + cTableProperty.getColumnName() + ",");
		}
		return cTablePropertyList;
	}

	
}
