package com.hobai;

import com.hobai.util.FileUtil;

public class GenerateBaseDao {
	public static final String LINE = "\r\n";//换行符
    public static final String TAB = "\t";//制表符
    
	public static void  generateInterface(String packetPath,String daopath){
		StringBuffer sb = new StringBuffer();
		sb.append("package"+ packetPath+".dao;");
        sb.append(LINE); 
        sb.append("import java.util.List;"
        		+ "import java.util.Map;"
        		+LINE
        		+ "public interface IBaseDao<E> {"
        		+LINE
        		+ "public E getObjById(Long id);"
        		+LINE
        		+ "public List<E> list(Map<String,Object> param);"
        		+LINE
        		+ "public int add(E entity);"
        		+LINE
        		+ "public int add(E entity);"
        		+LINE
        		+ "public int update(E entity);"
        		+LINE
        		+ "public int delete(Long id);"
        		+LINE
        		+ "}");
        FileUtil.toFile(daopath, "IBaseDao.java", sb.toString());

	}
	
	
}
