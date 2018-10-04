package com.example.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.database.BaseEntityClass;

/**
 * Прототип фильтров для выбора из БД
 * Взято из 3-го релиза с некоторыми сокращениями
 */
public  abstract class FilterList<T extends BaseEntityClass> {

	protected List<SimpleFilter<T>>	filters;
	
	public static enum LogicCombinationType {
		AND, OR;
	}

	protected abstract String getSqlLogicOperator();

	public abstract LogicCombinationType getCombinationType();

	/**
	 * Суммарный критерий
	 * 
	 * @param metadata
	 * 
	 * @return критерий
	 */
	public Predicate getCriterion(CriteriaBuilder cb, Root<T> root, Map<String, Join<?, ?>> paths)  {
		
		List<Predicate> predicates = new ArrayList<>();
		for (SimpleFilter<T> filter : filters) {
			Predicate p = filter.createCriterion(cb, root, paths);
			if (p != null) predicates.add(p);
		}

		if (!predicates.isEmpty()) 
			return getCombinationType() == LogicCombinationType.AND ? cb.and(predicates.toArray(new Predicate[predicates.size()])) 
					:   cb.or(predicates.toArray(new Predicate[predicates.size()]));
		
		return null;
	}
	
	/**
	 * Добавить простой фильтр
	 * 
	 * @param property свойство
	 * @param operator тип соответствия
	 * @param value значение
	 * @return этот объект (для цепочки)
	 */
	public FilterList<T> addFilter(SQLOperator operator, Object value, String propertyName) {

		return addFilter(new SimpleFilter<T>(operator, value, propertyName));
	}

	/**
	 * Добавить любой фильтр
	 * 
	 * @param filter
	 * @return
	 */
	public FilterList<T> addFilter(SimpleFilter<T> filter) {

		filters.add(filter);
		return this;
	}

	public List<SimpleFilter<T>> getFilters() {

		return filters;
	}

	public boolean isEmpty() {

		return filters.isEmpty();
	}

	public FilterList() {

		filters = new ArrayList<SimpleFilter<T>>();
	}

	
	
}
