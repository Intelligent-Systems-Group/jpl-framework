package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link ExpectedRankRegression}.
 * 
 * @author Pritha Gupta
 *
 */
public class ExpectedRankRegressionConfiguration extends AAlgorithmConfigurationWithBaseLearner {


   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "objectranking"
         + StringUtils.FORWARD_SLASH + "expected_rank_regression";


   /**
    * Creates a new configuration for the {@link ExpectedRankRegression}.
    */
   public ExpectedRankRegressionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertBaseLearnerIsRegressionLearningAlgorithm();
   }

}

