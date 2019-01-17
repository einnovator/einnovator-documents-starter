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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;
import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.model.DocumentBuilder;
import org.einnovator.documents.client.model.DocumentCategory;
import org.einnovator.documents.client.model.Permission;
import org.einnovator.documents.client.model.PermissionType;
import org.einnovator.documents.client.model.SubjectType;
import org.einnovator.documents.client.model.Template;
import org.einnovator.documents.client.modelx.TemplateFilter;
import org.einnovator.sso.client.support.SsoTestHelper;

import static org.einnovator.util.Profiler.run;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DocumentsClientConfig.class,
		DocumentsManagerTests.TestConfig.class }, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = { "documents.uri=http://localhost:9596" })

public class DocumentsManagerTests extends SsoTestHelper {

	@Autowired
	DocumentManager manager;
	Document document;

	public static final String TEST_USER0 = "jsimao71@gmail.com";

	public static final String TEST_USER = "marco.pereira@einnovator.org";
	public static final String TEST_USER2 = "tdd@einnovator.org";
	public static final String TEST_USER3 = "jobs@einnovator.org";
	public static final String TEST_PASSWORD = "Einnovator123!!";
	private static final String CLIENT_ID = "greenfence";
	private static final String CLIENT_SECRET = "greenfence$123";

	private String TEST_FILENAME = "TEST";
	private String TEST_FILE_CONTENT = "TEST FILE";
	private String TEST_DIR_PATH = "";
	private String SHARE_FOLDER = "SharedWithMe/";
	private String TRASH_DIR_PATH = ".Trash/";
	private String PUBLIC_DIR_PATH = ".public/";
	private String ATTACHMENT_DIR_PATH = ".attachments/";
	private String SHARE_USER = "marco.pereira@einnovator.org";
	private String NON_AUTHORIZED_SHARE_USER = "info@einnovator.org";

	@Configuration
	static class TestConfig extends SsoTestHelper.TestConfig {

		public TestConfig() {
			super(TEST_USER0, TEST_PASSWORD, CLIENT_ID, CLIENT_SECRET);
		}

	}

	private Document createTempDocument() {
		return createTempDocument(TEST_FILENAME, TEST_FILE_CONTENT);
	}

	private Document createTempDocument(String filename, String content) {
		try {
			File temp;
			temp = File.createTempFile(filename, ".tmp");
			FileOutputStream fos = new FileOutputStream(temp);
			fos.write(content.getBytes(), 0, content.length());
			fos.close();
			Document document = new DocumentBuilder().content(new FileInputStream(temp)).build();
			String name = temp.getName();
			document.setName(name);
			document.setDescription("Test document description.");
			// document.setPath("file://"+temp.getAbsolutePath());
			temp.deleteOnExit();
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@After
	public void removeTestDocument() {
		if (document != null) {
			manager.delete(document.getName(), true);
			System.out.println("DELETED!");
		}
	}

	@Test
	public void uploadDocumentAndFileTest() throws IOException {
		document = createTempDocument();
		URI uri = manager.upload(document);
		assertNotNull(uri);
	}

	@Test
	public void uploadNullDocumentTest() throws IOException {
		Document document = new Document();
		document.setName("testdocument");
		URI uri = manager.upload(document);
		assertNotNull(uri);
	}

	@Test
	public void uploadNullDocumentTest2() throws IOException {
		Document document = new Document();
		document.setName("testdocument");
		URI uri = manager.upload(".temp/test/emptyfile", document, false);
		assertNotNull(uri);
	}

	@Test
	public void uploadDocumentOnlyTest() throws IOException {
		document = new Document();
		document.setName("doc");
		document.setMeta(new HashMap<String, Object>());
		URI uri = manager.upload(document);
		assertNotNull(uri);
	}

	@Test
	public void uploadImgFileTest() throws IOException {
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
		URI uri = manager.upload(document);
		assertNotNull(uri);
		Document document2 = manager.download(uri);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(bytes.length, document2.getContent().length);
	}

	@Test
	public void uploadImgAsPublicFileTest() throws IOException {
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

		URI uri = manager.upload("/public/" + document.getName(), document, TEST_USER /* org.getOwner() */, true, false);

		assertNotNull(uri);
		System.out.println(uri);
		Document document2 = manager.download(uri);
		assertNotNull(document2);
		assertNotNull(document2.getContent());
		assertEquals(bytes.length, document2.getContent().length);
	}

	@Test
	public void uploadToFolderTest() throws IOException {
		document = createTempDocument();
		String location = "directory/";
		URI uri = manager.upload(location, document, false);
		assertNotNull(uri);
		manager.delete(location + document.getName(), true);
	}

	@Test
	public void metaTest() {
		document = createTempDocument();
		manager.upload(document);
		Document res = manager.meta(document.getName());
		assertNotNull(res);
	}

	@Test
	public void metaPublicDocumentTest() {
		document = createTempDocument();
		URI location = manager.upload(document, true);
		Document document = manager.meta(location);
		Document alias = manager.meta(PUBLIC_DIR_PATH + document.getName());
		assertNotNull(document);
		System.out.println(document.getUri());
		System.out.println(document);
		assertTrue(alias.getMeta().get("reference").toString().contains(document.getName()));
	}

	@Test
	public void listTest() {

		Document test0 = createTempDocument();
		Document test1 = createTempDocument();
		String location = "testing/";

		System.out.println("NAME1: " + test0.getName());
		System.out.println("NAME2: " + test1.getName() + " " + test1.getPath());
		manager.upload(location, test0, false);
		manager.upload(location, test1, false);

		List<Document> document = manager.list(location);
		System.out.println("LIST: " + document);
		assertNotNull(document);
		assertTrue(document.size() > 0);
		System.out.println(document);
		manager.delete(location + test0.getName(), true);
		manager.delete(location + test1.getName(), true);
	}

	@Test
	public void deleteTest() {
		document = createTempDocument();
		manager.upload(document);
		List<Document> documents = manager.list(TEST_DIR_PATH);
		Document foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		manager.delete(document.getName(), true);

		documents = manager.list(TEST_DIR_PATH);
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
		URI location = manager.upload(document, true);
		System.out.println("LOCATION: " + location);
		Document document = manager.meta(location);
		Document alias = manager.meta(PUBLIC_DIR_PATH + document.getName());
		assertNotNull(document);
		assertTrue(alias.getMeta().get("reference").toString().contains(document.getName()));
		System.out.println("CREATED DOC PUBLIC PASSED");
		// Move doc to trash
		manager.delete(document.getName(), false);

		List<Document> documents = manager.list(TRASH_DIR_PATH);
		System.out.println("TRASH: " + documents);
		Document foundDoc = null;
		alias = manager.meta(PUBLIC_DIR_PATH + document.getName());
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
		manager.restore(TRASH_DIR_PATH + document.getName());
		document = manager.meta(document.getName());
		alias = manager.meta(PUBLIC_DIR_PATH + document.getName());
		assertNotNull(document);
		assertTrue(alias.getMeta().get("reference").toString().contains(document.getName()));

		// Delete doc
		manager.delete(document.getName(), true);

		document = manager.meta(location);
		alias = manager.meta(PUBLIC_DIR_PATH);

		assertNull(document);
		assertNull(alias);
	}

	@Test
	public void recursiveDeleteTest() {
		document = createTempDocument();
		manager.upload("abc/", document, false);
		List<Document> documents = manager.list("");
		Document foundDoc = null;
		Document foundFolder = null;
		for (Document doc : documents) {
			if (doc.getPath().equals("abc/")) {
				foundFolder = doc;
				break;
			}

		}
		documents = null;
		documents = manager.list("abc/");
		for (Document doc : documents) {
			if (doc.getPath().equals("abc/" + document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		assertNotNull(foundFolder);

		manager.delete("abc", true);

		documents = null;
		documents = manager.list("");
		foundDoc = null;
		foundFolder = null;
		for (Document doc : documents) {
			if (doc.getPath().equals("abc/")) {
				foundFolder = doc;
				break;
			}

		}
		documents = null;
		documents = manager.list("abc/");
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
		manager.upload(document);
		List<Document> documents = manager.list(TEST_DIR_PATH);
		Document foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}

		}
		assertNotNull(foundDoc);
		manager.delete(document.getName(), false);

		documents = manager.list(TRASH_DIR_PATH);
		foundDoc = null;
		for (Document doc : documents) {
			if (doc.getPath().equals(TRASH_DIR_PATH + "" + document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNotNull(foundDoc);

		manager.delete(foundDoc.getPath(), false);

		foundDoc = null;
		documents = manager.list(TRASH_DIR_PATH);
		for (Document doc : documents) {
			if (doc.getPath().equals(document.getName())) {
				foundDoc = doc;
				break;
			}
		}
		assertNull(foundDoc);
	}

	@Test
	public void downloadTest() {
		document = createTempDocument();
		run("DocumentManagerTests:downloadTest:upload", () -> manager.upload(document));
		Document document2 = run("DocumentManagerTests:downloadTest:download", () -> manager.download(document.getName(), true));
		assertNotNull(document2);
	}

	@Test
	public void downloadTestN() {
		run(20, "DocumentManagerTests:downloadTest", () -> downloadTest());
	}

	@Test
	public void downloadPublicDocumentTest() {
		document = createTempDocument();
		URI location = manager.upload(document, true);
		Document document = manager.download(location, null);
		assertNotNull(document);
	}

	@Test
	public void shareTestAsAdim() {
		document = createTempDocument();
		manager.upload(document);

		List<Permission> permissions = new ArrayList<>();
		List<PermissionType> perms = new ArrayList<>();
		Permission p = new Permission();
		p.setUser(SHARE_USER);
		p.setPermTypes(perms);
		perms.add(PermissionType.READ);
		permissions.add(p);

		Document nullDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		System.out.println("DOC NULL: " + nullDoc);

		assertNull(nullDoc);

		manager.share(document.getName(), permissions);

		Document sharedDoc = manager.meta(document.getName());
		assertEquals(sharedDoc.getPermissions().get(0), permissions.get(0));

		System.out.println("SHARED DOC1: " + sharedDoc);

		sharedDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		System.out.println("SHARED DOC2: " + sharedDoc);
		assertNotNull(sharedDoc);

		manager.delete(document.getName(), true);

		sharedDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		System.out.println("SHARED DOC3: " + sharedDoc);
		assertNull(sharedDoc);
	}

	@Test
	public void shareTest() {
		document = createTempDocument();
		manager.upload(document);

		List<Permission> permissions = new ArrayList<>();
		List<PermissionType> perms = new ArrayList<>();
		Permission p = new Permission();
		p.setUser(SHARE_USER);
		p.setPermTypes(perms);
		perms.add(PermissionType.READ);
		permissions.add(p);

		setPrincipal(SHARE_USER, TEST_PASSWORD);
		Document nullDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		System.out.println("DOC NULL: " + nullDoc);

		assertNull(nullDoc);

		setPrincipal(TEST_USER, TEST_PASSWORD);
		manager.share(document.getName(), permissions);

		Document sharedDoc = manager.meta(document.getName());
		assertEquals(sharedDoc.getPermissions().get(0), permissions.get(0));

		System.out.println("SHARED DOC1: " + sharedDoc);

		setPrincipal(SHARE_USER, TEST_PASSWORD);
		sharedDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		System.out.println("SHARED DOC2: " + sharedDoc);
		assertNotNull(sharedDoc);

		setPrincipal(TEST_USER, TEST_PASSWORD);
		manager.delete(document.getName(), true);

		setPrincipal(SHARE_USER, TEST_PASSWORD);
		sharedDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		System.out.println("SHARED DOC3: " + sharedDoc);
		assertNull(sharedDoc);
	}

	@Test
	public void unshareTest() {
		document = createTempDocument();
		manager.upload(document);

		List<Permission> permissions = new ArrayList<>();
		List<PermissionType> perms = new ArrayList<>();
		Permission p = new Permission();
		p.setUser(SHARE_USER);
		p.setPermTypes(perms);
		perms.add(PermissionType.READ);
		permissions.add(p);

		manager.share(document.getName(), permissions);
		manager.shareDelete(document.getName(), permissions);

		Document sharedDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);
		assertEquals(null, sharedDoc);

		sharedDoc = manager.meta(document.getName());
		assertFalse(sharedDoc.getPermissions().contains(p));

	}

	@Test
	public void shareDeleteOriginal() {
		document = createTempDocument();
		manager.upload(document);

		List<Permission> permissions = new ArrayList<>();
		List<PermissionType> perms = new ArrayList<>();
		Permission p = new Permission();
		p.setUser(SHARE_USER);
		p.setPermTypes(perms);
		perms.add(PermissionType.READ);
		permissions.add(p);

		manager.share(document.getName(), permissions);

		Document updtDoc = manager.meta(document.getName());
		assertTrue(updtDoc.getPermissions().contains(p));

		manager.delete(document.getName(), true);

		updtDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);
		assertNull(updtDoc);
	}

	@Test
	public void uploadWithPermissions() {
		document = createTempDocument();
		List<Permission> permissions = new ArrayList<>();
		List<PermissionType> perms = new ArrayList<>();
		Permission p = new Permission();
		p.setUser(SHARE_USER);
		p.setPermTypes(perms);
		perms.add(PermissionType.READ);
		permissions.add(p);
		document.setPermissions(permissions);

		manager.upload(document);

		Document updtDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);

		assertNotNull(updtDoc);
	}

	@Test
	public void uploadAttachment() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.upload(document);

		Document updtDoc = manager.meta(document.getName());
		List<Document> attachments = updtDoc.getAttachments();
		for (Document att : attachments) {
			assertNotNull(manager.meta(att.getPath()));
		}
	}

	@Test
	public void moveAttachmentToTrashAndDelete() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.upload(document);
		manager.delete(document.getName(), false);

		Document updtDoc = manager.meta(TRASH_DIR_PATH + document.getName());

		for (Document doc : updtDoc.getAttachments()) {
			Document att = manager.meta(doc.getPath());
			assertNotNull(att);
		}

		manager.delete(TRASH_DIR_PATH + document.getName(), false);

		for (Document doc : updtDoc.getAttachments()) {
			Document att = manager.meta(doc.getPath());
			assertNull(att);
		}

	}

	@Test
	public void forceDeleteDocWithAttachment() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.upload(document);
		manager.delete(document.getName(), true);

		Document updtDoc = manager.meta(TRASH_DIR_PATH + document.getName());

		assertNull(updtDoc);

		for (Document doc : document.getAttachments()) {
			Document att = manager.meta(TRASH_DIR_PATH + ATTACHMENT_DIR_PATH + doc.getPath());
			assertNull(att);
		}
	}

	@Test
	public void restoreDocWithAttachment() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.upload(document);

		manager.delete(document.getName(), false);

		Document trashDoc = manager.meta(TRASH_DIR_PATH + document.getName());

		for (Document doc : trashDoc.getAttachments()) {
			Document att = manager.meta(doc.getPath());
			assertNotNull(att);
		}

		manager.restore(TRASH_DIR_PATH + document.getName());
		Document restored = manager.meta(document.getName());

		for (Document doc : restored.getAttachments()) {
			Document att = manager.meta(TRASH_DIR_PATH + doc.getPath());
			assertNull(att);
		}

		for (Document doc : restored.getAttachments()) {
			Document att = manager.meta(doc.getPath());
			assertNotNull(att);
		}

	}

	@Test
	public void AccessSharedAttachment() throws URISyntaxException {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));

		manager.upload(document);

		List<Permission> permissions = new ArrayList<>();
		List<PermissionType> perms = new ArrayList<>();
		Permission p = new Permission();
		p.setUser(SHARE_USER);
		p.setPermTypes(perms);
		perms.add(PermissionType.READ);
		permissions.add(p);
		document.setPermissions(permissions);

		manager.share(document.getName(), permissions);

		Document updtDoc = manager.meta(SHARE_FOLDER + document.getName(), SHARE_USER);
		for (Document attachment : updtDoc.getAttachments()) {
			System.out.println("ATTCH: " + attachment + " URI: " + attachment.getUri());
			assertNotNull(manager.meta(new URI(attachment.getUri()), SHARE_USER));
		}
	}

	@Test
	public void unauthorizedAccess() throws URISyntaxException {
		document = createTempDocument();
		manager.upload(document);
		Document doc = manager.meta(document.getName());
		System.out.println("URI: " + doc.getUri().replaceAll("/_/", "/_/" + "~" + TEST_USER + "/"));

		setPrincipal(NON_AUTHORIZED_SHARE_USER, TEST_PASSWORD);
		assertNull(manager.meta(new URI(doc.getUri().replaceAll("/_/", "/_/" + "~" + TEST_USER + "/")),
				NON_AUTHORIZED_SHARE_USER));
	}

	@Test
	public void deleteFileWithSpaces() throws URISyntaxException {
		document = createTempDocument("Filename with spaces", "TEST CONTENT");
		manager.upload(document);
		manager.delete(document.getName(), true);
		assertNull(manager.meta(document.getName()));
	}

	@Test
	public void uploadAttributes() {
		document = createTempDocument();
		Map<String, String> attributes = new HashMap<>();
		attributes.put("TESTATTRIBUTE", "testValue");
		attributes.put("TEST ATTRIBUTE", "testValue2");
		attributes.put("TEST ATTRIBUTE2", "test Value2");
		document.setAttributes(attributes);
		manager.upload(document);
		Document storedDoc = manager.meta(document.getName());
		System.out.println("ATTRIBUTES: " + storedDoc.getAttributes());
		assertEquals("testValue", storedDoc.getAttributes().get("TESTATTRIBUTE"));
		assertEquals("testValue2", storedDoc.getAttributes().get("TEST ATTRIBUTE"));
		assertEquals("test Value2", storedDoc.getAttributes().get("TEST ATTRIBUTE2"));
	}

	@Test
	public void mkdir() {
		URI location = manager.newFolder("folder");
		Document folder = manager.meta("folder/");
		assertTrue(folder.isFolder());
		folder = manager.meta(location);
		assertTrue(folder.isFolder());
		manager.delete("folder/", true);
	}

	@Test
	public void createSubFolder() {
		manager.newFolder("folder");
		URI location = manager.newFolder("folder/subdir");
		Document folder = manager.meta(location);
		assertTrue(folder.isFolder());
		manager.delete("folder/subdir/", true);
		manager.delete("folder/", true);
		folder = manager.meta(location);
		assertNull(folder);
	}

	@Test
	public void deleteSubFolderWithSpaces() throws URISyntaxException {
		manager.newFolder("folder");
		URI location = manager.newFolder("folder/subdir with spaces");
		location = new URI(location.toString().replaceAll("9595", "9596"));
		Document folder = manager.meta(location);
		assertTrue(folder.isFolder());
		manager.delete("folder/subdir with spaces/", true);
		manager.delete("folder/", true);
		folder = manager.meta(location);
		assertNull(folder);
	}

	@Test
	public void copyTest() {
		document = createTempDocument();
		manager.upload(document);
		URI newLocation = manager.copy(document.getName());
		Document newDoc = manager.meta(newLocation);
		assertNotNull(newDoc);
		manager.delete(newDoc.getPath(), true);
	}

	@Test
	public void copyAttachmentsTest() {
		document = createTempDocument();
		Document attachment0 = createTempDocument("AttachmentA", "AttachmentA");
		Document attachment1 = createTempDocument("AttachmentB", "AttachmentB");

		document.setAttachments(Arrays.asList(attachment0, attachment1));
		manager.upload(document);
		URI newLocation = manager.copy(document.getName());
		Document newDoc = manager.meta(newLocation);
		assertNotNull(newDoc);
		for (Document att : newDoc.getAttachments()) {
			assertNotNull(manager.meta(att.getPath()));
		}
		manager.delete(newDoc.getPath(), true);

	}

	@Test
	public void moveTest() {
		document = createTempDocument();
		manager.upload(document);
		System.out.println("UPLOADED");
		URI newLocation = manager.move(document.getName(), "moved/" + document.getName());
		System.out.println("MOVED TO: " + newLocation);
		document = manager.meta(newLocation);
		assertNotNull(document);
	}

	@Test
	public void movePublicDocTest() {
		document = createTempDocument();
		manager.upload(document, true);
		System.out.println("UPLOADED");
		URI newLocation = manager.move(document.getName(), "moved/" + document.getName());
		System.out.println("MOVED TO: " + newLocation);
		String oldDocName = document.getName();
		document = manager.meta(newLocation);
		System.out.println("NEW DOC: " + document);
		Document alias = manager.meta(PUBLIC_DIR_PATH + oldDocName);
		System.out.println("ALIAS: " + alias);
		assertNotNull(document);
		assertTrue(alias.getMeta().get("reference").toString().contains(document.getName()));
	}

	@Test
	public void moveDirWithSpaces() {
		manager.newFolder("folder");
		URI location = manager.newFolder("folder/subdir with spaces");
		Document folder0 = manager.meta(location);
		URI location1 = manager.newFolder("folder/the other dir");
		Document folder1 = manager.meta(location1);
		URI newLocation = manager.move(folder0.getPath(), folder1.getPath() + "moved with name/");
		document = manager.meta(newLocation);
		manager.delete(document.getPath(), true);
		manager.delete(folder1.getPath(), true);
		manager.delete(folder0.getPath() + "/", true);
		assertNotNull(document);
	}

	@Test
	public void listGlobalTemplates() {
		// List<Template> templates = manager.listTemplate(null, null);
		// assertNull(templates);
		TemplateFilter templateFilter = new TemplateFilter();
		templateFilter.setGlobal(true);
		templateFilter.setShared(false);
		templateFilter.setLocal(false);
		List<Template> templates = manager.listTemplates(TEST_USER, templateFilter);
		assertTrue(templates.size() > 0);
	}

	@Test
	public void listTemplatesWithFilterPersonal() {
		TemplateFilter templateFilter = new TemplateFilter();
		templateFilter.setSubject(SubjectType.PERSONAL.getDisplayName());
		List<Template> templates = manager.listTemplates(TEST_USER, templateFilter);
		System.out.println("GET TEMPLATES: " + templates);
		assertTrue(templates.size() > 0);
		for (Template template : templates) {
			assertTrue(template.getSubject().equals(SubjectType.PERSONAL));
		}

		// templateFilter = new TemplateFilter();
		// templateFilter.setCategory(Category.CERTIFICATE.getDisplayName());
		// templates = manager.listTemplates(TEST_USER, templateFilter);
		// for (Template template : templates) {
		// assertTrue(template.getCategory().equals(Category.CERTIFICATE));
		// }
	}

	@Test
	public void listCertificateTemplates() {
		TemplateFilter filter = new TemplateFilter();
		filter.setSubject(SubjectType.ORGANIZATIONAL.getDisplayName());
		filter.setGlobal(true);
		filter.setCategory(DocumentCategory.CERTIFICATE.toString());
		List<Template> templates = manager.listTemplates(filter);
		System.out.println("listCertificateTemplates: " + templates);
		assertTrue(templates.size() > 0);
		for (Template template : templates) {
			System.out.println(template);
			assertTrue(template.getSubject().equals(SubjectType.ORGANIZATIONAL));
			assertTrue(template.getCategory().equals(DocumentCategory.CERTIFICATE));
		}
	}

	@Test
	public void getUserTemplates() {
		TemplateFilter templateFilter = new TemplateFilter();
		templateFilter.setGlobal(false);
		templateFilter.setShared(false);
		List<Template> templates = manager.listTemplates(TEST_USER, templateFilter);
		System.out.println("GET TEMPLATES: " + templates);
		assertTrue(templates.size() == 0);
	}

	// @Test
	// public void getTemplate() {
	// TemplateFilter templateFilter = new TemplateFilter();
	// templateFilter.setGlobal(true);
	// templateFilter.setShared(false);
	// templateFilter.setLocal(false);
	// List<Template> templates = manager.listTemplates(TEST_USER, templateFilter);
	// System.out.println("GET TEMPLATES: "+templates);
	//
	// assertTrue(templates.size() > 0);
	//
	// for (Template template : templates) {
	// Template t = manager.getTemplate(TEST_USER, "global/"+template.getName());
	// System.out.println("GET TEMPLATE: "+t);
	// assertNotNull(t);
	// }
	// }
}