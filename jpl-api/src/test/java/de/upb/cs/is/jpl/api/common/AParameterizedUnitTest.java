package de.upb.cs.is.jpl.api.common;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AEvaluation;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The {@link AParameterizedUnitTest} defines common functionality for unit tests checking
 * parameters of objects which can be defined or configured via a JSON file, i.e.
 * {@link AEvaluation}. Furthermore it offers functionality to obtain the full default configuration
 * file path for a given file name and the path itself. All tests checking something on a subclass
 * of {@link AJsonConfiguration} should subclass this class.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AParameterizedUnitTest extends AUnitTest {

   private static final String PATH_TO_DEFAULT_CONFIGURATIONS = TestUtils.DEFAULT_CONFIGURATIONS_ROOT_PATH;


   /**
    * Creates a new {@link AParameterizedUnitTest} with the given resource directory level and the
    * given additional resource path.
    * 
    * @param resourceDirectoryLevel the resource level directory
    * @param additionalResourcePath the additional path to the resources
    */
   public AParameterizedUnitTest(String resourceDirectoryLevel, String additionalResourcePath) {
      super(resourceDirectoryLevel, additionalResourcePath);
   }


   /**
    * Returns the path to the default configurations of the implementation of this class.
    * 
    * @return the path to the default configurations
    */
   protected String getDefaultConfigurationPath() {
      return PATH_TO_DEFAULT_CONFIGURATIONS + getTestRessourcePathRelativeToResourceRootDirectory();
   }


   /**
    * Returns the path to the default configuration file with the given name.
    * 
    * @param fileName the name of the file to add to the default configuration path, including the
    *           extension
    * 
    * @return the path to the default configuration file
    */
   protected String getDefaultConfigurationPathFor(String fileName) {
      return getDefaultConfigurationPath() + fileName;
   }


   /**
    * Returns a list of {@link JsonObject}s of which can be used as parameters.
    * 
    * @return a list of JSON objects which are correct
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      return new ArrayList<JsonObject>();
   }


   /**
    * Returns a list of {@link Pair}s of combinations out wrong {@link JsonObject}s and an error
    * message string.
    * 
    * @return a list of pairs of wrong JSON objects and exception messages which can not used with
    *         the algorithm
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      return new ArrayList<Pair<String, JsonObject>>();
   }


   /**
    * Tests if the parameters defined in {@link #getCorrectParameters()} are actually correct.
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   public abstract void testCorrectParameters() throws JsonParsingFailedException;


   /**
    * Tests if the parameters defined in {@link #getWrongParameters()} are actually leading to an
    * error.
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   public abstract void testWrongParameters() throws JsonParsingFailedException;

}
