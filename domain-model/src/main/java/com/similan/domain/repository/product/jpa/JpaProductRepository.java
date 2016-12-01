package com.similan.domain.repository.product.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.product.Product;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
	
	 @Query("select product from Product product where (product.owner=:owner)"
	 		+ " and (product.name=:name)")
	 public Product find(@Param("name")String name, @Param("owner")SocialOrganization owner);
	 
	 @Query("select product from Product product where product.owner=:owner")
	 public abstract List<Product> findProducts(@Param("owner")SocialOrganization owner);

}
