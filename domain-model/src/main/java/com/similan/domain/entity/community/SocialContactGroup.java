package com.similan.domain.entity.community;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity(name="SocialContactGroup")
public class SocialContactGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String type;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinColumn
	private Set<SocialContact> contacts;
	
	@JoinColumn
	@ManyToOne
	private SocialActor owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public SocialActor getOwner() {
		return owner;
	}

	public void setOwner(SocialActor owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<SocialContact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<SocialContact> contacts) {
		this.contacts = contacts;
	}
	
	
}
