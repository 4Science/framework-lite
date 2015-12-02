package it.cilea.core.i18n.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.web.multipart.MultipartFile;

import it.cilea.core.i18n.conf.CileaMessageSource;
import it.cilea.core.i18n.conf.DatabaseReloadableMessageSource;
import it.cilea.core.util.MessageUtilConstant;

/**
 * externalStaticProperties / List<I18n> utils and filters
 *
 * @since 4.2Specifiche tecniche per la compilazione e l'uso degli attributi v.
 *        2.2 19 Giugno
 */
public class I18nUtil {

	final static String pattern = "^[a-zA-Z0-9 \\.\\(\\)\\{\\}\\[\\]_,\\?<>-\\\\-=]*$";

	public static void reload(AbstractMessageSource dynamicMessageSource) throws Exception {

		boolean done = false;
		String error = "";
		try {
			((DatabaseReloadableMessageSource) dynamicMessageSource).reload();

		} catch (Exception e) {
			error = error + "error: " + e.toString();
		}

		if (!done) {
			// au non possiede questo oggetto
			try {
				((DatabaseReloadableMessageSource) ((CileaMessageSource) MessageUtilConstant.messageUtil
						.getMessageSource()).getDynamicMessageSource()).reload();
				done = true;
			} catch (Exception e) {
				error = error + " - error: " + e.toString();
			}
		}

		if (!done) {
			throw new Exception("reload i18n exception " + error);
		}

	}

	public static String getModuleKeyValue(String moduleName, String locale, String key,
			Map<Locale, Map<String, Map<String, String>>> externalStaticProperties) {

		if (moduleName == null)
			moduleName = "";

		String keyValue = null;
		Locale l = Locale.forLanguageTag(locale);

		if (externalStaticProperties.containsKey(l)) {

			Map<String, Map<String, String>> localizedExternalStaticProperties = externalStaticProperties.get(l);

			if (localizedExternalStaticProperties != null) {

				if (localizedExternalStaticProperties.containsKey(moduleName)) {

					Map<String, String> m = localizedExternalStaticProperties.get(moduleName);
					if (m != null) {
						if (m.containsKey(key)) {

							keyValue = m.get(key);
						}
					}
				}
			}

		}

		return keyValue;
	}

	public static byte[] getByteFromMultipartFile(MultipartFile mf) throws IOException {

		InputStream is = mf.getInputStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[(int) mf.getSize()];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		buffer.close();

		// System.out.println(" mf.getSize() " + mf.getSize());

		return data;

	}

	/**
	 * Si occupa di scriver eil buffer nella response
	 * 
	 * @param buffer
	 * @param response
	 */
	public static void writeStreamContext(Properties prop, HttpServletResponse response, String fileName, Boolean sort,
			String charset) {
		if (StringUtils.isBlank(charset))
			charset = "UTF-8";
		// response.setContentType("text/plain");
		// response.setHeader("Content-disposition", "attachment;filename=\"" +
		// fileName + "\"");
		// ServletOutputStream out;
		// try {
		// out = response.getOutputStream();
		// prop.store(out, null);
		// out.flush();
		// } catch (IOException e) {
		// throw new
		// IllegalArgumentException("Errore durante l'esportazione dell'i18n");
		// }

		response.setContentType("text/plain");
		response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
		ServletOutputStream out;

		try {

			out = response.getOutputStream();

			String inte = "# " + new Date(System.currentTimeMillis());
			out.write(inte.getBytes(charset));
			out.write(System.lineSeparator().getBytes(charset));

			Set<String> set;
			if (sort) {
				set = new TreeSet<String>(prop.stringPropertyNames());
			} else
				set = prop.stringPropertyNames();

			for (String key : set) {

				if (key != null) {

					String value = prop.getProperty(key);

					key = key.replace("\\", "\\\\");
					key = key.replace("=", "\\=");
					key = key.replace(" ", "\\ ");
					key = key.replace("\r\n", "\\\r\n");
					if (value != null) {
						value = StringUtils.replace(value, "\r\n", "\n");
						value = StringUtils.replace(value, "\r", "\n");
						value = StringUtils.replace(value, "\n", "\\\r\n");
					} else {
						value = "";
					}
					String line = key + "=" + value;

					byte[] b = line.getBytes(charset);

					out.write(b);
					out.write(System.lineSeparator().getBytes(charset));

				}

			} // for

			out.flush();
			out.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
