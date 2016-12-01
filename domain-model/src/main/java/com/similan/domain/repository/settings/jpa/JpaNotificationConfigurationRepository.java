package com.similan.domain.repository.settings.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.settings.NotificationConfiguration;

public interface JpaNotificationConfigurationRepository extends
    JpaRepository<NotificationConfiguration, Long> {

  public NotificationConfiguration findByMember(SocialActor member);

}
