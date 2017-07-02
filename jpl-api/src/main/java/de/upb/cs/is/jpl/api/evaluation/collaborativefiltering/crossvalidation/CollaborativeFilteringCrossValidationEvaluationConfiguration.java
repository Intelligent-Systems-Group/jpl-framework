package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Evaluation by splitting the dataset into training and testing datasets based on a number of
 * folds.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringCrossValidationEvaluationConfiguration extends ACrossValidationEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "collaborativefiltering" + StringUtils.FORWARD_SLASH
         + "collaborative_filtering_cross_validation_evaluation";


   /**
    * Creates a new Configuration for percentage split evaluation.
    */
   public CollaborativeFilteringCrossValidationEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
