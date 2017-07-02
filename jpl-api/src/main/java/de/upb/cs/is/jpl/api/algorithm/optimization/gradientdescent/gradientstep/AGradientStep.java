package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.WrongConfigurationTypeException;


/**
 * The abstract gradient step is supposed to be the superclass of all gradient step implementations.
 * It implements common functionality among all gradient steps. Most importantly it handles the
 * configuration of gradient step techniques which should all be subclasses from
 * {@link AGradientStepConfiguration}.
 * 
 * @author Alexander Hetzer
 * 
 * @param <CONFIG> the type of the configuration of this gradient step
 *
 */
public abstract class AGradientStep<CONFIG extends AGradientStepConfiguration> implements IGradientStep {

   private static final String ERROR_WRONG_CONFIGURATION_TYPE = "Initialized algorithm configuration with wrong configuration type.";

   protected CONFIG configuration;


   /**
    * Creates a new {@link AGradientStep}. This constructor DOES NOT call the init method, which
    * might lead to strange behavior.
    */
   protected AGradientStep() {
      getConfiguration();
   }


   /**
    * Creates the default algorithm configuration of this gradient step and returns it.
    * 
    * @return Default algorithm configuration of this algorithm
    */
   protected abstract AGradientStepConfiguration createDefaultConfiguration();


   @Override
   public AGradientStepConfiguration getDefaultConfiguration() {
      AGradientStepConfiguration defaultConfiguration = createDefaultConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public void setConfiguration(AGradientStepConfiguration configuration) {
      if (configuration.getClass().isInstance(createDefaultConfiguration().getClass())) {
         throw new WrongConfigurationTypeException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      this.configuration = (CONFIG) configuration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public AGradientStepConfiguration getConfiguration() {
      if (configuration == null) {
         configuration = (CONFIG) getDefaultConfiguration();
      }
      return configuration;
   }


   @Override
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException {
      getConfiguration().overrideConfiguration(jsonObject);
   }


   @Override
   public String toString() {
      return getClass().getSimpleName();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AGradientStep<?> other = (AGradientStep<?>) obj;
      if (configuration == null) {
         if (other.configuration != null)
            return false;
      } else if (!configuration.equals(other.configuration))
         return false;
      return true;
   }


}
