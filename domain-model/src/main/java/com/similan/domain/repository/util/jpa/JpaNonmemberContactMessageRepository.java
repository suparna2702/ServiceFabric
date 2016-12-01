package com.similan.domain.repository.util.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.util.NonmemberContactMessage;

public interface JpaNonmemberContactMessageRepository 
                          extends JpaRepository<NonmemberContactMessage, Long>{

}
