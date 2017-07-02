package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration stores all parameters of the
 * {@link CombinedRankingAndRegressionLearningAlgorithm}.
 *
 * @author Sebastian Gottschalk
 */
public class CombinedRankingAndRegressionConfiguration extends AAlgorithmConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "instanceranking"
         + StringUtils.FORWARD_SLASH + "combined_ranking_and_regression";
   private static final String WRONG_VALUE_FOR_LOSSFUNCTION = "The parameter lossfunction must be either 'squared' or 'logistic'.";
   private static final String WRONG_VALUE_FOR_ALPHA = "The tradeoff parameter alpha must be in range [0,1].";
   private static final String WRONG_VALUE_FOR_ITERATIONS = "The algorithms must at least run 1 iterations.";
   private static final String WRONG_VALUE_FOR_LAMBDA = "The lambda value should not be zero.";

   private static final String LOGISTIC_PARAMETER = "logistic";
   private static final String SQUARED_PARAMETER = "squared";

   private static final String PARAMETER_LOSS_FUNCTION_IDENTIFIER = "loss_function_identifier";
   private static final String PARAMETER_ALPLHA = "alpha";
   private static final String PARAMETER_LAMBDA = "lambda";
   private static final String PARAMETER_ITERATIONS = "iterations";

   private static final String TO_STRING_OUTPUT = "(lossfunction: %s, alpha: %s, lambda: %s, iterations: %s)";

   @SerializedName(PARAMETER_LOSS_FUNCTION_IDENTIFIER)
   private String lossFunctionIdentifier = StringUtils.EMPTY_STRING;
   @SerializedName(PARAMETER_ALPLHA)
   private double tradeoffParameter = Double.MAX_VALUE;
   @SerializedName(PARAMETER_LAMBDA)
   private double regularizationParameter = Double.MAX_VALUE;
   @SerializedName(PARAMETER_ITERATIONS)
   private int iterations = Integer.MAX_VALUE;


   /**
    * Creates a new instance of the CRRAlgorithmConfiguration.
    */
   public CombinedRankingAndRegressionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);

   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (!(lossFunctionIdentifier.equals(SQUARED_PARAMETER) || lossFunctionIdentifier.equals(LOGISTIC_PARAMETER))) {
         throw new ParameterValidationFailedException(WRONG_VALUE_FOR_LOSSFUNCTION);
      }
      if (this.tradeoffParameter < 0.0 || this.tradeoffParameter > 1.0) {
         throw new ParameterValidationFailedException(WRONG_VALUE_FOR_ALPHA);
      }
      if (this.iterations < 1) {
         throw new ParameterValidationFailedException(WRONG_VALUE_FOR_ITERATIONS);
      }
      if (Double.compare(this.regularizationParameter, 0.0) == 0) {
         throw new ParameterValidationFailedException(WRONG_VALUE_FOR_LAMBDA);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      CombinedRankingAndRegressionConfiguration crrConfiguration = (CombinedRankingAndRegressionConfiguration) configuration;
      if (!crrConfiguration.lossFunctionIdentifier.equals(StringUtils.EMPTY_STRING)) {
         this.lossFunctionIdentifier = crrConfiguration.lossFunctionIdentifier;
      }
      if (Double.compare(crrConfiguration.tradeoffParameter, Double.MAX_VALUE) != 0) {
         this.tradeoffParameter = crrConfiguration.tradeoffParameter;
      }
      if (Double.compare(crrConfiguration.regularizationParameter, Double.MAX_VALUE) != 0) {
         this.regularizationParameter = crrConfiguration.regularizationParameter;
      }
      if (Integer.compare(crrConfiguration.iterations, Integer.MAX_VALUE) != 0) {
         this.iterations = crrConfiguration.iterations;
      }

   }


   /**
    * Returns the loss function identifier which is used by the algorithm.
    * 
    * @return "squared" if the squared loss function is used and "logistic" if the logistic loss is
    *         used
    */
   public String getLossFunctionIdentifier() {
      return this.lossFunctionIdentifier;
   }


   /**
    * Returns the tradeoff parameter alpha which is used by the algorithm.
    * 
    * @return the tradeoff parameter
    */
   public Double getTradeoffParameter() {
      return this.tradeoffParameter;
   }


   /**
    * Returns the regularization parameter which is used by the algorithm.
    * 
    * @return the regularization parameter
    */
   public Double getRegularizationParameter() {
      return this.regularizationParameter;
   }


   /**
    * Returns the number of iterations which are used by the algorithm.
    * 
    * @return number of iterations
    */
   public int getNumberOfIterations() {
      return this.iterations;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof CombinedRankingAndRegressionConfiguration) {
         CombinedRankingAndRegressionConfiguration crrAlgorithmConfiguration = (CombinedRankingAndRegressionConfiguration) secondObject;
         if (lossFunctionIdentifier.equals(crrAlgorithmConfiguration.lossFunctionIdentifier)
               && (Double.compare(crrAlgorithmConfiguration.tradeoffParameter, tradeoffParameter) == 0)
               && (Double.compare(crrAlgorithmConfiguration.regularizationParameter, regularizationParameter) == 0)
               && (Double.compare(crrAlgorithmConfiguration.iterations, iterations) == 0)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * Double.hashCode(tradeoffParameter);
      hashCode += 31 * lossFunctionIdentifier.hashCode();
      hashCode += 31 * Integer.hashCode(iterations);
      hashCode += 31 * Double.hashCode(regularizationParameter);
      return hashCode;
   }


   @Override
   public String toString() {
      return String.format(TO_STRING_OUTPUT, lossFunctionIdentifier, tradeoffParameter, regularizationParameter, iterations);
   }

}
