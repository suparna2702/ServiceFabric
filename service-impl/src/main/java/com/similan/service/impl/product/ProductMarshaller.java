package com.similan.service.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.product.Product;
import com.similan.domain.entity.product.ProductFeature;
import com.similan.domain.entity.product.ProductTag;
import com.similan.domain.repository.product.ProductRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.product.dto.basic.ProductDto;
import com.similan.service.api.product.dto.basic.ProductFeatureDto;
import com.similan.service.api.product.dto.basic.ProductTagDto;
import com.similan.service.api.product.dto.key.ProductFeatureKey;
import com.similan.service.api.product.dto.key.ProductKey;
import com.similan.service.api.product.dto.key.ProductTagKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Component
public class ProductMarshaller extends Marshaller {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  public ProductDto marshallProduct(Product product) {
    ProductKey productKey = this.marshallProductKey(product);

    ProductDto productDto = new ProductDto(productKey, product.getDescription(),
        product.getProductNodeCode(), product.getImageLocation(),
        product.getProductNodeType(), product.getDomainId());

    return productDto;
  }

  public ProductKey marshallProductKey(Product product) {
    SocialActor owner = product.getOwner();
    String name = product.getName();
    SocialActorKey ownerKey = actorMarshaller
        .marshallActorKey(owner);
    ProductKey productKey = new ProductKey(ownerKey, name);
    return productKey;
  }

  public Product unmarshallProductKey(ProductKey prodKey) {
    SocialOrganization owner = (SocialOrganization) actorMarshaller
        .unmarshallActorKey(prodKey.getOwner(), true);
    Product product = this.productRepository.find(prodKey.getName(), owner);
    return product;
  }

  public List<ProductDto> marshallProducts(List<Product> products) {
    List<ProductDto> productDtos = new ArrayList<ProductDto>();
    for (Product product : products) {
      ProductDto productDto = this.marshallProduct(product);
      productDtos.add(productDto);
    }

    return productDtos;
  }

  public ProductFeatureKey marshallFeatureKey(ProductFeature prodFeature) {
    ProductKey productKey = this.marshallProductKey(prodFeature.getOwner());
    ProductFeatureKey featureKey = new ProductFeatureKey(productKey,
        prodFeature.getDomainId());
    featureKey.setId(prodFeature.getId());
    
    return featureKey;
  }

  public List<ProductFeatureDto> marshallFeatures(
      List<ProductFeature> featureList) {
    if (featureList == null) {
      throw new IllegalArgumentException("Product feature list cannot be NULL ");
    }

    List<ProductFeatureDto> featureDtoList = new ArrayList<ProductFeatureDto>();
    for (ProductFeature prodFeature : featureList) {
      ProductFeatureDto featureDto = marshallFeature(prodFeature);
      featureDtoList.add(featureDto);
    }

    return featureDtoList;
  }

  public ProductFeatureDto marshallFeature(ProductFeature prodFeature) {
    ProductFeatureKey featureKey = this.marshallFeatureKey(prodFeature);
    ProductFeatureDto featureDto = new ProductFeatureDto(featureKey,
    		prodFeature.getName(), prodFeature.getDescription());
 
    return featureDto;
  }

  @Deprecated
  public ProductTagKey marshallTagKey(ProductTag productTag) {
    ProductKey prodKey = this.marshallProductKey(productTag.getProduct());
    ProductTagKey tagKey = new ProductTagKey(prodKey, productTag.getName());
    tagKey.setId(productTag.getId());
    
    return tagKey;
  }

  @Deprecated
  public ProductTagDto marshallTag(ProductTag prodTag) {
    ProductTagKey tagKey = marshallTagKey(prodTag);
    ProductTagDto tagDto = new ProductTagDto(tagKey);
    return tagDto;
  }

  @Deprecated
  public List<ProductTagDto> marshallTags(List<ProductTag> tags) {
    List<ProductTagDto> tagDtos = new ArrayList<ProductTagDto>();
    for (ProductTag tag : tags) {
      ProductTagDto tagDto = this.marshallTag(tag);
      tagDtos.add(tagDto);
    }

    return tagDtos;
  }

}
