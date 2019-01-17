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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.documents.client.DocumentsClient;
import org.einnovator.documents.client.config.FilesConfiguration;
import org.einnovator.documents.client.model.Document;
import org.einnovator.util.PathUtil;
import org.einnovator.util.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FileUploadController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	protected DocumentsClient docClient;

	public FileUploadController() {
	}

	@PostMapping({ "/upload", "/api/upload", "/_upload", "/api/_upload" })
	public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "pwd", required = false) String folder,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "unique", required = false) Boolean unique,
			@RequestParam(value = "id", required = false) String id, Document document,
			MultipartHttpServletRequest request, 
			@RequestParam(required = false) Boolean crop,
			@RequestParam(value = "username", required = false) String username,
			Principal principal,
			Authentication authentication) throws IOException {

		if (principal == null) {
			logger.error("upload: " + HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String name = file.getOriginalFilename();

		if (!StringUtils.isEmpty(key)) {
			List<String> validExtensions = getValidExtensions(key);

			key = key.toLowerCase();
			String extension = name.substring(name.lastIndexOf(".") + 1);

			if (validExtensions != null && !validExtensions.contains(extension.toLowerCase())) {
				logger.error("upload: Invalid file extension: " + extension.toLowerCase());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		InputStream in;
		if (Boolean.TRUE.equals(crop)) {
			File croppedImage = cropImage(file);
			if (croppedImage == null) {
				logger.error("upload: Error cropping image");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
			uri = uploadResource(resourcePath, document, principal.getName(), true);
			if (uri == null) {
				logger.error("upload: [UPLOAD]: " + key + " " + id + " " + name);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (RuntimeException e) {
			logger.error("upload: " + key + " " + name + " " + id + " " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			update(key, id, uri, principal);
		} catch (RuntimeException e) {
			logger.error("upload: [UPDATE]: " + key + " " + name + " " + id + " " + uri + " " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.info("upload: " + key + " " + name + " " + id + " " + uri);

		return ResponseEntity.created(uri).build();
	}

	protected URI uploadResource(String resourcePath, Document document, String owner, boolean publicLink) {
		return docClient.upload(resourcePath, document, owner, true);
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
			BufferedImage newImage = originalImgage.getSubimage(width / 2 - (side / 2), height / 2 - (side / 2), side,
					side);
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
		FilesConfiguration config = getFileConfiguration();
		if (config != null) {
			String location = config.getLocation(key);
			if (location != null) {
				return location;
			}
			String folder = getTmpFolder();
			if (folder != null) {
				return folder + getResourceName(key, name, appendUuid);
			}
			return config.getRoot() + getResourceName(key, name, appendUuid);
		}
		return null;
	}

	protected String getResourcePath(String key, String folder, String name, boolean appendUuid) {
		FilesConfiguration config = getFileConfiguration();
		if (config != null) {
			String location = config.getLocation(key);
			if (location != null) {
				return location;
			}
			if (StringUtils.hasText(folder)) {
				return PathUtil.concat(config.getRoot(),
						PathUtil.concat(folder, getResourceName(key, name, appendUuid)));
			} else if (StringUtils.hasText(key)) {
				key = key.trim();
				folder = config.getFolder(key);
				if (folder != null) {
					return PathUtil.concat(folder, getResourceName(key, name, appendUuid));
				}
			}
			return config.getRoot() + getResourceName(key, name, appendUuid);
		}
		return null;
	}

	protected String getResourceName(String key, String name, boolean appendUuid) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(key) && appendUuid) {
			sb.append(key.trim());
		}
		String ext = null;
		if (StringUtils.hasText(name)) {
			int i= name.lastIndexOf(".");
			if (i>0 && i<name.length()-1) {
				ext = name.substring(i+1);
				name = name.substring(0, i);
			}
			if (sb.length() > 0) {
				sb.append("-");
			}
			sb.append(name.trim());
		}
		if (appendUuid) {
			String uuid = generateUUID(key, name);
			if (StringUtils.hasText(uuid) && appendUuid) {
				if (sb.length() > 0) {
					sb.append("-");
				}
				sb.append(uuid);
			}			
		}
		if (StringUtils.hasText(ext)) {
			sb.append(".");
			sb.append(ext);
		}
		return sb.toString();
	}

	protected String generateUUID(String key, String name) {
		return Long.toString(System.currentTimeMillis());
	}

	protected FilesConfiguration getFileConfiguration() {
		return null;
	}

	protected void update(String key, String id, URI uri, Principal principal) {
	}

	protected URI moveFromTmp(String tmpUri) {
		try {
			return docClient.move(tmpUri, getPathFromTmpPath(tmpUri));
		} catch (RuntimeException e) {
			logger.error("moveFromTmp: " + tmpUri + " " + e);
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

}
