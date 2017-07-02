package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used as an algorithm configuration for the
 * {@link BinaryRelevanceLearningAlgorithm}.
 * 
 * @author Alexander Hetzer
 *
 */
public class BinaryRelevanceLearningConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH
         + "multilabelclassification" + StringUtils.FORWARD_SLASH + "binary_relevance_learning";


   /**
    * Creates a binary relevance learning algorithm configuration.
    */
   public BinaryRelevanceLearningConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertBaseLearnerIsClassifier();
   }


}
