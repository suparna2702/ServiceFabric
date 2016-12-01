package com.similan.service.internal.impl.security.component;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.repository.document.DocumentInstanceRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.SecurityCheckResult;

@Component
@Slf4j
public class DocumentInstanceEnforcer extends AbstractComponentEnforcer {

  @Autowired
  private DocumentRepository documentRepository;
  @Autowired
  private DocumentInstanceRepository documentInstanceRepository;
  @Autowired
  private ManagementWorkspaceEnforcer managementWorkspaceEnforcer;

  public SecurityCheck uploadDocument(final DocumentInstance documentInstance) {
    final ManagementWorkspace workspace = documentInstance.getDocument()
        .getWorkspace();
    return managementWorkspaceEnforcer.get(workspace).combinedWith(
        new SecurityCheck() {
          @Override
          protected SecurityCheckResult evaluate(ExecutionContext context) {
            SocialOrganization business = (SocialOrganization) workspace
                .getOwner();

            BusinessAccountType accountType = business.getAccountType();

            int documentsCount = documentRepository.countByOwner(business);
            log.info("Allowed count "
                + accountType.getNumberOfDocumentAllowances()
                + " current count " + documentsCount);

            if (accountType.getNumberOfDocumentAllowances() < documentsCount) {
              return deny("Document allowances exceeded.");
            }

            long documentsSize = documentInstanceRepository
                .totalSizeByOwner(business);

            long allowedDocumentSizeInBytes = (1073741824L*accountType
                .getStorageSpaceAllowances());

            log.info("Allowed space " + allowedDocumentSizeInBytes
                + " current space " + documentsSize);

            if (allowedDocumentSizeInBytes <= documentsSize) {
              return deny("Document storage space exceeded.");
            }
            return allow();
          }
        });
  }
}
