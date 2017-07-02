package de.upb.cs.is.jpl.api.metric.kendallstau;


import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;


/**
 * This class stores the configuration i.e. all parameters for a implementation of for
 * {@link KendallsTauConfiguration}.
 * 
 * @see de.upb.cs.is.jpl.api.metric.AMetricConfiguration
 * @author Pritha Gupta
 * @author Andreas Kornelsen
 */
public class KendallsTauConfiguration extends AMetricConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "kendalls_tau";


   /**
    * Creates a new object {@link KendallsTau} configuration and initialize it with default
    * configuration provided in the file.
    * 
    */
   public KendallsTauConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // not applicable

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      // not applicable

   }


}
