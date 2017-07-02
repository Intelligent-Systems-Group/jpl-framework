package de.upb.cs.is.jpl.api.evaluation;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.percentagesplit.CollaborativeFilteringPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.crossvalidation.InstanceRankingCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.insample.InstanceRankingInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.percentagesplit.InstanceRankingPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.suppliedtestset.InstanceRankingSuppliedTestsetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.crossvalidation.LabelRankingCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.insample.LabelRankingInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.percentagesplit.LabelRankingPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.suppliedtestset.LabelRankingSuppliedTestsetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.crossvalidation.MultilabelClassificationCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.insample.MultilabelClassificationInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.percentagesplit.MultilabelClassificationPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.suppliedtestset.MultilabelClassificationSuppliedTestsetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.crossvalidation.ObjectRankingCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.insample.ObjectRankingInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.percentagesplit.ObjectRankingPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.objectranking.suppliedtestset.ObjectRankingSuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.crossvalidation.OrdinalClassificationCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.insample.OrdinalClassificationInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.percentagesplit.OrdinalClassificationPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.suppliedtestset.OrdinalClassificationSuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.insample.RankAggregationInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.suppliedtestset.RankAggregationSuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This file contains the ENUMS for all the Evaluations, i.e. the test options that are used in
 * evaluating any Preference Learning Problem represented by ENUM {@link ELearningProblem}. We have
 * four ways to evaluate or test each learning problem: use_training_dataset, supplied_testset,
 * cross_validation and percentage_split.
 * 
 * @author Pritha Gupta
 * 
 */
public enum EEvaluation {

   /**
    * Evaluation using the training dataset for instance ranking problems.
    */
   USE_TRAINING_DATASET_INSTANCE_RANKING(EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, ELearningProblem.INSTANCE_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new InstanceRankingInSampleEvaluation();
      }
   },
   /**
    * Evaluation using the percentage split for instance ranking problems.
    */
   PERCENTAGE_SPLIT_INSTANCE_RANKING(EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER, ELearningProblem.INSTANCE_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new InstanceRankingPercentageSplitEvaluation();
      }
   },
   /**
    * Evaluation for cross validation technique for instance ranking problems.
    */
   CROSS_VALIDATION_INSTANCE_RANKING(EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER, ELearningProblem.INSTANCE_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new InstanceRankingSuppliedTestsetEvaluation();
      }
   },
   /**
    * Evaluation for test set technique for instance ranking problems.
    */
   SUPPLIED_TEST_SET_INSTANCE_RANKING(EvaluationsKeyValuePairs.EVALUATION_CROSS_VALIDATION_IDENTIFIER, ELearningProblem.INSTANCE_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new InstanceRankingCrossValidationEvaluation();
      }
   },
   /**
    * Evaluation using the training dataset for collaborative filtering problems.
    */
   PERCENTAGE_SPLIT_COLLABORATIVE_FILTERING(EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER, ELearningProblem.COLLABORATIVE_FILTERING) {
      @Override
      public IEvaluation createEvaluation() {
         return new CollaborativeFilteringPercentageSplitEvaluation();
      }
   },

   /**
    * Evaluation using supplied testset for label ranking.
    */
   SUPPLIED_TEST_SET_LABEL_RANKING(EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER, ELearningProblem.LABEL_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new LabelRankingSuppliedTestsetEvaluation();
      }
   },

   /**
    * Evaluation using percentage split for label ranking.
    */
   PERCENTAGE_SPLIT_LABEL_RANKING(EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER, ELearningProblem.LABEL_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new LabelRankingPercentageSplitEvaluation();
      }
   },

   /**
    * Evaluation using the training dataset for label ranking.
    */
   USE_TRAINING_DATASET_LABEL_RANKING(EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, ELearningProblem.LABEL_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new LabelRankingInSampleEvaluation();
      }
   },

   /**
    * Evaluation for cross validation technique for label ranking.
    */
   CROSS_VALIDATION_LABEL_RANKING(EvaluationsKeyValuePairs.EVALUATION_CROSS_VALIDATION_IDENTIFIER, ELearningProblem.LABEL_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new LabelRankingCrossValidationEvaluation();
      }
   },
   /**
    * Evaluation for cross validation technique for object ranking problems.
    */
   CROSS_VALIDATION_OBJECT_RANKING(EvaluationsKeyValuePairs.EVALUATION_CROSS_VALIDATION_IDENTIFIER, ELearningProblem.OBJECT_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new ObjectRankingCrossValidationEvaluation();
      }
   },

   /**
    * Evaluation for percentage-split problems for object ranking problems.
    */
   PERCENTAGE_SPLIT_OBJECT_RANKING(EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER, ELearningProblem.OBJECT_RANKING) {
      @Override
      public IEvaluation createEvaluation() {
         return new ObjectRankingPercentageSplitEvaluation();
      }
   },

   /**
    * Evaluation using provided in-sample evaluation for Rank Aggregation problems.
    */
   USE_TRAINING_DATASET_OBJECT_RANKING(EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, ELearningProblem.OBJECT_RANKING) {


      @Override
      public IEvaluation createEvaluation() {
         return new ObjectRankingInSampleEvaluation();
      }
   },

   /**
    * Evaluation using provided in-sample evaluation for Rank Aggregation problems.
    */
   SUPPLIED_TEST_SET_OBJECT_RANKING(EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER, ELearningProblem.OBJECT_RANKING) {


      @Override
      public IEvaluation createEvaluation() {
         return new ObjectRankingSuppliedTestSetEvaluation();
      }
   },

   /**
    * Evaluation using provided in-sample evaluation for Rank Aggregation problems.
    */
   USE_TRAINING_DATASET_RANK_AGGREGATION(EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, ELearningProblem.RANK_AGGREGATION) {


      @Override
      public IEvaluation createEvaluation() {
         return new RankAggregationInSampleEvaluation();
      }
   },

   /**
    * Evaluation using provided supplied test set evaluation for Rank Aggregation problems.
    */
   SUPPLIED_TEST_SET_RANK_AGGREGATION(EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER, ELearningProblem.RANK_AGGREGATION) {


      @Override
      public IEvaluation createEvaluation() {
         return new RankAggregationSuppliedTestSetEvaluation();
      }
   },
   /**
    * Evaluation using the cross validation for Rank Aggregation problems.
    */
   CROSS_VALIDATION_DATASET_RANK_AGGREGATION(EvaluationsKeyValuePairs.EVALUATION_CROSS_VALIDATION_IDENTIFIER, ELearningProblem.RANK_AGGREGATION) {


      @Override
      public IEvaluation createEvaluation() {
         return null;
      }
   },
   /**
    * This entry is associated with the {@link MultilabelClassificationCrossValidationEvaluation}.
    */
   CROSS_VALIDATION_MULTILABEL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_CROSS_VALIDATION_IDENTIFIER, ELearningProblem.MULTILABEL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new MultilabelClassificationCrossValidationEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link MultilabelClassificationInSampleEvaluation}.
    */
   USE_TRAINING_DATASET_MULTILABEL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, ELearningProblem.MULTILABEL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new MultilabelClassificationInSampleEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link MultilabelClassificationPercentageSplitEvaluation}.
    */
   PERCENTAGE_SPLIT_MULTILABEL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER, ELearningProblem.MULTILABEL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new MultilabelClassificationPercentageSplitEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link MultilabelClassificationSuppliedTestsetEvaluation}.
    */
   SUPPLIED_TEST_SET_MULTILABEL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER, ELearningProblem.MULTILABEL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new MultilabelClassificationSuppliedTestsetEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link OrdinalClassificationCrossValidationEvaluation}.
    */
   CROSS_VALIDATION_ORDINAL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_CROSS_VALIDATION_IDENTIFIER, ELearningProblem.ORDINAL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new OrdinalClassificationCrossValidationEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link OrdinalClassificationInSampleEvaluation}.
    */
   USE_TRAINING_DATASET_ORDINAL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, ELearningProblem.ORDINAL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new OrdinalClassificationInSampleEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link OrdinalClassificationPercentageSplitEvaluation}.
    */
   PERCENTAGE_SPLIT_ORDINAL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER, ELearningProblem.ORDINAL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new OrdinalClassificationPercentageSplitEvaluation();
      }
   },
   /**
    * This entry is associated with the {@link OrdinalClassificationPercentageSplitEvaluation}.
    */
   SUPPLIED_TEST_SET_ORDINAL_CLASSIFICATION(EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER, ELearningProblem.ORDINAL_CLASSIFICATION) {
      @Override
      public IEvaluation createEvaluation() {
         return new OrdinalClassificationSuppliedTestSetEvaluation();
      }
   };

   private static final String SELECTED_EVALUATION_MESSAGE = "Selected evaluation:  '%s'  for learning problem: '%s'";
   private static final Logger logger = LoggerFactory.getLogger(EEvaluation.class);

   private String evaluationIdentifier;
   private ELearningProblem learningProblem;


   private EEvaluation(String evaluationIdentifier, ELearningProblem learningProblem) {
      this.evaluationIdentifier = evaluationIdentifier;
      this.learningProblem = learningProblem;
   }


   /**
    * Returns the evaluationIdentifier of type {@link String}associated with the
    * {@code EEvaluation}. .
    * 
    * @return the evalautionIdentifier associated with Evaluation
    */
   public String getEvaluationIdentifier() {
      return evaluationIdentifier;
   }


   /**
    * 
    * Returns the learningProblem of type {@link ELearningProblem} associated with the
    * {@code EEvaluation}.
    * 
    * @return the learningProblem associated with {@code EEvaluation}
    */
   public ELearningProblem getLearningProblem() {
      return learningProblem;
   }


   /**
    * Returns the {@code EEvaluation} {@link Enum} associated with provided learningProblem and
    * identifier to search for.
    * 
    * @param evaluationIdentifier the evaluation identifier of type {@link String}
    * @param learningProblem the learning problem of type {@link ELearningProblem}
    * @return the {@link Enum} {@code EEvaluation} for the learningProblem and identifier if found,
    *         {@code null} if not found
    */
   public static EEvaluation getEEvaluationByProblemAndIdentifier(ELearningProblem learningProblem, String evaluationIdentifier) {
      EEvaluation[] evaluations = EEvaluation.values();
      for (EEvaluation evaluation : evaluations) {
         if (evaluation.getLearningProblem() == learningProblem && evaluation.getEvaluationIdentifier().equals(evaluationIdentifier)) {
            logger.debug(String.format(SELECTED_EVALUATION_MESSAGE, evaluationIdentifier,
                  evaluation.learningProblem.getLearningProblemIdentifier()));
            return evaluation;
         }
      }
      return null;
   }


   /**
    * Returns a list containing all EEvaluation.
    * 
    * @return list of all {@link Enum} EEvaluations
    */
   public static List<EEvaluation> getEEvaluations() {
      return Arrays.asList(EEvaluation.values());
   }


   /**
    * Creates a new instance of the evaluation implementation which is linked to this {@link Enum}.
    * 
    * @return a new instance of the evaluation implementation
    */
   public abstract IEvaluation createEvaluation();
}
