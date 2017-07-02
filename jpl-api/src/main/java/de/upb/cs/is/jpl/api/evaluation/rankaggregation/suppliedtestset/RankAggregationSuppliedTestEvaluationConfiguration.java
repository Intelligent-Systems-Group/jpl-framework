package de.upb.cs.is.jpl.api.evaluation.rankaggregation.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class stores the configuration i.e. all parameters for a implementation of for
 * {@link RankAggregationSuppliedTestSetEvaluation}.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration
 * @author Pritha Gupta
 */
public class RankAggregationSuppliedTestEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {


   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "rankaggregation" + StringUtils.FORWARD_SLASH
         + "rank_aggregation_supplied_testset_evaluation";


   /**
    * Creates a new object for {@link RankAggregationSuppliedTestEvaluationConfiguration} and
    * initialize it with default configuration provided in the file.
    */
   public RankAggregationSuppliedTestEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


}
