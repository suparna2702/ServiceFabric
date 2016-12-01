package com.similan.domain.repository.product.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.product.Product;
import com.similan.domain.entity.product.ProductTag;

public interface JpaProductTagRepository extends JpaRepository<ProductTag, Long>{
	
	@Query("select productTag from ProductTag productTag where productTag.product=:product")
	public List<ProductTag> findByProduct(@Param("product")Product product);

}
