package com.similan.domain.entity.util;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.common.DomainBaseEntity;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.service.api.feedback.dto.MemberFeedbackType;

@Entity(name = "MemberFeedback")
@Getter
@Setter
@ToString
public class MemberFeedback extends DomainBaseEntity {

  @ManyToOne
  private SocialPerson reporter;

  @Column(length = 5000)
  private String memberFeedback;

  @Column
  private Date feedbackDate;

  @Column
  @Enumerated(EnumType.STRING)
  private MemberFeedbackStatus status;

  @Column
  @Enumerated(EnumType.STRING)
  private MemberFeedbackType feedbackType;

}
