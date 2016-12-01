package com.similan.service.api.chat.dto.key;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class ChatSessionKey extends Key {

  @XmlElement
  private SocialActorKey initiator;

  @XmlElement
  private SocialActorKey firstInvitee;

  @XmlAttribute
  private Date date;

  public ChatSessionKey() {
  }

  public ChatSessionKey(String initiatorName, String firstInviteeName, Date date) {
    this(new SocialActorKey(initiatorName),
        new SocialActorKey(firstInviteeName), date);
  }

  public ChatSessionKey(SocialActorKey initiator, SocialActorKey firstInvitee,
      Date date) {
    super();
    this.initiator = initiator;
    this.firstInvitee = firstInvitee;
    this.date = date;
  }

  public EntityType getEntityType() {
    return EntityType.CHAT_SESSION;
  }

  public SocialActorKey getInitiator() {
    return initiator;
  }

  public SocialActorKey getFirstInvitee() {
    return firstInvitee;
  }

  public Date getDate() {
    return date;
  }

}
