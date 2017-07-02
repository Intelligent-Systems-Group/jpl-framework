package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.insample;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Evaluation by splitting the dataset into training and testing datasets based on a number of
 * folds.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringInSampleEvaluationConfiguration extends ACrossValidationEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "collaborativefiltering" + StringUtils.FORWARD_SLASH
         + "collaborative_filtering_in_sample_evaluation";


   /**
    * Creates a new Configuration for percentage split evaluation.
    */
   public CollaborativeFilteringInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
