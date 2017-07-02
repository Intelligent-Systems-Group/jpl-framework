package de.upb.cs.is.jpl.api.math.distribution.numerical.real.cauchy;


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
 * This class tests the functionality of the {@link CauchyDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class CauchyDistributionTest extends ARealDistributionTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "cauchy" + StringUtils.FORWARD_SLASH;

   private static final String PARAMETER_NAME_MU = "mu";
   private static final String PARAMETER_NAME_LAMBDA = "lambda";


   /**
    * Creates a new {@link CauchyDistributionTest}.
    */
   public CauchyDistributionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IRealDistribution getDistribution() {
      return new CauchyDistribution();
   }


   @Override
   public List<JsonObject> getCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> correctParameters = new ArrayList<>();
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { "0", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { "0", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { "1", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { "1", "0.99" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { "20", "0.01" }));
      correctParameters.add(JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { "20", "0.99" }));
      return correctParameters;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> incorrectParameters = new ArrayList<>();
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils.createJsonObjectFromKeyAndValueArrays(
            new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA }, new String[] { "1", "-0.5" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils.createJsonObjectFromKeyAndValueArrays(
            new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA }, new String[] { "0", "-0.1" })));
      incorrectParameters.add(Pair.of(getExpectedErrorForNonNegativeScalingParameter(), JsonUtils
            .createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA }, new String[] { "0", "0" })));
      return incorrectParameters;
   }


   @Override
   protected JsonObject getTestingParameters() throws JsonParsingFailedException {
      return JsonUtils.createJsonObjectFromKeyAndValueArrays(new String[] { PARAMETER_NAME_MU, PARAMETER_NAME_LAMBDA },
            new String[] { 0 + StringUtils.EMPTY_STRING, 0.5 + StringUtils.EMPTY_STRING });
   }


   @Override
   protected double getTestingVarianceResult() {
      // Cauchy distribution has an undefined variance
      return Double.NaN;
   }


   @Override
   protected double getTestingExpectedValueResult() {
      // Cauchy distribution has an undefined expected value
      return Double.NaN;
   }


   @Override
   protected List<Triple<Double, Double, String>> getTestingProbabilityEventsAndResults() {
      List<Triple<Double, Double, String>> probabilityEventsAndResults = new ArrayList<>();
      probabilityEventsAndResults.add(Triple.of(1.0, 0.1273239544735162686151, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(0.0, 0.6366197723675813430755, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-1.0, 0.1273239544735162686151, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(3.0, 0.01720593979371841467772, StringUtils.EMPTY_STRING));
      probabilityEventsAndResults.add(Triple.of(-3.0, 0.01720593979371841467772, StringUtils.EMPTY_STRING));
      return probabilityEventsAndResults;
   }


   @Override
   protected List<Triple<Pair<Double, Double>, Double, String>> getTestingProbabilityEventPairsAndResults() {
      List<Triple<Pair<Double, Double>, Double, String>> probabilityEventPairsAndResults = new ArrayList<>();
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(0.0, 1.0), 0.3524163823, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(-3.0, 3.0), 0.8948630866, StringUtils.EMPTY_STRING));
      probabilityEventPairsAndResults.add(Triple.of(Pair.of(1.0, 3.0), 0.09501516094, StringUtils.EMPTY_STRING));
      return probabilityEventPairsAndResults;
   }


   @Override
   protected List<Pair<Double, Double>> getTestingCumulativeEventsAndResults() {
      List<Pair<Double, Double>> cumulativeEventsAndResults = new ArrayList<>();
      cumulativeEventsAndResults.add(Pair.of(1.0, 0.8524163823495667258246));
      cumulativeEventsAndResults.add(Pair.of(0.0, 0.5));
      cumulativeEventsAndResults.add(Pair.of(-1.0, 0.1475836177));
      cumulativeEventsAndResults.add(Pair.of(3.0, 0.9474315432887465700492));
      cumulativeEventsAndResults.add(Pair.of(-3.0, 0.05256845671125342995078));
      return cumulativeEventsAndResults;
   }


}
