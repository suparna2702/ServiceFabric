package com.similan.service.internal.impl.asset.store.s3;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.PostConstruct;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.similan.framework.util.HttpUtils;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.store.AbstractAssetStore;

@Component
public class S3AssetStore extends AbstractAssetStore {
  @Autowired
  private AmazonS3Client client;
  @Getter
  @Setter
  private String bucketName;

  public S3AssetStore() {
    super("s3");
  }
  
  private String toS3Key(String key) {
    return "assets/" + key;
  }

  @Override
  public String create(String category, TemporaryFile file,
      StoredAssetMetadata metadata) throws IOException {
    String uuid = UUID.randomUUID().toString();
    String key = category + '/' + uuid;
    String s3Key = toS3Key(key);
    try {
      @Cleanup
      InputStream input = file.openInput();
      client.putObject(bucketName, s3Key, input, toS3Metadata(file, metadata));
    } catch (AmazonClientException e) {
      throw new IOException("AWS S3 exception putting object " + s3Key
          + " at bucket " + bucketName, e);
    }
    return key;
  }

  private ObjectMetadata toS3Metadata(TemporaryFile file,
      StoredAssetMetadata metadata) {
    ObjectMetadata s3Metadata = new ObjectMetadata();
    s3Metadata.setContentLength(file.get().length());
    s3Metadata.setContentType(metadata.getContentType());
    String contentDisposition = HttpUtils.buildContentDisposition(
        metadata.isAttachment(), metadata.getFilename());
    s3Metadata.setContentDisposition(contentDisposition);
    s3Metadata.setCacheControl("max-age=31104000"); // 360 days
    return s3Metadata;
  }

  @Override
  public InputStream get(String key) throws IOException {
    String s3Key = toS3Key(key);
    try {
      return client.getObject(bucketName, s3Key).getObjectContent();
    } catch (AmazonClientException e) {
      throw new IOException("AWS S3 exception getting object " + s3Key
          + " at bucket " + bucketName, e);
    }
  }

  @Override
  public void delete(String key) throws IOException {
    String s3Key = toS3Key(key);
    try {
      client.deleteObject(bucketName, s3Key);
    } catch (AmazonClientException e) {
      throw new IOException("AWS S3 exception deleting object " + s3Key
          + " at bucket " + bucketName, e);
    }
  }

  @PostConstruct
  public void initialize() {
    checkState(bucketName != null, "Bucket name not set");
  }
}
