package it.cineca.sr.simplecms.controller;

import it.cilea.core.spring.util.MessageUtil;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;

import org.elfinder.servlets.config.AbstractConnectorConfig;
import org.elfinder.servlets.fs.DiskFsImpl;
import org.elfinder.servlets.fs.IFsImpl;

/**
 * @author Antoine Walter (www.anw.fr)
 * @date 29 aug. 2011
 * @version $Id$
 * @license BSD
 */

/**
 * Sample of custom connector configuration.
 */
public class UploaderConfig extends AbstractConnectorConfig {

	private final MessageUtil msgUtils;

	private DiskFsImpl fsImpl;
	private String rootFsLocation;
	private String rootUrl;
	private String baseNameKey;
	private String thumbnailerBaseUrl;

	private final Map<String, String> initResourcereplace;

	private final ServletContext context;

	private long uploadMaxSize = 50;

	public UploaderConfig(MessageUtil msgUtils, ServletContext context, Map<String, String> pathShorter) {
		this.context = context;
		this.msgUtils = msgUtils;
		fsImpl = new DiskFsImpl();
		this.initResourcereplace = (pathShorter != null) ? pathShorter : Collections.EMPTY_MAP;
	}

	public String getThumbnailerBaseUrl() {
		return thumbnailerBaseUrl;
	}

	public void setThumbnailerBaseUrl(String thumbnailerBaseUrl) {
		this.thumbnailerBaseUrl = thumbnailerBaseUrl;
	}

	public IFsImpl getFs() {
		return fsImpl;
	}

	public String getRootFsLocation() {
		return rootFsLocation;
	}

	public void setRootFsLocation(String rootFsLocation) {
		this.rootFsLocation = rootFsLocation;
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public String getBaseNameKey() {
		return baseNameKey;
	}

	public void setBaseNameKey(String baseNameKey) {
		this.baseNameKey = baseNameKey;
	}

	@Override
	public String getFileUrl(File path) {
		String relativePath = getRelativePath(path);
		for (Map.Entry<String, String> entry : this.initResourcereplace.entrySet()) {
			String what = entry.getKey();
			if (relativePath.startsWith(what)) {
				String with = entry.getValue();
				relativePath = relativePath.replaceFirst(what, with);
				break;
			}
		}
		return this.getRootUrl() + relativePath;
	}

	@Override
	public String rootAliasOrBaseName() {
		String msg = this.msgUtils.findMessage(this.baseNameKey);
		return msg;
	}

	@Override
	public String getThumbnailUrl(File path) {
		return thumbnailerBaseUrl + getRelativePath(path);
	}

	@Override
	public boolean hasThumbnail(File path) {
		String mimeType = getMime(path);
		return (mimeType!=null) && mimeType.contains("image");
		// return false;
	}

	@Override
	public String getRoot() {
		return this.getRootFsLocation();
	}

	@Override
	public String getMime(File file) {
		String mimeType = (file!=null) ? context.getMimeType(file.toString().toLowerCase()) : null;
		return mimeType;
	}

	@Override
	public long getUploadMaxSize() {
		return this.uploadMaxSize;
	}

	public void setUploadMaxSize(long uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}
	
	
	
}
