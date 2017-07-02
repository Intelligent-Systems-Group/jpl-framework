package de.upb.cs.is.jpl.api.evaluation.labelranking.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class is the configuration for the {@link LabelRankingSuppliedTestsetEvaluation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingSuppliedTestsetEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "labelranking" + StringUtils.FORWARD_SLASH
         + "label_ranking_supplied_testset_evaluation";


   protected LabelRankingSuppliedTestsetEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


}
