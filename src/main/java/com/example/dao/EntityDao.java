package com.example.dao;

import java.util.Collection;
import java.util.List;

import com.example.database.BaseEntityClass;

/**
 * Интерфес базовый репозитория
 * Взято из 3-го релиза с большими сокращениями
 */
public interface EntityDao {

	<T extends BaseEntityClass> T findById(Class<T> _class, Long id );
	<T extends BaseEntityClass> void saveOrUpdate( T object );
	<T extends BaseEntityClass> void save(T object);
	
	<T extends BaseEntityClass> void update(T object);
	
	<T extends BaseEntityClass> void delete(Class<T> clazz, T object);
	
	<T extends BaseEntityClass> List<T> list(Class<T> clazz, FilterList<T> filters, OrderList<T> orders, Integer from, Integer count);
	
	<T extends BaseEntityClass> Long count(Class<T> clazz, FilterList<T> filters);
	
}
