package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStepConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.EGradientStep;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.IGradientStep;
import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NamedParameterDefinition;


/**
 * This class represents the abstract configuration for a gradient descent approach. Each gradient
 * descent approach holds a learning rate, a gradient step definition and its according
 * implementation and enum.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AGradientDescentConfiguration extends AJsonConfiguration {

   protected static final String ERROR_INVALID_VALUE_FOR_PARAMETER = "Invalid value for parameter %s: %s";

   private static final String ERROR_GRADIENT_STEP_CANNOT_BE_CREATED = "Gradient step cannot be created for the identifier: %s";
   private static final String ERROR_UNKNOWN_GRADIENT_STEP_IDENTIFIER = "Gradient step identifer provided is invalid: %s";

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "algorithm" + StringUtils.FORWARD_SLASH + "optimization"
         + StringUtils.FORWARD_SLASH + "gradient_descent";

   private static final String PARAMETER_NAME_GRADIENT_STEP = "gradient_step";
   private static final String PARAMETER_NAME_LEARNING_RATE = "learning_rate";

   @SerializedName(PARAMETER_NAME_GRADIENT_STEP)
   protected NamedParameterDefinition gradientStepDefinition = null;

   /** The learning rate used for the update of the weight vector. */
   @SerializedName(PARAMETER_NAME_LEARNING_RATE)
   protected double learningRate = Double.MAX_VALUE;

   /** The gradient step approach used during the gradient descent. */
   protected transient IGradientStep gradientStep;
   protected transient EGradientStep eGradientStep;


   /**
    * Creates a new {@link AGradientDescentConfiguration} with the given default configuration file
    * name.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   public AGradientDescentConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   /**
    * This method obtains the actual implementation of the gradient step approach defined in the
    * gradient step definition of this class.
    * 
    * @throws ParameterValidationFailedException if obtaining the actual implementation of the
    *            gradient step approach defined in the gradient step definition of this class fails
    */
   private void setGradientStepWithOverridenParameters() throws ParameterValidationFailedException {
      eGradientStep = EGradientStep.getEGradientStepByIdentifier(gradientStepDefinition.getIdentifier());
      if (eGradientStep == null) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_UNKNOWN_GRADIENT_STEP_IDENTIFIER, gradientStepDefinition.getIdentifier()));
      } else {
         gradientStep = eGradientStep.createGradientStep();
         if (gradientStep == null) {
            throw new ParameterValidationFailedException(
                  String.format(ERROR_GRADIENT_STEP_CANNOT_BE_CREATED, gradientStepDefinition.getIdentifier()));
         }
         AGradientStepConfiguration configuration = gradientStep.getConfiguration();
         configuration.overrideConfiguration(gradientStepDefinition.getParameters());
         gradientStep.setConfiguration(configuration);
      }
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      setGradientStepWithOverridenParameters();
   }


   @Override
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      return super.getDefaultConfigurationFileAsJsonObject().getAsJsonObject(DEFAULT_PARAMETER_VALUES);
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      AGradientDescentConfiguration gradientDescentConfiguration = (AGradientDescentConfiguration) configuration;
      if (gradientDescentConfiguration.gradientStepDefinition != null) {
         this.gradientStepDefinition = gradientDescentConfiguration.gradientStepDefinition;
      }
      if (gradientDescentConfiguration.learningRate < Double.MAX_VALUE) {
         this.learningRate = gradientDescentConfiguration.learningRate;
      }
   }


   /**
    * Returns the current learning rate.
    * 
    * @return the current learning rate
    */
   public double getLearningRate() {
      return learningRate;
   }


   /**
    * Sets the learning rate to the given value.
    * 
    * @param learningRate the learning rate to set
    */
   public void setLearningRate(double learningRate) {
      this.learningRate = learningRate;
   }


   /**
    * Returns the gradient step procedure.
    * 
    * @return the gradient step procedure
    */
   public IGradientStep getGradientStep() {
      return gradientStep;
   }


   /**
    * Sets the gradient step procedure to the given procedure.
    * 
    * @param gradientStep the gradient step procedure to set
    */
   public void setGradientStep(IGradientStep gradientStep) {
      this.gradientStep = gradientStep;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_LEARNING_RATE + StringUtils.COLON + learningRate + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + PARAMETER_NAME_GRADIENT_STEP + StringUtils.COLON + gradientStep.toString() + StringUtils.SINGLE_WHITESPACE
            + StringUtils.ROUND_BRACKET_OPEN + gradientStep.getConfiguration().toString() + StringUtils.ROUND_BRACKET_CLOSE;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((gradientStepDefinition == null) ? 0 : gradientStepDefinition.hashCode());
      long temp;
      temp = Double.doubleToLongBits(learningRate);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      AGradientDescentConfiguration other = (AGradientDescentConfiguration) obj;
      if (gradientStepDefinition == null) {
         if (other.gradientStepDefinition != null)
            return false;
      } else if (!gradientStepDefinition.equals(other.gradientStepDefinition))
         return false;
      if (Double.doubleToLongBits(learningRate) != Double.doubleToLongBits(other.learningRate))
         return false;
      return true;
   }


}
