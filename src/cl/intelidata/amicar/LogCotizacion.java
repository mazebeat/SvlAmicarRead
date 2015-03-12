package cl.intelidata.amicar;

import org.slf4j.LoggerFactory;

public class LogCotizacion {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(LogCotizacion.class);

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void error(String msg, Throwable e) {
		logger.error(msg, e);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(Throwable e) {
		logger.error(e.getMessage(), e);
	}

}