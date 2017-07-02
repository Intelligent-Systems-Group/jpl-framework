package de.upb.cs.is.jpl.api.evaluation.objectranking.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains the configuration for percentage-split evaluation for object ranking
 * problems.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingPercentageSplitEvaluationConfiguration extends APercentageSplitEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "objectranking" + StringUtils.FORWARD_SLASH
         + "object_ranking_percenatge_split_evaluation";


   /**
    * Creates a new object {@link ObjectRankingPercentageSplitEvaluationConfiguration} configuration
    * and initialize it with default configuration provided in the file.
    */
   protected ObjectRankingPercentageSplitEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
