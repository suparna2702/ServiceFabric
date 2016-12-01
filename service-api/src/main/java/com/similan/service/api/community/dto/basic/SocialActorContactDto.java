package com.similan.service.api.community.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.connection.dto.basic.ContactType;

public class SocialActorContactDto extends
                            KeyHolderDto<SocialActorKey> {
	
	@XmlElement
	ActorDto contact;
	
	@XmlAttribute
	private ContactType type;

	@XmlAttribute
	private Date date;
	
	protected SocialActorContactDto(){
		
	}
	
	public SocialActorContactDto(SocialActorKey ownerKey, 
			ActorDto contact, ContactType type, Date date){
		super(ownerKey);
		this.contact = contact;
		this.date = date;
		this.type = type;
	}
	
	public ActorDto getContact() {
		return contact;
	}

	public ContactType getType() {
		return type;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "SocialActorDisplayableContactDto [contact=" + contact
				+ ", type=" + type + ", date=" + date + "]";
	}

	

}
