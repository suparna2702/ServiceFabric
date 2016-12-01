package com.similan.portal.view.product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageDeletion;
import com.similan.portal.view.handler.ImageUpload;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.product.ProductNodeType;
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

@Scope("view")
@Component("productView")
@Slf4j
public class ProductView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  MemberInfoDto memberInfo;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private String productLogo = IMAGES_PRODUCT_DEFAULT_LOGO;

  private String name = StringUtils.EMPTY;

  private String productNodeCode = StringUtils.EMPTY;

  private String description = StringUtils.EMPTY;

  private Boolean productLogoExists = false;

  private ProductNodeType productNodeType;

  private String productFeatureToAdd = StringUtils.EMPTY;

  private String productFeatureDescriptionToAdd = StringUtils.EMPTY;

  private String productTagToAdd = StringUtils.EMPTY;

  private PhotoViewOption photoViewOption = PhotoViewOption.ShowPhoto;

  private ProductDto product = null;

  private String domainId = UUID.randomUUID().toString();

  private List<ProductFeatureDto> features = null;

  private List<ProductTagDto> tags = null;

  @PostConstruct
  public void init() {

    log.info("Initializing product view ");

    String productName = this.getContextParam("pname");
    log.info("Product edit view id " + productName);

    if (productName != null) {
      SocialActorKey orgKey = this.getOrgKey(orgInfo);
      ProductKey prodKey = new ProductKey(orgKey, productName);
      this.product = this.getProductService().get(prodKey);
      this.features = this.getProductService().getFeatures(prodKey);
      this.tags = this.getProductService().getTags(prodKey);
      this.domainId = this.product.getDomainId();

      this.name = product.getKey().getName();
      this.productLogo = PhotoViewOption.ShowPhoto.effectivePhoto(
          IMAGES_PRODUCT_DEFAULT_LOGO, product.getImageLocation());

      this.description = product.getDescription();
      this.productNodeType = product.getProductNodeType();
      this.productNodeCode = product.getProductNodeCode();
    }

  }

  public String getDomainId() {
    return domainId;
  }

  public List<ProductTagDto> getTags() {
    if (tags == null) {
      tags = new ArrayList<ProductTagDto>();
    }
    return tags;
  }

  public void setTags(List<ProductTagDto> tags) {
    this.tags = tags;
  }

  public String getProductFeatureDescriptionToAdd() {
    return productFeatureDescriptionToAdd;
  }

  public void setProductFeatureDescriptionToAdd(
      String productFeatureDescriptionToAdd) {
    this.productFeatureDescriptionToAdd = productFeatureDescriptionToAdd;
  }

  public List<ProductFeatureDto> getFeatures() {
    return features;
  }

  public void setFeatures(List<ProductFeatureDto> features) {
    this.features = features;
  }

  public ProductDto getProduct() {
    return product;
  }

  public PhotoViewOption getPhotoViewOption() {
    return photoViewOption;
  }

  public void setPhotoViewOption(PhotoViewOption photoViewOption) {
    this.photoViewOption = photoViewOption;
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
    this.orgInfo = orgInfo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProductNodeCode() {
    return productNodeCode;
  }

  public void setProductNodeCode(String productNodeCode) {
    this.productNodeCode = productNodeCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductNodeType getProductNodeType() {
    return productNodeType;
  }

  public void setProductNodeType(ProductNodeType productNodeType) {
    this.productNodeType = productNodeType;
  }

  public String getProductTagToAdd() {
    return productTagToAdd;
  }

  public void setProductTagToAdd(String productTagToAdd) {
    this.productTagToAdd = productTagToAdd;
  }

  public String getProductFeatureToAdd() {
    return productFeatureToAdd;
  }

  public void setProductFeatureToAdd(String productFeatureToAdd) {
    this.productFeatureToAdd = productFeatureToAdd;
  }

  public Boolean getProductLogoExists() {
    return productLogoExists;
  }

  public void setProductLogoExists(Boolean productLogoExists) {
    this.productLogoExists = productLogoExists;
  }

  public String getProductLogo() {
    return PhotoViewOption.ShowPhoto.effectivePhoto(
        IMAGES_PRODUCT_DEFAULT_LOGO, productLogo);
  }

  public void setProductLogo(String productLogo) {
    this.productLogo = productLogo;
  }

  public String updateProduct() {
    log.info("Update product " + this.getDescription());
    UpdateProductDto update = new UpdateProductDto(this.getDescription(),
        productLogo);

    product = productService.update(product.getKey(), update,
        getMemberKey(memberInfo));
    return "result";
  }

  public String saveProduct() {
    SocialActorKey orgKey = this.getOrgKey(orgInfo);

    log.info("Creating new product with name " + this.name);
    if (StringUtils.isBlank(this.name)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Product name cannot be empty"));
      return "error";
    }

    ProductKey productKey = new ProductKey(orgKey, this.name);

    NewProductDto newProduct = new NewProductDto(this.description,
        this.productNodeCode, this.productNodeType, this.domainId);

    this.getProductService().create(productKey, newProduct,
        this.getMemberKey(memberInfo));

    return "result";
  }

  public void deleteLogo() {
    log.info("deleting product logo");
    this.imageUploadHandler.handleImageDeletion(new ImageDeletion() {

      public void update() throws Exception {
        UpdateProductDto update = new UpdateProductDto(
            product.getDescription(), IMAGES_PRODUCT_DEFAULT_LOGO);
        product = productService.update(product.getKey(), update,
            getMemberKey(memberInfo));

      }

      public String getCurrentKey() {
        log.info("Product logo " + productLogo);
        return productLogo;
      }
    });
  }

  public void photoViewOptionSelected(AjaxBehaviorEvent ajaxEvent) {
    log.info("value Selected ");
  }

   public void handleLogoUpload(final FileUploadEvent event) {

    log.info("uploading logo ");
    String result = this.imageUploadHandler
        .handleImageUpload(new ImageUpload() {

          public void update() throws Exception {

            /*
             * if (product != null) { UpdateProductDto update = new
             * UpdateProductDto(product .getDescription(), productLogo); product
             * = productService.update(product.getKey(), update,
             * getMemberKey(memberInfo)); }
             */
            log.info("dont do anything ");
          }

          public void setImageKey(String key) {
            log.info("Product logo " + key);
            productLogo = key;
          }

          public String getType() {
            return "product";
          }

          public String getId() {
            // Using the Organization id here, since that will make all
            // the images go to the same logical bucket.
            // The uniqueness is given by the file name.
            // Example, all the product images for organization 5 wil go
            // to /product/5/. If two images there have the same name,
            // it's assumed that they are the same image.
            log.info("Product key id " + orgInfo.getId());
            return String.valueOf(orgInfo.getId());

          }

          public UploadedFile getFile() {
            return event.getFile();
          }

          public String currentKey() {
            log.info("Product logo " + productLogo);
            return productLogo;
          }
        });

    if (result != null) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Error uploading image", result));
    }

  }

  public void addProductFeature() {

    log.info("adding feature " + this.productFeatureToAdd);
    if (StringUtils.isEmpty(this.productFeatureToAdd)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Feature cannot be empty "));
      return;
    }

    NewProductFeatureDto newFeature = new NewProductFeatureDto(
        this.productFeatureToAdd, this.productFeatureDescriptionToAdd);

    ProductFeatureDto featureDto = this.getProductService().createFeature(
        product.getKey(), newFeature);
    this.features.add(featureDto);
    this.productFeatureToAdd = StringUtils.EMPTY;
    this.productFeatureDescriptionToAdd = StringUtils.EMPTY;
  }

  public void addProductTag() {

    log.info("adding tag " + this.productTagToAdd);
    if (StringUtils.isEmpty(this.productTagToAdd)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Tag cannot be empty "));
      return;
    }

    ProductTagKey tagKey = new ProductTagKey(this.product.getKey(),
        this.productTagToAdd);
    NewProductTagDto newTag = new NewProductTagDto();

    ProductTagDto tagDto = this.getProductService().createTag(tagKey, newTag);
    this.getTags().add(tagDto);
    this.productTagToAdd = StringUtils.EMPTY;

  }

  public void deleteProductFeature(ProductFeatureKey featureKey) {
    log.info("deleting feature " + featureKey);
    this.getProductService().deleteFeature(featureKey);

    for (ProductFeatureDto feature : features) {
      if (feature.getKey().getId().equals(featureKey.getId())) {
        features.remove(feature);
        break;
      }
    }
  }

  public void deleteProductTag(ProductTagKey tagKey) {
    log.info("deleting tag " + tagKey);
    this.getProductService().deleteTag(tagKey);

    for (ProductTagDto tag : tags) {
      if (tag.getKey().getId().equals(tagKey.getId())) {
        tags.remove(tag);
        break;
      }
    }
  }

}
