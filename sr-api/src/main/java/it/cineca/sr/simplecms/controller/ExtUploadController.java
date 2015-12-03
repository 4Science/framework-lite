package it.cineca.sr.simplecms.controller;

import it.cilea.core.CoreConstant;
import it.cilea.core.spring.controller.Spring3CoreController;
import it.cineca.sr.simplecms.CmsUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.elfinder.servlets.ConnectorException;
import org.elfinder.servlets.FileItem;
import org.elfinder.servlets.commands.AbstractCommand;
import org.elfinder.servlets.commands.AbstractCommandOverride;
import org.elfinder.servlets.commands.ContentCommand;
import org.elfinder.servlets.commands.OpenCommand;
import org.elfinder.servlets.config.AbstractConnectorConfig;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = ExtUploadController.BASE_CONTROLLER_URL)
public class ExtUploadController extends Spring3CoreController {

	private static final String MAX_UPLOAD_SIZE_PROP_NAME = "MAX_UPLOAD_SIZE";

	private static final String ELFINDER_HTM = "elfinder.htm";

	private static final String CMS_ROOT_ASSETS_STORE_NAME = "label.cms.rootAssetsStoreName";

	private static final String FILE_PARAM = "f";

	private static final String ASSETSSTORE_DIR_PROPERTY_NAME = "ASSETS_STORE";
	private static final String SETTINGS_PROPERTIES_FILE_NAME = "settings.properties";

	public static final String BASE_CONTROLLER_URL = "/cms";
	public static final String FS_CONNECTOR_PATH = "/connector.json";
	public static final String THUMBNAILER_PATH = "/thumbnailer";
	private static final String DOWNLOADPATH_PATH = "/download";


	private static final String MAPPINGS_PROPERTY_NAME = "ASSETSSTORE_RESOURCE_MAPPINGS";

	private String thumbnailerRequest = null;

	/**
	 * JSON response.
	 */
	private Map<String, Object> json;
	
	/**
	 * Parameters read from request.
	 */
	private Map<String, Object> requestParams;
	
	/**
	 * Files read from request.
	 */
	private List<FileItem> listFiles;
	
	/**
	 * Files data read from request.
	 */
	private List<ByteArrayOutputStream> listFileStreams;

	private CmsUtils pathUtils;
	private final Map<String, String> pathMappings;

	private String maxUploadSizeInByte;

	public ExtUploadController() throws Exception {
		try {
			Properties props = new Properties();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream is = cl.getResourceAsStream(SETTINGS_PROPERTIES_FILE_NAME);
			props.load(is);
			String baseDirStr = props.getProperty(ASSETSSTORE_DIR_PROPERTY_NAME);
			if (baseDirStr == null) {
				throw new IllegalStateException("Property " + ASSETSSTORE_DIR_PROPERTY_NAME + " not found in "
						+ SETTINGS_PROPERTIES_FILE_NAME + " of module MA");
			}
			this.pathUtils = new CmsUtils();
			this.pathUtils.setBaseDirStr(baseDirStr);

			String resourceMappings = props.getProperty(MAPPINGS_PROPERTY_NAME);
			this.pathMappings = this.computePathTranslation(resourceMappings);

			this.ensureDirs(CoreConstant.ALL_MODULE_LIST);
			
			this.maxUploadSizeInByte = props.getProperty(MAX_UPLOAD_SIZE_PROP_NAME);

		} catch (IOException exc) {
			log.error(String.format("Initializing SimpleCms reading classpath resource \"%s\" ",
					SETTINGS_PROPERTIES_FILE_NAME), exc);
			throw new Exception(exc);
		}
	}

	private void ensureDirs(String moduleList) {
		if (moduleList != null) {
			String[] modules = moduleList.split(",");
			for (String module : modules) {
				Path moduleDir = this.pathUtils.getBaseDir().resolve(module);
				try {
					Files.createDirectory(moduleDir);
				} catch (FileAlreadyExistsException exc) {
					// OK
				} catch (IOException exc) {
					log.info("Error creating cmsFolder \"" + moduleDir + "\"", exc);
				}
			}
		}
	}

	private Map<String, String> computePathTranslation(String resourceMappings) {
		Map<String, String> initResourcereplace = new HashMap<>();
		if (resourceMappings != null) {
			String[] resourceMappingsArray = resourceMappings.split(",");

			for (String oneMappingStr : resourceMappingsArray) {
				String[] mappingParts = oneMappingStr.split("=>");
				if (mappingParts.length != 2) {
					throw new RuntimeException();
				}
				String what = mappingParts[0].trim();
				if (what.startsWith("/")) {
					what = what.substring(1);
				}
				String with = mappingParts[1].trim();
				initResourcereplace.put(what, with);
			}
		}
		return initResourcereplace;
	}

	/**
	 * This function must be implemented to return appropriate configuration.<br>
	 * A single servlet can manage multiple configurations and be used as backend of multiple ElFinder clients.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected AbstractConnectorConfig prepareConfig(HttpServletRequest request) throws Exception {
		UploaderConfig result = new UploaderConfig(this.messageUtil, request.getServletContext(), this.pathMappings);
		
		if(this.maxUploadSizeInByte!=null) {
			long maxUploadBytes = Long.parseLong(this.maxUploadSizeInByte);
			result.setUploadMaxSize(maxUploadBytes / (1024 * 1024));
		}
		result.setBaseNameKey(CMS_ROOT_ASSETS_STORE_NAME);

		thumbnailerRequest = request.getContextPath() + BASE_CONTROLLER_URL + THUMBNAILER_PATH
				+ BASE_CONTROLLER_URL + THUMBNAILER_PATH + "/";

		result.setThumbnailerBaseUrl(thumbnailerRequest);
		result.setRootFsLocation(this.pathUtils.getBaseDirStr());
		
		// mettendolo vuoto e impostando i nomi dei moduli
		// esattamente come sono mappati su tomcat
		// ottengo il link per leggere le risorse
		String rootUrl = request.getContextPath() + BASE_CONTROLLER_URL + DOWNLOADPATH_PATH + "?" + FILE_PARAM + "=";
		result.setRootUrl(rootUrl);
		return result;
	}

	@RequestMapping(value = ELFINDER_HTM)
	public ModelAndView index() {
		ModelAndView result = new ModelAndView("simplecms/elfinder");
		result.addObject("connectorUrl", BASE_CONTROLLER_URL + FS_CONNECTOR_PATH);

		String language = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("lang", language);
		return result;
	}
	
	/**
	 * Processing a new request from ElFinder client.
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = FS_CONNECTOR_PATH, method = { RequestMethod.GET, RequestMethod.POST })
	public void rootAssetsStoreConnector(HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("processing request: " + request.getRequestURI() + request.getQueryString());
		}

		// important: set encoding BEFORE writing anything in the response...
		response.setCharacterEncoding("UTF-8");
		//HttpUtils.noCache(response);

		// parse request parameters and files...
		parseRequest(request, response);

		json = new HashMap<>();

		try {
			// set configuration
			AbstractConnectorConfig config = prepareConfig(request);
			if (config == null) {
				throw new Exception("Configuration problem");
			}

			// prepare command and run
			AbstractCommand command = prepareCommand((String) requestParams.get("cmd"), request, response, config);
			try {
				command.execute();
			} catch (ConnectorException e) {
				log.warn("command returned an error", e);
				putResponse("error", e.getMessage());
			}

			// append init info if needed
			if (command.mustRunInit()) {
				try {
					command.initCommand();
				} catch (ConnectorException e) {
					log.warn("command returned an error", e);
					putResponse("error", e.getMessage());
				}
			}

			// output if command didn't do it
			if (!command.isResponseOutputDone()) {
				output(response, command.isResponseTextHtml(), json, command.getResponseWriter());
				command.setResponseOutputDone(true);
			}
		} catch (Exception e) {
			log.error("Unknown error", e);
			putResponse("error", "Unknown error");

			// output the error
			try {
				output(response, false, json, response.getWriter());
			} catch (Exception ee) {
				log.error("", ee);
			}
		}

		// close streams
		if (listFileStreams != null) {
			for (ByteArrayOutputStream os : listFileStreams) {
				try {
					os.close();
				} catch (Exception e) {}
			}
		}
	}

	
		
	protected void output(HttpServletResponse response, boolean isResponseTextHtml, Map<String, Object> json,
			PrintWriter responseWriter) {
		// encoding was already set by servlet
		if (isResponseTextHtml) {
			response.setContentType("text/html; charset=UTF-8");
		} else {
			response.setContentType("application/json; charset=UTF-8");
		}

		try {
			Gson gson = new Gson();
			gson.toJson(json, responseWriter);
			//json.write(responseWriter);
		} catch (Exception e) {
			log.error("", e);
		}

		AbstractCommand.closeWriter(responseWriter);
	}

	/**
	 * Parse request parameters and files.
	 * @param request
	 * @param response
	 */
	protected void parseRequest(HttpServletRequest request, HttpServletResponse response) {
		requestParams = new HashMap<String, Object>();
		listFiles = new ArrayList<FileItem>();
		listFileStreams = new ArrayList<ByteArrayOutputStream>();

		// Parse the request
		if (ServletFileUpload.isMultipartContent(request)) {
			// multipart request
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				
				Iterator<String> fieNamesIter = multipartRequest.getFileNames();
				//FileItemIterator iter = upload.getItemIterator(request);
				while (fieNamesIter.hasNext()) {
					String fileName = fieNamesIter.next();
					for (MultipartFile multipartFile : multipartRequest.getFiles(fileName)) {
					//String name = item.getFieldName();
						InputStream stream = multipartFile.getInputStream();
						String name = multipartFile.getOriginalFilename();
						if (name != null && !"".equals(name.trim())) {
							FileItem item = new FileItem();
							item.setName(name);
							listFiles.add(item);
							ByteArrayOutputStream os = new ByteArrayOutputStream();
							IOUtils.copy(stream, os);
							listFileStreams.add(os);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Unexpected error parsing multipart content", e);
			}
		} //else {
			// not a multipart
			for (Object mapKey : request.getParameterMap().keySet()) {
				String mapKeyString = (String) mapKey;

				if (mapKeyString.endsWith("[]")) {
					// multiple values
					String values[] = request.getParameterValues(mapKeyString);
					List<String> listeValues = new ArrayList<String>();
					for (String value : values) {
						listeValues.add(value);
					}
					requestParams.put(mapKeyString, listeValues);
				} else {
					// single value
					String value = request.getParameter(mapKeyString);
					requestParams.put(mapKeyString, value);
				}
			}
		//}
	}

	/**
	 * Instantiate command implementation and prepare it before execution.
	 * 
	 * @param commandStr
	 * @param request
	 * @param response
	 * @param config
	 * @return
	 */
	protected AbstractCommand prepareCommand(String commandStr, HttpServletRequest request, HttpServletResponse response, AbstractConnectorConfig config) {
		if (commandStr != null) {
			commandStr = commandStr.trim();
		}

		if (commandStr == null && "POST".equals(request.getMethod())) {
			putResponse("error", "Data exceeds the maximum allowed size");
		}

		if (!config.isCommandAllowed(commandStr)) {
			putResponse("error", "Permission denied");
		}

		AbstractCommand command = null;
		if (commandStr != null) {
			command = instanciateCommand(commandStr);
			if (command == null) {
				putResponse("error", "Unknown command");
			}
		} else {
			String[] values = request.getParameterValues("current");
			String current = (values!=null && values.length>0) ? values[0] : null; 
			if (current != null) {
				command = new OpenCommand();
			} else {
				command = new ContentCommand();
			}
		}

		if (command != null) {
			command.setRequest(request);
			command.setResponse(response);
			command.setJson(json);
			command.setRequestParameters(requestParams);
			command.setListFiles(listFiles);
			command.setListFileStreams(listFileStreams);
			command.setConfig(config);
			command.init();
		} else {
			log.error("Command not found: [{}]", commandStr);
		}

		return command;
	}

	/**
	 * Instanciate a command from its name.
	 * @param commandName
	 * @return
	 */
	protected AbstractCommand instanciateCommand(String commandName) {
		AbstractCommand instance = null;
		try {
			Class<AbstractCommand> clazz = getCommandClass(commandName);
			if (clazz != null) {
				instance = clazz.newInstance();
				if (instance == null) {
					throw new Exception("Command not found : " + commandName);
				}
			}
		} catch (Exception e) {
			// instance will be null
			log.error("Could not instanciate connector configuration", e);
		}
		return instance;
	}

	/**
	 * Get command class for a command name.
	 * @param commandName
	 * @return
	 */
	protected Class<AbstractCommand> getCommandClass(String commandName) {
		// do we have override for command?
		Class<AbstractCommand> clazz = getCommandClassOverride(commandName);
		if (clazz == null) {
			// no override, use the default command
			clazz = getCommandClassDefault(commandName);
		}
		return clazz;
	}

	/**
	 * Get default implementation class for a command.
	 * @param commandName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Class<AbstractCommand> getCommandClassDefault(String commandName) {
		String className = AbstractCommand.class.getPackage().getName() + "." +StringUtils.capitalize(commandName) + "Command";
		Class<AbstractCommand> clazz = null;
		try {
			clazz = (Class<AbstractCommand>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			// not found
		}
		return clazz;
	}

	/**
	 * Get override implementation class for a command.
	 * @param commandName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Class<AbstractCommand> getCommandClassOverride(String commandName) {
		String className = AbstractCommandOverride.class.getPackage().getName() + "." + StringUtils.capitalize(commandName) + "CommandOverride";
		Class<AbstractCommand> clazz = null;
		try {
			clazz = (Class<AbstractCommand>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			// not found
		}
		return clazz;
	}

	/**
	 * Append data to JSON response.
	 * @param param
	 * @param value
	 */
	protected void putResponse(String param, Object value) {
		json.put(param, value);
	}

	@RequestMapping(value = THUMBNAILER_PATH + "/**", method = { RequestMethod.GET })
	public void imageThumbnailer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// String widthStr = request.getParameter("w");
		// int width = (widthStr == null) ? 150 :
		// Integer.valueOf(widthStr.trim());
		int width = 150;

		String fullPath = request.getRequestURI();
		String path = fullPath.substring(this.thumbnailerRequest.length());
		try {
			path = URLDecoder.decode(path.replace("/", File.separator), "UTF-8");
		} catch (UnsupportedEncodingException exc) {
			throw new IllegalStateException("UTF-8 not supported ?!?!?", exc);
		}

		// security
		Path imgFile = this.pathUtils.computeFullPath(path, false);

		if (imgFile != null) {
			if (!this.pathUtils.clientAlreadyHasLastVersion(request, response, imgFile)) {
				File f = imgFile.toFile();
				BufferedImage image = ImageIO.read(f);
				BufferedImage b = Scalr.resize(image, Method.SPEED, Mode.FIT_TO_WIDTH, width);
				ImageIO.write(b, "png", response.getOutputStream());
			}

		}
	}
	
	@RequestMapping(value = DOWNLOADPATH_PATH, method = { RequestMethod.GET })
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = request.getParameter(FILE_PARAM);
		try {
			path = URLDecoder.decode(path.replace("/", File.separator), "UTF-8");
		} catch (UnsupportedEncodingException exc) {
			throw new IllegalStateException("UTF-8 not supported ?!?!?", exc);
		}
		Path toRead = this.pathUtils.ensurePathExistence(path);
		if (toRead != null) {
			this.pathUtils.handleContentType(request, response, toRead);
			this.pathUtils.handleContentDisposition(toRead.getFileName().toString(), response);
			try (InputStream inStrm = Files.newInputStream(toRead, StandardOpenOption.READ)) {
				IOUtils.copy(inStrm, response.getOutputStream());
				response.flushBuffer();
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
