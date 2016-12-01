package com.similan.service.api.product;

import java.util.List;

import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentDto;
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

/**
 * 
 * @author suparna1108
 *
 */
public interface ProductService {
  
   public ProductDto create(ProductKey productKey, NewProductDto newProductDto,
		  SocialActorKey creator);
  
   public ProductDto update(ProductKey productKey, UpdateProductDto updateProductDto,
		  SocialActorKey updater);
  
   public ProductFeatureDto createFeature(ProductKey productKey,
      NewProductFeatureDto newFeatureDto);
  
   @Deprecated
   public ProductTagDto createTag(ProductTagKey tagKey,
      NewProductTagDto newTagDto);
  
   public List<ProductDto> getForOwner(SocialActorKey ownerKey);
  
   public ProductDto get(ProductKey productKey);
  
   public List<DocumentDto> getRelatedDocuments(ProductKey productKey);
  
   public List<ProductFeatureDto> getFeatures(ProductKey productKey);
  
   @Deprecated
   public List<ProductTagDto> getTags(ProductKey productKey);
  
   @Deprecated
   public void deleteTag(ProductTagKey tagKey);
  
   public void deleteFeature(ProductFeatureKey featureKey);

}
