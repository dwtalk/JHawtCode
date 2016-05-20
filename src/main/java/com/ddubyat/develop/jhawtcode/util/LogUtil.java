package com.ddubyat.develop.jhawtcode.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by dtalk on 5/19/16.
 *
 * @author dwtalk
 * @version 1.0.7
 * @since 2016-06-19
 */
public class LogUtil {

	private static Logger log = LoggerFactory.getLogger(LogUtil.class);

	static PrintWriter jhc;

	public LogUtil(PrintWriter jhc) {
		this.jhc = jhc;
	}

	/**
	 * Reads loggers name from the Log Manager of Log4j / Slf4j log Manager
	 */
	public static void read() {
		log.trace("Reading active loggers");
		Enumeration<org.apache.log4j.Logger> loggers = LogManager.getCurrentLoggers();
		List<String> logNames = new ArrayList<>();
		while (loggers.hasMoreElements()) {
			org.apache.log4j.Logger logger = loggers.nextElement();
			Level rootLevel = LogManager.getRootLogger().getLevel();
			Level thisLevel = logger.getLevel();
			String level;
			if(thisLevel == null) {
				if(rootLevel == null) {
					level = "UNDEFINED";
				} else {
					level = rootLevel.toString();
				}
			} else {
				level = thisLevel.toString();
			}
			logNames.add("\"" + logger.getName() + "\",\"" + level + "\"");
		}
		if(logNames.size() > 0) {
			Collections.sort(logNames);
			for (String name : logNames) {
				jhc.println(name);
			}
		} else {
			jhc.println("No loggers defined");
		}
	}

	/**
	 * Reads loggers level from the logger in Log Manager
	 * @param logName The name of the logger, probably fqn
	 */
	public static void read(String logName) {
		log.trace("Reading log level for logger {}", logName);
		Level rootLevel = LogManager.getRootLogger().getLevel();
		org.apache.log4j.Logger logger = LogManager.getLogger(logName);
		Level level = logger.getLevel();
		if (level == null) {
			level = rootLevel;
		}
		if(level != null) {
			jhc.println(level.toString());
		} else {
			log.trace("Log level error, uninitialized or inaccessible");
			jhc.println("Error, log4j may not have been initialized");
		}
	}

	/**
	 * Sets the logger to the log level in Log Manager, new loggers are acceptable
	 * @param logName The name of the logger, probably fqn
	 * @param logLevel the string version of the log level we should set
	 */
	public static void set(String logName, String logLevel) {
		log.trace("Setting log level, {} , for logger {}", logLevel, logName);
		if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(logName)) {
			org.apache.log4j.Logger logger = LogManager.getLogger(logName);
			try {
				if (logger != null) {
					logger.setLevel(Level.toLevel(logLevel));
					jhc.println("Log level for " + logName + " has been set to " + logLevel.toUpperCase());
				} else {
					log.trace("Logger not found");
					jhc.println("ERROR");
				}
			} catch (Exception e) {
				log.trace("Exception occurred: " + e.getMessage());
				jhc.println("ERROR");
			}
		}
	}

}
