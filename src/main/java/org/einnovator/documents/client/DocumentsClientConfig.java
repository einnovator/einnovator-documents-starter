package org.einnovator.documents.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.manager.DocumentManagerImpl;


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

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		ConnectionConfiguration connectionConf = config.getConnection();
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		
		if(connectionConf.getTimeout() != null) {
			clientHttpRequestFactory.setConnectTimeout(connectionConf.getTimeout());
		}
		if(connectionConf.getRequestTimeout() != null) {
			clientHttpRequestFactory.setConnectionRequestTimeout(connectionConf.getRequestTimeout());
		}
		if(connectionConf.getReadTimeout() != null) {
			clientHttpRequestFactory.setReadTimeout(connectionConf.getReadTimeout());
		}
		return clientHttpRequestFactory;
	}

	@Bean
	public OAuth2RestTemplate documentsRestTemplate() {
		OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);			
		template.setRequestFactory(clientHttpRequestFactory());
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
