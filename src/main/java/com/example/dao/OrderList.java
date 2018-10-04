package com.example.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.example.database.BaseEntityClass;
import com.example.database.BaseEntityClass_;

/**
 *  СПисок ппарметров сортировки при выборе из БД
 * Взято из 3-го релиза с некоторыми сокращениями
 */
public class OrderList<T extends BaseEntityClass> {

		/**
		 * список сортировок - чтобы не было расхождения типа в сортировках и фильтрах
		 */
		private List<OrderItem<T>> orders = new LinkedList<OrderItem<T>>();

		
		public List<Order> getOrders(CriteriaQuery<T> query, Root<T> root, CriteriaBuilder cb,  Map<String, Join<?, ?>> paths) {
			List<Order> result = new LinkedList<>();
			if (this.orders == null || this.orders.isEmpty()) {
				result.add(cb.asc(root.get(BaseEntityClass_.id)));
			}
			else {
				this.orders.forEach(item -> {
					Join j = null;
					String property = item.getOrderProperty();
					Object convertedValue;
					if (item.getOrderProperty().contains(".")) 
					{
						String[] props = item.getOrderProperty().split("\\.");
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
					}
					if (Boolean.TRUE.equals(item.getDirection())) result.add(cb.asc(j == null ? root.get(property) : j.get(property)));
					else result.add(cb.desc(j == null ? root.get(property) : j.get(property)));
				});
			}
			return result;
		}

		/**
		 * Добавить сортировку 
		 * 
		 * @param order
		 * @return
		 */
		public OrderList<T> addOrder(OrderItem<T> order) {
			orders.add(order);
			return this;
		}

		public OrderList<T> addOrder(String property, Boolean asceding) {

			return addOrder(new OrderItem<T>(property, asceding));
		}


		
	
}
