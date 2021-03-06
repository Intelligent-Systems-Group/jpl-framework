package de.upb.cs.is.jpl.api.algorithm;


import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.common.AParameterizedUnitTest;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * An abstract class for testing general functions of the {@link IAlgorithm}s. The abstract
 * functions must be overridden with an algorithm specific content.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class AAlgorithmTest extends AParameterizedUnitTest {

   private static final String ERROR_CORRECT_PARAMETER_NOT_ACCEPTED = "The algorithm should accept the parameter key and value pair: %s.";
   private static final String ERROR_INCORRECT_PARAMETER_ACCEPTED = "The algorithm should not accept the parameter value pair: %s.";
   protected static final String ERROR_WRONG_OUTPUT = "The output should be \"%s\" but is \"%s\".";

   private static final String RESOURCE_DIRECTORY_LEVEL = "algorithm" + File.separator;


   /**
    * Creates a new unit test for algorithms with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AAlgorithmTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
   }


   /**
    * Returns an instance of the {@link ITrainableAlgorithm} which should be checked.
    * 
    * @return instance of the algorithm to check
    */
   public abstract IAlgorithm getAlgorithm();


   /**
    * {@inheritDoc} Test if the {@link ITrainableAlgorithm} if able to detect correct parameter.
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   @Test
   @Override
   public void testCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> listOfJsonObjects = getCorrectParameters();
      // Test each pair
      for (int i = 0; i < listOfJsonObjects.size(); i++) {
         IAlgorithm algorithm = getAlgorithm();

         try {
            algorithm.setParameters(listOfJsonObjects.get(i));
         } catch (ParameterValidationFailedException e) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_PARAMETER_NOT_ACCEPTED, listOfJsonObjects.get(i)));
         }
      }
   }


   /**
    * {@inheritDoc} Test if the {@link ITrainableAlgorithm} if able to detect incorrect parameter.
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   @Test
   @Override
   public void testWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> listOfPairsOfStringAndJsonObjects = getWrongParameters();
      // Test each triple
      for (int i = 0; i < listOfPairsOfStringAndJsonObjects.size(); i++) {
         IAlgorithm algorithm = getAlgorithm();

         try {
            algorithm.setParameters(listOfPairsOfStringAndJsonObjects.get(i).getSecond());
            // Fail if no exception in set parameters is thrown
            fail(String.format(ERROR_INCORRECT_PARAMETER_ACCEPTED, listOfPairsOfStringAndJsonObjects.get(i).getSecond()));
         } catch (ParameterValidationFailedException e) {
            Assert.assertEquals(ERROR_WRONG_OUTPUT, e.getMessage(), listOfPairsOfStringAndJsonObjects.get(i).getFirst());
         }
      }
   }
}
