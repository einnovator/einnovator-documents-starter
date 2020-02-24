package org.einnovator.documents.client;

import static org.einnovator.documents.client.modelx.DocumentOptions.CONTENT_AND_META;
import static org.einnovator.documents.client.modelx.DocumentOptions.FORCE;
import static org.einnovator.documents.client.modelx.DocumentOptions.META_ONLY;
import static org.einnovator.documents.client.modelx.DocumentOptions.PUBLIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.sso.client.support.SsoTestHelper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DocumentsClientConfig.class,
		DocumentsManagerTests.TestConfig.class }, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = { "documents.uri=http://localhost:9596" })

public class DocumentsManagerTests extends SsoTestHelper {

	@Autowired
	DocumentManager manager;

	Document document;

	public static final String TEST_USER = "jsimao71@gmail.com";
	public static final String TEST_USER2 = "tdd@einnovator.org";
	public static final String TEST_USER3 = "info@einnovator.org";
	public static final String TEST_PASSWORD = "Einnovator123!!";
	private static final String CLIENT_ID = "application";
	private static final String CLIENT_SECRET = "application$123";

	private String TEST_FILENAME = "test";
	private String TEST_FILE_CONTENT = "test content";
	private String TEST_DIR_PATH = "";
	private String SHARE_FOLDER = "SharedWithMe/";
	private String TRASH_DIR_PATH = ".Trash/";

	private String PUBLIC_DIR_PATH = ".public/";
	private String ATTACHMENT_DIR_PATH = ".attachments/";

	private String SHARE_USER = TEST_USER3;
	private String NON_AUTHORIZED_SHARE_USER = TEST_USER2;

	private String tmp = "/tmp/";
	
	@Configuration
	static class TestConfig extends SsoTestHelper.TestConfig {

		public TestConfig(ApplicationContext context) {
			super(TEST_USER, TEST_PASSWORD, CLIENT_ID, CLIENT_SECRET, context);
		}

	}

	private Document createTempDocument() {
		return createTempDocument(tmp + TEST_FILENAME, TEST_FILE_CONTENT);
	}

	private Document createTempDocument(String filename, String content) {
		try {
			File temp;
			temp = File.createTempFile(filename, ".tmp");
			FileOutputStream fos = new FileOutputStream(temp);
			fos.write(content.getBytes(), 0, content.length());
			fos.close();
			Document document = new Document().withInputStream(new FileInputStream(temp));
			String name = temp.getName();
			document.setName(name);
			document.setInfo("TestInfo.");
			temp.deleteOnExit();
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@After
	public void clean() {
		if (document != null) {
			manager.delete(document.getPath(), DocumentOptions.FORCE, null);
		}
	}

	@Test
	public void writeTest() throws IOException {
		document = createTempDocument();
		URI uri = manager.write(document, CONTENT_AND_META, null);
		assertNotNull(uri);
		System.out.println(uri);
		assertTrue(uri.toString().contains(document.getName()));
	}

	@Test
	public void writeNullDocumentTest() throws IOException {
		Document document = new Document();
		document.setPath("/tmp/test-doc");
		URI uri = manager.write(document, CONTENT_AND_META, null);
		assertNotNull(uri);
	}

	@Test
	public void writeNullDocumentTest2() throws IOException {
		Document document = new Document();
		document.setPath("/tmp/test/emptyfile/testdocument");
		URI uri = manager.write(document, null, null);
		assertNotNull(uri);
	}

	@Test
	public void writeDocumentOnlyTest() throws IOException {
		document = new Document();
		document.setPath("/tmp/doc");
		URI uri = manager.write(document, null, null);
		assertNotNull(uri);
	}

	@Test
	public void writeImgFileTest() throws IOException {
		String TEST_IMG_FILE = "ei-newlogo6-smooth300.png";
		ClassPathResource resource = new ClassPathResource(TEST_IMG_FILE);

		byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
		assertNotNull(bytes);
		assertTrue(bytes.length > 0);

		resource = new ClassPathResource(TEST_IMG_FILE);
		Document document = new Document();
		document.setInputStream(resource.getInputStream());
		document.setName("ei-newlogo6-smooth300.png");
		document.setContentType("image/png");
		// document.setMeta(new HashMap<String, Object>());
		URI uri = manager.write(document, null, null);
		assertNotNull(uri);
		Document document2 = manager.read(uri, null, null);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(bytes.length, document2.getContent().length);
	}

	@Test
	public void writeImgAsPublicFileTest() throws IOException {
		String TEST_IMG_FILE = "ei-newlogo6-smooth300.png";
		ClassPathResource resource = new ClassPathResource(TEST_IMG_FILE);

		byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
		assertNotNull(bytes);
		assertTrue(bytes.length > 0);

		resource = new ClassPathResource(TEST_IMG_FILE);
		Document document = new Document();
		document.setInputStream(resource.getInputStream());
		document.setName("ei-newlogo6-smooth300.png");
		document.setContentType("image/png");
		document.setPath("/public/" + document.getName());
		URI uri = manager.write(document, PUBLIC, null);

		assertNotNull(uri);
		System.out.println(uri);
		Document document2 = manager.read(uri, CONTENT_AND_META, null);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(bytes.length, document2.getContent().length);
	}

	@Test
	public void writeToFolderTest() throws IOException {
		document = createTempDocument();
		String path = "directory/";
		document.setPath(path);
		URI uri = manager.write(document, null, null);
		assertNotNull(uri);
		manager.delete(path + document.getName(), FORCE, null);
	}

	@Test
	public void metaTest() {
		document = createTempDocument();
		manager.write(document, null, null);
		Document res = manager.read(document.getName(), META_ONLY, null);
		assertNotNull(res);
	}

	@Test
	public void listTest() {

		Document doc0 = createTempDocument();
		Document doc1 = createTempDocument();
		doc0.setPath(tmp + doc0.getName());
		doc1.setPath(tmp + doc1.getName());

		manager.write(doc0, null, null);
		manager.write(doc1, null, null);

		List<Document> documents = manager.list(tmp, null, null, null);
		assertNotNull(documents);
		for (Document doc: documents) {
			System.out.println(doc);
		}
		assertTrue(documents.size() > 0);
		System.out.println(document);
		manager.delete(doc1.getPath(), FORCE, null);
		manager.delete(doc1.getPath(), FORCE, null);
	}

	@Test
	public void deleteTest() {
		document = createTempDocument();
		manager.write(document, null, null);
		List<Document> documents = manager.list(TEST_DIR_PATH, null, null, null);
		Document foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		manager.delete(document.getName(), FORCE, null);

		documents = manager.list(TEST_DIR_PATH, null, null, null);
		foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNull(foundDoc);
	}

	@Test
	public void deletePublicTest() {
		// Doc creation
		document = createTempDocument();
		URI location = manager.write(document, null, null);
		System.out.println("LOCATION: " + location);
		Document document = manager.read(location, META_ONLY, null);
		Document alias = manager.read(PUBLIC_DIR_PATH + document.getName(), META_ONLY, null);
		assertNotNull(document);
		//assertTrue(alias.getReference().contains(document.getPath())); //TODO
		System.out.println("CREATED DOC PUBLIC PASSED");
		// Move doc to trash
		manager.delete(document.getName(), FORCE, null);

		List<Document> documents = manager.list(TRASH_DIR_PATH, null, null, null);
		System.out.println("TRASH: " + documents);
		Document foundDoc = null;
		alias = manager.read(PUBLIC_DIR_PATH + document.getName(), META_ONLY, null);
		System.out.println("PUBLIC: " + alias);
		for (Document doc : documents) {
			if (doc.getPath().equals(TRASH_DIR_PATH + "" + document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		System.out.println("FOUND: " + foundDoc);
		assertNotNull(foundDoc);
		// assertTrue(alias.getMeta().get("reference").toString().contains(document.getName())
		// &&
		// alias.getMeta().get("reference").toString().contains(TRASH_DIR_PATH));

		// Restore doc
		manager.restore(TRASH_DIR_PATH + document.getName(), null, null);
		document = manager.read(document.getName(), META_ONLY, null);
		alias = manager.read(PUBLIC_DIR_PATH + document.getName(), META_ONLY, null);
		assertNotNull(document);
		//assertTrue(alias.getReference().contains(document2.getName())); //TODO

		// Delete doc
		manager.delete(document.getName(), FORCE, null);
		

		document = manager.read(location, META_ONLY, null);
		alias = manager.read(PUBLIC_DIR_PATH, META_ONLY, null);

		assertNull(document);
		assertNull(alias);
	}

	@Test
	public void recursiveDeleteTest() {
		document = createTempDocument();
		String folder = "abc/";
		document.setPath(tmp + folder);
		manager.write(document, null, null);
		List<Document> documents = manager.list(tmp, null, null, null);
		Document foundDoc = null;
		Document foundFolder = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(folder)) {
				foundFolder = doc;
				break;
			}

		}
		documents = null;
		documents = manager.list(folder, null, null, null);
		for (Document doc : documents) {
			if (doc.getPath().equals(folder + document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		assertNotNull(foundFolder);

		manager.delete(folder, FORCE, null);

		documents = null;
		documents = manager.list(tmp, null, null, null);
		foundDoc = null;
		foundFolder = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(folder)) {
				foundFolder = doc;
				break;
			}

		}
		documents = null;
		documents = manager.list(folder, null, null, null);
		if (!documents.isEmpty()) {
			for (Document doc : documents) {
				if (doc.getPath().equals("abc/" + document.getName())) {
					foundDoc = doc;
					break;
				}

			}
		}
		assertNull(foundDoc);
		assertNull(foundFolder);
	}

	@Test
	public void moveToTrashAndDeleteTest() {
		document = createTempDocument();
		manager.write(document, null, null);
		List<Document> documents = manager.list(TEST_DIR_PATH, null, null, null);
		Document foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		manager.delete(document.getName(), FORCE, null);

		documents = manager.list(TRASH_DIR_PATH, null, null, null);
		foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(TRASH_DIR_PATH + "" + document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNotNull(foundDoc);

		manager.delete(foundDoc.getPath(), FORCE, null);

		foundDoc = null;
		documents = manager.list(TRASH_DIR_PATH, null, null, null);
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNull(foundDoc);
	}

	@Test
	public void readTest() {
		document = createTempDocument();
		manager.write(document, null, null);
		Document document2 = manager.read(document.getName(), CONTENT_AND_META, null);
		assertNotNull(document2);
	}

	@Test
	public void readPublicDocumentTest() {
		document = createTempDocument();
		URI location = manager.write(document, CONTENT_AND_META, null);
		Document document = manager.read(location, null, null);
		assertNotNull(document);
	}


	@Test
	public void moveAttachmentToTrashAndDeleteTest() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.write(document, null, null);
		manager.delete(document.getName(), FORCE, null);

		Document doc2 = manager.read(TRASH_DIR_PATH + document.getName(), META_ONLY, null);

		for (Document doc : doc2.getAttachments()) {
			Document att = manager.read(doc.getPath(), META_ONLY, null);
			assertNotNull(att);
		}

		manager.delete(TRASH_DIR_PATH + document.getName(), FORCE, null);

		for (Document doc : doc2.getAttachments()) {
			Document att = manager.read(doc.getPath(), META_ONLY, null);
			assertNull(att);
		}

	}

	@Test
	public void forceDeleteDocWithAttachmentTest() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.write(document, null, null);
		manager.delete(document.getName(), FORCE, null);

		Document doc2 = manager.read(TRASH_DIR_PATH + document.getName(), META_ONLY, null);

		assertNull(doc2);

		for (Document doc : document.getAttachments()) {
			Document att = manager.read(TRASH_DIR_PATH + ATTACHMENT_DIR_PATH + doc.getPath(), META_ONLY, null);
			assertNull(att);
		}
	}

	@Test
	public void restoreDocWithAttachmentTest() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.write(document, null, null);

		manager.delete(document.getName(), FORCE, null);

		Document trashDoc = manager.read(TRASH_DIR_PATH + document.getName(), META_ONLY, null);

		for (Document doc : trashDoc.getAttachments()) {
			Document att = manager.read(doc.getPath(), META_ONLY, null);
			assertNotNull(att);
		}

		manager.restore(TRASH_DIR_PATH + document.getName(), null, null);
		Document restored = manager.read(document.getName(), META_ONLY, null);

		for (Document doc : restored.getAttachments()) {
			Document att = manager.read(TRASH_DIR_PATH + doc.getPath(), META_ONLY, null);
			assertNull(att);
		}

		for (Document doc : restored.getAttachments()) {
			Document att = manager.read(doc.getPath(), META_ONLY, null);
			assertNotNull(att);
		}

	}

	@Test
	public void unauthorizedAccess() throws URISyntaxException {
		document = createTempDocument();
		manager.write(document, null, null);
		Document doc = manager.read(document.getName(), META_ONLY, null);
		String uri = doc.getUri().replaceAll("/_/", "/_/" + "~" + TEST_USER + "/");
		System.out.println("URI: " + uri);

		setPrincipal(NON_AUTHORIZED_SHARE_USER, TEST_PASSWORD);
		assertNull(manager.read(new URI(uri),new DocumentOptions(META_ONLY).username(NON_AUTHORIZED_SHARE_USER), null));
	}

	@Test
	public void deleteFileWithSpaces() throws URISyntaxException {
		document = createTempDocument("Filename with spaces", "TEST CONTENT");
		manager.write(document, null, null);
		manager.delete(document.getName(), FORCE, null);
		assertNull(manager.read(document.getName(), META_ONLY, null));
	}

	@Test
	public void writeAttributes() {
		document = createTempDocument();
		Map<String, String> attributes = new HashMap<>();
		attributes.put("TESTATTRIBUTE", "testValue");
		attributes.put("TEST ATTRIBUTE", "testValue2");
		attributes.put("TEST ATTRIBUTE2", "test Value2");
		document.setAttributes(attributes);
		manager.write(document, null, null);
		Document storedDoc = manager.read(document.getPath(), META_ONLY, null);
		System.out.println("ATTRIBUTES: " + storedDoc.getAttributes());
		assertEquals("testValue", storedDoc.getAttributes().get("TESTATTRIBUTE"));
		assertEquals("testValue2", storedDoc.getAttributes().get("TEST ATTRIBUTE"));
		assertEquals("test Value2", storedDoc.getAttributes().get("TEST ATTRIBUTE2"));
	}

	@Test
	public void mkdir() {
		URI location = manager.mkdir(tmp+"folder-"+ UUID.randomUUID(), null, null);
		Document folder = manager.read("folder/", null, null);
		assertTrue(folder.isFolder());
		folder = manager.read(location, META_ONLY, null);
		assertTrue(folder.isFolder());
		manager.delete("folder/", FORCE, null);
	}

	@Test
	public void createSubFolder() {
		manager.mkdir("folder", null, null);
		URI location = manager.mkdir("folder/subdir", null, null);
		Document folder = manager.read(location, DocumentOptions.META_ONLY, null);
		assertTrue(folder.isFolder());
		manager.delete("folder/subdir/", null, null);
		manager.delete("folder/", null, null);
		folder = manager.read(location, META_ONLY, null);
		assertNull(folder);
	}

	@Test
	public void deleteSubFolderWithSpaces() throws URISyntaxException {
		manager.mkdir("folder", null, null);
		URI location = manager.mkdir("folder/subdir with spaces", null, null);
		location = new URI(location.toString().replaceAll("9595", "9596"));
		Document folder = manager.read(location, DocumentOptions.META_ONLY, null);
		assertTrue(folder.isFolder());
		manager.delete("folder/subdir with spaces/", DocumentOptions.FORCE, null);
		manager.delete("folder/", DocumentOptions.FORCE, null);
		folder = manager.read(location, DocumentOptions.META_ONLY, null);
		assertNull(folder);
	}

	@Test
	public void copyTest() {
		document = createTempDocument();
		manager.write(document, null, null);
		URI newLocation = manager.copy(document.getPath(), document.getPath()+"-Copy", null, null);
		Document doc2 = manager.read(newLocation, META_ONLY, null);
		assertNotNull(doc2);
		manager.delete(doc2.getPath(), FORCE, null);
	}

	@Test
	public void copyAttachmentsTest() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.write(document, null, null);
		URI uri2 = manager.copy(document.getPath(), document.getPath() + "-copy", null, null);
		Document doc2 = manager.read(uri2, null, null);
		assertNotNull(doc2);
		for (Document att : doc2.getAttachments()) {
			assertNotNull(manager.read(att.getPath(), null, null));
		}
		manager.delete(doc2.getPath(), DocumentOptions.FORCE, null);

	}

	@Test
	public void moveTest() {
		document = createTempDocument();
		manager.write(document, DocumentOptions.META_ONLY, null);
		System.out.println("UPLOADED");
		URI newLocation = manager.move(document.getName(), "moved/" + document.getName(), DocumentOptions.META_ONLY, null);
		System.out.println("MOVED TO: " + newLocation);
		document = manager.read(newLocation, DocumentOptions.META_ONLY, null);
		assertNotNull(document);
	}


	@Test
	public void moveDirWithSpaces() {
		manager.mkdir("folder", null, null);
		URI location = manager.mkdir("folder/subdir with spaces", null, null);
		Document folder0 = manager.read(location, DocumentOptions.META_ONLY, null);
		URI location1 = manager.mkdir("folder/the other dir", null, null);
		Document folder1 = manager.read(location1, DocumentOptions.META_ONLY, null);
		URI newLocation = manager.move(folder0.getPath(), folder1.getPath() + "moved with name/", null, null);
		document = manager.read(newLocation, META_ONLY, null);
		manager.delete(document.getPath(), FORCE, null);
		manager.delete(folder1.getPath(), FORCE, null);
		manager.delete(folder0.getPath() + "/", FORCE, null);
		assertNotNull(document);
	}
}