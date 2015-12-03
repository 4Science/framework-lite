package it.cineca.sr.simplecms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsFilter implements Filter {

	protected FilterConfig filterConfig = null;

	protected static Logger log = LoggerFactory.getLogger(CmsFilter.class);

	/*
	 * key / value pairs
	 */
	private static final String[] CONFIGURATIONS = new String[] { "PROPERTY_FILE", "settings.properties",
			"CMS_FOLDER_PROPERTY_NAME", "CMS_FOLDER", "PREFIX_PATH", "", "DISPOSITION_INLINE",
			"htm html js css jpg png gif" };

	private static String PROPERTY_FILE = CONFIGURATIONS[0];
	private static String ASSETSSTORE_DIR_PROPERTY_NAME = CONFIGURATIONS[2];
	private static String PATH_PREFIX = CONFIGURATIONS[4];
	private static String DISPLOSITION_INLINE = CONFIGURATIONS[6];

	private static final String FILTER_APPLIED = "__cineca__CmsFilter__applied__";

	private CmsUtils cmsUtils;
	private String prefixPath;
	private List<String> dispositionInlineExtension;

	/* *********************************************************************************** */
	/* *** PARTE DI INIZIALIZZAZIONE *** */
	/* *********************************************************************************** */

	protected void initConfig(FilterConfig filterCfg) throws Exception {
		Properties props = loadConfigs(filterCfg);
		String assetsStorePropertyConfigName = props.getProperty(ASSETSSTORE_DIR_PROPERTY_NAME);
		String baseDir = props.getProperty(assetsStorePropertyConfigName);
		this.getCmsUtils().setBaseDirStr(baseDir);
		this.prefixPath = props.getProperty(PATH_PREFIX);
		String dispositionInlineString = props.getProperty(DISPLOSITION_INLINE);
		this.dispositionInlineExtension = Arrays.<String> asList(dispositionInlineString.split("\\s+"));
	}

	protected Properties loadConfigs(FilterConfig filterCfg) throws IOException {
		Properties defaultProperties = new Properties();
		for (int i = 0; i < CONFIGURATIONS.length; i += 2) {
			defaultProperties.setProperty(CONFIGURATIONS[i + 0], CONFIGURATIONS[i + 1]);
		}

		Properties filterProperties = new Properties();
		Enumeration<String> parametersNames = filterCfg.getInitParameterNames();
		while (parametersNames.hasMoreElements()) {
			String parameterName = parametersNames.nextElement();
			String parameterValue = filterCfg.getInitParameter(parameterName);
			filterProperties.setProperty(parameterName, parameterValue);
		}

		defaultProperties.putAll(filterProperties);
		String propertyFileName = defaultProperties.getProperty(PROPERTY_FILE);

		Properties externalProperties = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try (InputStream is = cl.getResourceAsStream(propertyFileName)) {
			if (is != null) {
				externalProperties.load(is);
			} else {
				log.warn("resource \"{}\" not found", propertyFileName);
			}
		}
		defaultProperties.putAll(externalProperties);
		return defaultProperties;
	}

	/**
	 * 
	 * @param basedirAbsolutePath
	 * @throws Exception
	 */
	public CmsFilter(String basedirAbsolutePath) {
		this.cmsUtils = new CmsUtils();
		this.cmsUtils.setBaseDirStr(basedirAbsolutePath);

	}

	public CmsFilter() throws Exception {
		this.cmsUtils = new CmsUtils();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		try {
			this.initConfig(filterConfig);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	protected CmsUtils getCmsUtils() {
		return cmsUtils;
	}

	protected String getPrefixPath() {
		return prefixPath;
	}

	protected List<String> getDispositionInlineExtension() {
		return dispositionInlineExtension;
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) rq;

		if (rq.getAttribute(FILTER_APPLIED) != null) {
			chain.doFilter(rq, rs);
		} else {
			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
			boolean isExternalResource = this.get((HttpServletRequest) rq, (HttpServletResponse) rs);
			if (!isExternalResource) {
				chain.doFilter(rq, rs);
			}
		}
	}

	private int prefixLength = -1;

	/**
	 * 
	 * @param request
	 * @return never start with "/"
	 */
	private String getResourceRelativePathSegmentFromRequest(HttpServletRequest request) {
		String pathAfterServlet = request.getRequestURI();
		if (prefixLength < 0) {
			String contextPath = request.getContextPath();
			if (contextPath == null) {
				contextPath = "/";
			}
			if (!contextPath.endsWith("/")) {
				contextPath = contextPath + "/";
			}

			String prefixPath = this.getPrefixPath();
			prefixPath = (prefixPath != null) ? prefixPath.trim() : "";
			if (prefixPath.startsWith("/")) {
				prefixPath = prefixPath.substring(1);
			}

			contextPath = contextPath + prefixPath;
			if (!contextPath.endsWith("/")) {
				contextPath = contextPath + "/";
			}

			this.prefixLength = contextPath.length();
		}
		String result = pathAfterServlet.substring(this.prefixLength);
		try {
			result = URLDecoder.decode(result.replace("/", File.separator), "UTF-8");
		} catch (UnsupportedEncodingException exc) {
			throw new IllegalStateException("UTF-8 not supported ?!?!?", exc);
		}

		return result;
	}

	public boolean get(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String relPath = getResourceRelativePathSegmentFromRequest(request);

		if (log.isDebugEnabled()) {
			log.debug("Access to \"{}\"", relPath);
		}
		Path toRead = this.getCmsUtils().ensurePathExistence(relPath);
		boolean externalResource;
		if (toRead != null) {
			String fileName = toRead.getFileName().toString();

			// Last-Modified date handling
			if (!this.getCmsUtils().clientAlreadyHasLastVersion(request, response, toRead)) {
				this.cmsUtils.handleContentType(request, response, toRead);

				this.handleContentDispositionAttachementIfNecessary(fileName, response, request);
				log.debug("file \"{}\" served", toRead);

				this.serveCustomResource(request, response, toRead);
			}
			externalResource = true;
		} else {
			log.debug("resource \"{}\" not in cms, forwarding to servletcontainer.", relPath);
			externalResource = false;
		}
		return externalResource;
	}

	protected void serveCustomResource(HttpServletRequest request, HttpServletResponse response, Path toRead)
			throws IOException {
		try (InputStream inStrm = Files.newInputStream(toRead, StandardOpenOption.READ)) {
			IOUtils.copy(inStrm, response.getOutputStream());
			response.flushBuffer();
		}
	}

	protected void handleContentDispositionAttachementIfNecessary(String fileName, HttpServletResponse response,
			HttpServletRequest request) {

		assert !fileName.endsWith("/");

		String fileNameExtension = FilenameUtils.getExtension(fileName);
		fileNameExtension = (fileNameExtension != null) ? fileNameExtension.toLowerCase() : "";
		log.debug("Extension \"{}\" detected in filename \"{}\"", fileNameExtension, fileName);

		boolean needContentDispositionAttachement = !getDispositionInlineExtension().contains(fileNameExtension);
		if (needContentDispositionAttachement) {
			this.getCmsUtils().handleContentDisposition(fileName, response);
		}
	}


}
