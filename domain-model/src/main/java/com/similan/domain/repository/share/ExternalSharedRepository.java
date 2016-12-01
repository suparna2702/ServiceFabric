package com.similan.domain.repository.share;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.share.jpa.JpaExternalSharedRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.ISharable;

@Repository
public class ExternalSharedRepository {

  @Autowired
  private JpaExternalSharedRepository repository;

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public ExternalShared save(ExternalShared externalShare) {
    return this.repository.save(externalShare);
  }

  public ExternalShared findBySharedName(String name) {
    return this.repository.findBySharedName(name);
  }

  public ExternalShared findOne(Long id) {
    return this.repository.findOne(id);
  }
  
  public ExternalShared findBySharedEntity(
      GenericReference<ISharable> sharedEntity) {
    return this.repository.findBySharedEntity(sharedEntity);
  }

  public ExternalShared create(SocialPerson sharer,
      ExternalSocialPerson sharedTo, ISharable sharable) {
    
    ExternalShared externalShare = new ExternalShared();
    externalShare.setSharedBy(sharer);
    externalShare.setSharedTo(sharedTo);
    externalShare.setSharedName(UUID.randomUUID().toString());

    GenericReference<ISharable> sharableRef = genericReferenceRepository
        .create(sharable);
    externalShare.setSharedEntity(sharableRef);

    return externalShare;
  }

}
