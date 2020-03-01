package org.einnovator.documents.client.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.documents.client.config.DocumentsClientConfiguration;
import org.einnovator.documents.client.config.FilesConfiguration;
import org.einnovator.documents.client.manager.DocumentManager;
import org.einnovator.documents.client.model.Document;
import org.einnovator.documents.client.modelx.DocumentOptions;
import org.einnovator.util.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RequestMapping({"/", "/api"})
public class FileUploadController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	protected DocumentManager manager;

	@Autowired
	protected DocumentsClientConfiguration config;
	

	/**
	 * Create instance of {@code FileUploadController}.
	 *
	 */
	public FileUploadController() {
	}
	
	@PostMapping({ "/upload", "/_upload" })
	public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, MultipartHttpServletRequest request, 
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "pwd", required = false) String folder,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "unique", required = false) Boolean unique,
			@RequestParam(value = "id", required = false) String id, Document document,
			@RequestParam(required = false) Boolean crop,
			DocumentOptions options,
			Principal principal, Authentication authentication, HttpServletResponse response) throws IOException {

		if (principal == null) {
			return unauthorized("upload", response);
		}

		String name = file.getOriginalFilename();

		if (!StringUtils.isEmpty(key)) {
			List<String> validExtensions = getValidExtensions(key);

			key = key.toLowerCase();
			String extension = name.substring(name.lastIndexOf(".") + 1);

			if (validExtensions != null && !validExtensions.contains(extension.toLowerCase())) {
				return badrequest("upload: Invalid file extension:", response, extension.toLowerCase());
			}
		}

		InputStream in;
		if (Boolean.TRUE.equals(crop)) {
			File croppedImage = cropImage(file);
			if (croppedImage == null) {
				return badrequest("upload: Error cropping image", response);

			}
			in = new FileInputStream(croppedImage);
		} else {
			in = file.getInputStream();
		}

		if (unique==null) {
			unique = !StringUtils.hasText(filename);
		}
		if (!StringUtils.hasText(filename)) {
			filename = file.getOriginalFilename();
		}
		String resourcePath = useTmp(key)
				? getTmpResourcePath(key, filename,	unique)
				: getResourcePath(key, folder, filename, unique);

		document.setName(name);
		document.setInputStream(in);

		URI uri = null;
		try {
			setupToken(principal, authentication);
			document.setPath(resourcePath);
			if (options.getPublicShare()==null) {
				options.share(true, true);				
			}
			uri = uploadResource(document, options);
			if (uri == null) {
				return internalerror("upload", response, key, id, name);
			}
		} catch (RuntimeException e) {
			return internalerror("upload", response, e, key, id, name);
		}

		try {
			update(key, id, uri, principal);
		} catch (RuntimeException e) {
			return internalerror("upload", response, e, key, id, name, uri);
		}

		if (logger.isDebugEnabled()) {
			debug("upload", key, name, id, uri);			
		}

		return ResponseEntity.created(uri).build();
	}

	protected URI uploadResource(Document document, DocumentOptions options) {
		return manager.write(document, options, null);
	}

	private File cropImage(MultipartFile file) {
		try {
			String format = file.getContentType().split("/")[1];
			BufferedImage originalImgage = ImageIO.read(file.getInputStream());
			int width = originalImgage.getWidth();
			int height = originalImgage.getHeight();
			int side;
			if (width > height) {
				side = height;
			} else {
				side = width;
			}
			BufferedImage newImage = originalImgage.getSubimage(width / 2 - (side / 2), height / 2 - (side / 2), side, side);
			File outFile = File.createTempFile(file.getOriginalFilename() + "-cropped", "tmp");
			ImageIO.write(newImage, format, outFile);
			return outFile;
		} catch (IOException e) {
			return null;
		}
	}

	protected List<String> getValidExtensions(String key) {
		FilesConfiguration config = getFileConfiguration();
		if (config != null) {
			return config.getExts(key);
		}
		return null;
	}

	protected String getLocation(String key) {
		FilesConfiguration config = getFileConfiguration();
		if (config != null) {
			return config.getLocation(key);
		}
		return null;
	}

	protected String getTmpResourcePath(String key, String name, boolean appendUuid) {
		return UploadUtils.getTmpResourcePath(key, name, appendUuid, getFileConfiguration());
	}

	protected String getResourcePath(String key, String folder, String name, boolean appendUuid) {
		return UploadUtils.getResourcePath(key, folder, name, appendUuid, getFileConfiguration());
	}

	protected String getResourceName(String key, String name, boolean appendUuid) {
		return UploadUtils.getResourceName(key, name, appendUuid, getFileConfiguration());
	}

	protected String generateUUID(String key, String name) {
		return Long.toString(System.currentTimeMillis()/1000);
	}

	protected void update(String key, String id, URI uri, Principal principal) {
	}

	protected URI moveFromTmp(String tmpUri, DocumentOptions options) {
		try {
			return manager.move(tmpUri, getPathFromTmpPath(tmpUri), options, null);
		} catch (RuntimeException e) {
			error("moveFromTmp",  tmpUri, e);
			return UriUtils.makeURI(tmpUri);
		}
	}

	protected String getPathFromTmpPath(String uri) {
		String tmp = getTmpFolder();
		String key = null; // key specific folder not supported when using tmp folder
		return uri.replace(tmp, getFolder(key));
	}

	protected String getTmpFolder() {
		FilesConfiguration config = getFileConfiguration();
		if (config != null) {
			return config.getTmp();
		}
		return null;
	}

	protected String getFolder(String key) {
		FilesConfiguration config = getFileConfiguration();
		if (config != null && !StringUtils.isEmpty(key)) {
			return config.getFolder(key);
		}
		return null;
	}

	protected boolean useTmp(String key) {
		FilesConfiguration config = getFileConfiguration();
		if (config != null && !StringUtils.isEmpty(key)) {
			return config.useTmpFor(key);
		}
		return config != null ? config.isUseTmp() : false;
	}

	protected FilesConfiguration getFileConfiguration() {
		return config.getFiles();
	}


}
