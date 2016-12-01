package com.similan.service.api.product.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.product.dto.key.ProductTagKey;

@Deprecated
public class ProductTagDto extends KeyHolderDto<ProductTagKey> {

  protected ProductTagDto() {
  }

  public ProductTagDto(ProductTagKey key) {
    super(key);
  }

}
