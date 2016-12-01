package com.similan.service.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.lead.CrmLeadFieldSettingConfig;
import com.similan.domain.entity.lead.CrmLeadSettings;
import com.similan.domain.entity.lead.CrmLeadSettingsZohoConstants;
import com.similan.domain.entity.lead.CrmLeadSyncRecord;
import com.similan.domain.entity.lead.CrmRecordId;
import com.similan.domain.entity.lead.CrmRecordType;
import com.similan.domain.entity.lead.CrmSystemType;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.repository.lead.CrmLeadSyncRecordRepository;
import com.similan.domain.repository.lead.CrmRecordIdRepository;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.CrmLeadActivityDto;
import com.similan.framework.dto.lead.CrmLeadDto;
import com.similan.framework.dto.lead.CrmLeadNoteDto;
import com.similan.framework.dto.lead.LeadActivityDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadNoteDto;
import com.similan.service.api.CoreServiceErrorCode;
import com.similan.service.api.CrmConnect;
import com.similan.service.api.CrmIntegrationService;
import com.similan.service.api.LeadManagementService;
import com.similan.service.api.OrganizationManagementService;
import com.similan.service.api.ZohoFacade;
import com.similan.service.exception.CoreServiceException;

@Slf4j
public class CrmZohoConnect implements CrmConnect {

	@Autowired
	private CrmLeadSyncRecordRepository crmLeadSyncRecordRepository = null;
	private ZohoFacade zohoFacade = null;
	private LeadManagementService leadManagementService = null;
	private OrganizationManagementService organizationManagementService = null;
	@Autowired
	private CrmRecordIdRepository crmRecordIdRepository;
	private CrmIntegrationService crmIntegrationService;

	public CrmLeadSyncRecord performLeadSync(CrmLeadSettings crmLeadSettings)
			throws CoreServiceException, Exception {

		log.info("Starting sync for organization "
				+ crmLeadSettings.getOwner().getId());
		CrmLeadSyncRecord syncRecord = createSyncRecord(crmLeadSettings);
		
		OrganizationDetailInfoDto orgInfo = getOrganizationInfoFromSettings(crmLeadSettings);

		CrmProcessCache cache = new CrmProcessCache();

		fillCrmDataCaches(crmLeadSettings, orgInfo, cache);

		fillLocalDataCaches(crmLeadSettings, orgInfo, cache);

		if (crmLeadSettings.getEnableTransferFromCrm()) {
			log.info("Starting transfer from CRM");
			performSyncPull(crmLeadSettings, orgInfo, syncRecord, cache);
			log.info("Finished transfer from CRM");
		}


		if (crmLeadSettings.getEnableTransferToCrm()) {
			log.info("Starting transfer to CRM");
			rebuildLocalCaches(orgInfo, cache);
			performSyncPush(crmLeadSettings, orgInfo, syncRecord, cache);
			log.info("Finished transfer to CRM");
		}
		return syncRecord;
	}

	private void rebuildLocalCaches(OrganizationDetailInfoDto orgInfo,
			CrmProcessCache cache) throws Exception {
		// Rebuild local data after pull
		List<LeadDto> localData = getLocalLeads(orgInfo);
		cache.getLocalLeadDataLookup().clear();
		cache.getLocalNoteDataLookup().clear();
		cache.getLocalActivityDataLookup().clear();
		
		// Save or update CrmRecords
		for (LeadDto localLead : localData) {
			cache.getLocalLeadDataLookup().put(localLead.getId(), localLead);

			// Process notes
			for (LeadNoteDto note : this.leadManagementService
					.getLeadComments(localLead)) {
				localLead.getNoteList().add(note);
				cache.getLocalNoteDataLookup().put(note.getId(), note);
			}
			
			// Process notes
			for (LeadActivityDto activity : this.leadManagementService
					.getLeadActivity(localLead)) {
				localLead.getActivityList().add(activity);
				cache.getLocalActivityDataLookup().put(activity.getId(), activity);
			}			
		}
	}

	private OrganizationDetailInfoDto getOrganizationInfoFromSettings(
			CrmLeadSettings crmLeadSettings) {
		OrganizationDetailInfoDto orgInfo = null;
		try {
			orgInfo = organizationManagementService
					.getOrganization(crmLeadSettings.getOwner().getId());
		} catch (Exception exception) {
			log.error("Error getting organization for sync", exception);
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
		return orgInfo;
	}

	private void fillLocalDataCaches(CrmLeadSettings crmLeadSettings,
			OrganizationDetailInfoDto orgInfo, CrmProcessCache cache) throws Exception {
		List<LeadDto> localData = getLocalLeads(orgInfo);

		// Save or update CrmRecords
		for (LeadDto localLead : localData) {
			CrmRecordId leadRecordId = this.crmRecordIdRepository
					.findByLocalIdAndCrmSourceAndCrmRecordType(
							localLead.getId(), CrmSystemType.Zoho,
							CrmRecordType.Lead);
			if (leadRecordId == null) {
				createCrmRecordIdForLocalLead(crmLeadSettings, localLead);
			}
			// crmRecordId.setLastLocalTimestamp(localLead.getTimeStamp());
			cache.getLocalLeadDataLookup().put(localLead.getId(), localLead);

			// Process notes
			for (LeadNoteDto note : this.leadManagementService
					.getLeadComments(localLead)) {
				localLead.getNoteList().add(note);
				CrmRecordId noteRecordId = this.crmRecordIdRepository
						.findByLocalIdAndCrmSourceAndCrmRecordType(
								note.getId(), CrmSystemType.Zoho,
								CrmRecordType.Note);
				if (noteRecordId == null) {
					createCrmRecordIdForLocalNote(crmLeadSettings, note);
				}
				cache.getLocalNoteDataLookup().put(note.getId(), note);
			}
			
			// Process activities
			for (LeadActivityDto activity : this.leadManagementService
					.getLeadActivity(localLead)) {
				localLead.getActivityList().add(activity);
				CrmRecordId activityRecordId = this.crmRecordIdRepository
						.findByLocalIdAndCrmSourceAndCrmRecordType(
								activity.getId(), CrmSystemType.Zoho,
								CrmRecordType.Activity);
				if (activityRecordId == null) {
					activityRecordId = createCrmRecordIdForLocalActivity(
							crmLeadSettings, activity);
					crmRecordIdRepository.save(activityRecordId);
				}
				cache.getLocalActivityDataLookup().put(activity.getId(), activity);
			}
		}
	}

	private CrmRecordId createCrmRecordIdForLocalActivity(
			CrmLeadSettings crmLeadSettings, LeadActivityDto activity) {
		CrmRecordId activityRecordId;
		activityRecordId = crmRecordIdRepository.create();
		activityRecordId.setLocalId(activity.getId());
		activityRecordId.setCrmRecordType(CrmRecordType.Activity);
		activityRecordId.setCrmSource(CrmSystemType.Zoho);
		activityRecordId.setOwner(crmLeadSettings.getOwner());
		return activityRecordId;
	}

	private void createCrmRecordIdForLocalNote(CrmLeadSettings crmLeadSettings,
			LeadNoteDto note) {
		CrmRecordId noteRecordId;
		noteRecordId = crmRecordIdRepository.create();
		noteRecordId.setLocalId(note.getId());
		noteRecordId.setCrmRecordType(CrmRecordType.Note);
		noteRecordId.setCrmSource(CrmSystemType.Zoho);
		noteRecordId.setOwner(crmLeadSettings.getOwner());
		crmRecordIdRepository.save(noteRecordId);
	}

	private void createCrmRecordIdForLocalLead(CrmLeadSettings crmLeadSettings,
			LeadDto localLead) {
		CrmRecordId leadRecordId;
		leadRecordId = crmRecordIdRepository.create();
		leadRecordId.setLocalId(localLead.getId());
		leadRecordId.setCrmRecordType(CrmRecordType.Lead);
		leadRecordId.setCrmSource(CrmSystemType.Zoho);
		leadRecordId.setOwner(crmLeadSettings.getOwner());
		crmRecordIdRepository.save(leadRecordId);
	}

	private void fillCrmDataCaches(CrmLeadSettings crmLeadSettings,
			OrganizationDetailInfoDto orgInfo, CrmProcessCache cache) {
		List<CrmLeadDto> remoteLeads = getRemoteLeads(crmLeadSettings, orgInfo);

		// Save or update CrgmRecords
		for (CrmLeadDto remoteLead : remoteLeads) {
			CrmRecordId leadRecordId = this.crmRecordIdRepository
					.findByRemoteIdAndCrmSourceAndCrmRecordType(
							remoteLead.getCrmId(), CrmSystemType.Zoho,
							CrmRecordType.Lead);
			if (leadRecordId == null) {
				createCrmRecordIdForCrmLead(crmLeadSettings, remoteLead);
			}
			// crmRecordId.setLastRemoteTimestamp(remoteLead.getRemoteLastUpdateTimestamp());
			cache.getRemoteLeadDataLookup().put(remoteLead.getCrmId(), remoteLead);

			// Process notes
			for (LeadNoteDto note : remoteLead.getNoteList()) {
				CrmLeadNoteDto castedNote = (CrmLeadNoteDto) note;
				CrmRecordId noteRecordId = this.crmRecordIdRepository
						.findByRemoteIdAndCrmSourceAndCrmRecordType(
								castedNote.getCrmId(), CrmSystemType.Zoho,
								CrmRecordType.Note);
				if (noteRecordId == null) {
					createCrmRecordIdForCrmNote(crmLeadSettings, castedNote);
				}
				cache.getRemoteNotesDataLookup().put(castedNote.getCrmId(),
						castedNote);
			}
			
			// Process activities
			for (LeadActivityDto activity : remoteLead.getActivityList()) {
				CrmLeadActivityDto castedActivity = (CrmLeadActivityDto) activity;
				CrmRecordId activtyRecordId = this.crmRecordIdRepository
						.findByRemoteIdAndCrmSourceAndCrmRecordType(
								castedActivity.getCrmId(), CrmSystemType.Zoho,
								CrmRecordType.Activity);
				if (activtyRecordId == null) {
					createCrmRecordIdForCrmActivity(crmLeadSettings,
							castedActivity);
				}
				cache.getRemoteActivityDataLookup().put(castedActivity.getCrmId(),
						castedActivity);
			}			
		}
	}

	private void createCrmRecordIdForCrmActivity(
			CrmLeadSettings crmLeadSettings, CrmLeadActivityDto castedActivity) {
		CrmRecordId activtyRecordId;
		activtyRecordId = crmRecordIdRepository.create();
		activtyRecordId.setRemoteId(castedActivity.getCrmId());
		activtyRecordId.setCrmRecordType(CrmRecordType.Activity);
		activtyRecordId.setCrmSource(CrmSystemType.Zoho);
		activtyRecordId.setOwner(crmLeadSettings.getOwner());
		crmRecordIdRepository.save(activtyRecordId);
	}

	private void createCrmRecordIdForCrmNote(CrmLeadSettings crmLeadSettings,
			CrmLeadNoteDto castedNote) {
		CrmRecordId noteRecordId;
		noteRecordId = crmRecordIdRepository.create();
		noteRecordId.setRemoteId(castedNote.getCrmId());
		noteRecordId.setCrmRecordType(CrmRecordType.Note);
		noteRecordId.setCrmSource(CrmSystemType.Zoho);
		noteRecordId.setOwner(crmLeadSettings.getOwner());
		crmRecordIdRepository.save(noteRecordId);
	}

	private void createCrmRecordIdForCrmLead(CrmLeadSettings crmLeadSettings,
			CrmLeadDto remoteLead) {
		CrmRecordId leadRecordId;
		leadRecordId = crmRecordIdRepository.create();
		leadRecordId.setRemoteId(remoteLead.getCrmId());
		leadRecordId.setCrmRecordType(CrmRecordType.Lead);
		leadRecordId.setCrmSource(CrmSystemType.Zoho);
		leadRecordId.setOwner(crmLeadSettings.getOwner());
		crmRecordIdRepository.save(leadRecordId);
	}

	private CrmLeadSyncRecord createSyncRecord(CrmLeadSettings crmLeadSettings) {
		CrmLeadSyncRecord record = crmLeadSyncRecordRepository.create();
		record.setOwner(crmLeadSettings.getOwner());
		record.setCrmSource(crmLeadSettings.getCrmSystemType());
		record.setTimeStamp(new Date());
		return record;
	}

	private List<LeadDto> getLocalLeads(OrganizationDetailInfoDto orgInfo)
			throws Exception {
		return this.leadManagementService.getAllOrganizationLeads(orgInfo
				.getId());
	}

	private List<CrmLeadDto> getRemoteLeads(CrmLeadSettings crmLeadSettings,
			OrganizationDetailInfoDto orgInfo) {
		return zohoFacade.getZohoLeads(getZohoAuthToken(crmLeadSettings));
	}

	private void performSyncPull(CrmLeadSettings crmLeadSettings,
			OrganizationDetailInfoDto orgInfo, CrmLeadSyncRecord syncRecord,
			CrmProcessCache cache) throws CoreServiceException, Exception {

		List<CrmRecordId> leadRecordIds = this.crmRecordIdRepository
				.findByOwnerAndCrmSourceAndCrmRecordType(
						crmLeadSettings.getOwner(), CrmSystemType.Zoho,
						CrmRecordType.Lead);

		CrmLeadFieldSettingConfig config = getCrmLeadFieldSettingsConfig(crmLeadSettings);

		long numberOfNewLeadsFromCrm = 0;
		long numberOfUpdatesFromCrm = 0;

		for (CrmRecordId leadRecordId : leadRecordIds) {
			boolean wasInserted = false;
			boolean wasUpdated = false;

			// If remote ID is null (local only) then skip (nothing to pull)
			if (leadRecordId.getRemoteId() == null) {
				continue;
			}

			CrmLeadDto crmLead = cache.getRemoteLeadDataLookup().get(
					leadRecordId.getRemoteId());

			if (crmLead == null) {
				// Remote lead missing even though we have a record of it. It
				// may have been deleted
				// HANDLE DELETES HERE
				log.warn("Possible delete of remote record detected "
						+ leadRecordId.getRemoteId());

				continue;
			}
			// If no local id (remote only) then create a new local lead and
			// store it
			if (leadRecordId.getLocalId() == null) {
				createLocalLeadFromCrmLead(crmLeadSettings, orgInfo,
						syncRecord, cache, config, leadRecordId, crmLead);				
				log.info("Lead successfully saved");
				wasInserted = true;

			} else {
				// Local lead exists. Need to check to see if it needs to be
				// updated

				// First check to see if the remote update time has changed. If
				// not, then skip
				LeadDto localLead = cache.getLocalLeadDataLookup().get(
						leadRecordId.getLocalId());
				if (localLead == null) {
					// Lead may have been deleted. Skip for now
					continue;
				}
				List<CrmLeadDto> relatedRemoteLeads = getRelatedRemoteLeadsForLead(localLead);

				List<LeadNoteDto> remoteNotes = crmLead.getNoteList();
				List<LeadActivityDto> remoteActivities = crmLead.getActivityList();
				relatedRemoteLeads.add(crmLead);
				crmLead = null;
				for (CrmLeadDto relatedLead : relatedRemoteLeads) {
					if (crmLead == null)
						crmLead = relatedLead;
					if (relatedLead.getCrmLastUpdateTimestamp().after(
							crmLead.getCrmLastUpdateTimestamp()))
						crmLead = relatedLead;
				}
				
				crmLead.setNoteList(remoteNotes);
				crmLead.setActivityList(remoteActivities);
				
				if (crmLead.getCrmLastUpdateTimestamp().after(
						leadRecordId.getLastRemoteTimestamp())) {

					wasUpdated = updateLocalLeadFromCrmLead(orgInfo,
							syncRecord, cache, config, leadRecordId,
							wasUpdated, crmLead, localLead);
				}

				// Sync Notes always even if lead is not updated
				wasUpdated = synchronizeLocalNotesForLead(crmLeadSettings,
						syncRecord, wasUpdated, crmLead, localLead);
				// Sync Activities always even if lead is not updated
				wasUpdated = synchronizeLocalActivitiesForLead(syncRecord,
						wasUpdated, crmLead, localLead);
			}
			if (wasInserted == true)
				numberOfNewLeadsFromCrm++;
			if (wasUpdated == true)
				numberOfUpdatesFromCrm++;
		}

		// Delete local notes
		numberOfUpdatesFromCrm = deleteLocalNotesNotInCrm(crmLeadSettings,
				cache, numberOfUpdatesFromCrm);

		// Delete local activities
		numberOfUpdatesFromCrm = deleteLocalActivitiesNotInCrm(crmLeadSettings,
				cache, numberOfUpdatesFromCrm);
		
		syncRecord.setNumberOfNewLeadsFromCrm(numberOfNewLeadsFromCrm);
		syncRecord.setNumberOfUpdatesFromCrm(numberOfUpdatesFromCrm);
	}

	private long deleteLocalActivitiesNotInCrm(CrmLeadSettings crmLeadSettings,
			CrmProcessCache cache, long numberOfUpdatesFromCrm) {
		List<CrmRecordId> crmExistingActivityRecordId = this.crmRecordIdRepository
				.findByOwnerAndCrmSourceAndCrmRecordType(
						crmLeadSettings.getOwner(), CrmSystemType.Zoho,
						CrmRecordType.Activity);
		for (CrmRecordId record : crmExistingActivityRecordId) {
			if (record.getRemoteId() != null) {
				CrmLeadActivityDto remoteActivity = cache.getRemoteActivityDataLookup()
						.get(record.getRemoteId());
				if (remoteActivity == null) {
					LeadActivityDto activity = cache.getLocalActivityDataLookup().get(
							record.getLocalId());
					if (activity != null) {
						this.leadManagementService.deleteLeadActivity(activity);
						numberOfUpdatesFromCrm++;
						this.crmRecordIdRepository.delete(record.getId());
					}
				}
			}
		}
		return numberOfUpdatesFromCrm;
	}

	private long deleteLocalNotesNotInCrm(CrmLeadSettings crmLeadSettings,
			CrmProcessCache cache, long numberOfUpdatesFromCrm) {
		List<CrmRecordId> crmExistingNotesRecordId = this.crmRecordIdRepository
				.findByOwnerAndCrmSourceAndCrmRecordType(
						crmLeadSettings.getOwner(), CrmSystemType.Zoho,
						CrmRecordType.Note);
		for (CrmRecordId record : crmExistingNotesRecordId) {
			if (record.getRemoteId() != null) {
				CrmLeadNoteDto remoteNote = cache.getRemoteNotesDataLookup()
						.get(record.getRemoteId());
				if (remoteNote == null) {
					LeadNoteDto note = cache.getLocalNoteDataLookup().get(
							record.getLocalId());
					if (note != null) {
						this.leadManagementService.deleteLeadNote(note);
						numberOfUpdatesFromCrm++;
						this.crmRecordIdRepository.delete(record.getId());

					}
				}
			}
		}
		return numberOfUpdatesFromCrm;
	}

	private boolean synchronizeLocalActivitiesForLead(
			CrmLeadSyncRecord syncRecord, boolean wasUpdated,
			CrmLeadDto crmLead, LeadDto localLead) {
		for (LeadActivityDto leadActivity : crmLead.getActivityList()) {
			CrmLeadActivityDto remoteLeadActivity = (CrmLeadActivityDto) leadActivity;
			CrmRecordId crmActivityRecordId = this.crmRecordIdRepository
					.findByRemoteIdAndCrmSourceAndCrmRecordType(
							remoteLeadActivity.getCrmId(),
							CrmSystemType.Zoho, CrmRecordType.Activity);

			if (crmActivityRecordId.getLocalId() == null) {
				// Create new local activity
				LeadActivityDto localActivity = createLocalLeadActivityDtoFromCrmLeadActivity(
						syncRecord, remoteLeadActivity);
				
				leadManagementService.addLeadActivity(localActivity, localLead);
				localLead.getActivityList().add(localActivity);

				wasUpdated = true;
				crmActivityRecordId.setLocalId(localActivity.getId());
				crmActivityRecordId.setLastRemoteTimestamp(remoteLeadActivity
						.getCrmLastUpdateTimestamp());
				crmActivityRecordId.setLastLocalTimestamp(syncRecord
						.getTimeStamp());
				this.crmRecordIdRepository.save(crmActivityRecordId);

			} else {
				// Otherwise get existing local activity for update
				if (remoteLeadActivity.getCrmLastUpdateTimestamp().after(
						crmActivityRecordId.getLastRemoteTimestamp())) {
					for (LeadActivityDto localActivity : localLead
							.getActivityList()) {
						if (localActivity.getId().equals(
								crmActivityRecordId.getLocalId())) {
							
							updateLocalActivityDtoFromCrmLeadActivity(
									syncRecord, remoteLeadActivity,
									localActivity);
							
							log.info("Saving activity with data "
									+ localActivity);
							leadManagementService
									.updateLeadActivity(localActivity);

							wasUpdated = true;
							crmActivityRecordId.setLocalId(localActivity
									.getId());
							crmActivityRecordId
									.setLastRemoteTimestamp(remoteLeadActivity
											.getCrmLastUpdateTimestamp());
							crmActivityRecordId
									.setLastLocalTimestamp(syncRecord
											.getTimeStamp());
							this.crmRecordIdRepository
									.save(crmActivityRecordId);
						}
					}
				}
			}
		}
		return wasUpdated;
	}

	private void updateLocalActivityDtoFromCrmLeadActivity(
			CrmLeadSyncRecord syncRecord,
			CrmLeadActivityDto remoteLeadActivity, LeadActivityDto localActivity) {
		localActivity.setSubjectActivity(remoteLeadActivity.getSubjectActivity());
		localActivity.setActivityDescription(remoteLeadActivity.getActivityDescription());
		localActivity.setLeadActivityType(remoteLeadActivity.getLeadActivityType());
		localActivity.setStartDate(remoteLeadActivity.getStartDate());
		localActivity.setEndDate(remoteLeadActivity.getEndDate());
		localActivity.setVenue(remoteLeadActivity.getVenue());
		
		localActivity.setTimeStamp(
				syncRecord.getTimeStamp());
	}

	private LeadActivityDto createLocalLeadActivityDtoFromCrmLeadActivity(
			CrmLeadSyncRecord syncRecord, CrmLeadActivityDto remoteLeadActivity) {
		LeadActivityDto localActivity = new LeadActivityDto();
		updateLocalActivityDtoFromCrmLeadActivity(syncRecord,
				remoteLeadActivity, localActivity);
		return localActivity;
	}

	private boolean synchronizeLocalNotesForLead(
			CrmLeadSettings crmLeadSettings, CrmLeadSyncRecord syncRecord,
			boolean wasUpdated, CrmLeadDto crmLead, LeadDto localLead) {
		// Sync Notes always even if lead is not updated
		for (LeadNoteDto leadNote : crmLead.getNoteList()) {
			CrmLeadNoteDto remoteLeadNote = (CrmLeadNoteDto) leadNote;
			CrmRecordId crmNoteRecordId = this.crmRecordIdRepository
					.findByRemoteIdAndCrmSourceAndCrmRecordType(
							remoteLeadNote.getCrmId(),
							CrmSystemType.Zoho, CrmRecordType.Note);

			if (crmNoteRecordId.getLocalId() == null) {
				// Create new local lead
				LeadNoteDto localNote = createLocalLeadNoteDtoFromCrmLeadNote(
						crmLeadSettings, syncRecord, remoteLeadNote);
				leadManagementService.addLeadNote(localNote, localLead,
						null);
				localLead.getNoteList().add(localNote);

				wasUpdated = true;
				crmNoteRecordId.setLocalId(localNote.getId());
				crmNoteRecordId.setLastRemoteTimestamp(remoteLeadNote
						.getCrmLastUpdateTimestamp());
				crmNoteRecordId.setLastLocalTimestamp(syncRecord
						.getTimeStamp());
				this.crmRecordIdRepository.save(crmNoteRecordId);

			} else {
				// Otherwise get existing local lead for update
				if (remoteLeadNote.getCrmLastUpdateTimestamp().after(
						crmNoteRecordId.getLastRemoteTimestamp())) {
					for (LeadNoteDto localNote : localLead
							.getNoteList()) {
						if (localNote.getId().equals(
								crmNoteRecordId.getLocalId())) {
							updateLocalNoteDtoFromCrmNote(syncRecord,
									remoteLeadNote, localNote);

							log.info("Saving note with data "
									+ localNote);
							leadManagementService
									.updateLeadNote(localNote);

							wasUpdated = true;
							crmNoteRecordId.setLocalId(localNote
									.getId());
							crmNoteRecordId
									.setLastRemoteTimestamp(remoteLeadNote
											.getCrmLastUpdateTimestamp());
							crmNoteRecordId
									.setLastLocalTimestamp(syncRecord
											.getTimeStamp());
							this.crmRecordIdRepository
									.save(crmNoteRecordId);
						}
					}
				}
			}
		}
		return wasUpdated;
	}

	private void updateLocalNoteDtoFromCrmNote(CrmLeadSyncRecord syncRecord,
			CrmLeadNoteDto remoteLeadNote, LeadNoteDto localNote) {
		localNote.getLeadNote().setMessage(
				remoteLeadNote.getLeadNote()
						.getMessage());
		localNote.getLeadNote().setSubject(
				remoteLeadNote.getLeadNote()
						.getSubject());
		localNote.getLeadNote().setTimeStamp(
				syncRecord.getTimeStamp());
	}

	private LeadNoteDto createLocalLeadNoteDtoFromCrmLeadNote(
			CrmLeadSettings crmLeadSettings, CrmLeadSyncRecord syncRecord,
			CrmLeadNoteDto remoteLeadNote) {
		LeadNoteDto localNote = new LeadNoteDto();
		updateLocalNoteDtoFromCrmNote(syncRecord, remoteLeadNote, localNote);
		localNote.setCommenterId(crmLeadSettings.getOwner()
				.getId());
		return localNote;
	}

	private boolean updateLocalLeadFromCrmLead(OrganizationDetailInfoDto orgInfo,
			CrmLeadSyncRecord syncRecord, CrmProcessCache cache,
			CrmLeadFieldSettingConfig config, CrmRecordId leadRecordId,
			boolean wasUpdated, CrmLeadDto crmLead, LeadDto localLead)
			throws Exception {
		log.info("Updating existing lead. Data was " + localLead);

		// If local lead is not tied to member then update the main
		// lead records
		if (localLead.getForSocialActorId() != null) {
			// Check to see if any actual changes have taken place
			boolean hasChanges = pushChangesToLeadSource(
					crmLead, localLead,
					config.getLeadFieldImports());
			if (hasChanges) {
				localLead.setTimeStamp(syncRecord.getTimeStamp());
				leadManagementService.saveLead(localLead, orgInfo,
						true);
				cache.getLocalLeadDataLookup().put(localLead.getId(),
						localLead);

				log.info("Lead successfully saved");
				wasUpdated = true;

				leadRecordId.setLastLocalTimestamp(syncRecord
						.getTimeStamp());
			}
		}
		// Record remote timestamp even if local wasn't updated so
		// that
		// futue syncs skip this record
		leadRecordId.setLastRemoteTimestamp(crmLead
				.getCrmLastUpdateTimestamp());
		this.crmRecordIdRepository.save(leadRecordId);
		return wasUpdated;
	}

	private void createLocalLeadFromCrmLead(CrmLeadSettings crmLeadSettings,
			OrganizationDetailInfoDto orgInfo, CrmLeadSyncRecord syncRecord,
			CrmProcessCache cache, CrmLeadFieldSettingConfig config,
			CrmRecordId leadRecordId, CrmLeadDto crmLead) throws Exception {
		// Create new local lead
		LeadDto localLead = new LeadDto();
		localLead.setForSocialActorId(crmLeadSettings.getOwner()
				.getId());
		pushChangesToLeadSource(crmLead, localLead,
				config.getLeadFieldImports());

		localLead.setLeadType(LeadType.ContactLead);
		localLead.setLeadStatus(LeadStatusType.Unassigned);
		localLead.setTimeStamp(syncRecord.getTimeStamp());
		localLead.setDateCreated(syncRecord.getTimeStamp());
		log.info("Saving lead with data " + localLead);
		leadManagementService.saveLead(localLead, orgInfo, true);

		cache.getLocalLeadDataLookup().put(localLead.getId(), localLead);

		leadRecordId.setLocalId(localLead.getId());
		leadRecordId.setLastRemoteTimestamp(crmLead
				.getCrmLastUpdateTimestamp());
		leadRecordId.setLastLocalTimestamp(syncRecord.getTimeStamp());
		this.crmRecordIdRepository.save(leadRecordId);

		// Sync Notes
		for (LeadNoteDto leadNote : crmLead.getNoteList()) {
			CrmLeadNoteDto crmLeadNote = (CrmLeadNoteDto) leadNote;
			CrmRecordId noteRecordId = this.crmRecordIdRepository
					.findByRemoteIdAndCrmSourceAndCrmRecordType(
							crmLeadNote.getCrmId(),
							CrmSystemType.Zoho, CrmRecordType.Note);
			LeadNoteDto localNote = createLocalLeadNoteDtoFromCrmLeadNote(
					crmLeadSettings, syncRecord, crmLeadNote);

			log.info("Saving note with data " + localNote);
			leadManagementService.addLeadNote(localNote, localLead,
					null);
			localLead.getNoteList().add(localNote);
			noteRecordId.setLocalId(localNote.getId());
			noteRecordId.setLastRemoteTimestamp(crmLeadNote
					.getCrmLastUpdateTimestamp());
			noteRecordId.setLastLocalTimestamp(syncRecord
					.getTimeStamp());
			this.crmRecordIdRepository.save(noteRecordId);

		}
		
		// Sync Activities
		for (LeadActivityDto leadActivity : crmLead.getActivityList()) {
			CrmLeadActivityDto crmLeadActivity = (CrmLeadActivityDto) leadActivity;
			CrmRecordId activityRecordId = this.crmRecordIdRepository
					.findByRemoteIdAndCrmSourceAndCrmRecordType(
							crmLeadActivity.getCrmId(),
							CrmSystemType.Zoho, CrmRecordType.Activity);
			LeadActivityDto localActivity = createLocalLeadActivityDtoFromCrmLeadActivity(
					syncRecord, crmLeadActivity);

			log.info("Saving activity with data " + localActivity);
			leadManagementService.addLeadActivity(localActivity, localLead);
			localLead.getActivityList().add(localActivity);
			activityRecordId.setLocalId(localActivity.getId());
			activityRecordId.setLastRemoteTimestamp(crmLeadActivity
					.getCrmLastUpdateTimestamp());
			activityRecordId.setLastLocalTimestamp(syncRecord
					.getTimeStamp());
			this.crmRecordIdRepository.save(activityRecordId);

		}
	}

	private List<CrmLeadDto> getRelatedRemoteLeadsForLead(LeadDto localLead) {
		List<CrmLeadDto> remoteLeads = new ArrayList<CrmLeadDto>();
		try {
			List<LeadDto> relatedLeads = this.leadManagementService
					.getTransferRelatedLeads(localLead);

			for (LeadDto relatedLead : relatedLeads) {
				CrmLeadDto remoteLead = this.crmIntegrationService
						.getRemoteLeadDtoForLead(relatedLead);
				if (remoteLead != null)
					remoteLeads.add(remoteLead);
			}
		} catch (Exception e) {
			// Not sure if this should fail the entire sync. For now, just log
			log.warn("Unable to get transfer related leads for sync", e);
		}
		return remoteLeads;
	}

	private boolean pushChangesToLeadSource(LeadDto fromLead, LeadDto toLead,
			List<String> leadFieldExports) {

		boolean changesOccurred = false;

		if (leadFieldExports.contains("FirstName")) {
			if (!StringUtils.equals(fromLead.getFirstName(),
					toLead.getFirstName())) {
				changesOccurred = true;
				toLead.setFirstName(fromLead.getFirstName());
			}
		}

		if (leadFieldExports.contains("LastName")) {
			if (!StringUtils.equals(fromLead.getLastName(),
					toLead.getLastName())) {
				changesOccurred = true;
				toLead.setLastName(fromLead.getLastName());
			}
		}

		if (leadFieldExports.contains("Title")) {
			if (!StringUtils.equals(fromLead.getName(), toLead.getName())) {
				changesOccurred = true;
				toLead.setName(fromLead.getName());
			}
		}

		if (leadFieldExports.contains("WorkPhone")) {
			if (!StringUtils.equals(fromLead.getContactPhone(),
					toLead.getContactPhone())) {
				changesOccurred = true;
				toLead.setContactPhone(fromLead.getContactPhone());
			}
		}

		if (leadFieldExports.contains("Email")) {
			if (!StringUtils.equals(fromLead.getContactEmail(),
					toLead.getContactEmail())) {
				changesOccurred = true;
				toLead.setContactEmail(fromLead.getContactEmail());
			}
		}

		if (leadFieldExports.contains("AddressCity")) {
			if (!StringUtils.equals(fromLead.getLocation().getCity(), toLead
					.getLocation().getCity())) {
				changesOccurred = true;
				toLead.getLocation().setCity(fromLead.getLocation().getCity());
			}
		}

		if (leadFieldExports.contains("AddressStreet")) {
			if (!StringUtils.equals(fromLead.getLocation().getStreet(), toLead
					.getLocation().getStreet())) {
				changesOccurred = true;
				toLead.getLocation().setStreet(
						fromLead.getLocation().getStreet());
			}
		}

		if (leadFieldExports.contains("AddressState")) {
			if (!StringUtils.equals(fromLead.getLocation().getState(), toLead
					.getLocation().getState())) {
				changesOccurred = true;
				toLead.getLocation()
						.setState(fromLead.getLocation().getState());
			}
		}

		if (leadFieldExports.contains("AddressCountry")) {
			if (!StringUtils.equals(fromLead.getLocation().getCountry(), toLead
					.getLocation().getCountry())) {
				changesOccurred = true;
				toLead.getLocation().setCountry(
						fromLead.getLocation().getCountry());
			}
		}

		if (leadFieldExports.contains("AddressZipcode")) {
			if (!StringUtils.equals(fromLead.getLocation().getZipCode(), toLead
					.getLocation().getZipCode())) {
				changesOccurred = true;
				toLead.getLocation().setZipCode(
						fromLead.getLocation().getZipCode());
			}
		}

		if (leadFieldExports.contains("WorkCompany")) {
			if (!StringUtils.equals(fromLead.getCompany(), toLead.getCompany())) {
				changesOccurred = true;
				toLead.setCompany(fromLead.getCompany());
			}
		}

		return changesOccurred;
	}

	private CrmLeadFieldSettingConfig getCrmLeadFieldSettingsConfig(
			CrmLeadSettings crmLeadSettings) {
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(CrmLeadFieldSettingConfig.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (CrmLeadFieldSettingConfig) unmarshaller
					.unmarshal(new StringReader(crmLeadSettings
							.getConfigListItems()));
		} catch (Exception e) {
			return null;
		}
	}

	private String getZohoAuthToken(CrmLeadSettings crmLeadSettings) {
		return crmLeadSettings.getParameters().get(
				CrmLeadSettingsZohoConstants.AUTH_TOKEN);
	}

	private void performSyncPush(CrmLeadSettings crmLeadSettings,
			OrganizationDetailInfoDto orgInfo, CrmLeadSyncRecord syncRecord,
			CrmProcessCache cache) throws CoreServiceException, Exception {

		List<CrmRecordId> crmRecordIds = this.crmRecordIdRepository
				.findByOwnerAndCrmSourceAndCrmRecordType(
						crmLeadSettings.getOwner(), CrmSystemType.Zoho,
						CrmRecordType.Lead);

		CrmLeadFieldSettingConfig config = getCrmLeadFieldSettingsConfig(crmLeadSettings);
		long numberOfNewLeadsFromBr = 0;
		long numberOfUpdatesFromBr = 0;

		for (CrmRecordId crmRecordId : crmRecordIds) {
			boolean wasInserted = false;
			boolean wasUpdated = false;
			// If local ID is null (remote only) then skip (nothing to push)
			if (crmRecordId.getLocalId() == null) {
				continue;
			}

			LeadDto localLead = cache.getLocalLeadDataLookup().get(
					crmRecordId.getLocalId());

			if (localLead == null) {
				// Remote lead missing even though we have a record of it. It
				// may have been deleted
				// HANDLE DELETES HERE
				log.warn("Possible delete of local record detected "
						+ crmRecordId.getLocalId());
				continue;
			}
			// If no remote id (local only) then create a new remote lead and
			// push it
			if (crmRecordId.getRemoteId() == null) {
				log.info("Adding new lead to Zoho");

				CrmLeadDto remoteLead = new CrmLeadDto();
				pushChangesToLeadSource(localLead, remoteLead,
						config.getLeadFieldExports());

				try {
					zohoFacade.createLead(getZohoAuthToken(crmLeadSettings),
							remoteLead);

					crmRecordId.setLastLocalTimestamp(localLead.getTimeStamp());
					crmRecordId.setRemoteId(remoteLead.getCrmId());
					crmRecordId.setLastRemoteTimestamp(remoteLead
							.getCrmLastUpdateTimestamp());

					this.crmRecordIdRepository.save(crmRecordId);

					updateCrmLeadNotesFromLocalNotes(crmLeadSettings,
							localLead, remoteLead);
					
					updateCrmLeadActivitiesFromLocalActivity(crmLeadSettings,
							syncRecord, localLead, remoteLead);

				} catch (CoreServiceException exception) {
					if (exception
							.getErrorCode()
							.equals(CoreServiceErrorCode.CRM_ZOHO_API_ERROR_INVALID_DATA)) {
						// We shouldn't be in this situation but if there is no
						// data to send
						// to zoho for the given record, just skip it for now.
						log.warn("Attempt to send empty lead data to zoho: "
								+ remoteLead);

						continue;
					} else
						throw exception;
				}

				// Save local lead as it has been updated with its new crm id

				log.info("Lead successfully updated");
				numberOfNewLeadsFromBr++;

			} else {
				// Updating an existing zoho lead

				if (localLead.getTimeStamp().after(
						crmRecordId.getLastLocalTimestamp())) {

					log.info("Updating existing lead in Zoho. Data was "
							+ localLead);
					CrmLeadDto remoteLead = cache.getRemoteLeadDataLookup().get(
							crmRecordId.getRemoteId());
					if (remoteLead == null) {
						// Lead may have been deleted. Skip for now
						continue;
					}

					pushChangesToLeadSource(localLead, remoteLead,
							config.getLeadFieldExports());
					try {
						zohoFacade.updateLead(
								getZohoAuthToken(crmLeadSettings), remoteLead);
					} catch (CoreServiceException exception) {
						if (exception
								.getErrorCode()
								.equals(CoreServiceErrorCode.CRM_ZOHO_API_ERROR_INVALID_DATA)) {
							// We shouldn't be in this situation but if there is
							// no
							// data to send
							// to zoho for the given record, just skip it for
							// now.
							log.warn("Attempt to send empty lead data to zoho: "
									+ remoteLead);
							continue;
						} else
							throw exception;
					}

					crmRecordId.setRemoteId(remoteLead.getCrmId());
					crmRecordId.setLastRemoteTimestamp(remoteLead
							.getCrmLastUpdateTimestamp());
					crmRecordId.setLastLocalTimestamp(localLead.getTimeStamp());

					this.crmRecordIdRepository.save(crmRecordId);

					log.info("Lead successfully saved to zoho");
					numberOfUpdatesFromBr++;
				}

				// Sync Notes always even if lead is not updated
				wasUpdated = synchronizeCrmLeadNotesFromLocalNotes(
						crmLeadSettings, syncRecord, cache, crmRecordId,
						wasUpdated, localLead);
				// Sync Activities always even if lead is not updated
				wasUpdated = synchronizeCrmLeadActivitiesFromLocalActivitys(
						crmLeadSettings, syncRecord, cache, crmRecordId,
						wasUpdated, localLead);				
			}
			if (wasInserted == true)
				numberOfNewLeadsFromBr++;
			if (wasUpdated == true)
				numberOfUpdatesFromBr++;

		}

		numberOfUpdatesFromBr = deleteCrmNotesNotInCrm(crmLeadSettings, cache,
				numberOfUpdatesFromBr);
		
		numberOfUpdatesFromBr = deleteCrmActivitiesNotInLocal(crmLeadSettings,
				cache, numberOfUpdatesFromBr);

		syncRecord.setNumberOfNewLeadsFromBr(numberOfNewLeadsFromBr);
		syncRecord.setNumberOfUpdatesFromBr(numberOfUpdatesFromBr);

	}

	private void updateCrmLeadNotesFromLocalNotes(
			CrmLeadSettings crmLeadSettings, LeadDto localLead,
			CrmLeadDto remoteLead) {
		for (LeadNoteDto leadNoteDto : localLead.getNoteList()) {
			CrmRecordId crmNoteRecordId = this.crmRecordIdRepository
					.findByLocalIdAndCrmSourceAndCrmRecordType(
							leadNoteDto.getId(),
							CrmSystemType.Zoho, CrmRecordType.Note);

			CrmLeadNoteDto remoteLeadNoteDto = new CrmLeadNoteDto();
			remoteLeadNoteDto
					.setLeadNote(leadNoteDto.getLeadNote());

			zohoFacade.createLeadNote(
					getZohoAuthToken(crmLeadSettings), remoteLead,
					remoteLeadNoteDto);
			remoteLead.getNoteList().add(remoteLeadNoteDto);

			crmNoteRecordId.setLastLocalTimestamp(leadNoteDto
					.getLeadNote().getTimeStamp());
			crmNoteRecordId.setRemoteId(remoteLeadNoteDto
					.getCrmId());
			crmNoteRecordId
					.setLastRemoteTimestamp(remoteLeadNoteDto
							.getCrmLastUpdateTimestamp());

			this.crmRecordIdRepository.save(crmNoteRecordId);
		}
	}

	private void updateCrmLeadActivitiesFromLocalActivity(
			CrmLeadSettings crmLeadSettings, CrmLeadSyncRecord syncRecord,
			LeadDto localLead, CrmLeadDto remoteLead) {
		for (LeadActivityDto leadActivityDto : localLead.getActivityList()) {
			
			CrmRecordId crmActivityRecordId = this.crmRecordIdRepository
					.findByLocalIdAndCrmSourceAndCrmRecordType(
							leadActivityDto.getId(),
							CrmSystemType.Zoho, CrmRecordType.Activity);

			CrmLeadActivityDto remoteLeadActivityDto = new CrmLeadActivityDto();
			remoteLeadActivityDto.setSubjectActivity(leadActivityDto.getSubjectActivity());
			remoteLeadActivityDto.setActivityDescription(leadActivityDto.getActivityDescription());
			remoteLeadActivityDto.setLeadActivityType(leadActivityDto.getLeadActivityType());
			remoteLeadActivityDto.setStartDate(leadActivityDto.getStartDate());
			remoteLeadActivityDto.setEndDate(leadActivityDto.getEndDate());
			remoteLeadActivityDto.setVenue(leadActivityDto.getVenue());
			remoteLeadActivityDto.setTimeStamp(
					syncRecord.getTimeStamp());
			
			zohoFacade.createLeadActivity(
					getZohoAuthToken(crmLeadSettings), remoteLead,
					remoteLeadActivityDto);
			remoteLead.getActivityList().add(remoteLeadActivityDto);

			crmActivityRecordId.setLastLocalTimestamp(leadActivityDto
					.getTimeStamp());
			crmActivityRecordId.setRemoteId(remoteLeadActivityDto
					.getCrmId());
			crmActivityRecordId
					.setLastRemoteTimestamp(remoteLeadActivityDto
							.getCrmLastUpdateTimestamp());

			this.crmRecordIdRepository.save(crmActivityRecordId);
		}
	}

	private boolean synchronizeCrmLeadNotesFromLocalNotes(
			CrmLeadSettings crmLeadSettings, CrmLeadSyncRecord syncRecord,
			CrmProcessCache cache, CrmRecordId crmRecordId, boolean wasUpdated,
			LeadDto localLead) {
		for (LeadNoteDto leadNote : localLead.getNoteList()) {
			CrmRecordId crmNoteRecordId = this.crmRecordIdRepository
					.findByLocalIdAndCrmSourceAndCrmRecordType(
							leadNote.getId(), CrmSystemType.Zoho,
							CrmRecordType.Note);

			if (crmNoteRecordId.getRemoteId() == null) {
				CrmLeadDto remoteLead = cache.getRemoteLeadDataLookup()
						.get(crmRecordId.getRemoteId());
				// Create new local lead
				CrmLeadNoteDto remoteLeadNote = new CrmLeadNoteDto();
				remoteLeadNote.getLeadNote().setMessage(
						leadNote.getLeadNote().getMessage());
				remoteLeadNote.getLeadNote().setSubject(
						leadNote.getLeadNote().getSubject());
				remoteLeadNote.getLeadNote().setTimeStamp(
						syncRecord.getTimeStamp());
				remoteLeadNote.setCommenterId(crmLeadSettings
						.getOwner().getId());

				zohoFacade.createLeadNote(
						getZohoAuthToken(crmLeadSettings), remoteLead,
						remoteLeadNote);
				remoteLead.getNoteList().add(remoteLeadNote);

				wasUpdated = true;
				crmNoteRecordId.setRemoteId(remoteLeadNote.getCrmId());
				crmNoteRecordId.setLastRemoteTimestamp(remoteLeadNote
						.getCrmLastUpdateTimestamp());
				crmNoteRecordId.setLastLocalTimestamp(syncRecord
						.getTimeStamp());
				this.crmRecordIdRepository.save(crmNoteRecordId);

			} else {
				// Otherwise get existing remote lead for update
				if (leadNote.getLeadNote().getTimeStamp()
						.after(crmNoteRecordId.getLastLocalTimestamp())) {
					CrmLeadDto remoteLead = cache.getRemoteLeadDataLookup()
							.get(crmRecordId.getRemoteId());
					for (LeadNoteDto note : remoteLead.getNoteList()) {
						CrmLeadNoteDto remoteLeadNoteDto = (CrmLeadNoteDto) note;
						if (remoteLeadNoteDto.getCrmId().equals(
								crmNoteRecordId.getRemoteId())) {
							remoteLeadNoteDto.getLeadNote()
									.setMessage(
											leadNote.getLeadNote()
													.getMessage());
							remoteLeadNoteDto.getLeadNote()
									.setSubject(
											leadNote.getLeadNote()
													.getSubject());
							remoteLeadNoteDto.getLeadNote()
									.setTimeStamp(
											syncRecord.getTimeStamp());

							log.info("Saving note with data "
									+ remoteLeadNoteDto);
							zohoFacade.updateLeadNote(
									getZohoAuthToken(crmLeadSettings),
									remoteLead, remoteLeadNoteDto);

							wasUpdated = true;
							crmNoteRecordId
									.setLastRemoteTimestamp(remoteLeadNoteDto
											.getCrmLastUpdateTimestamp());
							crmNoteRecordId
									.setLastLocalTimestamp(syncRecord
											.getTimeStamp());
							this.crmRecordIdRepository
									.save(crmNoteRecordId);

						}
					}
				}
			}
		}
		return wasUpdated;
	}

	private boolean synchronizeCrmLeadActivitiesFromLocalActivitys(
			CrmLeadSettings crmLeadSettings, CrmLeadSyncRecord syncRecord,
			CrmProcessCache cache, CrmRecordId crmRecordId, boolean wasUpdated,
			LeadDto localLead) {
		for (LeadActivityDto leadActivity : localLead.getActivityList()) {

			CrmRecordId crmActivityRecordId = this.crmRecordIdRepository
					.findByLocalIdAndCrmSourceAndCrmRecordType(
							leadActivity.getId(), CrmSystemType.Zoho,
							CrmRecordType.Activity);

			if (crmActivityRecordId.getRemoteId() == null) {
				CrmLeadDto remoteLead = cache.getRemoteLeadDataLookup()
						.get(crmRecordId.getRemoteId());
				// Create new remote activity
				CrmLeadActivityDto remoteLeadActivity = new CrmLeadActivityDto();
				remoteLeadActivity.setSubjectActivity(leadActivity.getSubjectActivity());
				remoteLeadActivity.setActivityDescription(leadActivity.getActivityDescription());
				remoteLeadActivity.setLeadActivityType(leadActivity.getLeadActivityType());
				remoteLeadActivity.setStartDate(leadActivity.getStartDate());
				remoteLeadActivity.setEndDate(leadActivity.getEndDate());
				remoteLeadActivity.setVenue(leadActivity.getVenue());
				
				remoteLeadActivity.setTimeStamp(
						syncRecord.getTimeStamp());

				zohoFacade.createLeadActivity(
						getZohoAuthToken(crmLeadSettings), remoteLead,
						remoteLeadActivity);
				remoteLead.getActivityList().add(remoteLeadActivity);

				wasUpdated = true;
				crmActivityRecordId.setRemoteId(remoteLeadActivity.getCrmId());
				crmActivityRecordId.setLastRemoteTimestamp(remoteLeadActivity
						.getCrmLastUpdateTimestamp());
				crmActivityRecordId.setLastLocalTimestamp(syncRecord
						.getTimeStamp());
				this.crmRecordIdRepository.save(crmActivityRecordId);

			} else {
				// Otherwise get existing remote activity for update
				if (leadActivity.getTimeStamp()
						.after(crmActivityRecordId.getLastLocalTimestamp())) {
					CrmLeadDto remoteLead = cache.getRemoteLeadDataLookup()
							.get(crmRecordId.getRemoteId());
					for (LeadActivityDto note : remoteLead.getActivityList()) {
						CrmLeadActivityDto remoteLeadActivityDto = (CrmLeadActivityDto) note;
						if (remoteLeadActivityDto.getCrmId().equals(
								crmActivityRecordId.getRemoteId())) {
							remoteLeadActivityDto.setSubjectActivity(leadActivity.getSubjectActivity());
							remoteLeadActivityDto.setActivityDescription(leadActivity.getActivityDescription());
							remoteLeadActivityDto.setLeadActivityType(leadActivity.getLeadActivityType());
							remoteLeadActivityDto.setStartDate(leadActivity.getStartDate());
							remoteLeadActivityDto.setEndDate(leadActivity.getEndDate());
							remoteLeadActivityDto.setVenue(leadActivity.getVenue());

							remoteLeadActivityDto.setTimeStamp(
									syncRecord.getTimeStamp());

							log.info("Saving activity with data "
									+ remoteLeadActivityDto);
							zohoFacade.updateLeadActivity(
									getZohoAuthToken(crmLeadSettings),
									remoteLead, remoteLeadActivityDto);

							wasUpdated = true;
							crmActivityRecordId
									.setLastRemoteTimestamp(remoteLeadActivityDto
											.getCrmLastUpdateTimestamp());
							crmActivityRecordId
									.setLastLocalTimestamp(syncRecord
											.getTimeStamp());
							this.crmRecordIdRepository
									.save(crmActivityRecordId);

						}
					}
				}
			}
		}
		return wasUpdated;
	}

	private long deleteCrmNotesNotInCrm(CrmLeadSettings crmLeadSettings,
			CrmProcessCache cache, long numberOfUpdatesFromBr) {
		List<CrmRecordId> crmExistingNotesRecordId = this.crmRecordIdRepository
				.findByOwnerAndCrmSourceAndCrmRecordType(
						crmLeadSettings.getOwner(), CrmSystemType.Zoho,
						CrmRecordType.Note);
		for (CrmRecordId record : crmExistingNotesRecordId) {
			if (record.getLocalId() != null) {
				LeadNoteDto localNote = cache.getLocalNoteDataLookup().get(
						record.getLocalId());
				if (localNote == null) {
					CrmLeadNoteDto note = cache.getRemoteNotesDataLookup().get(
							record.getRemoteId());
					if (note != null) {
						this.zohoFacade.deleteLeadNote(
								getZohoAuthToken(crmLeadSettings), note);
						numberOfUpdatesFromBr++;

						this.crmRecordIdRepository.delete(record.getId());

					}
				}
			}
		}
		return numberOfUpdatesFromBr;
	}

	private long deleteCrmActivitiesNotInLocal(CrmLeadSettings crmLeadSettings,
			CrmProcessCache cache, long numberOfUpdatesFromBr) {
		List<CrmRecordId> crmExistingActivitysRecordId = this.crmRecordIdRepository
				.findByOwnerAndCrmSourceAndCrmRecordType(
						crmLeadSettings.getOwner(), CrmSystemType.Zoho,
						CrmRecordType.Activity);
		for (CrmRecordId record : crmExistingActivitysRecordId) {
			if (record.getLocalId() != null) {
				LeadActivityDto localActivity = cache.getLocalActivityDataLookup().get(
						record.getLocalId());
				if (localActivity == null) {
					CrmLeadActivityDto activity = cache.getRemoteActivityDataLookup().get(
							record.getRemoteId());
					if (activity != null) {
						this.zohoFacade.deleteLeadActivity(
								getZohoAuthToken(crmLeadSettings), activity);
						numberOfUpdatesFromBr++;

						this.crmRecordIdRepository.delete(record.getId());

					}
				}
			}
		}
		return numberOfUpdatesFromBr;
	}

	public ZohoFacade getZohoFacade() {
		return zohoFacade;
	}

	public void setZohoFacade(ZohoFacade zohoFacade) {
		this.zohoFacade = zohoFacade;
	}

	public OrganizationManagementService getOrganizationManagementService() {
		return organizationManagementService;
	}

	public void setOrganizationManagementService(
			OrganizationManagementService organizationManagementService) {
		this.organizationManagementService = organizationManagementService;
	}

	public LeadManagementService getLeadManagementService() {
		return leadManagementService;
	}

	public void setLeadManagementService(
			LeadManagementService leadManagementService) {
		this.leadManagementService = leadManagementService;
	}

	public CrmLeadDto getRemoteLeadDtoForLead(CrmLeadSettings crmLeadSettings,
			LeadDto leadDto) {
		CrmRecordId crmRecordId = this.crmRecordIdRepository
				.findByLocalIdAndCrmSourceAndCrmRecordType(leadDto.getId(),
						CrmSystemType.Zoho, CrmRecordType.Lead);

		return this.zohoFacade.getLead(getZohoAuthToken(crmLeadSettings),
				crmRecordId.getRemoteId());

	}

	public CrmIntegrationService getCrmIntegrationService() {
		return crmIntegrationService;
	}

	public void setCrmIntegrationService(
			CrmIntegrationService crmIntegrationService) {
		this.crmIntegrationService = crmIntegrationService;
	}

	private class CrmProcessCache {
		private Map<String, CrmLeadDto> remoteLeadDataLookup = new HashMap<String, CrmLeadDto>();
		private Map<String, CrmLeadNoteDto> remoteNotesDataLookup = new HashMap<String, CrmLeadNoteDto>();
		private Map<String, CrmLeadActivityDto> remoteActivityDataLookup = new HashMap<String, CrmLeadActivityDto>();

		private Map<Long, LeadDto> localLeadDataLookup = new HashMap<Long, LeadDto>();
		private Map<Long, LeadNoteDto> localNoteDataLookup = new HashMap<Long, LeadNoteDto>();
		private Map<Long, LeadActivityDto> localActivityDataLookup = new HashMap<Long, LeadActivityDto>();

		public CrmProcessCache() {

		}

		public Map<String, CrmLeadDto> getRemoteLeadDataLookup() {
			return remoteLeadDataLookup;
		}

		public Map<String, CrmLeadNoteDto> getRemoteNotesDataLookup() {
			return remoteNotesDataLookup;
		}

		public Map<String, CrmLeadActivityDto> getRemoteActivityDataLookup() {
			return remoteActivityDataLookup;
		}

		public Map<Long, LeadDto> getLocalLeadDataLookup() {
			return localLeadDataLookup;
		}

		public Map<Long, LeadNoteDto> getLocalNoteDataLookup() {
			return localNoteDataLookup;
		}

		public Map<Long, LeadActivityDto> getLocalActivityDataLookup() {
			return localActivityDataLookup;
		}
	}
}
