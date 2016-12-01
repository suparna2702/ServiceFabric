package com.similan.service.impl.product;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.product.Product;
import com.similan.domain.entity.product.ProductFeature;
import com.similan.domain.entity.product.ProductTag;
import com.similan.domain.repository.product.ProductFeatureRepository;
import com.similan.domain.repository.product.ProductRepository;
import com.similan.domain.repository.product.ProductTagRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.product.ProductService;
import com.similan.service.api.product.dto.basic.ProductDto;
import com.similan.service.api.product.dto.basic.ProductFeatureDto;
import com.similan.service.api.product.dto.basic.ProductTagDto;
import com.similan.service.api.product.dto.key.ProductFeatureKey;
import com.similan.service.api.product.dto.key.ProductKey;
import com.similan.service.api.product.dto.key.ProductTagKey;
import com.similan.service.api.product.dto.operation.NewProductDto;
import com.similan.service.api.product.dto.operation.NewProductFeatureDto;
import com.similan.service.api.product.dto.operation.NewProductTagDto;
import com.similan.service.api.product.dto.operation.UpdateProductDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;

@Service
public class ProductServiceImpl extends ServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ProductFeatureRepository productFeatureRepository;
  @Autowired
  private ProductTagRepository productTagRepository;
  @Autowired
  private ProductMarshaller productMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

	@Override
	@Transactional
	public ProductDto create(ProductKey productKey,  NewProductDto newProductDto,
			  SocialActorKey creator) {
		
		if(newProductDto == null){
			throw new IllegalArgumentException("NewProductDto Cannot be null ");
		}
		
		if(productKey.getOwner() == null){
			throw new IllegalArgumentException("Product owner Cannot be null ");
		}
		
		if(creator == null){
			throw new IllegalArgumentException("Product creator Cannot be null ");
		}
		
		if(StringUtils.isEmpty(productKey.getName())){
			throw new IllegalArgumentException("Product name Cannot be null ");
		}
		
		if(newProductDto.getProductType() == null){
			throw new IllegalArgumentException("Product type Cannot be null ");
		}
		
        /* get the owner */
		SocialActor owner = actorMarshaller
				 .unmarshallActorKey(productKey.getOwner(), true);
		if(!(owner instanceof SocialOrganization)){
			throw new IllegalArgumentException("Product owner can only be SocialOrganization ");
		}
		
		Product product = this.productRepository.create(owner, productKey.getName(), newProductDto.getDescription(), 
				newProductDto.getProductNodeCode(), newProductDto.getProductType());
		this.productRepository.save(product);
		ProductDto productDto = productMarshaller
				.marshallProduct(product);
		
		return productDto;
	}
	
	  @Override
	  @Transactional
	  public ProductDto update(ProductKey productKey, UpdateProductDto updateProductDto,
			  SocialActorKey updater){
		  
		  if(productKey.getOwner() == null){
				throw new IllegalArgumentException("Product owner Cannot be null ");
		  }
		  
		  if(updater == null){
				throw new IllegalArgumentException("Product updator Cannot be null ");
		  }
		  
		  if(StringUtils.isEmpty(productKey.getName())){
				throw new IllegalArgumentException("Product name Cannot be null ");
		  }
		  
		  Product product = productMarshaller
		                       .unmarshallProductKey(productKey);
		  
		  product.setDescription(updateProductDto.getDescription());
		  product.setImageLocation(updateProductDto.getImageLocation());
		  this.productRepository.save(product);
		  ProductDto productDto = productMarshaller
					.marshallProduct(product);
		  
		  return productDto;
	  }
	
	private void validateProductKey(ProductKey productKey){
		if(productKey == null){
			throw new IllegalArgumentException("Product key cannot be NULL");
		}
		
		if(productKey.getOwner() == null){
			throw new IllegalArgumentException("Product owner cannot be NULL");
		}
		
		if(StringUtils.isEmpty(productKey.getName())){
			throw new IllegalArgumentException("Product name cannot be NULL");
		}
		
	}

	@Override
	@Transactional
	public List<ProductDto> getForOwner(SocialActorKey actorKey) {
		if(actorKey == null){
			throw new IllegalArgumentException("Actor key cannot be NULL");
		}
		
		SocialActor actor = actorMarshaller
				.unmarshallActorKey(actorKey, true);
		if(!(actor instanceof SocialOrganization)){
			throw new IllegalArgumentException("Product owner can only be SocialOrganization ");	
		}
		SocialOrganization orgOwner = (SocialOrganization)actor;
		List<Product> products = this.productRepository.findProducts(orgOwner);
		List<ProductDto> prodList = productMarshaller
				.marshallProducts(products);
		return prodList;
	}

	@Override
	@Transactional
	public ProductDto get(ProductKey productKey) {
		validateProductKey(productKey);
		
		Product product = productMarshaller
				.unmarshallProductKey(productKey);
		ProductDto productDto = productMarshaller
				.marshallProduct(product);
		return productDto;
	}

	@Override
	@Transactional
	public List<DocumentDto> getRelatedDocuments(ProductKey productKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<ProductFeatureDto> getFeatures(ProductKey productKey) {
		Product product = productMarshaller
				.unmarshallProductKey(productKey);
		List<ProductFeature> featureList = this.productFeatureRepository.getByProduct(product);
		List<ProductFeatureDto> retFeatureList = productMarshaller
				.marshallFeatures(featureList);
		return retFeatureList;
	}

	@Override
	@Transactional
	@Deprecated
	public List<ProductTagDto> getTags(ProductKey productKey) {
		Product product = productMarshaller
				.unmarshallProductKey(productKey);
		List<ProductTag> tags = this.productTagRepository.findByProduct(product);
		List<ProductTagDto> tagDtos = productMarshaller
				.marshallTags(tags);
		return tagDtos;
	}

	@Override
	@Transactional
	public ProductFeatureDto createFeature(ProductKey productKey, NewProductFeatureDto newFeatureDto) {
		Product product = productMarshaller
				.unmarshallProductKey(productKey);
		ProductFeature prodFeature = this.productFeatureRepository
				.create(newFeatureDto.getName(), newFeatureDto.getDescription(), product);
		this.productFeatureRepository.save(prodFeature);
		ProductFeatureDto productFeatureDto = productMarshaller
				.marshallFeature(prodFeature);
		
		return productFeatureDto;
	}

	@Override
	@Transactional
	@Deprecated
	public ProductTagDto createTag(ProductTagKey tagKey, NewProductTagDto newTagDto) {
		if(newTagDto == null){
			throw new IllegalArgumentException("Input parameter cannot be NULL ");
		}
		
		if(tagKey.getProduct() == null){
			throw new IllegalArgumentException("Product cannot be NULL ");
		}
		
		if(StringUtils.isEmpty(tagKey.getName())){
			throw new IllegalArgumentException("Tag cannot be NULL ");
		}
		
		Product product = productMarshaller
				.unmarshallProductKey(tagKey.getProduct());
		ProductTag tag = this.productTagRepository.create(tagKey.getName(), product);
		this.productTagRepository.save(tag);
		ProductTagDto tagDto = productMarshaller
				.marshallTag(tag);
		return tagDto;
	}

  @Override
  @Transactional
  @Deprecated
  public void deleteTag(ProductTagKey tagKey) {
    productTagRepository.delete(tagKey.getId());
    
  }

  @Override
  @Transactional
  public void deleteFeature(ProductFeatureKey featureKey) {
    productFeatureRepository.delete(featureKey.getId());
    
  }

}
