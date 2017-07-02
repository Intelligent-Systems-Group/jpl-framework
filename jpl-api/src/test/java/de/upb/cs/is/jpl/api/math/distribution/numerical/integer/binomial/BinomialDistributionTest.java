package de.upb.cs.is.jpl.api.math.distribution.numerical.integer.binomial;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistribution;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.AIntegerDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.IIntegerDistribution;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * Test for the {@link BinomialDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public class BinomialDistributionTest extends AIntegerDistributionTest {

   private static final String REFLECTION_BINOMIALDISTRIBUTIONCONFIGURATION_ERROR_PARAMETER_NOT_POSITIVE_INTEGER = "ERROR_PARAMETER_NOT_POSITIVE_INTEGER";
   private static final String REFLECTION_BINOMIALDISTRIBUTIONCONFIGURATION_ERROR_PARAMETER_NOT_PROBABILITY = "ERROR_PARAMETER_NOT_PROBABILITY";

   private static final String RESOURCE_DIRECTORY_LEVEL = "binomial" + StringUtils.FORWARD_SLASH;

   private static final String PARAMETER_N_STRING = "n";
   private static final double PARAMETER_N_VALUE = 1;

   private static final String PARAMETER_P_STRING = "p";
   private static final double PARAMETER_P_VALUE = 0.6;


   /**
    * Creates a new unit test for numerical distributions with the additional path to the resources
    * given.
    */
   public BinomialDistributionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IIntegerDistribution getDistribution() {
      return new BinomialDistribution();
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> incorrectParameters = new ArrayList<>();
      incorrectParameters.add(Pair.of(
            TestUtils.getStringByReflectionSafely(BinomialDistributionConfiguration.class,
                  REFLECTION_BINOMIALDISTRIBUTIONCONFIGURATION_ERROR_PARAMETER_NOT_POSITIVE_INTEGER),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
                  new String[] { "-1", "0.5" })));
      incorrectParameters.add(Pair.of(
            TestUtils.getStringByReflectionSafely(BinomialDistributionConfiguration.class,
                  REFLECTION_BINOMIALDISTRIBUTIONCONFIGURATION_ERROR_PARAMETER_NOT_PROBABILITY),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
                  new String[] { "0", "-0.1" })));
      incorrectParameters.add(Pair.of(
            TestUtils.getStringByReflectionSafely(BinomialDistributionConfiguration.class,
                  REFLECTION_BINOMIALDISTRIBUTIONCONFIGURATION_ERROR_PARAMETER_NOT_PROBABILITY),
            JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
                  new String[] { "0", "1.1" })));
      return incorrectParameters;
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> correctParameters = new ArrayList<>();
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { "0", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { "0", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { "1", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { "1", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { "20", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { "20", "0.99" }));
      return correctParameters;
   }


   @Override
   protected JsonObject getTestingParameters() throws JsonParsingFailedException {
      return JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_N_STRING, PARAMETER_P_STRING },
            new String[] { PARAMETER_N_VALUE + StringUtils.EMPTY_STRING, PARAMETER_P_VALUE + StringUtils.EMPTY_STRING });
   }


   @Override
   protected double getTestingVarianceResult() {
      return PARAMETER_N_VALUE * PARAMETER_P_VALUE * (1 - PARAMETER_P_VALUE);
   }


   @Override
   protected double getTestingExpectedValueResult() {
      return PARAMETER_N_VALUE * PARAMETER_P_VALUE;
   }


   @Override
   protected List<Triple<Integer, Double, String>> getTestingProbabilityEventsAndResults() {
      List<Triple<Integer, Double, String>> list = new ArrayList<>();
      list.add(Triple.of(new Integer(-1), new Double(0), StringUtils.EMPTY_STRING));
      list.add(Triple.of(new Integer(0), (1 - PARAMETER_P_VALUE), StringUtils.EMPTY_STRING));
      list.add(Triple.of(new Integer(1), PARAMETER_P_VALUE, StringUtils.EMPTY_STRING));
      list.add(Triple.of(new Integer(2), new Double(0), StringUtils.EMPTY_STRING));
      return list;
   }


   @Override
   protected List<Pair<Integer, Double>> getTestingCumulativeEventsAndResults() {
      List<Pair<Integer, Double>> list = new ArrayList<>();
      list.add(Pair.of(new Integer(-1), new Double(0)));
      list.add(Pair.of(new Integer(0), new Double(1 - PARAMETER_P_VALUE)));
      list.add(Pair.of(new Integer(1), new Double(1)));
      return list;
   }


   @Override
   protected List<Triple<Pair<Integer, Integer>, Double, String>> getTestingProbabilityEventPairsAndResults() {
      List<Triple<Pair<Integer, Integer>, Double, String>> list = new ArrayList<>();
      list.add(Triple.of(Pair.of(new Integer(1), new Integer(0)), new Double(0),
            String.format(TestUtils.getStringByReflectionSafely(ANumericalDistribution.class,
                  REFLECTION_ANUMERICALDISTRIBUTION_ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND), new Integer(1), new Integer(0))));
      list.add(Triple.of(Pair.of(new Integer(-1), new Integer(-1)), new Double(0), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Integer(-1), new Integer(0)), (1 - PARAMETER_P_VALUE), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Integer(-1), new Integer(1)), new Double(1), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Integer(0), new Integer(0)), new Double(0), StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Integer(0), new Integer(1)), PARAMETER_P_VALUE, StringUtils.EMPTY_STRING));
      list.add(Triple.of(Pair.of(new Integer(1), new Integer(1)), new Double(0), StringUtils.EMPTY_STRING));
      return list;
   }

}
