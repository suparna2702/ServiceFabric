package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "EmbeddedIdentity")
public class SocialEmbeddedIdentity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@Column
	private String uuid=null;
	
	@Column
	private String email;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SocialActor embeddedActor;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public SocialActor getEmbeddedActor() {
		return embeddedActor;
	}

	public void setEmbeddedActor(SocialActor embeddedActor) {
		this.embeddedActor = embeddedActor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
