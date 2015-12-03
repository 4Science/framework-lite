package it.cineca.sr.simplecms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletWebRequest;

public class CmsUtils {

	protected FilterConfig filterConfig = null;

	protected static Logger log = LoggerFactory.getLogger(CmsUtils.class);

	private Path baseDir;

	private Path checkBaseDir(String baseDirStr) {
		if (baseDirStr != null) {
			Path baseDir = Paths.get(baseDirStr);
			try {
                Files.createDirectories(baseDir);
            } catch (IOException exc) {
                log.info("Error creating cmsFolder \"" + baseDirStr + "\"", exc);
            }
			if (Files.exists(baseDir) && Files.isDirectory(baseDir)) {
                return baseDir.toAbsolutePath().normalize();
            } else {
                throw new IllegalStateException(String.format("Assets Store Directory \"%s\" don't exist!!",
                        baseDirStr));
            }
		} else {
			throw new IllegalStateException(String.format("Assets Store Directory not Configured"));
		}
	}

	public Path getBaseDir() {
		return this.baseDir;
	}

	public Path computeFullPath(String fsChildRelativePath, boolean selfAdmitted) throws IOException {
		Path fullPath;
		if (fsChildRelativePath != null) {
			Path parent = this.baseDir;
			Path child = Paths.get(fsChildRelativePath);
			log.debug("Checking for inclusion of \"{}\" into \"{}\"", child, parent);
			if (child.isAbsolute()) {
				fullPath = null; // never happening in reason of path variable
									// mapping TODO ?????????
			} else {
				child = parent.resolve(child).normalize();
				log.debug("child filepath resolved to \"{}\"", child);

				boolean isChildInRightPlace = child.startsWith(parent);
				if (!selfAdmitted) {
					// - More checking required: parent and child must be
					// different
					isChildInRightPlace = isChildInRightPlace && !parent.equals(child);
				}
				log.debug("Inclusion is {} (same path are {} admitted)", isChildInRightPlace, (selfAdmitted ? ""
						: "NOT"));

				fullPath = isChildInRightPlace ? child : null;
			}
		} else {
			// - gracefully handle null case
			fullPath = null;
		}
		return fullPath;
	}

	private Path lookingForIndex(Path dirPath) {
		Path indexPath = dirPath.resolve("index.html");
		if (!Files.exists(indexPath)) {
			indexPath = dirPath.resolve("index.htm");
			if (!Files.exists(indexPath)) {
				indexPath = null;
			}
		}
		return indexPath;
	}

	public Path ensurePathExistence(String pathFragment) throws IOException {
		log.debug("Resource \"{}\" ... ", pathFragment);
		Path result;
		Path toRead = this.computeFullPath(pathFragment, true);
		if (toRead != null) {
			if (Files.exists(toRead)) {
				if (Files.isDirectory(toRead)) {
					result = this.lookingForIndex(toRead);
				} else {
					result = toRead;
				}
			} else {
				result = null;
			}
		} else {
			result = null;
		}
		log.debug("... mapped to \"{}\"", result);
		return result;
	};

	public String getBaseDirStr() {
		return this.baseDir.toString();
	}

	public void setBaseDirStr(String baseDirAbsolutePath) {
		this.baseDir = this.checkBaseDir(baseDirAbsolutePath);
	}

	public boolean clientAlreadyHasLastVersion(HttpServletRequest request, HttpServletResponse response, Path toRead) {
		long lastModTime = toRead.toFile().lastModified();
		ServletWebRequest extendedRequest = new ServletWebRequest(request, response);

		boolean notModified = extendedRequest.checkNotModified(lastModTime);
		return notModified;
	}

	public void handleContentDisposition(String fileName, HttpServletResponse response) {
		String attachementName = FilenameUtils.getName(fileName);
		try {
			attachementName = URLEncoder.encode(attachementName, "UTF8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} finally {
			log.debug("As attachement \"{}\"", attachementName);
			response.setHeader("Content-Disposition", "attachment;filename=" + attachementName);
		}
	}

	public void handleContentType(HttpServletRequest request, HttpServletResponse response, Path toRead) {
		ServletContext context = request.getServletContext();
		String mimeType = context.getMimeType(toRead.toString().toLowerCase());
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);
	}

}
