package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.fixedlearningrate;


import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStepConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;


/**
 * This is the configuration for the {@link FixedLearningRateGradientStep}. This step procedure has
 * no parameters and therefore has an empty configuration.
 * 
 * @author Alexander Hetzer
 *
 */
public class FixedLearningRateGradientStepConfiguration extends AGradientStepConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "fixed_learning_rate_gradient_step";


   /**
    * Creates a new {@link FixedLearningRateGradientStepConfiguration}.
    */
   public FixedLearningRateGradientStepConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // empty as this configuration has no parameters
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      // empty as this configuration has no parameters
   }


   @Override
   public String toString() {
      return "-";
   }


}
