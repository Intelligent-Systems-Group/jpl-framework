package de.upb.cs.is.jpl.api.metric.spearmancorrelation;


import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;


/**
 * This class stores the configuration for the {@link SpearmansCorrelation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class SpearmansConfiguration extends AMetricConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "spearman_correlation";


   /**
    * Creates a new object {@link SpearmansConfiguration} configuration and initialize it with
    * default configuration provided in the file.
    * 
    */
   public SpearmansConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // No validation required.
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      // No copy of values required.
   }
}
