package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link SimpleOrdinalClassification}.
 * 
 * @author Tanja Tornede
 *
 */
public class SimpleOrdinalClassificationConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "ordinalclassification"
         + StringUtils.FORWARD_SLASH + "simple_ordinal_classification";


   /**
    * Creates a new configuration for the {@link SimpleOrdinalClassification}.
    */
   public SimpleOrdinalClassificationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof SimpleOrdinalClassificationConfiguration) {
         return true;
      }
      return false;
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertBaseLearnerReturnsProbability();
   }

}
