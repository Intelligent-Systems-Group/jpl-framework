package de.upb.cs.is.jpl.api.math.distribution.numerical.real.chisquared;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.IRealDistribution;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This test tests the functionality of the {@link ChiSquaredDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class ChiSquaredDistributionTest extends ARealDistributionTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "chi_squared" + StringUtils.FORWARD_SLASH;

   private static final String REFLECTION_ERROR_NON_POSITIVE_AMOUNT_OF_DEGREES_OF_FREEDOM = "ERROR_NON_POSITIVE_AMOUNT_OF_DEGREES_OF_FREEDOM";

   private static final String PARAMETER_NAME_DEGREES_OF_FREEDOM = "degrees_of_freedom";

   private static final int PARAMETER_VALUE_DEGREES_OF_FREEDOM = 2;


   /**
    * Creates a new {@link ChiSquaredDistributionTest}.
    */
   public ChiSquaredDistributionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IRealDistribution getDistribution() {
      return new ChiSquaredDistribution();
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> correctParameters = new ArrayList<>();
      correctParameters
            .add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM }, new String[] { "1" }));
      correctParameters.add(
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM }, new String[] { "2", }));
      correctParameters.add(
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM }, new String[] { "250" }));
      return correctParameters;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> incorrectParameters = new ArrayList<>();
      incorrectParameters.add(Pair.of(getExpectedErrorForNonPositiveDegreesOfFreedom(),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM }, new String[] { "-250" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonPositiveDegreesOfFreedom(),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM }, new String[] { "-5" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonPositiveDegreesOfFreedom(),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM }, new String[] { "0" })));
      return incorrectParameters;
   }


   @Override
   protected JsonObject getTestingParameters() throws JsonParsingFailedException {
      return JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_DEGREES_OF_FREEDOM },
            new String[] { String.valueOf(PARAMETER_VALUE_DEGREES_OF_FREEDOM) });
   }


   @Override
   protected double getTestingVarianceResult() {
      return 2 * PARAMETER_VALUE_DEGREES_OF_FREEDOM;
   }


   @Override
   protected double getTestingExpectedValueResult() {
      return PARAMETER_VALUE_DEGREES_OF_FREEDOM;
   }


   @Override
   protected List<Triple<Double, Double, String>> getTestingProbabilityEventsAndResults() {
      List<Triple<Double, Double, String>> probabilityEventsAndResults = new ArrayList<>();
      probabilityEventsAndResults.add(Triple.of(1.0, 0.3032653298563167118019, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(0.0, 0.5, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-1.0, 0.0, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(3.0, 0.1115650800742149144666, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-3.0, 0.0, StringUtils.EMPTY_STRING));
      return probabilityEventsAndResults;
   }


   @Override
   protected List<Triple<Pair<Double, Double>, Double, String>> getTestingProbabilityEventPairsAndResults() {
      List<Triple<Pair<Double, Double>, Double, String>> probabilityEventPairsAndResults = new ArrayList<>();
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(0.0, 1.0), 0.3934693402873665763962, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(1.0, 3.0), 0.3834004996, StringUtils.EMPTY_STRING));
      return probabilityEventPairsAndResults;
   }


   @Override
   protected List<Pair<Double, Double>> getTestingCumulativeEventsAndResults() {
      List<Pair<Double, Double>> cumulativeEventsAndResults = new ArrayList<>();
      cumulativeEventsAndResults.add(Pair.of(1.0, 0.3934693402873665763962));
      cumulativeEventsAndResults.add(Pair.of(0.0, 0.0));
      cumulativeEventsAndResults.add(Pair.of(-1.0, 0.0));
      cumulativeEventsAndResults.add(Pair.of(3.0, 0.7768698398515701710667));
      cumulativeEventsAndResults.add(Pair.of(-3.0, 0.0));
      return cumulativeEventsAndResults;
   }


   /**
    * Returns the expected exception message for a non positive amount of degrees of freedom.
    * 
    * @return the expected exception message for a non positive amount of degrees of freedom.
    */
   private String getExpectedErrorForNonPositiveDegreesOfFreedom() {
      return TestUtils.getStringByReflectionSafely(ChiSquaredDistributionConfiguration.class,
            REFLECTION_ERROR_NON_POSITIVE_AMOUNT_OF_DEGREES_OF_FREEDOM);
   }

}
