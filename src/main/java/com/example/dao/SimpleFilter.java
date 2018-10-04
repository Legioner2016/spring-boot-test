package com.example.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.metadata.ClassMetadata;

import com.example.database.BaseEntityClass;

/**
 * Элемент списка фильтров при выьоре из БД 
 * Взято из 3-го релиза с некоторыми сокращениями
 */
public class SimpleFilter<U extends BaseEntityClass>  {

	//1999-01-08 04:05:06
	public static String ISO8601_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private String propertyName;
	private SQLOperator	SQLOperator;
	private Object		value;

	public SimpleFilter(SQLOperator sqlOperator, Object value, String propertyName) {

		this.SQLOperator = sqlOperator;
		this.propertyName = propertyName;
		this.value = value;
	}

	public Object getValue() {

		return value;
	}

	public void setValue(Object value) {

		this.value = value;
	}
	

	public Predicate createCriterion(CriteriaBuilder cb, Root<U> root, Map<String, Join<?, ?>> paths) {
		
		Predicate result = null;
		Join j = null;
		String property = propertyName;
		Object convertedValue = null;
		if (propertyName.contains(".")) 
		{
			String[] props = propertyName.split("\\.");
			String temp = "";
			for (int i = 0; i < props.length - 1; i++) {
				temp += props[i] + ".";
				if (paths.get(temp) != null) j = paths.get(temp);
				else {
					if (j == null) j = root.join(props[i], JoinType.LEFT);
					else j = j.join(props[i], JoinType.LEFT); 
					paths.put(temp, j);
				}
			}
			property = props[props.length - 1];
			try {
				convertedValue = (SQLOperator == SQLOperator.ISNOTNULL || SQLOperator == SQLOperator.ISNULL)
						? null
						: convertValue(this.value, Class.forName(j.get(property).getJavaType().getName()));
			} catch (ClassNotFoundException e) {
				
			}
		} else {
			try {
				convertedValue = (SQLOperator == SQLOperator.ISNOTNULL || SQLOperator == SQLOperator.ISNULL)
						? null
						: convertValue(this.value, Class.forName(root.get(property).getJavaType().getName()));
			} catch (ClassNotFoundException e) {
				
			}
		}
			                                           
			
			switch (SQLOperator) {
			case EQUAL:
				result = cb.equal(j == null ? root.get(property) : j.get(property), convertedValue);
				break;
			case EQUALWITHNULL:
				result = cb.or(
							cb.equal(j == null ? root.get(property) : j.get(property), convertedValue),
							cb.isNull(j == null ? root.get(property) : j.get(property))
						);
				break;
			case EQUAL_CASE_INSENSITIVE:
				String string = (String) convertedValue;
				result = cb.equal(cb.lower(j == null ? root.get(property) : j.get(property)), string == null ? null : string.toLowerCase());
				break;
			case NOT_EQUAL:
				result = cb.notEqual(j == null ? root.get(property) : j.get(property), convertedValue);
				break;
			case NOT_EQUAL_CASE_INSENSITIVE:
				string = (String) convertedValue;
				result = cb.notEqual(cb.lower(j == null ? root.get(property) : j.get(property)), string == null ? null : string.toLowerCase());
				break;
			case LESS: case GREATER: case LESS_OR_EQUAL: case GREATER_OR_EQUAL:
				//Need specific - cannot create predicate without use of concrete class
				Class<?> clazz;
				if (convertedValue instanceof String) {
					string = (String) convertedValue;
					if (SQLOperator == SQLOperator.LESS) {
						result = cb.lessThan(j == null ? root.<String>get(property) : j.<String>get(property) , string);
					} else if (SQLOperator == SQLOperator.LESS_OR_EQUAL) {
						result = cb.lessThanOrEqualTo(j == null ? root.<String>get(property) : j.<String>get(property), string);
					} else if (SQLOperator == SQLOperator.GREATER) {
						result = cb.greaterThan(j == null ? root.<String>get(property) : j.<String>get(property), string);
					} else if (SQLOperator == SQLOperator.GREATER_OR_EQUAL) {
						result = cb.greaterThanOrEqualTo(j == null ? root.<String>get(property) : j.<String>get(property), string);
					}
				} else if (convertedValue instanceof Integer) {
					Integer num = (Integer) convertedValue;
					if (SQLOperator == SQLOperator.LESS) {
						result = cb.lessThan(j == null ? root.<Integer>get(property) : j.<Integer>get(property), num);
					} else if (SQLOperator == SQLOperator.LESS_OR_EQUAL) {
						result = cb.lessThanOrEqualTo(j == null ? root.<Integer>get(property) : j.<Integer>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER) {
						result = cb.greaterThan(j == null ? root.<Integer>get(property) : j.<Integer>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER_OR_EQUAL) {
						result = cb.greaterThanOrEqualTo(j == null ? root.<Integer>get(property) : j.<Integer>get(property), num);
					}
				} else if (convertedValue instanceof Long) {
					Long num = (Long) convertedValue;
					if (SQLOperator == SQLOperator.LESS) {
						result = cb.lessThan(j == null ? root.<Long>get(property) : j.<Long>get(property), num);
					} else if (SQLOperator == SQLOperator.LESS_OR_EQUAL) {
						result = cb.lessThanOrEqualTo(j == null ? root.<Long>get(property) : j.<Long>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER) {
						result = cb.greaterThan(j == null ? root.<Long>get(property) : j.<Long>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER_OR_EQUAL) {
						result = cb.greaterThanOrEqualTo(j == null ? root.<Long>get(property) : j.<Long>get(property), num);
					}
				} else if (convertedValue instanceof BigDecimal) {
					BigDecimal num = (BigDecimal) convertedValue;
					if (SQLOperator == SQLOperator.LESS) {
						result = cb.lessThan(j == null ? root.<BigDecimal>get(property) : j.<BigDecimal>get(property), num);
					} else if (SQLOperator == SQLOperator.LESS_OR_EQUAL) {
						result = cb.lessThanOrEqualTo(j == null ? root.<BigDecimal>get(property) : j.<BigDecimal>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER) {
						result = cb.greaterThan(j == null ? root.<BigDecimal>get(property) : j.<BigDecimal>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER_OR_EQUAL) {
						result = cb.greaterThanOrEqualTo(j == null ? root.<BigDecimal>get(property) : j.<BigDecimal>get(property), num);
					}
				} else if (convertedValue instanceof Date) {
					Date num = (Date) convertedValue;
					if (SQLOperator == SQLOperator.LESS) {
						result = cb.lessThan(j == null ? root.<Date>get(property) : j.<Date>get(property), num);
					} else if (SQLOperator == SQLOperator.LESS_OR_EQUAL) {
						result = cb.lessThanOrEqualTo(j == null ? root.<Date>get(property) : j.<Date>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER) {
						result = cb.greaterThan(j == null ? root.<Date>get(property) : j.<Date>get(property), num);
					} else if (SQLOperator == SQLOperator.GREATER_OR_EQUAL) {
						result = cb.greaterThanOrEqualTo(j == null ? root.<Date>get(property) : j.<Date>get(property), num);
					}
				} 
				break;
			case LIKE:
				string = (String) convertedValue;
				result = cb.like(j == null ? root.get(property) : j.get(property), string == null ? null : percentedString(string));
				break;
			case ILIKE:
				string = (String) convertedValue;
				result = cb.like(cb.lower(j == null ? root.get(property) : j.get(property)), string == null ? null : percentedString(string));
				break;
			case ISNULL:
				result = cb.isNull(j == null ? root.get(property) : j.get(property));
				break;
			case ISNOTNULL:
				result = cb.isNotNull(j == null ? root.get(property) : j.get(property));
				break;
			case ANY:
				result = (j == null ? root.get(property) : j.get(property)).in((List)value);
				break;
			}
			
		
		
		return result;
	}

	/**
	 * Преобразование из любого Number -> в любой Number и из String в любой Enum
	 * 
	 * @param value
	 * @param destClass
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private Object convertValue(Object value, Class<?> destClass) {

		Object result = value;
		if (value instanceof Number) {
			if (destClass.equals(String.class)) {
				result = value.toString();
			}
			else if (destClass.equals(Long.class)) {
				result = ((Number) value).longValue();
			}
			else if (destClass.equals(Integer.class)) {
				result = ((Number) value).intValue();
			}
			else if (destClass.equals(Float.class)) {
				result = ((Number) value).floatValue();
			}
			else if (destClass.equals(Double.class)) {
				result = ((Number) value).doubleValue();
			}
			else if (destClass.equals(BigDecimal.class)) {
				if (value instanceof Long) {
					result = new BigDecimal((Long) value);
				}
				else if (value instanceof Integer) {
					result = new BigDecimal((Integer) value);
				}
				else if (value instanceof Double) {
					result = new BigDecimal((Double) value);
				}
				else if (value instanceof Float) {
					result = new BigDecimal(((Number) value).floatValue());
				}
			}
		}else if (value instanceof String) {
			if (destClass.isEnum()) {// if (Enum.class.isAssignableFrom(destClass)){
				result = Enum.valueOf((Class<? extends Enum>) destClass, (String) value);
			}
		}
		return result;
	}

	/**
	 * Определить класс свойства в замапленном хибернейтовском типе.<br/>
	 * Если определить не получается (ассоциация/свойство не найдено) - возвращается defaultClass
	 * 
	 * @param metadata
	 * @param property
	 * @param defaultClass
	 * @return
	 */
	private Class<?> getParameterClass(ClassMetadata metadata, String property, Class<?> defaultClass) {

		Class<?> result = null;
		try {
			result = metadata.getPropertyType(property)
								.getReturnedClass();
		}
		catch (Exception e) {
			result = defaultClass;
		}
		return result;

	}

	private String percentedString(String text) {

		return new StringBuilder("%").append(text)
				.append("%")
				.toString();
	}
	
}
