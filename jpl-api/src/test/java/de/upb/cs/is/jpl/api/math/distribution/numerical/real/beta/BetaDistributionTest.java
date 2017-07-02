package de.upb.cs.is.jpl.api.math.distribution.numerical.real.beta;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistribution;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.IRealDistribution;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * Test for the {@link BetaDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public class BetaDistributionTest extends ARealDistributionTest {

   private static final String REFLECTION_BETADISTRIBUTIONCONFIGURATION_ERROR_ALPHA_NOT_POSITIVE = "ERROR_ALPHA_NOT_POSITIVE";
   private static final String REFLECTION_BETADISTRIBUTIONCONFIGURATION_ERROR_BETA_NOT_POSITIVE = "ERROR_BETA_NOT_POSITIVE";

   private static final String RESOURCE_DIRECTORY_LEVEL = "beta" + StringUtils.FORWARD_SLASH;

   private static final String PARAMETER_ALPHA_STRING = "alpha";
   private static final double PARAMETER_ALPHA_VALUE = 2;

   private static final String PARAMETER_BETA_STRING = "beta";
   private static final double PARAMETER_BETA_VALUE = 5;


   /**
    * Creates a new unit test for numerical distributions with the additional path to the resources
    * given.
    */
   public BetaDistributionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IRealDistribution getDistribution() {
      return new BetaDistribution();
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> incorrectParameters = new ArrayList<>();
      incorrectParameters.add(Pair.of(
            TestUtils.getStringByReflectionSafely(BetaDistributionConfiguration.class,
                  REFLECTION_BETADISTRIBUTIONCONFIGURATION_ERROR_ALPHA_NOT_POSITIVE),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_ALPHA_STRING, PARAMETER_BETA_STRING },
                  new String[] { "0", "1" })));
      incorrectParameters.add(Pair.of(
            TestUtils.getStringByReflectionSafely(BetaDistributionConfiguration.class,
                  REFLECTION_BETADISTRIBUTIONCONFIGURATION_ERROR_BETA_NOT_POSITIVE),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_ALPHA_STRING, PARAMETER_BETA_STRING },
                  new String[] { "1", "0" })));
      return incorrectParameters;
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> correctParameters = new ArrayList<>();
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_ALPHA_STRING, PARAMETER_BETA_STRING },
            new String[] { "0.01", "1" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_ALPHA_STRING, PARAMETER_BETA_STRING },
            new String[] { "1", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_ALPHA_STRING, PARAMETER_BETA_STRING },
            new String[] { "100000", "300000" }));
      return correctParameters;
   }


   @Override
   protected JsonObject getTestingParameters() throws JsonParsingFailedException {
      return JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_ALPHA_STRING, PARAMETER_BETA_STRING },
            new String[] { PARAMETER_ALPHA_VALUE + StringUtils.EMPTY_STRING, PARAMETER_BETA_VALUE + StringUtils.EMPTY_STRING });

   }


   @Override
   protected double getTestingVarianceResult() {
      return (PARAMETER_ALPHA_VALUE * PARAMETER_BETA_VALUE)
            / ((PARAMETER_ALPHA_VALUE + PARAMETER_BETA_VALUE + 1.0) * Math.pow((PARAMETER_ALPHA_VALUE + PARAMETER_BETA_VALUE), 2));
   }


   @Override
   protected double getTestingExpectedValueResult() {
      return PARAMETER_ALPHA_VALUE / (PARAMETER_ALPHA_VALUE + PARAMETER_BETA_VALUE);
   }


   @Override
   protected List<Triple<Double, Double, String>> getTestingProbabilityEventsAndResults() {
      List<Triple<Double, Double, String>> list = new ArrayList<>();
      list.add(Triple.of(new Double(0), new Double(0), StringUtils.EMPTY_STRING));
      list.add(Triple.of(new Double(0.2), new Double(2.4576), StringUtils.EMPTY_STRING));
      list.add(Triple.of(new Double(1), new Double(0), StringUtils.EMPTY_STRING));
      return list;
   }


   @Override
   protected List<Pair<Double, Double>> getTestingCumulativeEventsAndResults() {
      List<Pair<Double, Double>> list = new ArrayList<>();
      list.add(Pair.of(new Double(0), new Double(0)));
      list.add(Pair.of(new Double(0.2), new Double(0.34464)));
      list.add(Pair.of(new Double(0.5), new Double(0.890625)));
      list.add(Pair.of(new Double(0.8), new Double(0.9984)));
      list.add(Pair.of(new Double(1), new Double(1)));
      return list;
   }


   @Override
   protected List<Triple<Pair<Double, Double>, Double, String>> getTestingProbabilityEventPairsAndResults() {
      List<Triple<Pair<Double, Double>, Double, String>> list = new ArrayList<>();
      list.add(Triple.of(Pair.of(new Double(1), new Double(0)), new Double(0),
            String.format(TestUtils.getStringByReflectionSafely(ANumericalDistribution.class,
                  REFLECTION_ANUMERICALDISTRIBUTION_ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND), new Double(1), new Double(0))));
      list.add(Triple.of(Pair.of(new Double(0), new Double(0)), new Double(0), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Double(0), new Double(0.2)), new Double(0.34464), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Double(0), new Double(0.5)), new Double(0.890625), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Double(0), new Double(0.8)), new Double(0.9984), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Double(0), new Double(1)), new Double(1), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Double(1), new Double(1)), new Double(0), StringUtils.EMPTY_STRING));
      return list;
   }


}
