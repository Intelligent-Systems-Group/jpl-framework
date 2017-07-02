package de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;
import de.upb.cs.is.jpl.api.math.distribution.special.ranking.ARankingDistributionTest;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for {@link PlackettLuceModel}.
 * 
 * @author Tanja Tornede
 *
 */
public class PlackettLuceModelTest extends ARankingDistributionTest {

   private static final String FAIL_THIS_TEST_SHOULD_RESULT_IN_AN_EXCEPTION_BUT_IS_NOT = "This test should result in an exception, but is not!";

   private static final String REFLECTION_PLACKETTLUCEMODELCONFIGURATION_ERROR_UNEQUAL_AMOUNT_OF_ITEMS_AND_PARAMETERS = "ERROR_UNEQUAL_AMOUNT_OF_ITEMS_AND_PARAMETERS";
   private static final String REFLECTION_PLACKETTLUCEMODEL_ERROR_INVALID_RANKING = "ERROR_INVALID_RANKING";

   private static final String RESOURCE_DIRECTORY_LEVEL = "plackettluce" + StringUtils.FORWARD_SLASH;

   private static final String JSON_FILE_WRONG_1 = "wrong_1.json";
   private static final String JSON_FILE_CORRECT_1 = "correct_1.json";

   private static final double DOUBLE_DELTA = 0.000001;


   /**
    * Creates a {@link PlackettLuceModelTest}.
    */
   public PlackettLuceModelTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDistribution<Ranking> getDistribution() {
      return new PlackettLuceModel();
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> parameterList = new ArrayList<>();
      parameterList.add(JsonUtils.createJsonObjectFromFile(new File(getTestRessourcePathFor(JSON_FILE_CORRECT_1))));
      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();
      parameterList.add(Pair.of(
            TestUtils.getStringByReflectionSafely(PlackettLuceModelConfiguration.class,
                  REFLECTION_PLACKETTLUCEMODELCONFIGURATION_ERROR_UNEQUAL_AMOUNT_OF_ITEMS_AND_PARAMETERS),
            JsonUtils.createJsonObjectFromFile(new File(getTestRessourcePathFor(JSON_FILE_WRONG_1)))));
      return parameterList;
   }


   /**
    * Tests if the model is working as expected.
    * 
    * @throws ParameterValidationFailedException if something went wrong initializing the
    *            {@link PlackettLuceModel}
    * @throws DistributionException if the model is not able to return a probability for a ranking
    */
   @Test
   public void testModel() throws ParameterValidationFailedException, DistributionException {
      int[][] definedValidItems = new int[][] { { 1, 2, 3, 4, 5, 6 }, { 1, 7, 9 } };

      int[][][] validCompleteItems = new int[][][] { { { 1, 2, 3, 4, 5, 6 }, { 2, 4, 6, 1, 3, 5 }, { 6, 5, 4, 3, 2, 1 } },
            { { 1, 7, 9 }, { 1, 9, 7 }, { 7, 1, 9 } } };
      double[][] probabilitiesComplete = new double[][] { { 7d / 20026, 784d / 213525, 49d / 667368 }, { 9d / 35, 3d / 70, 9d / 20 } };

      int[][] validPartialItems = new int[][] { { 1, 3, 5 }, { 1, 9, } };
      double[] probabilitiesPartial = new double[] { 64d / 117, 3d / 4 };

      int[][] invalidItems = new int[][] { { 1, 3, 5, 7, 8, 9 }, { 1, 2, 3, 4, 5, 6 } };
      double[][] validParameters = new double[][] { { 0.32, 0.2, 0.1, 0.07, 0.03, 0.28 }, { 0.3, 0.6, 0.1 } };
      int[][] compareOperators = new int[][] { { Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING,
            Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING }, { Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING } };
      int[] invalidCompareOperators = new int[] { Ranking.CLOSING_BRACKETS_ENCODING, Ranking.COMPARABLE_ENCODING,
            Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING, };

      for (int i = 0; i < definedValidItems.length; i++) {
         PlackettLuceModel model = new PlackettLuceModel(validParameters[i], definedValidItems[i]);
         for (int j = 0; j < validCompleteItems[i].length; j++) {
            testProbabilityOfModel(model, validCompleteItems[i][j], compareOperators[i], probabilitiesComplete[i][j]);
         }
         testProbabilityOfModel(model, validPartialItems[i], compareOperators[i], probabilitiesPartial[i]);
         testProbabilityOfModelWithInvalidRanking(model, invalidItems[i], compareOperators[i]);
         testProbabilityOfModelWithInvalidCompareOperator(model, validCompleteItems[i][0], invalidCompareOperators);

         testModeOfModel(model, definedValidItems[i], validParameters[i], compareOperators[i]);
      }
   }


   /**
    * Tests if the model returns the correct probability for given valid items and valid compare
    * operators.
    * 
    * @param model the initialized model to check
    * @param validItems the valid items to get a ranking from
    * @param validCompareOperators the valid compare operators to get a valid ranking from
    * @param expectedProbability the expected probability
    * 
    * @throws DistributionException if the model is not able to return a probability for the given
    *            ranking
    */
   private void testProbabilityOfModel(PlackettLuceModel model, int[] validItems, int[] validCompareOperators, double expectedProbability)
         throws DistributionException {
      Ranking ranking = new Ranking(validItems, validCompareOperators);
      Assert.assertEquals(expectedProbability, model.getProbabilityFor(ranking), DOUBLE_DELTA);
   }


   /**
    * Tests if the model is not able to work with invalid items.
    * 
    * @param model the model to check
    * @param invalidItems the invalid items to get an invalid ranking from
    * @param validCompareOperators the valid compare operators to get a valid ranking from
    */
   private void testProbabilityOfModelWithInvalidRanking(PlackettLuceModel model, int[] invalidItems, int[] validCompareOperators) {
      Ranking ranking = new Ranking(invalidItems, validCompareOperators);
      try {
         model.getProbabilityFor(ranking);
         Assert.fail(FAIL_THIS_TEST_SHOULD_RESULT_IN_AN_EXCEPTION_BUT_IS_NOT);
      } catch (DistributionException ex) {
         String reflectionString = TestUtils.getStringByReflectionSafely(PlackettLuceModel.class,
               REFLECTION_PLACKETTLUCEMODEL_ERROR_INVALID_RANKING);
         Assert.assertEquals(reflectionString, ex.getMessage());
      }
   }


   /**
    * Tests if the model is not able to work with invalid compare operators.
    * 
    * @param model the model to check
    * @param validItems the valid items to get a ranking from
    * @param invalidCompareOperators the invalid compare operators to get an invalid ranking from
    */
   private void testProbabilityOfModelWithInvalidCompareOperator(PlackettLuceModel model, int[] validItems, int[] invalidCompareOperators) {
      Ranking ranking = new Ranking(validItems, invalidCompareOperators);
      try {
         model.getProbabilityFor(ranking);
         Assert.fail(FAIL_THIS_TEST_SHOULD_RESULT_IN_AN_EXCEPTION_BUT_IS_NOT);
      } catch (DistributionException ex) {
         String reflectionString = TestUtils.getStringByReflectionSafely(PlackettLuceModel.class,
               REFLECTION_PLACKETTLUCEMODEL_ERROR_INVALID_RANKING);
         Assert.assertEquals(reflectionString, ex.getMessage());
      }
   }


   /**
    * Tests the mode of the given model for the given items and parameters.
    * 
    * @param model the model to check
    * @param items the defined items of the model
    * @param parameters the defined parameters of the model
    * @param compareOperators the compare operators of the model
    */
   private void testModeOfModel(PlackettLuceModel model, int[] items, double[] parameters, int[] compareOperators) {
      Assert.assertEquals(new Ranking(sortItemsByParameters(items, parameters), compareOperators), model.getMode());
   }


   /**
    * Sorts the given array of items according to the given array of parameters.
    * 
    * @param items the items to sort
    * @param parameters the parameters to use to sort the items
    * @return an array of items, sorted by the given parameters
    */
   private int[] sortItemsByParameters(int[] items, double[] parameters) {
      List<Pair<Integer, Double>> pairs = new ArrayList<>();
      for (int i = 0; i < items.length; i++) {
         pairs.add(Pair.of(items[i], parameters[i]));
      }

      Collections.sort(pairs, new Comparator<Pair<Integer, Double>>() {
         @Override
         public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
            if (o1.getSecond() < o2.getSecond()) {
               return -1;
            } else if (o1.getSecond() > o2.getSecond()) {
               return 1;
            } else {
               return 0;
            }
         }
      }.reversed());

      int[] sortedItems = new int[items.length];
      for (int i = 0; i < items.length; i++) {
         sortedItems[i] = pairs.get(i).getFirst();
      }

      return sortedItems;
   }
}
