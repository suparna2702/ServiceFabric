package com.similan.portal.view.product;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.portal.model.ProductDetailModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.product.dto.basic.ProductDto;
import com.similan.service.api.product.dto.basic.ProductFeatureDto;
import com.similan.service.api.product.dto.basic.ProductTagDto;

@Scope("view")
@Component("productHomeView")
@Slf4j
public class ProductManagementHomeView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	protected List<ProductDetailModel> productList = null;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	@PostConstruct
	public void init() {
		log.info("Product management home view " + orgInfo.getId());
		
		/* get the products */
		SocialActorKey orgKey = this.getOrgKey(orgInfo);
		List<ProductDto> products = this.getProductService().getForOwner(orgKey);
		if(products != null){
			this.productList = new ArrayList<ProductDetailModel>();
			for(ProductDto prod : products){
				List<ProductTagDto> tags = this.getProductService()
						.getTags(prod.getKey());
				List<ProductFeatureDto> features = this.getProductService()
						.getFeatures(prod.getKey());
				
				ProductDetailModel productModel = new ProductDetailModel();
				productModel.setProduct(prod);
				productModel.setTags(tags);
				productModel.setFeatures(features);
				this.productList.add(productModel);
			}
		}
		
	}

	public List<ProductDetailModel> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDetailModel> productList) {
		this.productList = productList;
	}
}
