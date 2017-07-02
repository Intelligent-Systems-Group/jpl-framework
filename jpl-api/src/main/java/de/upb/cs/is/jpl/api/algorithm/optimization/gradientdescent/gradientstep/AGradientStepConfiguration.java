package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents an abstract configuration for {@link AGradientStep}. By default it does not
 * define any parameter, as there are no common parameters for gradient step approaches. All
 * implementations of {@link AGradientStep} should create a configuration extending this class.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AGradientStepConfiguration extends AJsonConfiguration {

   protected static final String ERROR_INVALID_VALUE_FOR_PARAMETER = "Invalid value for parameter %s: %s";

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "algorithm" + StringUtils.FORWARD_SLASH + "optimization"
         + StringUtils.FORWARD_SLASH + "gradient_descent" + StringUtils.FORWARD_SLASH + "gradient_step";


   /**
    * Creates a new {@link AGradientStepConfiguration} with the given default configuration file
    * name.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file name
    */
   public AGradientStepConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   @Override
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      return super.getDefaultConfigurationFileAsJsonObject().getAsJsonObject(DEFAULT_PARAMETER_VALUES);
   }

}
