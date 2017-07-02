package de.upb.cs.is.jpl.api.math.distribution.numerical.real.gumbel;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.IRealDistribution;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This test tests the functionality of the {@link GumbelDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class GumbelDistributionTest extends ARealDistributionTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "gumbel" + StringUtils.FORWARD_SLASH;

   private static final String PARAMETER_NAME_MU = "mu";
   private static final String PARAMETER_NAME_BETA = "beta";

   private static final double TEST_PARAMETER_MU = 0;
   private static final double TEST_PARAMETER_BETA = 0.7;

   private static final double EULER_MASCHERONI_CONSTANT_APPROXIMATION = Math.PI / (2 * Math.E);


   /**
    * Creates a new {@link GumbelDistributionTest}.
    */
   public GumbelDistributionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IRealDistribution getDistribution() {
      return new GumbelDistribution();
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> correctParameters = new ArrayList<>();
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { "0", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { "0", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { "1", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { "1", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { "20", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { "20", "0.99" }));
      return correctParameters;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> incorrectParameters = new ArrayList<>();
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils
            .createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA }, new String[] { "1", "-0.5" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils
            .createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA }, new String[] { "0", "-0.1" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils
            .createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA }, new String[] { "0", "0" })));
      return incorrectParameters;
   }


   @Override
   protected JsonObject getTestingParameters() throws JsonParsingFailedException {
      return JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_BETA },
            new String[] { String.valueOf(TEST_PARAMETER_MU), String.valueOf(TEST_PARAMETER_BETA) });
   }


   @Override
   protected double getTestingVarianceResult() {
      return Math.pow(Math.PI * TEST_PARAMETER_BETA, 2) / 6.0;
   }


   @Override
   protected double getTestingExpectedValueResult() {
      return TEST_PARAMETER_MU + TEST_PARAMETER_BETA * EULER_MASCHERONI_CONSTANT_APPROXIMATION;
   }


   @Override
   protected List<Triple<Double, Double, String>> getTestingProbabilityEventsAndResults() {
      List<Triple<Double, Double, String>> probabilityEventsAndResults = new ArrayList<>();
      probabilityEventsAndResults.add(Triple.of(1.0, 0.269403, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(0.0, 0.525542, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-1.0, 0.0918602, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(3.0, 0.0193938, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-3.0, 2.90234e-30, StringUtils.EMPTY_STRING));
      return probabilityEventsAndResults;
   }


   @Override
   protected List<Triple<Pair<Double, Double>, Double, String>> getTestingProbabilityEventPairsAndResults() {
      List<Triple<Pair<Double, Double>, Double, String>> probabilityEventPairsAndResults = new ArrayList<>();
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(0.0, 1.0), 0.419023, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(-3.0, 3.0), 0.986331, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(1.0, 3.0), 0.199428, StringUtils.EMPTY_STRING));
      return probabilityEventPairsAndResults;
   }


   @Override
   protected List<Pair<Double, Double>> getTestingCumulativeEventsAndResults() {
      List<Pair<Double, Double>> cumulativeEventsAndResults = new ArrayList<>();
      cumulativeEventsAndResults.add(Pair.of(1.0, 0.786902));
      cumulativeEventsAndResults.add(Pair.of(0.0, 0.367879));
      cumulativeEventsAndResults.add(Pair.of(-1.0, 0.0154101));
      cumulativeEventsAndResults.add(Pair.of(3.0, 0.986331));
      cumulativeEventsAndResults.add(Pair.of(-3.0, 2.7963e-32));
      return cumulativeEventsAndResults;
   }

}
