package com.similan.adminapp.controller;

import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.similan.framework.manager.ProductCatalogueImportInfo;
import com.similan.framework.manager.ProductCatalogueManager;

@Controller
@Slf4j
public class ProductController {
  @Autowired
  private ProductCatalogueManager catalogueManager;
	
	@RequestMapping("/importProducts")
	public String importProducts(HttpServletRequest request, ModelMap model)	{
		
		return "importProducts";
	}
	
	@RequestMapping("/viewProducts")
	public String viewProducts(HttpServletRequest request, ModelMap model)	{
		
		return "viewProducts";
	}
	
	@Transactional
	@RequestMapping("/unmarshallProducts")
	public String unmarshalProduct(HttpServletRequest request, ModelMap model, Object command)	{
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile fileProductCatalogue = multipartRequest.getFile("fileProductCatalogue");
		MultipartFile fileProduct = multipartRequest.getFile("fileProduct");
		
		if (fileProductCatalogue != null && fileProduct != null) {
			
			try {
				
				InputStreamReader streamReaderProductCatalogue = new InputStreamReader(fileProductCatalogue.getInputStream());
				InputStreamReader streamReaderProduct = new InputStreamReader(fileProduct.getInputStream());
				ProductCatalogueImportInfo productCatalogueList = this.catalogueManager.importProductCatalogueAndProduct(streamReaderProductCatalogue, streamReaderProduct);
				Assert.notNull(productCatalogueList);
				model.addAttribute("productCatalogueImportInfo", productCatalogueList);
			}
			catch(Exception exp){
				log.error("Error while importing product ", exp);
			}
		}
		
		return "importProducts";
	}

}
