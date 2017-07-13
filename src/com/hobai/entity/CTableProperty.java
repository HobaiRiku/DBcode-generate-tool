package com.hobai.entity;

/**
 * 
 * @Title: CTableProperty.java
 * @Package com.entity
 * @Description: 表结构的列信息实体
 * @author Hobai Riku
 * @date 2016年7月13日 上午11:46:01
 * @version 1.0
 */
public class CTableProperty {
	
	/**
	 * 数据库中的列名
	 */
	private String columnName;

	/**
	 * 数据库中的类型名
	 */
	private String columnTypeName;

	/**
	 * 数据库中的数字长度
	 */
	private int precision;
	
	/**
	 * 数据库中的小数点
	 */
	private int scale;


	/**
	 * 类中的属性名
	 */
	private String fieldName;

/**
 * 类中的类型名
 */
	private String fieldTypeName;
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}

