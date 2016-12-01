package com.similan.framework.manager;

import java.util.List;

public class ProductCatalogueImportInfo {
	
	private List<ProductCatalogueInfo> productCatalogueList;
	
	private List<ProductInfo> productInfoList;

	public List<ProductCatalogueInfo> getProductCatalogueList() {
		return productCatalogueList;
	}

	public void setProductCatalogueList(
			List<ProductCatalogueInfo> productCatalogueList) {
		this.productCatalogueList = productCatalogueList;
	}

	public List<ProductInfo> getProductInfoList() {
		return productInfoList;
	}

	public void setProductInfoList(List<ProductInfo> productInfoList) {
		this.productInfoList = productInfoList;
	}

}
