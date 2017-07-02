package de.upb.cs.is.jpl.api.metric;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;


/**
 * AEvaluationMetricConfiguration class contains configuration for the EvaluationMetric that is used
 * on current command. This class can be extended for different types of EvaluationMetric. At one
 * time there can be different evaluationMetrics on the basis of which we do the evaluation(s).
 * 
 * @author Pritha Gupta
 */
public abstract class AMetricConfiguration extends AJsonConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "metric";


   /**
    * Creates an abstract evaluation metric configuration and initialize it with default
    * configuration provided in the file.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   public AMetricConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   @Override
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      return super.getDefaultConfigurationFileAsJsonObject().getAsJsonObject(DEFAULT_PARAMETER_VALUES);
   }

}
