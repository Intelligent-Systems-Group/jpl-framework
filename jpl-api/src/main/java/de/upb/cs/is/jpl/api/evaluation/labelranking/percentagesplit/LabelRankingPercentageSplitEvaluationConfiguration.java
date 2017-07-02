package de.upb.cs.is.jpl.api.evaluation.labelranking.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class is the configuration for the {@link LabelRankingPercentageSplitEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingPercentageSplitEvaluationConfiguration extends APercentageSplitEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "labelranking" + StringUtils.FORWARD_SLASH
         + "label_ranking_percentage_split_evaluation";


   protected LabelRankingPercentageSplitEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
