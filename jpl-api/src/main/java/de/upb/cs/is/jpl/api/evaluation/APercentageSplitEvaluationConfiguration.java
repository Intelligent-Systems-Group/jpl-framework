package de.upb.cs.is.jpl.api.evaluation;


import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;


/**
 * This is an abstract class containing configuration for the percentage-split evaluation that is
 * running on current command. The percentage parameter define the split percentage for the spiting
 * the dataset in to testing and training dataset. The number of datasets parameter defines the
 * number of shuffled pairs of training and testing dataset, which will be created. You should
 * always call up to your superclass when implementing these methods:
 * <ul>
 * <li>validateParameters()</li>
 * <li>copyValues(IJsonConfiguration configuration)</li>
 * </ul>
 * 
 * @author Pritha Gupta
 * @see AEvaluationConfiguration
 * 
 */
public abstract class APercentageSplitEvaluationConfiguration extends AEvaluationConfiguration {
   private static final String VALIDATION_PERCENTAGE_ERROR_MESSAGE = "Value of percentage '%f' for the percentage split evaluation is invalid";
   private static final String VALIDATION_NUMBER_OF_DATASETS_ERROR_MESSAGE = "Value of number of datasets '%d' for the percentage split evaluation is invalid";

   private static final Logger logger = LoggerFactory.getLogger(ACrossValidationEvaluationConfiguration.class);
   private static final String PERCENTAGE_SPLIT_MESSAGE = "Percentage Split evaluation with %s split-percentage and %d shuffled datasets for %s.";

   @SerializedName(EvaluationsKeyValuePairs.PERCENTAGE_FOR_EVALUATION)
   protected float percentage = Float.MAX_VALUE;

   @SerializedName(EvaluationsKeyValuePairs.NUMBER_OF_ITERATIONS_FOR_PERCENATGE_SPLIT)
   protected int numOfDatasets = Integer.MAX_VALUE;


   /**
    * Creates an abstract percentage split evaluation configuration and initialize it with default
    * configuration provided in the file.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   public APercentageSplitEvaluationConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      if (percentage < 0.0 || percentage >= 1.0) {
         logger.error(String.format(VALIDATION_PERCENTAGE_ERROR_MESSAGE, percentage));
         throw new ParameterValidationFailedException(String.format(VALIDATION_PERCENTAGE_ERROR_MESSAGE, percentage));
      }
      if (numOfDatasets < 0 || numOfDatasets > 10) {
         logger.error(String.format(VALIDATION_NUMBER_OF_DATASETS_ERROR_MESSAGE, numOfDatasets));
         throw new ParameterValidationFailedException(String.format(VALIDATION_NUMBER_OF_DATASETS_ERROR_MESSAGE, numOfDatasets));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      APercentageSplitEvaluationConfiguration castedConfiguration = (APercentageSplitEvaluationConfiguration) configuration;
      if (castedConfiguration.percentage < Float.MAX_VALUE) {
         this.percentage = castedConfiguration.percentage;
      }
      if (castedConfiguration.numOfDatasets < Integer.MAX_VALUE) {
         this.numOfDatasets = castedConfiguration.numOfDatasets;
      }

   }


   /**
    * Returns the float value of the percentage for splitting the data into test and training
    * dataset.
    * 
    * @return the percentage for the split
    */
   public float getPercentage() {
      return percentage;
   }


   /**
    * Returns the integer value of the dataset pairs of test and training dataset, split using
    * percentage provided.
    * 
    * @return the number of datasets pairs to be generated
    */
   public int getNumOfDatasets() {
      return numOfDatasets;
   }


   /**
    * Set the integer value of the dataset pairs of test and training dataset, split using
    * percentage provided.
    * 
    * @param numOfDatasets the number of datasets pairs to be generated
    * 
    */
   public void setNumOfDatasets(int numOfDatasets) {
      this.numOfDatasets = numOfDatasets;
   }


   /**
    * Sets the percentage in the configuration.
    * 
    * @param newPercentage the new value for the percentage split
    */
   public void setPercentage(float newPercentage) {
      percentage = newPercentage;
   }


   @Override
   public String toString() {
      DecimalFormat decimalFormat = new DecimalFormat("#.##");
      return String.format(PERCENTAGE_SPLIT_MESSAGE, decimalFormat.format(percentage), numOfDatasets,
            this.eLearningProblem.getLearningProblemIdentifier());
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + numOfDatasets;
      result = prime * result + Float.floatToIntBits(percentage);
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof APercentageSplitEvaluationConfiguration) {
         APercentageSplitEvaluationConfiguration castedObject = APercentageSplitEvaluationConfiguration.class.cast(secondObject);
         if (Float.compare(percentage, castedObject.percentage) == 0 || numOfDatasets == castedObject.numOfDatasets) {
            return true;
         }
      }
      return false;
   }
}
