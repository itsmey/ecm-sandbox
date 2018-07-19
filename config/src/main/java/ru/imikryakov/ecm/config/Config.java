package ru.imikryakov.ecm.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Logger logger = LogManager.getLogger();

    private static final String DEFAULT_CONFIG_FILE = "ecm-sandbox.default.properties";
    private static final String CONFIG_FILE = "ecm-sandbox.properties";

    private static Properties defaultProps = new Properties();
    private static Properties props = new Properties();

    private static boolean isInitialized = false;

    private static void initConfig() {
        if (!isInitialized) {
            initDefaultProps();
            initProps();
            logger.info("default properties: " + defaultProps);
            logger.info("properties: " + props);
            isInitialized = true;
        }
    }

    private static void initDefaultProps() {
        try {
            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
            defaultProps.load(inputStream);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("cannot load default properties", e);
        }
    }

    private static void initProps() {
        try {
            InputStream inputStream = new FileInputStream(CONFIG_FILE);
            props.load(inputStream);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static String getProperty(String name, boolean exceptionOnNull) {
        if (!isInitialized) {
            initConfig();
        }
        String value = props.getProperty(name);
        if (value == null) {
            return checkValue(name, defaultProps.getProperty(name), exceptionOnNull);
        } else {
            return checkValue(name, value, exceptionOnNull);
        }
    }

    public static String getProperty(String name) {
        return getProperty(name, false);
    }

    private static String checkValue(String name, String value, boolean exceptionOnNull) {
        if (value == null && exceptionOnNull) {
            throw new RuntimeException("required property " + name + " not found!");
        } else {
            return value;
        }
    }
}
