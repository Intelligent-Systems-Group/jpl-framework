package de.upb.cs.is.jpl.api.evaluation.instanceranking.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Configuration file for the {@link InstanceRankingSuppliedTestsetEvaluation}.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingSuppliedTestsetEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "instanceranking" + StringUtils.FORWARD_SLASH
         + "instance_ranking_supplied_testset_evaluation";


   /**
    * Creates a new configuration file.
    */
   protected InstanceRankingSuppliedTestsetEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


}
