package com.similan.domain.entity.metadata;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="CountryType")
@XmlRootElement(name = "country")
public class CountryType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String countryName;
	
	@OrderBy("stateName ASC")
	@OneToMany(cascade = CascadeType.ALL)
	private List<StateType> states;
	
	public CountryType(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlAttribute(name = "countryName")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@XmlElements(@XmlElement(name="state",type=StateType.class))
	public List<StateType> getStates() {
		return states;
	}

	public void setStates(List<StateType> states) {
		this.states = states;
	}
}
