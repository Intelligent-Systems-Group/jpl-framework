package de.upb.cs.is.jpl.api.configuration.logging;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for managing the logging configuration. Most importantly it enables the
 * logging backend to use a custom logging configuration. Currently this uses the
 * {@link java.util.logging} package, in particular the {@link LogManager}.
 * 
 * @author Alexander Hetzer
 *
 */
public class LoggingConfiguration {

   private static final String ERROR_LOGGING_CONFIG_IO_EXCEPTION = "Could not configure logging, as of an IOException.";
   private static final String ERROR_LOGGING_CONFIG_FILE_NOT_FOUND = "Could not configure logging, as the configuration file was not found.";
   private static final String ERROR_LOGGING_CONFIG_SECURITY = "Could not configure logging due to security issues.";

   private static final String INFO_CREATED_DEFAULT_LOGS_FOLDER = "Created default logs folder at '%s'.";

   private static final String DEFAULT_LOGGING_PROPERTIES_PATH = "/configuration/logging.properties";
   private static final String CUSTOM_LOGGING_PROPERTIES_PATH = "configuration/logging.properties";

   private static final String DEFAULT_LOGGING_FOLDER_PATH = "logs";

   private static final Logger logger = LoggerFactory.getLogger(LoggingConfiguration.class);

   private static boolean isCustomLoggingConfigurationSet = false;


   /**
    * Empty default constructor to ensure this class is not instantiated.
    */
   private LoggingConfiguration() {
   }


   /**
    * Initiates the setup of the logging configuration. Ensures that the custom configuration is
    * only set once.
    */
   public static void setupLoggingConfiguration() {
      if (!isCustomLoggingConfigurationSet) {
         try {
            setupCustomLoggingConfiguration();
            if (!isCustomLoggingConfigurationSet) {
               setupDefaultLoggingConfiguration();
            }
         } catch (SecurityException e) {
            logger.error(ERROR_LOGGING_CONFIG_SECURITY, e);
         } catch (FileNotFoundException e) {
            logger.error(ERROR_LOGGING_CONFIG_FILE_NOT_FOUND, e);
         } catch (IOException e) {
            logger.error(ERROR_LOGGING_CONFIG_IO_EXCEPTION, e);
         }
      }
   }


   /**
    * Initializes the logging according to the logging configuration found in the given input
    * stream. Note that this method assumes that the given stream is valid and points to a valid
    * configuration file.
    * 
    * @param inputStreamOfLoggingConfigurationFile the valid stream of a valid logging configuration
    *           file
    * @throws IOException if there are any problems reading from the input stream
    */
   private static void initializeLoggingFromInputStream(InputStream inputStreamOfLoggingConfigurationFile) throws IOException {
      LogManager.getLogManager().readConfiguration(inputStreamOfLoggingConfigurationFile);
      isCustomLoggingConfigurationSet = true;
   }


   /**
    * Tries to read a custom logging configuration provided by the user and initialize the logging
    * accordingly.
    * 
    * @throws IOException if there are any problems reading from the file input stream.
    */
   private static void setupCustomLoggingConfiguration() throws IOException {
      File customLoggingFile = new File(CUSTOM_LOGGING_PROPERTIES_PATH);
      if (customLoggingFile.exists()) {
         initializeLoggingFromInputStream(new FileInputStream(customLoggingFile));
      }
   }


   /**
    * Setups the logging according to the default configuration stored inside the project.
    * 
    * @throws IOException if there are any problems reading from the input stream
    */
   private static void setupDefaultLoggingConfiguration() throws IOException {
      InputStream loggingConfigurationFileAsInputStream = LoggingConfiguration.class.getResourceAsStream(DEFAULT_LOGGING_PROPERTIES_PATH);
      if (loggingConfigurationFileAsInputStream != null) {
         initializeLoggingFromInputStream(loggingConfigurationFileAsInputStream);
         createDefaultLoggingFolderIfNecessary();
      }
   }


   /**
    * Creates the default logging folder, where the log files will be stored at, if it does not yet
    * exist.
    */
   private static void createDefaultLoggingFolderIfNecessary() {
      File defaultLoggingFolder = new File(DEFAULT_LOGGING_FOLDER_PATH);
      if (!defaultLoggingFolder.exists()) {
         defaultLoggingFolder.mkdirs();
         logger.info(String.format(INFO_CREATED_DEFAULT_LOGS_FOLDER, defaultLoggingFolder.getAbsolutePath()));
      }
   }

}
