package com.similan.adminapp.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

import com.similan.framework.dto.member.MemberInfoDto;

@Slf4j
public class MemberInfoSelectionModel extends ListDataModel<MemberInfoDto> 
                                              implements SelectableDataModel<MemberInfoDto>, Serializable{

	private static final long serialVersionUID = 1L;
			
	public MemberInfoSelectionModel(){
		
	}
	
	public MemberInfoSelectionModel(List<MemberInfoDto> memberList){
		super(memberList);
	}

	@SuppressWarnings("unchecked")
	public MemberInfoDto getRowData(String memberId) {
		
		log.info("Member to be fetched " + memberId);
		
		List<MemberInfoDto> memberInfoList = (List<MemberInfoDto>)getWrappedData();
		log.info("Member list " + memberInfoList);
		
		for(MemberInfoDto memberInfo : memberInfoList){
			
		   String id = memberInfo.getId().toString();
		   log.info("Member id to be compared " + id);
		   
		   if(id.contentEquals(memberId) == true){
			   return memberInfo;
		   }
		}
		
		return null;
	}

	public Object getRowKey(MemberInfoDto memberInfo) {
		return memberInfo.getId().toString();
	}

}
