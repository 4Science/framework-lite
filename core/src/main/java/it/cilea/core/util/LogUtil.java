package it.cilea.core.util;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * Classe di utilità per la gestione a runtime dei logger e degli appender
 * 
 * @author ccarretti
 *
 */
public class LogUtil {

	public static final String APPENDER_QUARTZ_NAME = "quartztriggerlog";

	/**
	 * Aggiunge un appender estratto da un logger sorgente, aggiungendo lo a un
	 * logger destinazione
	 * 
	 * @param destNameLogger
	 *            - Nome logger destinazione a cui aggiungere l'appender
	 * @param srcNameLogger
	 *            - Nome logger sorgente da cui estrarre l'appender
	 * @param appenderName
	 *            - Nome dell'appender da estrarre
	 * 
	 * @return
	 */
	public static boolean addAppender(String destNameLogger, String srcNameLogger, String appenderName) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger dl = lc.getLogger(destNameLogger);
		Logger sl = lc.getLogger(srcNameLogger);
		if (sl != null) {
			Appender<ILoggingEvent> a = sl.getAppender(appenderName);
			if (dl != null && a != null) {
				dl.addAppender(a);
				return true;
			}
		}
		return false;
	}


	/**
	 * Rimuove un appender da un dato logger
	 * 
	 * @param nameLogger
	 *            - Nome logger a cui rimuover l'appender
	 * @param appenderName
	 *            - Nome dell'appendere da rimuovere
	 * 
	 * @return
	 */
	public static boolean removeAppender(String nameLogger, String appenderName) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger dl = lc.getLogger(nameLogger);
		if (dl != null) {
			return dl.detachAppender(appenderName);
		}
		return false;
	}

}
