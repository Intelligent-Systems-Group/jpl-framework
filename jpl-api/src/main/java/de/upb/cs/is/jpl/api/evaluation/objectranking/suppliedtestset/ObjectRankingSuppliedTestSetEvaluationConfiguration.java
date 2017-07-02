package de.upb.cs.is.jpl.api.evaluation.objectranking.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.objectranking.percentagesplit.ObjectRankingPercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains the configuration for percentage-split evaluation for object ranking
 * problems.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingSuppliedTestSetEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "objectranking" + StringUtils.FORWARD_SLASH
         + "object_ranking_supplied_test_set_evaluation";


   /**
    * Creates a new object {@link ObjectRankingPercentageSplitEvaluationConfiguration} configuration
    * and initialize it with default configuration provided in the file.
    */
   protected ObjectRankingSuppliedTestSetEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
