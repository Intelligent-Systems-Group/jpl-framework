package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Evaluation by splitting the dataset into a training and a testing dataset based on a percentage
 * number.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringPercentageSplitEvaluationConfiguration extends APercentageSplitEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "collaborativefiltering" + StringUtils.FORWARD_SLASH
         + "collaborative_filtering_percentage_split_evaluation";


   /**
    * Creates a new Configuration for percentage split evaluation.
    */
   public CollaborativeFilteringPercentageSplitEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
