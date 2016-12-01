package com.similan.framework.manager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import com.similan.framework.importexport.csv.CsvJavaBeanImporter;

@Component
public class ProductCatalogueManager {
	/**
	 * 
	 * @param streamReaderCatalogue
	 * @param streamReaderProduct
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	public ProductCatalogueImportInfo importProductCatalogueAndProduct(InputStreamReader streamReaderCatalogue, 
			                                     InputStreamReader streamReaderProduct) throws IOException, 
			                                     InstantiationException, IllegalAccessException, 
			                                     InvocationTargetException, NoSuchMethodException, 
			                                     ClassNotFoundException {
		
		ProductCatalogueImportInfo catalogueImportInfo = new ProductCatalogueImportInfo();
		
		/* import from CSV and save in DB */
/*		List<Object> productCatalogueList = this.productCatalogueListImporter.importFromCsv(streamReaderCatalogue);
		Assert.assertTrue(productCatalogueList != null);
		
		 Put them in a Map and then build the Tree 
		Map<String, ProductCatalogue> catalogueMap = new HashMap<String, ProductCatalogue>();
		List<ProductCatalogue> catalogueTree =  new ArrayList<ProductCatalogue>();
		
		 create the map 
		for(Object catalogueObj : productCatalogueList){
			ProductCatalogue catalogue = (ProductCatalogue)catalogueObj;
			
			if(catalogueMap.containsKey(catalogue.getCategoryCode()) != true){
				catalogueMap.put(catalogue.getCategoryCode(), catalogue);
				catalogueTree.add(catalogue);
			}
		}
		
		 build the tree where we only associate parent 
		for(ProductCatalogue catalogue : catalogueTree){
			
			 Get the parent from Map and associate 
			if(catalogueMap.containsKey(catalogue.getParentCategoryCode()) == true){
				
				ProductCatalogue parentCatalogue = catalogueMap.get(catalogue.getParentCategoryCode());
				catalogue.setParent(parentCatalogue);
				
				if(parentCatalogue.getChildCatalogues() == null){
					Set<ProductCatalogue> childCatalogues = new HashSet<ProductCatalogue>();
					parentCatalogue.setChildCatalogues(childCatalogues);
				}
				
				parentCatalogue.getChildCatalogues().add(catalogue);
			}
		}
		
		 Save it to DB 
		this.productCatalogueRepo.save(catalogueTree);
		
		 import from CSV and save in DB 
		List<Object> productList = this.productListImporter.importFromCsv(streamReaderProduct);
		Assert.assertTrue(productList != null);
		
		 Put them in a Map and then build the Tree 
		List<Product> productTree =  new ArrayList<Product>();
		
		 create the map 
		for(Object productObj : productList){
			Product product = (Product)productObj;
			productTree.add(product);
			
			 put it into catalogue also 
			if(catalogueMap.containsKey(product.getCategoryCode()) == true){
				ProductCatalogue productCatalogue = catalogueMap.get(product.getCategoryCode());
				
				if(productCatalogue.getProducts() == null){
					List<Product> catalogueProducts = new ArrayList<Product>();
					productCatalogue.setProducts(catalogueProducts);
				}
				
				productCatalogue.getProducts().add(product);
			}
		}
		
		 Now save products and then Update catalogues 
		this.productRepo.save(productTree);
		this.productCatalogueRepo.save(catalogueTree);
		
		 Now create the return 
		List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
		
		for(Product product : productTree){
			ProductInfo productInfo = new ProductInfo();
			productInfo.setId(product.getId());
			productInfo.setName(product.getName());
			productInfo.setCategoryCode(product.getCategoryCode());
			productInfo.setDescription(product.getDescription());
			productInfo.setProductCode(product.getProductCode());
			
			productInfoList.add(productInfo);
		}
		
		catalogueImportInfo.setProductInfoList(productInfoList);
		
		 Now put the catalogue info 
		List<ProductCatalogueInfo> catalogueInfo = new ArrayList<ProductCatalogueInfo>();
		
		for(ProductCatalogue catalogue : catalogueTree){
			ProductCatalogueInfo productCatalogueInfo = new ProductCatalogueInfo();
			productCatalogueInfo.setId(catalogue.getId());
			productCatalogueInfo.setName(catalogue.getCatalogueName());
			productCatalogueInfo.setCategoryCode(catalogue.getCategoryCode());
			productCatalogueInfo.setDescription(catalogue.getDescription());
			productCatalogueInfo.setParentCategoryCode(catalogue.getParentCategoryCode());
			productCatalogueInfo.setSupplierCode(catalogue.getSupplierCode());
			
			catalogueInfo.add(productCatalogueInfo);
		}
		
		catalogueImportInfo.setProductCatalogueList(catalogueInfo);
*/		
		return catalogueImportInfo;
	}
}
