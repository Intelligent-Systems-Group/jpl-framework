package de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking.plackettluce;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.distributionfitting.ADistributionFittingAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.distributionfitting.IDistributionFittingAlgorithm;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.algorithm.FittingDistributionFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce.PlackettLuceModel;
import de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce.PlackettLuceModelSampleSet;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for {@link PlackettLuceModelFittingAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class PlackettLuceModelFittingAlgorithmTest extends ADistributionFittingAlgorithmTest<Ranking> {

   private static final Logger logger = LoggerFactory.getLogger(PlackettLuceModelFittingAlgorithmTest.class);

   private static final String PATH_TO_DEFAULT_CONFIGURATIONS = "plackettluce";

   private static final String REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE = "minimum_required_change_on_update";
   private static final String REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER = "iterations_sample_set_size_multiplier";

   private static final double GREATER_DOUBLE_DELTA = 0.1;


   /**
    * Creates a new {@link PlackettLuceModelFittingAlgorithmTest}.
    *
    */
   public PlackettLuceModelFittingAlgorithmTest() {
      super(PATH_TO_DEFAULT_CONFIGURATIONS);
   }


   @Override
   public IDistributionFittingAlgorithm<Ranking> getDistributionFittingAlgorithm() {
      return new PlackettLuceModelFittingAlgorithm();
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> parameterList = new ArrayList<>();

      String[] parameterNames = {
            REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE,
            REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER };
      double[] parameterValues = { 0, 1 };

      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i])));
      }

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();

      String[] parameterNames = {
            REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE,
            REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER };
      double[] parameterValues = { -1, 0 };
      String[] x = {
            String.format(
                  TestUtils.getStringByReflectionSafely(PlackettLuceModelFittingAlgorithmConfiguration.class,
                        "ERROR_NON_NEGATIVE_PARAMETER"),
                  REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE),
            String.format(
                  TestUtils.getStringByReflectionSafely(PlackettLuceModelFittingAlgorithmConfiguration.class, "ERROR_POSITIVE_PARAMETER"),
                  REFLECTION_PLACKETTLUCEMODELFITTINGALGORITHMCONFIGURATION_PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER) };

      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(Pair.of(x[i], JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i]))));
      }

      return parameterList;
   }


   /**
    * Tests if the method
    * {@link IDistributionFittingAlgorithm#fit(de.upb.cs.is.jpl.api.math.distribution.IDistributionSampleSet)}
    * is working as expected.
    * 
    * @throws ParameterValidationFailedException if something went wrong while initializing the
    *            {@link PlackettLuceModel}
    * @throws DistributionException if something went wrong while generating the sample
    * @throws FittingDistributionFailedException if something went wrong while fitting the sample
    *            set
    */
   @Test
   public void testFitting() throws ParameterValidationFailedException, DistributionException, FittingDistributionFailedException {
      double[] expectedParameters = new double[] { 0.2, 0.1, 0.1, 0.1, 0.25, 0.25 };
      int[] validItems = new int[] { 1, 2, 3, 4, 5, 6 };
      PlackettLuceModel model = new PlackettLuceModel(expectedParameters, validItems);

      long startingTimeSampling = System.currentTimeMillis();
      PlackettLuceModelSampleSet sampleSet = new PlackettLuceModelSampleSet(validItems);
      for (int i = 0; i < 2000; i++) {
         sampleSet.addSample(model.generateSample());
      }
      long stoppingTimeSampling = System.currentTimeMillis();
      logger.debug(
            "Stopped sampling at " + stoppingTimeSampling + " and took " + (stoppingTimeSampling - startingTimeSampling) / 1000.0 + " s.");

      PlackettLuceModelFittingAlgorithm fittingAlgorithm = new PlackettLuceModelFittingAlgorithm();
      long startingTime = System.currentTimeMillis();
      logger.debug("Started at " + startingTime);
      PlackettLuceModel fittedModel = fittingAlgorithm.fit(sampleSet);
      long stoppingTime = System.currentTimeMillis();
      logger.debug("Stopped at " + stoppingTime + " and took " + (stoppingTime - startingTime) / 1000.0 + " s.");

      assertEquals(expectedParameters[0], fittedModel.getDistributionConfiguration().getParameterValueOfItem(1), GREATER_DOUBLE_DELTA);
      assertEquals(expectedParameters[1], fittedModel.getDistributionConfiguration().getParameterValueOfItem(2), GREATER_DOUBLE_DELTA);
      assertEquals(expectedParameters[2], fittedModel.getDistributionConfiguration().getParameterValueOfItem(3), GREATER_DOUBLE_DELTA);
   }

}
