package com.hobai.required;

import java.util.List;
import java.util.Map;
/**
 * DAO基类
 * @author ZHQL
 * @param <E>
 */
public interface IBaseDao<E> {
	public E getObjById(Long id);
	public List<E> list(Map<String,Object> param);
	public int add(E entity);
	public int update(E entity);
	public int delete(Long id);
}
