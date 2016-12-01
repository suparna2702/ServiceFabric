package com.similan.domain.repository.poll.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.poll.PollSubmission;

public interface JpaPollSubmissionRepository extends
    JpaRepository<PollSubmission, Long> {

  @Query("select pollSubmission from PollSubmission pollSubmission where pollSubmission.pollTemplateId=:templateId")
  public List<PollSubmission> findByPollTemplate(
      @Param("templateId") Long templateId);

  @Query("select count(pollSubmission) from PollSubmission pollSubmission where pollSubmission.pollTemplateId=:templateId")
  public Long getPollSubmissionEventCount(@Param("templateId") Long templateId);

  @Query("select count(pollSubmission) from PollSubmission pollSubmission where (pollSubmission.timeStamp "
      + "between :fromDate and :toDate)")
  public Long getPollSubmissionEventCountInDateRange(
      @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

  /**
   * @param fromDate
   * @param toDate
   * @param forOrg
   * @return
   */
  @Query("select count(pollSubmission) from PollSubmission as pollSubmission, PollTemplate as pollTemplate where "
      + "(pollSubmission.timeStamp between :fromDate and :toDate) "
      + "and (pollTemplate.pollOwner.id=:forOrg) "
      + "and (pollTemplate.id=pollSubmission.pollTemplateId)")
  public abstract Long getPollSubmissionEventCountInDateRange(
      @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
      @Param("forOrg") Long forOrg);

}
