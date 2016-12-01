package com.similan.domain.entity.community;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "MemberSuggestion")
@DiscriminatorValue("MemberSuggestion")
public class MemberSuggestion extends Suggestion {
	
	@ManyToOne
	private SocialPerson forMember;

	public SocialPerson getForMember() {
		return forMember;
	}

	public void setForMember(SocialPerson forMember) {
		this.forMember = forMember;
	}

}
