package org.einnovator.documents.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;
import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.documents.client.config.DocumentsConfiguration;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.DocumentBuilder;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.model.PermissionType;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.sso.client.support.SsoTestHelper;

import static org.einnovator.documents.client.modelx.DocumentOptions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DocumentsClientConfig.class, DocumentsClientTests.TestConfig.class }, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = { "documents.server=http://localhost:2021", "sso.server=http://localhost:2001/auth",
		"spring.cache.ehcache.config:ehcache-sso-starter.xml" })
public class DocumentsClientTests extends SsoTestHelper {

	@Autowired
	DocumentsClient client;

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
	
	@Autowired
	private DocumentsConfiguration config;
	
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
			Document document = new DocumentBuilder().content(new FileInputStream(temp)).contentLength((long)content.length()).build();
			String name = tmp + temp.getName();
			document.setPath(name);
			document.setDescription("Test document description.");
			temp.deleteOnExit();
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Test
	public void writeReadTest() throws IOException {
		Document document = createTempDocument();
		System.out.println(config);

		URI uri = client.write(document, null);
		assertNotNull(uri);
		System.out.println(uri);
		assertTrue(uri.toString().contains(document.getPath()));
		Document document2 = client.read(uri, CONTENT_AND_META);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(document.getContentLength(), document2.getContentLength());
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}

	@Test
	public void writrReadPublicDocumentTest() {
		Document document = createTempDocument();
		URI location = client.write(document, CONTENT_AND_META);
		Document document2 = client.read(location, null);
		assertNotNull(document2);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}

	@Test
	public void writeNullDocumentTest() throws IOException {
		Document document = new Document();
		document.setPath(tmp + "/test-doc");
		URI uri = client.write(document, CONTENT_AND_META);
		assertNotNull(uri);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}

	@Test
	public void writeNullDocumentTest2() throws IOException {
		Document document = new Document();
		document.setPath(tmp + "test/emptyfile/testdocument");
		URI uri = client.write(document, null);
		assertNotNull(uri);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}
	
	@Test
	public void metaTest() {
		Document document = createTempDocument();
		client.write(document, null);
		Document res = client.read(document.getName(), META_ONLY);
		assertNotNull(res);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}

	@Test
	public void metaPublicDocumentTest() {
		Document document = createTempDocument();
		URI location = client.write(document, FORCE);
		Document document2 = client.read(location, META_ONLY);
		Document alias = client.read(PUBLIC_DIR_PATH + document.getName(), META_ONLY);
		assertNotNull(document2);
		System.out.println(document2.getUri());
		System.out.println(document2);
		assertTrue(alias.getMeta().get("reference").toString().contains(document2.getPath()));
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}



	@Test
	public void writeDocumentOnlyTest() throws IOException {
		Document document = new Document();
		document.setPath(tmp + "doc");
		document.setMeta(new HashMap<String, Object>());
		URI uri = client.write(document, null);
		assertNotNull(uri);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
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
		document.setPath(tmp + "ei-newlogo6-smooth300.png");
		document.setContentType("image/png");
		// document.setMeta(new HashMap<String, Object>());
		URI uri = client.write(document, null);
		assertNotNull(uri);
		Document document2 = client.read(uri, null);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(bytes.length, document2.getContent().length);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
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
		document.setName(tmp + "ei-newlogo6-smooth300.png");
		document.setContentType("image/png");
		document.setPath("/public/" + document.getName());
		URI uri = client.write(document, PUBLIC);

		assertNotNull(uri);
		System.out.println(uri);
		Document document2 = client.read(uri, CONTENT_AND_META);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(bytes.length, document2.getContent().length);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}

	@Test
	public void writeToFolderTest() throws IOException {
		Document document = createTempDocument();
		String path = "directory/";
		document.setPath(path);
		URI uri = client.write(document, null);
		assertNotNull(uri);
		client.delete(path + document.getName(), FORCE);
		client.delete(document.getPath(), DocumentOptions.FORCE);		
	}

	@Test
	public void listTest() {

		Document doc0 = createTempDocument();
		Document doc1 = createTempDocument();

		System.out.println(doc0.getPath());
		System.out.println(doc1.getPath());
		assertNotNull(client.write(doc0, null));
		assertNotNull(client.write(doc1, null));

		List<Document> documents = client.list(tmp, null, null);
		assertNotNull(documents);
		for (Document doc: documents) {
			System.out.println(doc);
		}
		assertTrue(documents.size() > 0);

		client.delete(doc0.getPath(), FORCE);
		client.delete(doc1.getPath(), FORCE);
	}

	@Test
	public void deleteTest() {
		Document document = createTempDocument();
		client.write(document, null);
		List<Document> documents = client.list(TEST_DIR_PATH, null, null);
		Document foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		client.delete(document.getName(), FORCE);

		documents = client.list(TEST_DIR_PATH, null, null);
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
		Document document = createTempDocument();
		URI location = client.write(document, null);
		System.out.println("LOCATION: " + location);
		Document document2 = client.read(location, META_ONLY);
		Document alias = client.read(PUBLIC_DIR_PATH + document2.getName(), META_ONLY);
		assertNotNull(document2);
		assertTrue(alias.getMeta().get("reference").toString().contains(document2.getPath()));
		System.out.println("CREATED DOC PUBLIC PASSED");
		// Move doc to trash
		client.delete(document.getName(), FORCE);

		List<Document> documents = client.list(TRASH_DIR_PATH, null, null);
		System.out.println("TRASH: " + documents);
		Document foundDoc = null;
		alias = client.read(PUBLIC_DIR_PATH + document.getName(), META_ONLY);
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
		client.restore(TRASH_DIR_PATH + document.getName(), null);
		Document document3 = client.read(document.getName(), META_ONLY);
		alias = client.read(PUBLIC_DIR_PATH + document3.getName(), META_ONLY);
		assertNotNull(document3);
		assertTrue(alias.getMeta().get("reference").toString().contains(document.getName()));

		// Delete doc
		client.delete(document3.getPath(), FORCE);
		

		Document document4 = client.read(location, META_ONLY);
		alias = client.read(PUBLIC_DIR_PATH, META_ONLY);

		assertNull(document4);
		assertNull(alias);
		client.delete(document.getPath(), DocumentOptions.FORCE);		

	}

	@Test
	public void recursiveDeleteTest() {
		Document document = createTempDocument();
		String folder = "abc/";
		document.setPath(tmp + folder);
		client.write(document, null);
		List<Document> documents = client.list(tmp, null, null);
		Document foundDoc = null;
		Document foundFolder = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(folder)) {
				foundFolder = doc;
				break;
			}

		}
		documents = null;
		documents = client.list(folder, null, null);
		for (Document doc : documents) {
			if (doc.getPath().equals(folder + document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		assertNotNull(foundFolder);

		client.delete(folder, FORCE);

		documents = null;
		documents = client.list(tmp, null, null);
		foundDoc = null;
		foundFolder = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(folder)) {
				foundFolder = doc;
				break;
			}

		}
		documents = null;
		documents = client.list(folder, null, null);
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
		Document document = createTempDocument();
		client.write(document, null);
		List<Document> documents = client.list(TEST_DIR_PATH, null, null);
		Document foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		client.delete(document.getName(), FORCE);

		documents = client.list(TRASH_DIR_PATH, null, null);
		foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(TRASH_DIR_PATH + "" + document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNotNull(foundDoc);

		client.delete(foundDoc.getPath(), FORCE);

		foundDoc = null;
		documents = client.list(TRASH_DIR_PATH, null, null);
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNull(foundDoc);
	}


	@Test
	public void shareTestAsAdim() {
		Document document = createTempDocument();
		client.write(document, null);


		List<Permission> permissions = 	 Permission.make(PermissionType.READ, SHARE_USER);

		Document nullDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));

		System.out.println("DOC NULL: " + nullDoc);

		assertNull(nullDoc);

		client.share(document.getPath(), permissions, null);

		Document sharedDoc = client.read(document.getPath(), META_ONLY);
		assertEquals(sharedDoc.getPermissions().get(0), permissions.get(0));

		System.out.println("shared document1: " + sharedDoc);

		sharedDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));

		System.out.println("shared document2: " + sharedDoc);
		assertNotNull(sharedDoc);

		client.delete(document.getName(), FORCE);

		sharedDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));

		System.out.println("shared document3: " + sharedDoc);
		assertNull(sharedDoc);
	}

	@Test
	public void shareTest() {
		Document document = createTempDocument();
		client.write(document, null);

		List<Permission> permissions = 	 Permission.make(PermissionType.READ, SHARE_USER);

		setPrincipal(SHARE_USER, TEST_PASSWORD);
		Document nullDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));

		System.out.println("DOC NULL: " + nullDoc);

		assertNull(nullDoc);

		setPrincipal(TEST_USER, TEST_PASSWORD);
		client.share(document.getName(), permissions, null);

		Document sharedDoc = client.read(document.getName(), META_ONLY);
		assertEquals(sharedDoc.getPermissions().get(0), permissions.get(0));

		System.out.println("shared document1: " + sharedDoc);

		setPrincipal(SHARE_USER, TEST_PASSWORD);
		sharedDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));

		System.out.println("shared document2: " + sharedDoc);
		assertNotNull(sharedDoc);

		setPrincipal(TEST_USER, TEST_PASSWORD);
		client.delete(document.getName(), FORCE);

		setPrincipal(SHARE_USER, TEST_PASSWORD);
		sharedDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));

		System.out.println("shared document3: " + sharedDoc);
		assertNull(sharedDoc);
	}

	@Test
	public void unshareTest() {
		Document document = createTempDocument();
		client.write(document, null);

		List<Permission> permissions = 	 Permission.make(PermissionType.READ, SHARE_USER);
		client.share(document.getName(), permissions, null);
		client.unshare(document.getName(), permissions, null);

		Document sharedDoc = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));
		assertEquals(null, sharedDoc);

		sharedDoc = client.read(document.getName(), META_ONLY);
		assertFalse(sharedDoc.getPermissions().contains(permissions.get(0)));

	}

	@Test
	public void shareDeleteOriginal() {
		Document document = createTempDocument();
		client.write(document, null);

		List<Permission> permissions = 	 Permission.make(PermissionType.READ, SHARE_USER);

		client.share(document.getName(), permissions, null);

		Document doc2 = client.read(document.getName(), FORCE);
		assertTrue(doc2.getPermissions().contains(permissions.get(0)));

		client.delete(document.getName(), FORCE);

		doc2 = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));
		assertNull(doc2);
	}

	@Test
	public void writeWithPermissionsTest() {
		Document document = createTempDocument();
		document.setPermissions(Permission.make(PermissionType.READ, SHARE_USER));

		client.write(document, CONTENT_AND_META);

		Document document2 = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(CONTENT_AND_META).username(SHARE_USER));

		assertNotNull(document2);
	}

	@Test
	public void writeAttachmentTest() {
		Document document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		client.write(document, null);

		Document doc2 = client.read(document.getName(), META_ONLY);
		List<Document> attachments = doc2.getAttachments();
		for (Document att : attachments) {
			assertNotNull(client.read(att.getPath(), META_ONLY));
		}
	}

	@Test
	public void moveAttachmentToTrashAndDeleteTest() {
		Document document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		client.write(document, null);
		client.delete(document.getName(), FORCE);

		Document doc2 = client.read(TRASH_DIR_PATH + document.getName(), META_ONLY);

		for (Document doc : doc2.getAttachments()) {
			Document att = client.read(doc.getPath(), META_ONLY);
			assertNotNull(att);
		}

		client.delete(TRASH_DIR_PATH + document.getName(), FORCE);

		for (Document doc : doc2.getAttachments()) {
			Document att = client.read(doc.getPath(), META_ONLY);
			assertNull(att);
		}

	}

	@Test
	public void forceDeleteDocWithAttachmentTest() {
		Document document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		client.write(document, null);
		client.delete(document.getName(), FORCE);

		Document doc2 = client.read(TRASH_DIR_PATH + document.getName(), META_ONLY);

		assertNull(doc2);

		for (Document doc : document.getAttachments()) {
			Document att = client.read(TRASH_DIR_PATH + ATTACHMENT_DIR_PATH + doc.getPath(), META_ONLY);
			assertNull(att);
		}
	}

	@Test
	public void restoreDocWithAttachmentTest() {
		Document document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		client.write(document, null);

		client.delete(document.getName(), FORCE);

		Document trashDoc = client.read(TRASH_DIR_PATH + document.getName(), META_ONLY);

		for (Document doc : trashDoc.getAttachments()) {
			Document att = client.read(doc.getPath(), META_ONLY);
			assertNotNull(att);
		}

		client.restore(TRASH_DIR_PATH + document.getName(), null);
		Document restored = client.read(document.getName(), META_ONLY);

		for (Document doc : restored.getAttachments()) {
			Document att = client.read(TRASH_DIR_PATH + doc.getPath(), META_ONLY);
			assertNull(att);
		}

		for (Document doc : restored.getAttachments()) {
			Document att = client.read(doc.getPath(), META_ONLY);
			assertNotNull(att);
		}

	}

	@Test
	public void accessSharedAttachmentTest() throws URISyntaxException {
		Document document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));

		client.write(document, null);

		List<Permission> permissions = 	 Permission.make(PermissionType.READ, SHARE_USER);
		document.setPermissions(permissions);

		client.share(document.getName(), permissions, null);

		Document doc2 = client.read(SHARE_FOLDER + document.getName(), new DocumentOptions(META_ONLY).username(SHARE_USER));
		for (Document attachment : doc2.getAttachments()) {
			System.out.println("ATTCH: " + attachment + " URI: " + attachment.getUri());
			assertNotNull(client.read(new URI(attachment.getUri()), new DocumentOptions(META_ONLY).username(SHARE_USER)));
		}
	}

	@Test
	public void unauthorizedAccess() throws URISyntaxException {
		Document document = createTempDocument();
		client.write(document, null);
		Document doc = client.read(document.getName(), META_ONLY);
		String uri = doc.getUri().replaceAll("/_/", "/_/" + "~" + TEST_USER + "/");
		System.out.println("URI: " + uri);

		setPrincipal(NON_AUTHORIZED_SHARE_USER, TEST_PASSWORD);
		assertNull(client.read(new URI(uri),new DocumentOptions(META_ONLY).username(NON_AUTHORIZED_SHARE_USER)));
	}

	@Test
	public void deleteFileWithSpaces() throws URISyntaxException {
		Document document = createTempDocument("Filename with spaces", "TEST CONTENT");
		client.write(document, null);
		client.delete(document.getName(), FORCE);
		assertNull(client.read(document.getName(), META_ONLY));
	}

	@Test
	public void writeAttributes() {
		Document document = createTempDocument();
		Map<String, String> attributes = new HashMap<>();
		attributes.put("TESTATTRIBUTE", "testValue");
		attributes.put("TEST ATTRIBUTE", "testValue2");
		attributes.put("TEST ATTRIBUTE2", "test Value2");
		document.setAttributes(attributes);
		client.write(document, null);
		Document storedDoc = client.read(document.getPath(), META_ONLY);
		System.out.println("ATTRIBUTES: " + storedDoc.getAttributes());
		assertEquals("testValue", storedDoc.getAttributes().get("TESTATTRIBUTE"));
		assertEquals("testValue2", storedDoc.getAttributes().get("TEST ATTRIBUTE"));
		assertEquals("test Value2", storedDoc.getAttributes().get("TEST ATTRIBUTE2"));
	}

	@Test
	public void mkdir() {
		URI location = client.mkdir(tmp+"folder-"+ UUID.randomUUID(), null);
		Document folder = client.read("folder/", null);
		assertTrue(folder.isFolder());
		folder = client.read(location, META_ONLY);
		assertTrue(folder.isFolder());
		client.delete("folder/", FORCE);
	}

	@Test
	public void createSubFolder() {
		client.mkdir("folder", null);
		URI location = client.mkdir("folder/subdir", null);
		Document folder = client.read(location, DocumentOptions.META_ONLY);
		assertTrue(folder.isFolder());
		client.delete("folder/subdir/", null);
		client.delete("folder/", null);
		folder = client.read(location, META_ONLY);
		assertNull(folder);
	}

	@Test
	public void deleteSubFolderWithSpaces() throws URISyntaxException {
		client.mkdir("folder", null);
		URI location = client.mkdir("folder/subdir with spaces", null);
		location = new URI(location.toString().replaceAll("9595", "9596"));
		Document folder = client.read(location, DocumentOptions.META_ONLY);
		assertTrue(folder.isFolder());
		client.delete("folder/subdir with spaces/", DocumentOptions.FORCE);
		client.delete("folder/", DocumentOptions.FORCE);
		folder = client.read(location, DocumentOptions.META_ONLY);
		assertNull(folder);
	}

	@Test
	public void copyTest() {
		Document document = createTempDocument();
		client.write(document, null);
		URI newLocation = client.copy(document.getPath(), document.getPath()+"-Copy", null);
		Document doc2 = client.read(newLocation, META_ONLY);
		assertNotNull(doc2);
		client.delete(doc2.getPath(), FORCE);
	}

	@Test
	public void copyAttachmentsTest() {
		Document document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		client.write(document, null);
		URI uri2 = client.copy(document.getPath(), document.getPath() + "-copy", null);
		Document doc2 = client.read(uri2, null);
		assertNotNull(doc2);
		for (Document att : doc2.getAttachments()) {
			assertNotNull(client.read(att.getPath(), null));
		}
		client.delete(doc2.getPath(), DocumentOptions.FORCE);

	}

	@Test
	public void moveTest() {
		Document document = createTempDocument();
		client.write(document, DocumentOptions.META_ONLY);
		System.out.println("UPLOADED");
		URI newLocation = client.move(document.getName(), "moved/" + document.getName(), DocumentOptions.META_ONLY);
		System.out.println("MOVED TO: " + newLocation);
		Document document2 = client.read(newLocation, DocumentOptions.META_ONLY);
		assertNotNull(document2);
	}

	@Test
	public void movePublicDocTest() {
		Document document = createTempDocument();
		client.write(document, null);
		System.out.println("UPLOADED");
		URI newLocation = client.move(document.getName(), "moved/" + document.getPath(), null);
		System.out.println("MOVED TO: " + newLocation);
		String oldDocName = document.getName();
		Document document2 = client.read(newLocation, DocumentOptions.META_ONLY);
		System.out.println("NEW DOC: " + document);
		Document alias = client.read(PUBLIC_DIR_PATH + oldDocName, META_ONLY);
		System.out.println("ALIAS: " + alias);
		assertNotNull(document2);
		assertTrue(alias.getMeta().get("reference").toString().contains(document2.getPath()));
	}

	@Test
	public void moveDirWithSpaces() {
		client.mkdir("folder", null);
		URI location = client.mkdir("folder/subdir with spaces", null);
		Document folder0 = client.read(location, DocumentOptions.META_ONLY);
		URI location1 = client.mkdir("folder/the other dir", null);
		Document folder1 = client.read(location1, DocumentOptions.META_ONLY);
		URI newLocation = client.move(folder0.getPath(), folder1.getPath() + "moved with name/", null);
		Document document = client.read(newLocation, META_ONLY);
		client.delete(document.getPath(), FORCE);
		client.delete(folder1.getPath(), FORCE);
		client.delete(folder0.getPath() + "/", FORCE);
		assertNotNull(document);
	}

}