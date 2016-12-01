package com.similan.domain.repository.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.product.Product;
import com.similan.domain.repository.product.jpa.JpaProductRepository;
import com.similan.service.api.product.ProductNodeType;

@Repository
public class ProductRepository {
  @Autowired
  private JpaProductRepository repository;
	
	public Product save(Product productNode) {
    return repository.save(productNode);
  }
	
	public void delete(Product productNode) {
    repository.delete(productNode);
  }
	
  public Product findOne(Long id) {
    return repository.findOne(id);
  }
    
  public Product find(String name, SocialOrganization owner) {
    return repository.find(name, owner);
  }
    
	public List<Product> findProducts(SocialOrganization orgOwner) {
    return repository.findProducts(orgOwner);
  }

	public Product create(SocialActor owner, String name, String description, 
			String productNodeCode, ProductNodeType nodeType){
		Product product = new Product();
		product.setDescription(description);
		product.setName(name);
		product.setOwner(owner);
		product.setProductNodeCode(productNodeCode);
		product.setProductNodeType(nodeType);
		
		return product;
	}

}
