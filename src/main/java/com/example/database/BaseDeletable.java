package com.example.database;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Базовый "удаляемый" (с пометкой удаления) предок для сущностей
 * 
 * @author legioner
 *
 */
@MappedSuperclass
public abstract class BaseDeletable extends BaseEntityClass {

	@Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean deleted;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean getDeleted() {
		return deleted;
	}
	
}
