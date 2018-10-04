package com.example.dao;

/**
 * Перечисление - вид сравнения для построения sql predicate-ов  
 * Взято из 3-го релиза
 */
public enum SQLOperator {
		EQUAL,
		NOT_EQUAL,
		EQUALWITHNULL,
		EQUAL_CASE_INSENSITIVE,
		NOT_EQUAL_CASE_INSENSITIVE,
		LESS,
		GREATER,
		LESS_OR_EQUAL,
		GREATER_OR_EQUAL,
		LIKE,
		ILIKE,
		ISNOTNULL,
		ISNULL,
		ANY
}
