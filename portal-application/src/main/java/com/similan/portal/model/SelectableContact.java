package com.similan.portal.model;

import java.io.Serializable;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.service.api.community.dto.basic.MemberDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.basic.SocialActorType;
import com.similan.service.api.connection.dto.basic.ContactType;

public class SelectableContact implements Serializable {

  private static final long serialVersionUID = 6228084429066853530L;
  private SocialActorContactDto contact;
  private boolean selected = false;
  private ContactSource contactSource = ContactSource.Business;

  public SelectableContact(SocialActorContactDto contact) {
    this.contact = contact;
  }

  public String getImagePath() {
    return PhotoViewOption.ShowPhoto.effectivePhoto(
        "/images/memberDefaultPhoto.png", contact.getContact()
            .getDisplayImage());
  }

  public Long getId() {
    return contact.getContact().getId();
  }

  public SocialActorContactDto getContact() {
    return contact;
  }

  public void setContact(SocialActorContactDto contact) {
    this.contact = contact;
  }

  public String getFullName() {
    return contact.getContact().getDisplayName();
  }

  public String getCompany() {
    return contact.getContact().equals(SocialActorType.Member) ?
        ((MemberDto)contact.getContact()).getEmployer() : "";
  }

  public ContactType getContactType() {
    return contact.getType();
  }

  public String getContactCategory() {
    return contact.getType().toString();
  }

  public boolean getIsBusiness() {
    if (contact.getContact().getType().equals(SocialActorType.Business)) {
      return true;
    }
    return false;
  }

  public boolean getIsMember() {
    if (contact.getContact().getType().equals(SocialActorType.Member)) {
      return true;
    }
    return false;
  }

  public String getEmail() {
    return contact.getContact().getContactEmail();
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public ContactSource getContactSource() {
    return contactSource;
  }

  public void setContactSource(ContactSource contactSource) {
    this.contactSource = contactSource;
  }

}
