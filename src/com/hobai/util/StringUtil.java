package com.hobai.util;
/**
 * 
 * @Title: StringUtil.java
 * @Package com.util
 * @Description: 字符串工具类
 * @author Hobai Riku
 * @date 2016年7月13日 上午11:50:15
 * @version 1.0
 */
public class StringUtil {

	// 表名称分隔符
	public static String tableSeparator = "_";
	// 字段名称分隔符
	public static String columnSeparator = "_";

	/**
	 * 
	 * @Description: 实例对象变量名称转换成字段名称
	 * @param variableName
	 * @return   
	 * String  
	 * @throws
	 * @author Hobai Riku
	 * @date 2016年7月13日 上午11:50:34
	 */
	public static String toColumnName(String variableName) {
		if (isEmpty(variableName)) {
			return variableName;
		}
		String res = "";

		for (int i = 0, len = variableName.length(); i < len; i++) {
			char ch = variableName.charAt(i);
			res += Character.isUpperCase(ch) ? tableSeparator + ch : ch;
		}

		return res.toUpperCase();
	}

	/**
	 * 字段名称转换成实例对象变量名称
	 * @param columnName
	 * @return
	 */
	public static String toVariableName(String columnName) {
		
		if (isEmpty(columnName)) {
			return columnName;
		}
		String[] strArray = columnName.split(columnSeparator);
		String res = "";

		for (int i = 0; i < strArray.length; i++) {
			if (i == 0) {
				res += strArray[i].toLowerCase();
			} else {
				res += strArray[i].substring(0, 1).toUpperCase() + strArray[i].substring(1).toLowerCase();
			}
		}
		return res;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() <= 0){
			return true;
		}
		return false;
	}

	/**
	 * 根据表字段名得到对应类变量名
	 * @param columnName
	 * @return
	 */
	public static String getFeildName(String columnName) {
		
		columnName = columnName.toLowerCase();
		String temp = "";
		while (true) {
			int index = columnName.indexOf("_");
			if (index == -1) {
				break;
			}
			temp = columnName.substring(index + 1, index + 2);
			columnName = columnName.replaceAll("_" + temp, temp.toUpperCase());
		}
		return columnName;
	}

	/**
	 * 根据表名得到对应类名
	 * @param columnName
	 * @return
	 */
	public static void main(String[] args) {
		String a=getClassName("T_4S_FITTINGS_INORDER");
		System.out.println(a);
	}
	public static String getClassName(String tableName) {
		
		tableName = tableName.toLowerCase();
		String temp = "";
		int i=2;
		while (i>0) {
			int index = tableName.indexOf("_");
			i=i-1;
			if (index == -1) {
				break;
			}
			//去除所有"_"，去除表名前两位单词
			temp = tableName.substring(index + 1, tableName.length());
			tableName=temp;

			// System.out.println("==:" + columnName);
		}
		while (true) {
			int index = tableName.indexOf("_");
			if (index == -1) {
				break;
			}
			//取出所有"_"，保留其他字符,并以命名规范保存
			temp = tableName.substring(index + 1, index+2);
			tableName = tableName.replaceAll("_" + temp, temp.toUpperCase());
			
		}
		
		// 首字母大字
		return tableName.replaceFirst(tableName.substring(0, 1), tableName.substring(0, 1).toUpperCase());
	}

/*	public static void main(String[] args) {
		String t = StringUtil.getFeildName("RULE_ID_II_AA");
		System.out.println(t);
	}*/
}
