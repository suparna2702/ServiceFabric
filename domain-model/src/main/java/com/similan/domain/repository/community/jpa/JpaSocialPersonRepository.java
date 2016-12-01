package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialPerson;

public interface JpaSocialPersonRepository extends JpaRepository<SocialPerson, Long> {
	
	@Query("select sp from SocialPerson sp where sp.primaryEmail=:email")
	SocialPerson findByEmail(@Param("email")String email);
	
	@Query("select sp.photoLocation from SocialPerson sp where sp.id=:id")
	String findPhotoById(@Param("id")Long id);
	
	@Query("select sp from SocialPerson sp where sp.primaryEmail=:email and sp.state=:state")
	SocialPerson findByEmailAndState(@Param("email")String email, @Param("state") MemberState state);

	@Query("select count(*) from SocialPerson")
	Long getTotalMemberCount();
	
	@Query("select count(*) from SocialPerson where state = :status")
	Long getTotalMemberCountByStatus(@Param("status")MemberState status);
	
	@Query("select sp.primaryEmail from SocialPerson sp")
	List<String> getAllMemberEmails();
	
}
