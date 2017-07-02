package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link LabelRankingByPairwiseComparisonLearningAlgorithm}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingByPairwiseComparisonConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "labelranking"
         + StringUtils.FORWARD_SLASH + "label_ranking_by_pairwise_comparison";


   /**
    * Creates a new configuration for the {@link LabelRankingByPairwiseComparisonLearningAlgorithm}
    */
   public LabelRankingByPairwiseComparisonConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


}