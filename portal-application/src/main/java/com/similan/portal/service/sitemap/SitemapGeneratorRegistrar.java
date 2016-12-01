package com.similan.portal.service.sitemap;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.similan.framework.configuration.PlatformCommonSettings;

/**
 * Helper class that registers the {@link SitemapGenerator} with the
 * scheduler.
 * @author psaavedra
 */
@Component
@Slf4j
public class SitemapGeneratorRegistrar {


	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	private SitemapGenerator generator;

	@Autowired
	private PlatformCommonSettings config;

	@PostConstruct
	public void init() {
		log.info("Scheduling site map generation");
		String cron = config.getSitemapGenerationFrecuency().getValue();
		log.info("Scheduling sitemap generation with expression {}", cron);
		CronTrigger trigger = new CronTrigger(cron);
		scheduler.schedule(generator, trigger);
		log.info("Sitemap generation scheduled successfully");
	}
}
