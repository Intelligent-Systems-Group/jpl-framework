package de.upb.cs.is.jpl.api.metric;


import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.WrongConfigurationTypeException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;


/**
 * The abstract class is for evaluation metric which are associated with current running Evaluation.
 * Each {@code AEvalautionMetric} child class will be associated with the {@link EMetric} and
 * implements {@link IMetric}.
 * 
 * @param <CONFIG> the generic type extending AEvaluationMetricConfiguration, configuration
 *           associated with the Evaluation Metric class
 * @param <INPUT> the input type of the metric
 * @param <OUTPUT> the result type of the metric, i.e. Double
 * @author Pritha Gupta
 */
public abstract class AMetric<CONFIG extends AMetricConfiguration, INPUT, OUTPUT> implements IMetric<INPUT, OUTPUT> {
   protected static final String OPERATION_NOT_SUPPORTED_ERROR_MESSAGE = "Operation not supported for non-decomposible loss";
   private static final String ERROR_WRONG_CONFIGURATION_TYPE = "Initialized evaluation metric configuration with wrong configuration type.";

   protected static final String ERROR_UNEQUAL_LIST_SIZES = "The lists of expected values and predicted values must not be null and should have the same size.";
   protected static final String ERROR_FALSE_WEIGHT_LIST = "The lists of weights must not be null and should have the same size as the lists of expected and predicted values.";
   protected static final String ERROR_UNEQUAL_VECTOR_SIZES = "The expected rating and the predicted rating should have the same length.";

   protected CONFIG metricConfiguration;
   private final String metricIdentifer;


   /**
    * Creates new {@link AMetric} with the default metric configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public AMetric(String metricIdentifer) {
      this.metricIdentifer = metricIdentifer;
      getMetricConfiguration();
   }


   @SuppressWarnings("unchecked")
   @Override
   public CONFIG getMetricConfiguration() {
      if (metricConfiguration == null) {
         metricConfiguration = (CONFIG) getMetricDefaultConfiguration();
      }
      return metricConfiguration;
   }


   @Override
   public AMetricConfiguration getMetricDefaultConfiguration() {
      AMetricConfiguration configuration = createDefaultMetricConfiguration();
      configuration.initializeDefaultConfiguration();
      return configuration;
   }


   /**
    * Creates the default evaluation metric configuration of this evaluation metric and returns it.
    * 
    * @return the default evaluation metric configuration
    */
   protected abstract AMetricConfiguration createDefaultMetricConfiguration();


   @Override
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException {
      getMetricDefaultConfiguration().overrideConfiguration(jsonObject);
   }


   /**
    * Asserts that the two given lists are not {@code null} and have the same length.
    * 
    * @param expectedValues the first list to check
    * @param predictedValues the second list to check
    * @throws LossException if either one of the given lists is {@code null} or if they have an
    *            unequal size
    */
   protected void assertListsAreNotNullAndHaveSameSize(List<INPUT> expectedValues, List<INPUT> predictedValues) throws LossException {
      if (expectedValues == null || predictedValues == null || expectedValues.size() != predictedValues.size()) {
         throw new LossException(ERROR_UNEQUAL_LIST_SIZES);
      }
   }


   /**
    * Asserts that the three given lists are not {@code null} and have the same length.
    * 
    * @param expectedValues the first list to check
    * @param predictedValues the second list to check
    * @param weights the weights to check
    * @throws LossException if either one of the given lists is {@code null} or if they have an
    *            unequal size
    */
   protected void assertListsAreNotNullAndHaveSameSize(List<INPUT> expectedValues, List<INPUT> predictedValues, List<Double> weights)
         throws LossException {
      assertListsAreNotNullAndHaveSameSize(expectedValues, predictedValues);
      if (weights == null || weights.size() != predictedValues.size()) {
         throw new LossException(ERROR_FALSE_WEIGHT_LIST);
      }
   }


   @Override
   @SuppressWarnings("unchecked")
   public void setMetricConfiguration(AMetricConfiguration metricConfiguration) {
      if (metricConfiguration.getClass().isInstance(createDefaultMetricConfiguration().getClass())) {
         throw new WrongConfigurationTypeException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      this.metricConfiguration = (CONFIG) metricConfiguration;
   }


   @Override
   public String toString() {
      return metricIdentifer;
   }

}
