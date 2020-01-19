/**
 * 
 */
package org.einnovator.documents.client.manager;

import static org.einnovator.util.EnvUtil.getEnv;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.documents.client.manager.DocumentsUtil;
import org.springframework.util.StringUtils;

/**
 *
 */
public class DocumentsUtil {
	private final static Log logger = LogFactory.getLog(DocumentsUtil.class);

	/**
	 * Setup notification from environment variables.
	 * 
	 * Enable or disable documents. Enable or disable AMQP.
	 * 
	 * @return array of Spring profiles to enable
	 */
	public static String[] setupFromEnv() {
		String documentsLocal = getEnv("DOCUMENTS_LOCAL");
		String documentsServer = getEnv("DOCUMENTS_SERVER");

		List<String> profiles = new ArrayList<>();

		boolean localfs = false;

		if ("true".equalsIgnoreCase(documentsLocal) || (!"false".equalsIgnoreCase(documentsLocal) && isEmpty(documentsServer))) {
			localfs = true;
		}

		if (!localfs) {
			System.setProperty("DOCUMENTS_LOCAL", "true");
			logger.info("setupFromEnv: Remote Documents enabled");
		} else {
			System.setProperty("DOCUMENTS_LOCAL", "false");
			logger.info("setupFromEnv: Remote Documents disabled");
			profiles.add(DocumentsClientConfig.LOCALFS);
		}

		return profiles.toArray(new String[profiles.size()]);
	}
	
	private static boolean isEmpty(String s) {
		return !StringUtils.hasText(s) || s.contains("***");
	}
	
	
}
