package com.similan.domain.repository.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.product.Product;
import com.similan.domain.entity.product.ProductTag;
import com.similan.domain.repository.product.jpa.JpaProductTagRepository;

@Repository
public class ProductTagRepository {
  @Autowired
  private JpaProductTagRepository repository;
	
	public ProductTag save(ProductTag tag) {
    return repository.save(tag);
  }
	
	public List<ProductTag> findByProduct(Product product) {
    return repository.findByProduct(product);
  }
	
	public void delete(Long id){
	  repository.delete(id);
	}
	
	public ProductTag create(String name, Product product){
		ProductTag prodTag = new ProductTag();
		prodTag.setName(name);
		prodTag.setProduct(product);
		return prodTag;
	}

}
