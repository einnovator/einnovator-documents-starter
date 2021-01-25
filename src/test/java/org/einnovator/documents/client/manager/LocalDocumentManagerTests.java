package org.einnovator.documents.client.manager;

import org.einnovator.documents.client.config.DocumentsClientConfiguration;
import org.junit.jupiter.api.Test;

public class LocalDocumentManagerTests {

	@Test
	public void test() {
		DocumentsClientConfiguration config = new DocumentsClientConfiguration();
		LocalDocumentManager manager = new LocalDocumentManager(config);
		System.out.println(manager.getLocalPath("http://cm.nativex.cloud/api/_/~admin/.Tmp/config-1607635260"));
	}
}
