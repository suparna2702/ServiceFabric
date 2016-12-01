package com.similan.service.impl.wall.entrymarshaller.link;

import org.springframework.stereotype.Component;

import com.similan.domain.entity.wall.wall.LinkReference;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.impl.Marshaller;

@Component
public class LinkReferenceMarshaller extends Marshaller {
  public LinkReferenceDto marshall(LinkReference link) {
    if (link == null) {
      return null;
    }
    return LinkReferenceDto.builder().url(link.getUrl()).title(link.getTitle())
        .content(link.getContent())
        .linkReferenceType(link.getLinkReferenceType())
        .imageUrl(link.getImageUrl()).build();
  }
}
