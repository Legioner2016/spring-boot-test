package com.example.database;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Спрачоник - правообладатели.
 * В данном проекте не используется
 * 
 * @author legioner
 *
 */
@Entity
@AttributeOverride(name = "title", column = @Column(length = 255))
public class RightOwner extends Dictonary {

}
