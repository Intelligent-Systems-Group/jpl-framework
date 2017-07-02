package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.minibatchpegasos;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for a implementation of
 * {@link MiniBatchPegasosLearningAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class MiniBatchPegasosConfiguration extends LinearClassificationConfiguration {

   private static final String ERROR_REGULARIZATION_PARAMETER_IS_NOT_POSITIVE_VALUE = "The regularization parameter is not a positive value!";
   private static final String ERROR_SIZE_OF_SUBSET_IS_TO_SMALL = "The size of the subset is to small, it has to be at least 1!";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "classification"
         + StringUtils.FORWARD_SLASH + "linear" + StringUtils.FORWARD_SLASH + "mini_batch_pegasos";

   private static final String SUBSET_SIZE = "subset_size";
   private static final String REGULATIZATION_PARAMETER = "regularization_parameter";

   @SerializedName(SUBSET_SIZE)
   private int subsetSize = Integer.MAX_VALUE;

   @SerializedName(REGULATIZATION_PARAMETER)
   private double regularizationParameter = Double.MAX_VALUE;


   /**
    * Creates a default configuration for {@link MiniBatchPegasosLearningAlgorithm}.
    */
   public MiniBatchPegasosConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertCorrectSubsetSize();
      assertCorrectRegularizationParameter();
   }


   /**
    * Checks weather the subset size is valid.
    * 
    * @throws ParameterValidationFailedException if the subset size is not valid
    */
   private void assertCorrectSubsetSize() throws ParameterValidationFailedException {
      if (subsetSize < 1) {
         throw new ParameterValidationFailedException(ERROR_SIZE_OF_SUBSET_IS_TO_SMALL);
      }
   }


   /**
    * Checks weather the regularization parameter is valid.
    * 
    * @throws ParameterValidationFailedException if the regularization parameter is not valid
    */
   private void assertCorrectRegularizationParameter() throws ParameterValidationFailedException {
      if (regularizationParameter <= 0) {
         throw new ParameterValidationFailedException(ERROR_REGULARIZATION_PARAMETER_IS_NOT_POSITIVE_VALUE);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      MiniBatchPegasosConfiguration castedConfiguration = (MiniBatchPegasosConfiguration) configuration;
      copySubsetSize(castedConfiguration);
      copyRegularizationParameter(castedConfiguration);
   }


   /**
    * Copies the regularization parameter of the given {@link MiniBatchPegasosConfiguration}.
    * 
    * @param castedConfiguration the configuration to copy from
    */
   private void copyRegularizationParameter(MiniBatchPegasosConfiguration castedConfiguration) {
      if (castedConfiguration.regularizationParameter < Double.MAX_VALUE) {
         this.regularizationParameter = castedConfiguration.regularizationParameter;
      }
   }


   /**
    * Copies the subset size of the given {@link MiniBatchPegasosConfiguration}.
    * 
    * @param castedConfiguration the configuration to copy from
    */
   private void copySubsetSize(MiniBatchPegasosConfiguration castedConfiguration) {
      if (castedConfiguration.subsetSize < Integer.MAX_VALUE) {
         this.subsetSize = castedConfiguration.subsetSize;
      }
   }


   /**
    * Returns the size of the subset used in the according algorithm.
    * 
    * @return the size of the subset used in the according algorithm
    */
   public int getSubsetSize() {
      return subsetSize;
   }


   /**
    * Sets the size of the subset used in the according algorithm.
    * 
    * @param subsetSize the size of the subset to be used in the according algorithm
    */
   public void setSubsetSize(int subsetSize) {
      this.subsetSize = subsetSize;
   }


   /**
    * Returns the regularization parameter used in the according algorithm.
    * 
    * @return the regularization parameter used in the according algorithm
    */
   public double getRegularizationParameter() {
      return regularizationParameter;
   }


   /**
    * Sets the regularization parameter used in the according algorithm.
    * 
    * @param regularizationParameter the regularization parameter to be used in the according
    *           algorithm
    */
   public void setRegularizationParameter(double regularizationParameter) {
      this.regularizationParameter = regularizationParameter;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      MiniBatchPegasosConfiguration other = (MiniBatchPegasosConfiguration) obj;
      if (Double.doubleToLongBits(regularizationParameter) != Double.doubleToLongBits(other.regularizationParameter))
         return false;
      if (subsetSize != other.subsetSize)
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(regularizationParameter);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + subsetSize;
      return result;
   }


   @Override
   public String toString() {
      return super.toString() + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SUBSET_SIZE + StringUtils.COLON + subsetSize
            + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + REGULATIZATION_PARAMETER + StringUtils.COLON + regularizationParameter;
   }

}
