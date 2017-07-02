package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.logistic;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic.LogisticRegression;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for an implementation of
 * {@link LogisticClassification}.
 * 
 * @author Tanja Tornede
 *
 */
public class LogisticClassificationConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String ERROR_THRESHOLD_INVALID = "The threshold has to be a positive value smaller 1!";
   private static final String ERROR_WRONG_IDENTIFIER_FOR_BASE_LEARNER = "The parameter " + PARAMETER_NAME_BASE_LEARNER
         + " has to be of type regression but is %s:";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "classification"
         + StringUtils.FORWARD_SLASH + "logistic" + StringUtils.FORWARD_SLASH + "logistic_classification";

   private static final String THRESHOLD_IDENTIFIER = "threshold";

   @SerializedName(THRESHOLD_IDENTIFIER)
   protected double threshold = Double.MAX_VALUE;


   /**
    * Creates a default configuration for {@link LogisticClassification}.
    */
   public LogisticClassificationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertCorrectThreshold();
      assertLogisticRegressionAsBaselearner();
   }


   /**
    * Checks weather the baselearner set is {@link LogisticRegression}.
    * 
    * @throws ParameterValidationFailedException if another baselearner is set
    */
   private void assertLogisticRegressionAsBaselearner() throws ParameterValidationFailedException {
      if (ebaseLearner != EBaseLearner.LOGISTIC_REGRESSION) {
         throw new ParameterValidationFailedException(String.format(ERROR_WRONG_IDENTIFIER_FOR_BASE_LEARNER, ebaseLearner.name()));
      }
   }


   /**
    * Checks weather the threshold is set correctly.
    * 
    * @throws ParameterValidationFailedException if the threshold is not set correctly
    */
   private void assertCorrectThreshold() throws ParameterValidationFailedException {
      if (threshold <= 0 || threshold >= 1) {
         throw new ParameterValidationFailedException(ERROR_THRESHOLD_INVALID);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      LogisticClassificationConfiguration logisticClassificationConfiguration = (LogisticClassificationConfiguration) configuration;
      copyThreshold(logisticClassificationConfiguration);
   }


   /**
    * Copies the threshold of the given {@link LogisticClassificationConfiguration}.
    * 
    * @param logisticClassificationConfiguration the configuration to copy from
    */
   private void copyThreshold(LogisticClassificationConfiguration logisticClassificationConfiguration) {
      if (logisticClassificationConfiguration.threshold < Double.MAX_VALUE) {
         this.threshold = logisticClassificationConfiguration.threshold;
      }
   }


   /**
    * Returns the threshold of the according learning model.
    * 
    * @return the threshold of the according learning model
    */
   public double getThreshold() {
      return threshold;
   }


   /**
    * Sets the threshold of the according learning model.
    * 
    * @param threshold the threshold of the according learning model to set
    */
   public void setThreshold(double threshold) {
      this.threshold = threshold;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      LogisticClassificationConfiguration other = (LogisticClassificationConfiguration) obj;
      if (Double.doubleToLongBits(threshold) != Double.doubleToLongBits(other.threshold))
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(threshold);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public String toString() {
      return THRESHOLD_IDENTIFIER + StringUtils.COLON + threshold + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + super.toString();
   }

}
