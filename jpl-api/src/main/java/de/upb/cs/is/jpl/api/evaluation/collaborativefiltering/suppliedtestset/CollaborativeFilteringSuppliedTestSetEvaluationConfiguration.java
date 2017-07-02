package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Evaluation by splitting the dataset into fixed a training and a testing dataset.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringSuppliedTestSetEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "collaborativefiltering" + StringUtils.FORWARD_SLASH
         + "collaborative_filtering_test_set_evaluation";


   /**
    * Creates a new Configuration for percentage split evaluation.
    */
   public CollaborativeFilteringSuppliedTestSetEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


}
