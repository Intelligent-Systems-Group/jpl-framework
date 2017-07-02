package de.upb.cs.is.jpl.api.math.distribution.numerical.real.normal;


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
 * This test tests the functionality of the {@link NormalDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class NormalDistributionTest extends ARealDistributionTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "normal" + StringUtils.FORWARD_SLASH;


   private static final String PARAMETER_NAME_MU = "mu";
   private static final String PARAMETER_NAME_SIGMA = "sigma";

   private static final double PARAMETER_VALUE_MU = 0.0;
   private static final double PARAMETER_VALUE_SIGMA = 2.0;


   /**
    * Creates a new {@link NormalDistributionTest}.
    */
   public NormalDistributionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IRealDistribution getDistribution() {
      return new NormalDistribution();
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> correctParameters = new ArrayList<>();
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { "0", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { "0", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { "1", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { "1", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { "20", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { "20", "0.99" }));
      return correctParameters;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> incorrectParameters = new ArrayList<>();
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils.createJsonObjectFromKeyAndValueArrays(
            new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA }, new String[] { "1", "-0.5" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils.createJsonObjectFromKeyAndValueArrays(
            new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA }, new String[] { "0", "-0.1" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils
            .createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA }, new String[] { "0", "0" })));
      return incorrectParameters;
   }


   @Override
   protected JsonObject getTestingParameters() throws JsonParsingFailedException {
      return JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_SIGMA },
            new String[] { String.valueOf(PARAMETER_VALUE_MU), String.valueOf(PARAMETER_VALUE_SIGMA) });
   }


   @Override
   protected double getTestingVarianceResult() {
      return Math.pow(PARAMETER_VALUE_SIGMA, 2);
   }


   @Override
   protected double getTestingExpectedValueResult() {
      return PARAMETER_VALUE_MU;
   }


   @Override
   protected List<Triple<Double, Double, String>> getTestingProbabilityEventsAndResults() {
      List<Triple<Double, Double, String>> probabilityEventsAndResults = new ArrayList<>();
      probabilityEventsAndResults.add(Triple.of(1.0, 0.1760326633821497388873, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(0.0, 0.19947114020071633897, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-1.0, 0.1760326633821497388873, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(3.0, 0.06475879783294586380705, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-3.0, 0.06475879783294586380705, StringUtils.EMPTY_STRING));
      return probabilityEventsAndResults;
   }


   @Override
   protected List<Triple<Pair<Double, Double>, Double, String>> getTestingProbabilityEventPairsAndResults() {
      List<Triple<Pair<Double, Double>, Double, String>> probabilityEventPairsAndResults = new ArrayList<>();
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(0.0, 1.0), 0.1914624613, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(-3.0, 3.0), 0.8663855975, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(1.0, 3.0), 0.2417303375, StringUtils.EMPTY_STRING));
      return probabilityEventPairsAndResults;
   }


   @Override
   protected List<Pair<Double, Double>> getTestingCumulativeEventsAndResults() {
      List<Pair<Double, Double>> cumulativeEventsAndResults = new ArrayList<>();
      cumulativeEventsAndResults.add(Pair.of(1.0, 0.6914624612740131036377));
      cumulativeEventsAndResults.add(Pair.of(0.0, 0.5));
      cumulativeEventsAndResults.add(Pair.of(-1.0, 0.3085375387259868963623));
      cumulativeEventsAndResults.add(Pair.of(3.0, 0.9331927987311419339955));
      cumulativeEventsAndResults.add(Pair.of(-3.0, 0.0668072012688580660045));
      return cumulativeEventsAndResults;
   }

}
