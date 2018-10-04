package com.example.database;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Метамодель базового предка всех сущностей.
 * Логично - для типобезопасных запросов 
 * 
 * @author legioner
 *
 */
@StaticMetamodel(BaseEntityClass.class)
public abstract class BaseEntityClass_ {

	public static volatile SingularAttribute<BaseEntityClass, Long> id;
}


