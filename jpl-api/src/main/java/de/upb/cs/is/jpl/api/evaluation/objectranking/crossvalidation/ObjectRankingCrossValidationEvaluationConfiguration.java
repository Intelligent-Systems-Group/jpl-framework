package de.upb.cs.is.jpl.api.evaluation.objectranking.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains the configuration for cross-validation evaluation for object ranking
 * problems.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingCrossValidationEvaluationConfiguration extends ACrossValidationEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "objectranking" + StringUtils.FORWARD_SLASH
         + "object_ranking_crossvalidation_evaluation";


   /**
    * Creates a new object {@link ObjectRankingCrossValidationEvaluationConfiguration} configuration
    * and initialize it with default configuration provided in the file.
    */
   protected ObjectRankingCrossValidationEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
