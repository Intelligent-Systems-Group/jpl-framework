package de.upb.cs.is.jpl.api.evaluation.instanceranking.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Configuration file for the {@link InstanceRankingPercentageSplitEvaluation}.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingPercentageSplitEvaluationConfiguration extends APercentageSplitEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "instanceranking" + StringUtils.FORWARD_SLASH
         + "instance_ranking_percentage_split_evaluation";


   /**
    * Creates a new configuration file.
    */
   protected InstanceRankingPercentageSplitEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
