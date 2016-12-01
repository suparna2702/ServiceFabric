package com.similan.domain.repository.event;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.event.MemberLoginEvent;
import com.similan.domain.repository.event.jpa.JpaMemberLoginEventRepository;

@Repository
public class MemberLoginEventRepository {
  @Autowired
  private JpaMemberLoginEventRepository repository;
	
	public MemberLoginEvent save(MemberLoginEvent message) {
    return repository.save(message);
  }
	
	public MemberLoginEvent findEventBySessionId(String sessionId) {
    return repository.findEventBySessionId(sessionId);
  }
	
	public List<MemberLoginEvent> findAll() {
    return repository.findAll();
  }
	
	public List<MemberLoginEvent> findEventsByMember(Long memId) {
    return repository.findEventsByMember(memId);
  }
	
	public List<MemberLoginEvent> findLoggerInMemberEvents() {
    return repository.findLoggerInMemberEvents();
  }
	
	public Long getLoginEventCountInDateRange(Date fromDate, Date toDate) {
    return repository.getLoginEventCountInDateRange(fromDate, toDate);
  }
	
	public MemberLoginEvent create(){
		MemberLoginEvent loginEvent = 
				new MemberLoginEvent();
		return loginEvent;
	}


}
