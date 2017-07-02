package de.upb.cs.is.jpl.api.evaluation.labelranking.insample;


import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class is the configuration for the {@link LabelRankingInSampleEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingInSampleEvaluationConfiguration extends AInSampleEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "labelranking" + StringUtils.FORWARD_SLASH
         + "label_ranking_in_sample_evaluation";


   protected LabelRankingInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
