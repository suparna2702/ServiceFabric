package com.similan.domain.repository.poll.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.poll.PollEvent;

public interface JpaPollEventRepository extends JpaRepository<PollEvent, Long> {

  @Query("select pollEvent from PollEvent pollEvent where pollEvent.updateFor=:forActor and (pollEvent.responded=0 or pollEvent.responded is null)")
  public List<PollEvent> findByFor(@Param("forActor") Long forActor);

  @Query("select pollEvent from PollEvent pollEvent where pollEvent.updateFrom=:fromActor and "
      + "(pollEvent.pollid=:pollId) and (pollEvent.responded=0 or pollEvent.responded is null)")
  public List<PollEvent> findByFrom(@Param("fromActor") Long fromActor,
      @Param("pollId") Long pollId);

  @Query("select pollEvent from PollEvent pollEvent where pollEvent.pollid=:templateId")
  public List<PollEvent> findByPollTemplate(@Param("templateId") Long templateId);

  @Query("select count(pollEvent) from PollEvent pollEvent where pollEvent.pollid=:templateId")
  public Long getPollEventCount(@Param("templateId") Long templateId);

  @Query("select count(pollEvent) from PollEvent pollEvent where (pollEvent.pollDueDate "
      + "between :fromDate and :toDate)")
  public Long getPollEventCountInDateRange(@Param("fromDate") Date fromDate,
      @Param("toDate") Date toDate);

  @Query("select count(pollEvent) from PollEvent pollEvent where pollEvent.updateFor=:forActor")
  public Long findPollEventCountFor(@Param("forActor") Long forActor);

  @Query("select count(pollEvent) from PollEvent as pollEvent, PollTemplate as pollTemplate where "
      + "(pollEvent.pollDueDate between :fromDate and :toDate) "
      + "and (pollTemplate.pollOwner.id=:forOrg) "
      + "and (pollTemplate.id=pollEvent.pollid)")
  public Long getPollEventCountInDateRange(@Param("fromDate") Date fromDate,
      @Param("toDate") Date toDate, @Param("forOrg") Long forOrg);
}
