package com.similan.domain.repository.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.repository.account.jpa.JpaBusinessAccountTypeRepository;

@Repository
public class BusinessAccountTypeRepository {
  @Autowired
  private JpaBusinessAccountTypeRepository businessAccountTypeRepository;
  
  public List<BusinessAccountType> findAll(){
    return this.businessAccountTypeRepository.findAll();
  }
  
  public BusinessAccountType findOne(Long id){
    return this.businessAccountTypeRepository.findOne(id);
  }
  
  public BusinessAccountType save(BusinessAccountType accType){
    return this.businessAccountTypeRepository.save(accType);
  }
  
  public BusinessAccountType findByName(String name){
    return this.businessAccountTypeRepository.findByName(name);
  }

}
