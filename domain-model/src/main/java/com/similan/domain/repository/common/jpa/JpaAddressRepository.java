package com.similan.domain.repository.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.common.Address;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
	
}
