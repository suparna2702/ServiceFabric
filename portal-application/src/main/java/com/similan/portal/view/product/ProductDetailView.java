package com.similan.portal.view.product;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.product.dto.basic.ProductDto;
import com.similan.service.api.product.dto.basic.ProductFeatureDto;
import com.similan.service.api.product.dto.basic.ProductTagDto;
import com.similan.service.api.product.dto.key.ProductKey;

@Scope("request")
@Component("productDetailView")
@Slf4j
public class ProductDetailView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private ProductDto product;
	
	private List<ProductFeatureDto> featureList;
	
	private List<ProductTagDto> tagList;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	@PostConstruct
	public void init() {
		String productName = this.getContextParam("pname");
		log.info("Product detail view id " + productName);
		
		if(productName != null){
			SocialActorKey orgKey = this.getOrgKey(orgInfo);
			ProductKey prodKey = new ProductKey(orgKey, productName);
			product = this.getProductService().get(prodKey);
			//featureList = this.getProductService().getProductFeatures(prodKey);
			tagList = this.getProductService().getTags(prodKey);
		}
	}
	
	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}
	
	public List<ProductTagDto> getTagList() {
		return tagList;
	}

	public List<ProductFeatureDto> getFeatureList() {
		return featureList;
	}

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

}
