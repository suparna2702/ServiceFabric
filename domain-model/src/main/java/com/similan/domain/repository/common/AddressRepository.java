package com.similan.domain.repository.common;

import java.util.List;

import org.opengis.geometry.coordinate.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.common.Address;
import com.similan.domain.repository.common.jpa.JpaAddressRepository;

@Repository
public class AddressRepository {
  @Autowired
  private JpaAddressRepository repository;

  public Address save(Address address) {
    return repository.save(address);
  }
  
  public Address findOne(Long id) {
    return repository.findOne(id);
  }
  
  public List<Address> findAll() {
    return repository.findAll();
  }

  public void delete(Long addrId) {
    repository.delete(addrId);
  }

  public Address create(String name, String type, Position position,
      String number, String street, String city, String area, String county,
      String state, String zipCode, String country, String formattedAddress,
      String searchableAddress) {
    return new Address(name, type, position, number, street, city, area,
            county, state, zipCode, country, formattedAddress,
            searchableAddress);
  }

}
