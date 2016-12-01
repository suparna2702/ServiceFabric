package com.similan.domain.repository.event.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.event.MemberLoginEvent;

public interface JpaMemberLoginEventRepository 
       extends JpaRepository<MemberLoginEvent, Long>{
	
	@Query("select loginEvent from MemberLoginEvent loginEvent where loginEvent.loginInfo.webSessionId=:sessionId")
	public MemberLoginEvent findEventBySessionId(@Param("sessionId")String sessionId);
	
	@Query("select loginEvent from MemberLoginEvent loginEvent where loginEvent.eventGenerator=:memId")
	public List<MemberLoginEvent> findEventsByMember(@Param("memId")Long memId);
	
	@Query("select loginEvent from MemberLoginEvent loginEvent where loginEvent.loginInfo.logoutTime = null")
	public List<MemberLoginEvent> findLoggerInMemberEvents();
	
	@Query("select count(loginEvent) from MemberLoginEvent loginEvent where (loginEvent.eventGenerationTime between :fromDate and :toDate)")
	public Long getLoginEventCountInDateRange(@Param("fromDate")Date fromDate, @Param("toDate")Date toDate);

}
