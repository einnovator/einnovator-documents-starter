package org.einnovator.documents.client.config;

import org.einnovator.documents.client.DocumentsClient;
import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.manager.DocumentManagerImpl;
import org.einnovator.documents.client.web.DocumentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;


@Configuration
@EnableConfigurationProperties(DocumentsConfiguration.class)
public class DocumentsClientConfig {

	@Autowired
	private DocumentsConfiguration config;
	
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	@Autowired
	private OAuth2ProtectedResourceDetails resourceDetails;
	
	@Autowired
	public DocumentsClientConfig() {
	}

	public DocumentsClientConfig(DocumentsConfiguration config) {
		this.config = config;
	}


	@Bean
	public OAuth2RestTemplate documentsRestTemplate() {
		OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);			
		template.setRequestFactory(config.getConnection().makeClientHttpRequestFactory());
		return template;
	}


	@Bean
	public DocumentsClient documentsClient() {
		return new DocumentsClient();
	}
	
	@Bean
	public DocumentManager documentManager(CacheManager cacheManager) {
		return new DocumentManagerImpl(cacheManager);
	}


}