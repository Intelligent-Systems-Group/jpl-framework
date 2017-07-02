package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link KemenyYoungLearningAlgorithm}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class KemenyYoungConfiguration extends AAlgorithmConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "rankaggregation"
         + StringUtils.FORWARD_SLASH + "kemeny_young";


   /**
    * Creates a new configuration for the {@link KemenyYoungLearningAlgorithm}
    */
   public KemenyYoungConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // This configuration has no parameters.
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      // This configuration has no parameters.
   }


   @Override
   public String toString() {
      return StringUtils.EMPTY_STRING;
   }


}