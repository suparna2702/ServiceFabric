package com.similan.framework.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.similan.domain.entity.util.AddressDto;

@XmlRootElement(name = "searchResultItem")
public class SearchResultItemDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer rank;
	
	private String contactEmail;
	
	private String contactPhone;
	
	private String firstName;
	
	private String lastName;
	
	private String name;
	
	private Long forSocialActorId;
	
	private AddressDto location = new AddressDto();
	
	@XmlAttribute(name="id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlAttribute(name="rank")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@XmlAttribute(name="contactEmail")
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@XmlAttribute(name="contactPhone")
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@XmlAttribute(name="firstName")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlAttribute(name="lastName")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlAttribute(name="forSocialActorId")
	public Long getForSocialActorId() {
		return forSocialActorId;
	}

	public void setForSocialActorId(Long forSocialActorId) {
		this.forSocialActorId = forSocialActorId;
	}

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public AddressDto getLocation() {
		return location;
	}

	public void setLocation(AddressDto location) {
		this.location = location;
	}

}
