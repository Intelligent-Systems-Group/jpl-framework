package de.upb.cs.is.jpl.api.math.distribution;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.common.AParameterizedUnitTest;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Abstract test class for {@link ADistribution} implementations.
 * 
 * @author Tanja Tornede
 * 
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 *
 */
public abstract class ADistributionTest<SPACE> extends AParameterizedUnitTest {

   private static final String ERROR_CORRECT_PARAMETER_NOT_ACCEPTED = "The distribution should accept the parameter key and value pair: %s.";
   private static final String ERROR_INCORRECT_PARAMETER_ACCEPTED = "The distribution should not accept the parameter value pair: %s.";
   protected static final String ERROR_WRONG_OUTPUT = "The output should be \"%s\" but is \"%s\".";

   private static final String RESOURCE_DIRECTORY_LEVEL = "math" + StringUtils.FORWARD_SLASH + "distribution" + StringUtils.FORWARD_SLASH;


   /**
    * Creates a new unit test for algorithms with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ADistributionTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
   }


   /**
    * Returns the distribution created with default parameters.
    * 
    * @return a distribution with the default parameters
    */
   protected abstract IDistribution<SPACE> getDistribution();


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
         IDistribution<SPACE> distribution = getDistribution();
         try {
            distribution.setParameters(listOfPairsOfStringAndJsonObjects.get(i).getSecond());
            // Fail if no exception in set parameters is thrown
            fail(String.format(ERROR_INCORRECT_PARAMETER_ACCEPTED, listOfPairsOfStringAndJsonObjects.get(i).getSecond()));
         } catch (ParameterValidationFailedException e) {
            Assert.assertEquals(ERROR_WRONG_OUTPUT, listOfPairsOfStringAndJsonObjects.get(i).getFirst(), e.getMessage());
         }
      }
   }


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
         IDistribution<SPACE> distribution = getDistribution();
         try {
            distribution.setParameters(listOfJsonObjects.get(i));
         } catch (ParameterValidationFailedException e) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_PARAMETER_NOT_ACCEPTED, listOfJsonObjects.get(i)));
         }
      }
   }
}
