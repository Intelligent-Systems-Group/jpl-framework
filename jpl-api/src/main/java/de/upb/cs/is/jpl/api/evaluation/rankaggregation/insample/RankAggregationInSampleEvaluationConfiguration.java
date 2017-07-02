package de.upb.cs.is.jpl.api.evaluation.rankaggregation.insample;


import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class stores the configuration i.e. all parameters for a implementation of for
 * {@link RankAggregationInSampleEvaluation}.
 * 
 * @author Pritha Gupta
 *
 */
public class RankAggregationInSampleEvaluationConfiguration extends AInSampleEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "rankaggregation" + StringUtils.FORWARD_SLASH
         + "rank_aggregation_in_sample_evaluation";


   /**
    * Creates a new object {@link RankAggregationInSampleEvaluationConfiguration} configuration and
    * initialize it with default configuration provided in the file.
    */
   protected RankAggregationInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
