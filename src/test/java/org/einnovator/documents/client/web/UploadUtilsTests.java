/**
 * 
 */
package org.einnovator.documents.client.web;

import static org.einnovator.documents.client.web.UploadUtils.getResourceName;
import static org.einnovator.documents.client.web.UploadUtils.getResourcePath;

import org.einnovator.documents.client.config.FilesConfiguration;
import org.junit.Test;

/**
 *
 */
public class UploadUtilsTests {

	@Test
	public void test() {
		FilesConfiguration config = new FilesConfiguration();
		config.setRoot("/.upload");
		String fn = getResourceName("key", "test.png", "original.png", true, config);
		System.out.println(fn);
		fn = getResourceName("key", "/xx/test.png", null, true, config);
		System.out.println(fn);
		String fp = getResourcePath("key", null, "test.png",  "original.png", true, config);
		System.out.println(fp);
	}

}
