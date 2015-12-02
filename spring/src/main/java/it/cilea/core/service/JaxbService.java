package it.cilea.core.service;

import it.cilea.core.jaxb.util.JaxbUtil;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.springframework.stereotype.Service;

@Service
public class JaxbService {
	
	public Writer marshal(Object object, String outputType, boolean marshalAsSelectable) throws Exception {
		if (!marshalAsSelectable) {
			return marshal(object, outputType);
		} else {
			Class clazz = JaxbUtil.getJaxbBindingClass(object);
			String bindingFile = JaxbUtil.getJaxbBindingFile(clazz, true);
			return marshal(object, clazz, outputType, bindingFile, null);
		}
	}

	public Writer marshal(Object object, String outputType, boolean marshalAsSelectable, InputStream xslStream)
			throws Exception {
		if (!marshalAsSelectable) {
			return marshal(object, outputType);
		} else {
			Class clazz = JaxbUtil.getJaxbBindingClass(object);
			String bindingFile = JaxbUtil.getJaxbBindingFile(clazz, true);
			return marshal(object, clazz, outputType, bindingFile, xslStream);
		}
	}

	public Writer marshal(Object object, String outputType) throws Exception {
		Class clazz = JaxbUtil.getJaxbBindingClass(object);
		String bindingFile = JaxbUtil.getJaxbBindingFile(clazz, false);
		return marshal(object, clazz, outputType, bindingFile, null);
	}

	public Writer marshal(Object object, String outputType, InputStream xslStream) throws Exception {
		Class clazz = JaxbUtil.getJaxbBindingClass(object);
		String bindingFile = JaxbUtil.getJaxbBindingFile(clazz, false);
		return marshal(object, clazz, outputType, bindingFile, xslStream);
	}

	public Writer marshal(Object object, Class clazz, String outputType, String modelFilePath, InputStream xslStream)
			throws Exception {

		Map<String, Object> properties = new HashMap<String, Object>();

		boolean json = false;

		if ("json".equals(outputType)) {
			properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
			properties.put(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
			properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
			json = true;
		}

		Marshaller marshaller = null;
		Writer output = new StringWriter();
		ClassLoader classLoader = this.getClass().getClassLoader();

		InputStream modelStream = null;
		modelStream = classLoader.getResourceAsStream(modelFilePath);
		if (modelStream != null) {
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, modelStream);
		} else
			throw new RuntimeException("It's not possible retrieve the widget JAXB_MODEL_CLASS_FILE stream ");

		JAXBContext ctx = null;

		try {
			ctx = JAXBContext.newInstance(new Class[] { clazz }, properties);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(object, output);

		if (!json && xslStream != null) {
			StringReader marshalXml = new StringReader(output.toString());
			Writer resultXml = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer(new StreamSource(xslStream));
			transformer.transform(new StreamSource(marshalXml), new StreamResult(resultXml));
			modelStream.close();
			marshalXml.close();
			xslStream.close();
			return resultXml;
		} else {
			modelStream.close();
			return output;
		}
	}

	public Writer marshal(Object object, Class clazz, String outputType, String modelFilePath[], InputStream xslStream)
			throws Exception {

		Map<String, Object> properties = new HashMap<String, Object>();

		boolean json = false;

		if ("json".equals(outputType)) {
			properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
			properties.put(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
			properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
			json = true;
		}

		Marshaller marshaller = null;
		Writer output = new StringWriter();
		ClassLoader classLoader = this.getClass().getClassLoader();

		List<Object> fileList = new ArrayList<Object>();
		for (String filePath : modelFilePath) {
			InputStream file1 = classLoader.getResourceAsStream(filePath);
			fileList.add(file1);
		}
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, fileList);

		JAXBContext ctx = null;
		try {
			ctx = JAXBContext.newInstance(new Class[] { clazz }, properties);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(object, output);

		if (!json && xslStream != null) {
			StringReader marshalXml = new StringReader(output.toString());
			Writer resultXml = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer(new StreamSource(xslStream));
			transformer.transform(new StreamSource(marshalXml), new StreamResult(resultXml));
			// modelStream.close();
			marshalXml.close();
			xslStream.close();
			return resultXml;
		} else {
			// modelStream.close();
			return output;
		}
	}

}
