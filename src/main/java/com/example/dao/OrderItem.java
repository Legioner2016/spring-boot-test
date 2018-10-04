package com.example.dao;

import com.example.database.BaseEntityClass;

/**
 * Параметр сортировки при выборе из БД
 * Взято из 3-го релиза с некоторыми сокращениями
 */
public class OrderItem<T extends BaseEntityClass> {
		
		String orderProperty;
		Boolean direction;
		
		public OrderItem(String  property, Boolean direction) {
			this.orderProperty = property;
			this.direction = direction;
		}
		
		public Boolean getDirection() {
			return direction;
		}

		public String getOrderProperty() {
			return orderProperty;
		}
		
	
}
