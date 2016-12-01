package com.similan.domain.repository.share;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.share.jpa.JpaInNetworkSharedRepository;
import com.similan.domain.share.ISharable;
import com.similan.domain.share.InNetworkShared;

@Repository
public class InNetworkSharedRepository {

  @Autowired
  private JpaInNetworkSharedRepository repository;

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public InNetworkShared findBySharedName(String name) {
    return this.repository.findBySharedName(name);
  }

  public InNetworkShared findOne(Long id) {
    return this.repository.findOne(id);
  }

  public InNetworkShared findBySharedEntity(
      GenericReference<ISharable> sharedEntity) {
    return this.repository.findBySharedEntity(sharedEntity);
  }

  public InNetworkShared save(InNetworkShared shared) {
    return this.repository.save(shared);
  }

  public InNetworkShared create(SocialPerson sharer, SocialPerson sharedTo,
      ISharable sharable) {
    InNetworkShared inNetworkShare = new InNetworkShared();
    inNetworkShare.setSharedBy(sharer);
    inNetworkShare.setSharedToActor(sharedTo);
    inNetworkShare.setSharedName(UUID.randomUUID().toString());

    GenericReference<ISharable> sharableRef = genericReferenceRepository
        .create(sharable);
    inNetworkShare.setSharedEntity(sharableRef);

    return inNetworkShare;
  }

}
