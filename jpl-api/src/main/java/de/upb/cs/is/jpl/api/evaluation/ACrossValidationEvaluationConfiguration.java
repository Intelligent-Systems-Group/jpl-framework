package de.upb.cs.is.jpl.api.evaluation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;


/**
 *
 * This is an abstract class containing configuration for the cross-validation evaluation that is
 * running on current command. The folds parameter defines the number of folds with which the
 * cross-validation evaluation will be running. You should always call up to your superclass when
 * implementing these methods:
 * <ul>
 * <li>validateParameters()</li>
 * <li>copyValues(IJsonConfiguration configuration)</li>
 * </ul>
 * Number of folds = 1 specifies that, you need leave-one-out cross validation else its specifies
 * number of folds
 * 
 * @author Pritha Gupta
 * @see AEvaluationConfiguration
 * 
 */
public abstract class ACrossValidationEvaluationConfiguration extends AEvaluationConfiguration {


   private static final String VALIDATION_FOLD_ERROR_MESSAGE = "Value of folds '%d' for the cross validation evaluation is invalid";
   private static final Logger logger = LoggerFactory.getLogger(ACrossValidationEvaluationConfiguration.class);
   private static final String LEAVE_ONE_OUT_MESSAGE = "Leave-one-out Cross Validation evaluation ran for learning problem %s.";
   private static final String CROSS_VALIDATION_MESSAGE = "%d-fold Cross Validation evaluation for %s.";

   @SerializedName(EvaluationsKeyValuePairs.FOLDS_CROSS_VALIDATION)
   protected int folds = Integer.MAX_VALUE;


   /**
    * Creates an abstract cross validation evaluation configuration and initialize it with default
    * configuration provided in the file.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   public ACrossValidationEvaluationConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      if (folds < 1) {
         String errorMessage = String.format(VALIDATION_FOLD_ERROR_MESSAGE, folds);
         logger.error(errorMessage);
         throw new ParameterValidationFailedException(errorMessage);
      }

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      ACrossValidationEvaluationConfiguration castedConfiguration = (ACrossValidationEvaluationConfiguration) configuration;
      if (castedConfiguration.folds != Integer.MAX_VALUE) {
         this.folds = castedConfiguration.folds;
      }
   }


   /**
    * Returns the value folds in K-fold cross validation. It signifies the number of instances taken
    * at a time for evaluations.
    * 
    * @return the number of folds required for evaluations
    */
   public int getFolds() {
      return folds;
   }


   /**
    * Set the value folds in K-fold cross validation. It signifies the number of instances taken at
    * a time for evaluations.
    * 
    * @param folds the number of folds
    */
   public void setFolds(int folds) {
      this.folds = folds;
   }


   @Override
   public String toString() {
      String output;
      if (folds == 1) {
         output = String.format(LEAVE_ONE_OUT_MESSAGE, this.eLearningProblem.getLearningProblemIdentifier());
      } else {
         output = String.format(CROSS_VALIDATION_MESSAGE, folds, this.eLearningProblem.getLearningProblemIdentifier());
      }
      return output;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + folds;
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ACrossValidationEvaluationConfiguration) {
         ACrossValidationEvaluationConfiguration castedObject = ACrossValidationEvaluationConfiguration.class.cast(secondObject);
         if (folds == castedObject.folds) {
            return true;
         }
      }
      return false;
   }

}
