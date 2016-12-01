package com.similan.framework.dto.network;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkedInConnection implements Serializable {

	private static final long serialVersionUID = 1631850301268798495L;

	private String linkedInId;

	private String firstName;

	private String lastName;

	private String password;
	
	private String photoLocation;

	private String description;
	
	private String company;
	

}
