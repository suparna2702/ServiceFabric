package com.similan.portal.model;

import java.io.Serializable;
import java.util.List;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.product.dto.basic.ProductDto;
import com.similan.service.api.product.dto.basic.ProductFeatureDto;
import com.similan.service.api.product.dto.basic.ProductTagDto;

public class ProductDetailModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProductDto product = null;
	
	private List<ProductFeatureDto> features = null;
	
	private List<ProductTagDto> tags = null;
	
	private List<DocumentDto> documents = null;
	
	public String getImageLocation(){
		return PhotoViewOption.ShowPhoto.effectivePhoto("/images/businessLogo.jpg", 
				product.getImageLocation());
	}

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

	public List<ProductFeatureDto> getFeatures() {
		return features;
	}

	public void setFeatures(List<ProductFeatureDto> features) {
		this.features = features;
	}

	public List<ProductTagDto> getTags() {
		return tags;
	}

	public void setTags(List<ProductTagDto> tags) {
		this.tags = tags;
	}

	public List<DocumentDto> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDto> documents) {
		this.documents = documents;
	}
	
	

}
