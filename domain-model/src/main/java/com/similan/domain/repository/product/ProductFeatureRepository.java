package com.similan.domain.repository.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.product.Product;
import com.similan.domain.entity.product.ProductFeature;
import com.similan.domain.repository.product.jpa.JpaProductFeatureRepository;

@Repository
public class ProductFeatureRepository {
  @Autowired
  private JpaProductFeatureRepository repository;
	
	public ProductFeature save(ProductFeature feature) {
    return repository.save(feature);
  }
	
	public List<ProductFeature> getByProduct(Product product) {
    return repository.getByProduct(product);
  }
	
	public void delete(Long id){
	  repository.delete(id);
	}
	
	public ProductFeature create(String name, String description, Product owner){
		ProductFeature prodFeature = new ProductFeature();
		prodFeature.setName(name);
		prodFeature.setDescription(description);
		prodFeature.setOwner(owner);
		prodFeature.setDomainId(UUID.randomUUID().toString());
		
		return prodFeature;
	}

}
