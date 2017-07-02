package de.upb.cs.is.jpl.cli.common;


import java.io.File;

import org.junit.Before;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The abstract unit test class which contains all the root resource directory paths and the
 * algorithms, evaluations and metric tests needs to implement this class with their resource level
 * directory and some additional path required.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class AUnitTest {
   private static final String PATH_TO_TEST_RESSOURCES = "src" + File.separator + "test" + File.separator + "resources" + File.separator;

   private String additionalResourcePath;
   private String resourceDirectoryLevel;


   /**
    * Creates a new unit test with the resource level directory path.
    * 
    * @param resourceDirectoryLevel the resource level directory
    */
   public AUnitTest(String resourceDirectoryLevel) {
      this.resourceDirectoryLevel = resourceDirectoryLevel;
      this.additionalResourcePath = StringUtils.EMPTY_STRING;
   }


   /**
    * Creates a new unit test with the additional path to the resources given.
    * 
    * @param resourceDirectoryLevel the resource level directory
    * @param additionalResourcePath the additional path to the resources
    */
   public AUnitTest(String resourceDirectoryLevel, String additionalResourcePath) {
      this.resourceDirectoryLevel = resourceDirectoryLevel;
      this.additionalResourcePath = additionalResourcePath;
   }


   /**
    * Returns the path to the test resource directory as a relative path to the resource root
    * directory. Note: This method should NOT be used by any test implementations. It is only used
    * internally.
    * 
    * @param the path to the test resource directory as a relative path to the resource root
    *           directory
    */
   protected String getTestRessourcePathRelativeToResourceRootDirectory() {
      return resourceDirectoryLevel + additionalResourcePath;
   }


   /**
    * Returns the path to the test resources of the implementation of this class.
    * 
    * @return the path to the test resources
    */
   protected String getTestRessourcePath() {
      return PATH_TO_TEST_RESSOURCES + getTestRessourcePathRelativeToResourceRootDirectory();
   }


   /**
    * Returns the path to the test resource file with the given name.
    * 
    * @param fileName the name of the file to add to the test resource path, including the extension
    * @return the path to the test resources
    */
   protected String getTestRessourcePathFor(String fileName) {
      return getTestRessourcePath() + fileName;
   }


   /**
    * Sets this test up.
    */
   @Before
   public void setupTest() {
      setupLogging();
   }


   /**
    * Initializes the logging.
    */
   protected void setupLogging() {
      LoggingConfiguration.setupLoggingConfiguration();
   }

}
