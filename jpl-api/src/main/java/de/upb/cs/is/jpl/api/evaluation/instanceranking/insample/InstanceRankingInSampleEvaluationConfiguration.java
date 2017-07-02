package de.upb.cs.is.jpl.api.evaluation.instanceranking.insample;


import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Configuration file for the {@link InstanceRankingInSampleEvaluation}.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingInSampleEvaluationConfiguration extends AInSampleEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "instanceranking" + StringUtils.FORWARD_SLASH
         + "instance_ranking_in_sample_evaluation";


   /**
    * Creates a new configuration file.
    */
   protected InstanceRankingInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
