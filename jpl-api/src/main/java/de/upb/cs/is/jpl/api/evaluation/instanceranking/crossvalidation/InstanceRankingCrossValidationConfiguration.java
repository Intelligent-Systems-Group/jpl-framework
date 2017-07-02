package de.upb.cs.is.jpl.api.evaluation.instanceranking.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Configuration file for the {@link InstanceRankingCrossValidationEvaluation}.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingCrossValidationConfiguration extends ACrossValidationEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "instanceranking" + StringUtils.FORWARD_SLASH
         + "instance_ranking_cross_validation_evaluation";


   /**
    * Creates a new configuration file.
    */
   protected InstanceRankingCrossValidationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
