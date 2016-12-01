package com.similan.framework.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.similan.domain.entity.template.VelocityDocumentTemplate;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.service.StaticContextHolderImpl;

@Slf4j
public class TemplateResourceLoader extends ResourceLoader {


	@Override
	public void init(ExtendedProperties configuration) {

	}

	@Override
	public InputStream getResourceStream(String name)
			throws ResourceNotFoundException {
		log.info("Getting template " + name);
		try {
			// Special case test. Velocity always asks for this resource.
			// Simply return empty result.
			if (name.equals("VM_global_library.vm"))
				return new ByteArrayInputStream("".getBytes("UTF-8"));

			log.info("Getting template " + name);
			VelocityTemplateRepository repository = StaticContextHolderImpl
					.getBean("velocityTemplateRepository",
							VelocityTemplateRepository.class);
			VelocityDocumentTemplate template = repository.findByName(name);
			if(template == null)
				throw new ResourceNotFoundException(name);
			return new ByteArrayInputStream(template.getTemplateContent()
					.getBytes("UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return true;
	}

	@Override
	public long getLastModified(Resource resource) {
		return System.currentTimeMillis();
	}
}
