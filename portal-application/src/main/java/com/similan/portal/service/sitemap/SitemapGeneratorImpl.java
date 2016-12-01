package com.similan.portal.service.sitemap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.xml.stream.XMLStreamWriter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.MemberManagementService;
import com.similan.service.api.OrganizationManagementService;

/**
 * Service responsible for generating the site map based on the existing data.
 * @author psaavedra
 */
@Component("sitemapGenerator")
@Slf4j
public class SitemapGeneratorImpl implements SitemapGenerator {
//
//	private static final String SITEMAP_NS = "http://www.sitemaps.org/schemas/sitemap/0.9";


	private static final Map<Class<? extends SocialActor>, String> uriMap;

	static {
		uriMap = new HashMap<Class<? extends SocialActor>, String>();
		uriMap.put(SocialPerson.class, "/member/");
		uriMap.put(SocialOrganization.class, "/business/");
	}

	@Autowired
	private MemberManagementService memberService;

	@Autowired
	private OrganizationManagementService organizationService;

	@Autowired
	private PlatformCommonSettings platformCommonSettings;

	private String changeFrequency = "weekly";

	private String siteMapLocation = null;

	@Autowired
	private ServletContext sc;

	@PostConstruct
	public void init() {
		if (this.siteMapLocation != null) {
			return;
		}
		this.siteMapLocation = sc.getRealPath("/") + "sitemap.xml";
	}

	/**
	 * Generates the sitemap file.
	 */
	@Transactional(readOnly = true)
	public void run() {
		/*FileOutputStream fos = null;
		try {
			long start = System.currentTimeMillis();
			XMLOutputFactory xos = XMLOutputFactory.newInstance();
			fos = new FileOutputStream(siteMapLocation);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			XMLStreamWriter writer = xos.createXMLStreamWriter(bos);
			writer.writeStartDocument();
			writer.writeStartElement("", "urlset", SITEMAP_NS);
			SocialPersonWriter spWriter = new SocialPersonWriter();
			spWriter.writer = writer;
			spWriter.host = this.platformCommonSettings.getPortalApplcationUrl().getValue();
			try {
				log.debug("Writing members to sitemap file");
				spWriter.write(this.memberService.findAllMembers(), changeFrequency);
			} catch (Exception e) {
				log.warn("Could not write members to the sitemap", e);
			}
			try {
				log.debug("Writing organizations to sitemap file");
				spWriter.write(this.organizationService.findAll(), changeFrequency);
			} catch (Exception e) {
				log.warn("Could not write organizations to the sitemap", e);
			}	
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();
			bos.flush();
			writer.close();
			log.debug(
					"Sitemap generation completed successfully in {} milliseconds",
					(System.currentTimeMillis() - start));
		} catch (Exception e) {
			log.error("Error while generating site map file", e);
		} finally {
			IOUtils.closeQuietly(fos);
		}*/
	}

	public static class SocialPersonWriter {

		private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		private XMLStreamWriter writer;

		private String host;

		public void write(final List<? extends SocialActor> actors,
				String changeFrequency) {
			for (Iterator<? extends SocialActor> it = actors.iterator(); it.hasNext();) {
				SocialActor actor = it.next();
				StringBuilder loc = new StringBuilder(host);
				try {
					
					loc.append("public").append(getActorUri(actor));
					loc.append(actor.getId()); //TODO Add the actor display name for better SEO friendliness
					writer.writeStartElement("url");
					writer.writeStartElement("loc");
					writer.writeCharacters(loc.toString()); //<loc>http://www.example.com/</loc>
					writer.writeEndElement();

					//TODO Add a last modified date to SocialActor
					writer.writeStartElement("lastmod");
					writer.writeCharacters(sdf.format(new Date())); //<lastmod>2005-01-01</lastmod>
					writer.writeEndElement();
					
					writer.writeStartElement("changefreq");
					writer.writeCharacters(changeFrequency); //<changefreq>monthly</changefreq>
					writer.writeEndElement();
					writer.writeCharacters("\n");
					
				} catch (Exception e) {
					log.warn("Error while generating site map for " + actor, e);
				}
				it.remove();
			} 
		}

		/**
		 * @param actor
		 */
		private String getActorUri(SocialActor actor) {
			for (Entry<Class<? extends SocialActor>, String> entry : uriMap
					.entrySet()) {
				if (entry.getKey().isInstance(actor)) {
					return entry.getValue();
				}
			}
			throw new IllegalArgumentException("Actor type is not supported: "
					+ actor.getClass());
		}
	}

	/**
	 * @param changeFrequency the changeFrequency to set
	 */
	public void setChangeFrequency(String changeFrequency) {
		this.changeFrequency = changeFrequency;
	}

	public String getChangeFrequency() {
    return changeFrequency;
  }
	
	/**
	 * @param siteMapLocation the siteMapLocation to set
	 */
	public void setSiteMapLocation(String siteMapLocation) {
		this.siteMapLocation = siteMapLocation;
	}

}
