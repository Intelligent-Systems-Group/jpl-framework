package de.upb.cs.is.jpl.api.evaluation.labelranking.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class is the configuration for the {@link LabelRankingCrossValidationEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingCrossValidationEvaluationConfiguration extends ACrossValidationEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "labelranking" + StringUtils.FORWARD_SLASH
         + "label_ranking_cross_validation_evaluation";


   protected LabelRankingCrossValidationEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
