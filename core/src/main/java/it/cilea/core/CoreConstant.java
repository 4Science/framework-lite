package it.cilea.core;

import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class CoreConstant {

	static {
		try {
			Properties props = new Properties();
			ClassLoader cl = CoreConstant.class.getClassLoader();
			InputStream is = cl.getResourceAsStream("settings.properties");
			props.load(is);

			if (StringUtils.isNotBlank(props.getProperty("HIGHLIGHT_PLSQL_FUNCTION")))
				HIGHLIGHT_PLSQL_FUNCTION = props.getProperty("HIGHLIGHT_PLSQL_FUNCTION");

			if (StringUtils.isNotBlank(props.getProperty("LIMITA_MAIL")))
				LIMITA_MAIL = new Boolean(props.getProperty("LIMITA_MAIL"));

			if (StringUtils.isNotBlank(props.getProperty("INDIRIZZO_MAIL_LIMITATA")))
				INDIRIZZO_MAIL_LIMITATA = props.getProperty("INDIRIZZO_MAIL_LIMITATA");

			if (StringUtils.isNotBlank(props.getProperty("MODULE_NAME")))
				MODULE_NAME = props.getProperty("MODULE_NAME");
			if (StringUtils.isNotBlank(props.getProperty("I18N_LIST")))
				I18N_LIST = props.getProperty("I18N_LIST");
			if (StringUtils.isNotBlank(props.getProperty("ALL_MODULE_LIST")))
				ALL_MODULE_LIST = props.getProperty("ALL_MODULE_LIST");
			if (StringUtils.isNotBlank(props.getProperty("I18N_MODULE_LIST")))
				I18N_MODULE_LIST = props.getProperty("I18N_MODULE_LIST");
			if (StringUtils.isNotBlank(props.getProperty("RELOAD_MODULE_LIST")))
				RELOAD_MODULE_LIST = props.getProperty("RELOAD_MODULE_LIST");
			if (StringUtils.isNotBlank(props.getProperty("I18N_BASE")))
				I18N_BASE = props.getProperty("I18N_BASE");

			if (StringUtils.isNotBlank(props.getProperty("SCHEMA_MA")))
				SCHEMA_MA = props.getProperty("SCHEMA_MA");

			if (StringUtils.isNotBlank(props.getProperty("SCHEMA_GA")))
				SCHEMA_GA = props.getProperty("SCHEMA_GA");

			if (StringUtils.isNotBlank(props.getProperty("TOMCAT_SURP_BALANCER_URL")))
				TOMCAT_SURP_BALANCER_URL = props.getProperty("TOMCAT_SURP_BALANCER_URL");

			if (StringUtils.isNotBlank(props.getProperty("TOMCAT_SURP_BALANCER_URL2")))
				TOMCAT_SURP_BALANCER_URL2 = props.getProperty("TOMCAT_SURP_BALANCER_URL2");

			if (StringUtils.isNotBlank(props.getProperty("AU_FULL_PATH")))
				AU_FULL_PATH = props.getProperty("AU_FULL_PATH");

			MESSAGE_PROPERTIES = props.getProperty("MESSAGE_PROPERTIES");

		} catch (IOException e) {
			throw new ExceptionInInitializerError();
		}
	}

	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String DATE_PATTERN_JAVASCRIPT = "%d/%m/%Y";

	public static Boolean LIMITA_MAIL;
	public static String INDIRIZZO_MAIL_LIMITATA;
	public static String HIGHLIGHT_PLSQL_FUNCTION;

	public static final String MESSAGE_PROPERTIES;

	public static String MODULE_NAME;

	public static String RELOAD_MODULE_LIST;

	public static String I18N_MODULE_LIST;
	public static String I18N_LIST;

	public static String I18N_BASE;

	public static String ALL_MODULE_LIST;

	public static String TOMCAT_SURP_BALANCER_URL;
	public static String TOMCAT_SURP_BALANCER_URL2;

	public static String AU_FULL_PATH;

	public static String SCHEMA_MA;
	public static String SCHEMA_GA;

	// comune a SearchStrategyData e XmlValidatorFactory
	public final static Format dateFormat = new SimpleDateFormat(DATE_PATTERN);
	public final static String dataPatternString = "^(((0?[1-9]|[12]\\d|3[01])/(0?[13578]|1[02])/((1[6-9]|[2-9]\\d)?\\d{2}))|((0?[1-9]|[12]\\d|30)/(0?[13456789]|1[012])/((1[6-9]|[2-9]\\d)?\\d{2}))|((0?[1-9]|1\\d|2[0-8])/0?2/((1[6-9]|[2-9]\\d)?\\d{2}))|(29/0?2/((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00)))$";
	public final static Pattern dataPattern = Pattern.compile(dataPatternString);
	public final static String dbIdPatternString = "^[0-9]{0,10}$";
	public final static Pattern dbIdPattern = Pattern.compile(dbIdPatternString);
	public final static String integerPatternString = "^[0-9]{0,10}$";
	public final static Pattern integerPattern = Pattern.compile(integerPatternString);

}
