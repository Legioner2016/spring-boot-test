package com.example.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.database.BaseDeletable;
import com.example.database.BaseEntityClass;
import com.example.database.BaseEntityClass_;

/**
 * Базовый репозиторий
 * Взято из 3-го релиза с большими сокращениями
 */
@Repository("entityDao")
public class EntityDaoImpl implements EntityDao {

	@PersistenceContext
	protected	EntityManager em;

	
	@Override
	public <T extends BaseEntityClass> T findById(Class<T> _class, Long id) {
		return (T) em.find(_class, id);
	}

	@Override
	@Transactional(readOnly = false)
	public <T extends BaseEntityClass> void saveOrUpdate(T object) {
		if (object.getId() != null && object.getId() > 0L) em.merge(object);
		else em.persist(object);
		em.flush();
	}

	@Override
	@Transactional(readOnly = false)
	public <T extends BaseEntityClass> void save(T object) {
		em.persist(object); 
		em.flush();
	}


	@Override
	@Transactional(readOnly = false)
	public <T extends BaseEntityClass> void update(T object) {
		em.merge(object);
		em.flush();
	}

	@Override
	@Transactional(readOnly = false)
	public <T extends BaseEntityClass> void delete(Class<T> clazz, T object) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<T> query = cb.createCriteriaDelete(clazz);
		Root<T> root = query.from(clazz);
		query.where(cb.equal(root.get(BaseEntityClass_.id), object.getId()));
		em.createQuery(query).executeUpdate();
		em.flush();
	}

	@Override
	public <T extends BaseEntityClass> List<T> list(Class<T> clazz, FilterList<T> filters, OrderList<T> orders,
			Integer from, Integer count) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		Map<String, Join<?, ?>> paths = new HashMap<>();
		addOrders(query, root, cb, orders, paths);
		Predicate pred = filters == null ? null : filters.getCriterion(cb, root, paths);
		Predicate predicate = addDeletedFilter(root, cb, clazz, pred);
		if (predicate != null)	query.where(predicate);
		TypedQuery<T> q = em.createQuery(query);
		setLimits(q, from, count);
		return q.getResultList();
	}

	@Override
	@Transactional( readOnly = true )
	public <T extends BaseEntityClass> Long count(Class<T> clazz, FilterList<T> filters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> root = query.from(clazz);
		query.select(cb.count(root.get(BaseEntityClass_.id)));
		Predicate pred = filters == null ? null : filters.getCriterion(cb, root, new HashMap<String, Join<?, ?>>());
        if( pred != null ){
            query.where(addDeletedFilter(root, cb, clazz, pred));
        }
		TypedQuery<Long> q = em.createQuery(query);
		return q.getSingleResult();
	}
	
	protected void setLimits(TypedQuery<?> query, Integer from, Integer count) {
		if (from != null) {
			query.setFirstResult(from);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
	}

	protected <T extends BaseEntityClass> Predicate addDeletedFilter(Root<T> root, CriteriaBuilder cb, Class<?> clazz, Predicate predicate) {
		if (BaseDeletable.class.isAssignableFrom(clazz)) {
			if (predicate == null) return cb.not(root.get("deleted"));
			return cb.and(cb.not(root.get("deleted")), predicate);
		} 
		return predicate;
	}

	
	protected <T extends BaseEntityClass> void addOrders(CriteriaQuery<T> query, Root<T> root, CriteriaBuilder cb,  OrderList<T> orders, Map<String, Join<?, ?>>  paths) {
        if( orders != null ){
            query.orderBy(orders.getOrders(query, root, cb, paths));
        } else query.orderBy(cb.asc(root.get(BaseEntityClass_.id)));
	}


	


}
