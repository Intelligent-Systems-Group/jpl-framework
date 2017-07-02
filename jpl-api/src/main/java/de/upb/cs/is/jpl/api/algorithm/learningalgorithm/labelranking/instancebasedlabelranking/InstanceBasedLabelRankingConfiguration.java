package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborClassification;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AlgorithmDefinition;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison.LabelRankingByPairwiseComparisonLearningAlgorithm;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link InstanceBasedLabelRankingLearningAlgorithm}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class InstanceBasedLabelRankingConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "labelranking"
         + StringUtils.FORWARD_SLASH + "instance_based_label_ranking";

   private static final String ERROR_MESSAGE_NO_ALGORITHM_SUPPORTED_IN_THE_CONFIGURATION = "There is no valid label ranking algorithm supported in the configuration file.";

   private static final String ERROR_MESSAGE_SET_PARAEMTERS_RANK_AGGREGATION_ALGORITHM = "The parameters of the rank aggregation learning algorithm for the instance based learning algorithm couldn't be set.";

   private static final String ERROR_MESSAGE_BASE_LEARNER_NOT_SUPPORTED = "Only the base learner %s is supported.";


   private static final String JSON_KEY_RANK_AGGREGATION_ALGORITHM_JSON = "rank_aggregation_algorithm";


   @SerializedName(JSON_KEY_RANK_AGGREGATION_ALGORITHM_JSON)
   private AlgorithmDefinition rankAggregationAlgorithmDefinition;


   private ILearningAlgorithm rankAggregationAlgorithm;


   /**
    * Creates a new configuration for the {@link LabelRankingByPairwiseComparisonLearningAlgorithm}
    */
   public InstanceBasedLabelRankingConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      validateLearningAndBaseLearnerAlgorithm();
      setRankAggregationAlgorithmParameters();
   }


   /**
    * Sets the rank aggregation algorithm parameters.
    *
    * @throws ParameterValidationFailedException if the setting of the parameters failed
    */
   private void setRankAggregationAlgorithmParameters() throws ParameterValidationFailedException {
      try {
         AAlgorithmConfiguration rankAggregationConfiguration = rankAggregationAlgorithm.getAlgorithmConfiguration();
         rankAggregationConfiguration.overrideConfiguration(rankAggregationAlgorithmDefinition.getParameters());
         rankAggregationAlgorithm.setAlgorithmConfiguration(rankAggregationConfiguration);
      } catch (ParameterValidationFailedException e) {
         throw new ParameterValidationFailedException(ERROR_MESSAGE_SET_PARAEMTERS_RANK_AGGREGATION_ALGORITHM, e);
      }
   }


   /**
    * Validates the base learner algorithm and the rank aggregation learning algorithm.
    *
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   private void validateLearningAndBaseLearnerAlgorithm() throws ParameterValidationFailedException {
      if (this.rankAggregationAlgorithmDefinition == null || this.rankAggregationAlgorithmDefinition.getName() == null) {
         throw new ParameterValidationFailedException(ERROR_MESSAGE_NO_ALGORITHM_SUPPORTED_IN_THE_CONFIGURATION);
      }
      String rankAggregationAlgorithmString = this.rankAggregationAlgorithmDefinition.getName();
      boolean isRankAggregationIdentifierValid = false;
      ELearningAlgorithm[] eLearningAlgorithmsByLearningProblem = ELearningAlgorithm
            .getELearningAlgorithmsByLearningProblem(ELearningProblem.RANK_AGGREGATION);
      for (ELearningAlgorithm eLearningAlgorithm : eLearningAlgorithmsByLearningProblem) {
         if (eLearningAlgorithm.getIdentifier().equals(rankAggregationAlgorithmString)) {
            isRankAggregationIdentifierValid = true;
            break;
         }
      }

      if (!isRankAggregationIdentifierValid) {
         throw new ParameterValidationFailedException(ERROR_MESSAGE_NO_ALGORITHM_SUPPORTED_IN_THE_CONFIGURATION);
      }

      if (!(baseLearnerAlgorithm instanceof KNearestNeighborClassification)) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_MESSAGE_BASE_LEARNER_NOT_SUPPORTED, EBaseLearner.KNEAREST_NEIGHBOUR.getBaseLearnerIdentifier()));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      InstanceBasedLabelRankingConfiguration instanceBasedLabelRankingAlgorithmConfiguration = (InstanceBasedLabelRankingConfiguration) configuration;

      rankAggregationAlgorithmDefinition = instanceBasedLabelRankingAlgorithmConfiguration.rankAggregationAlgorithmDefinition;

      if (rankAggregationAlgorithmDefinition != null && rankAggregationAlgorithmDefinition.getName() != null) {

         String rankAggregationAlgorithmIdentifier = instanceBasedLabelRankingAlgorithmConfiguration.rankAggregationAlgorithmDefinition
               .getName();

         ELearningAlgorithm[] eLearningAlgorithmsByLearningProblem = ELearningAlgorithm
               .getELearningAlgorithmsByLearningProblem(ELearningProblem.RANK_AGGREGATION);
         for (ELearningAlgorithm eLearningAlgorithm : eLearningAlgorithmsByLearningProblem) {
            if (eLearningAlgorithm.getIdentifier().equals(rankAggregationAlgorithmIdentifier)) {
               rankAggregationAlgorithm = eLearningAlgorithm.createLearningAlgorithm();
               break;
            }
         }
      }

   }


   /**
    * Returns the rank aggregation algorithm.
    *
    * @return the rank aggregation algorithm
    */
   public ILearningAlgorithm getRankAggregationAlgorithm() {
      return rankAggregationAlgorithm;
   }


   @Override
   public String toString() {
      StringBuilder toStringBuilder = new StringBuilder();
      toStringBuilder.append(super.toString());
      toStringBuilder.append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND);
      toStringBuilder.append(JSON_KEY_RANK_AGGREGATION_ALGORITHM_JSON);
      toStringBuilder.append(StringUtils.COLON);
      toStringBuilder.append(rankAggregationAlgorithm.toString());
      toStringBuilder.append(StringUtils.SINGLE_WHITESPACE);
      toStringBuilder.append(StringUtils.ROUND_BRACKET_OPEN);
      toStringBuilder.append(rankAggregationAlgorithm.getAlgorithmConfiguration().toString());
      toStringBuilder.append(StringUtils.ROUND_BRACKET_CLOSE);
      return toStringBuilder.toString();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((rankAggregationAlgorithm == null) ? 0 : rankAggregationAlgorithm.hashCode());
      result = prime * result + ((rankAggregationAlgorithmDefinition == null) ? 0 : rankAggregationAlgorithmDefinition.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof InstanceBasedLabelRankingConfiguration))
         return false;
      InstanceBasedLabelRankingConfiguration other = (InstanceBasedLabelRankingConfiguration) obj;
      if (rankAggregationAlgorithm == null) {
         if (other.rankAggregationAlgorithm != null)
            return false;
      } else if (!rankAggregationAlgorithm.equals(other.rankAggregationAlgorithm))
         return false;
      if (rankAggregationAlgorithmDefinition == null) {
         if (other.rankAggregationAlgorithmDefinition != null)
            return false;
      } else if (!rankAggregationAlgorithmDefinition.equals(other.rankAggregationAlgorithmDefinition))
         return false;
      return true;
   }


}
