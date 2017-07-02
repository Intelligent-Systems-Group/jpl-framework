package de.upb.cs.is.jpl.api.metric;


import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;


/**
 * This class represents an empty evaluation metric configuration, with no functionality. It is
 * intended to be used by evaluations having no parameters and therefore needing no configuration.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class EmptyMetricConfiguration extends AMetricConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "empty_evaluation_metric";


   /**
    * Creates a new empty evaluation metric configuration.
    * 
    */
   public EmptyMetricConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // Not needed because no parameter are used

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      // Not needed because no parameter are used

   }

}
