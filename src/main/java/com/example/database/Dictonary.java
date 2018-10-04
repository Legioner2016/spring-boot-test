package com.example.database;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * Предок всех сущностей - справочников
 * 
 * @author legioner
 *
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Dictonary extends BaseEntityClass {

	@Column(name = "title", columnDefinition = "VARCHAR")
	private String title;

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public Dictonary() {
		super();
	}
	
	public Dictonary(String title) {
		super();
		this.title = title;
	}
	
	public Dictonary(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public Dictonary(Long id) {
		super();
		this.id = id;
	}

}
