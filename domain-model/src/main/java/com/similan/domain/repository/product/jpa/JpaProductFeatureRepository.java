package com.similan.domain.repository.product.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.product.Product;
import com.similan.domain.entity.product.ProductFeature;

public interface JpaProductFeatureRepository extends
		JpaRepository<ProductFeature, Long> {
	
	@Query("select productFeature from ProductFeature productFeature where productFeature.owner=:product")
	public abstract List<ProductFeature> getByProduct(@Param("product")Product product);

}
