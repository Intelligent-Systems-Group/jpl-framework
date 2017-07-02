package de.upb.cs.is.jpl.api.metric;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.accuracy.Accuracy;
import de.upb.cs.is.jpl.api.metric.crossentropy.CrossEntropyError;
import de.upb.cs.is.jpl.api.metric.examplebasedfmeasure.ExampleBasedFMeasure;
import de.upb.cs.is.jpl.api.metric.examplebasedfmeasure.ExampleBasedPrecision;
import de.upb.cs.is.jpl.api.metric.examplebasedfmeasure.ExampleBasedRecall;
import de.upb.cs.is.jpl.api.metric.hammingloss.HammingLoss;
import de.upb.cs.is.jpl.api.metric.kendallstau.KendallsTau;
import de.upb.cs.is.jpl.api.metric.labelbasedfmeasure.MacroLabelBasedFMeasure;
import de.upb.cs.is.jpl.api.metric.meanabsoluteerror.MeanAbsoluteError;
import de.upb.cs.is.jpl.api.metric.meanabsoluteerror.MeanAbsoluteErrorForVectors;
import de.upb.cs.is.jpl.api.metric.meansquarederror.MeanSquaredError;
import de.upb.cs.is.jpl.api.metric.meansquarederror.MeanSquaredErrorForVectors;
import de.upb.cs.is.jpl.api.metric.rootmeansquareerror.RootMeanSquareError;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelation;
import de.upb.cs.is.jpl.api.metric.subset01.Subset01Loss;


/**
 * EEvaluationMetric will list all metrics (i.e. loss functions) which we support for different
 * problems, such as rank loss, kendalls_tau etc. This file contains the ENUMS for all the
 * EvaluationMetrics, i.e. the loss functions that are used in evaluations for associated Preference
 * Learning Problem. Each {@link Enum} contains list of associated Preference Learning Problems i.e.
 * {@link ELearningProblem}(s) and is identified by the {evaluationMetricIdentifier}.
 * 
 * @author Pritha Gupta
 *
 */

public enum EMetric {

   /**
    * Spearman's rank correlation coefficient.
    */
   SPEARMANS_RANK_CORRELATION("spearman_correlation", Arrays.asList(ELearningProblem.OBJECT_RANKING, ELearningProblem.RANK_AGGREGATION, ELearningProblem.LABEL_RANKING)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new SpearmansCorrelation();
      }
   },
   /**
    * Cosine similarity metric for collaborative filtering.
    */
   COSINE_SIMILARITY("cosine_similarity", Arrays.asList(ELearningProblem.COLLABORATIVE_FILTERING)) {
      @Override
      public IMetric<IVector, Double> createEvaluationMetric() {
         return new MeanAbsoluteErrorForVectors();
      }
   },
   /**
    * Pearson correlation metric for collaborative filtering.
    */
   PEARSON_CORRELATION("pearson_correlation", Arrays.asList(ELearningProblem.COLLABORATIVE_FILTERING)) {
      @Override
      public IMetric<IVector, Double> createEvaluationMetric() {
         return new MeanAbsoluteErrorForVectors();
      }
   },
   /**
    * Mean absolute error evaluation metric for vectors.
    */
   MEAN_ABSOLUTE_ERROR_FOR_VECTORS("mean_absolute_error_for_vector", Arrays.asList(ELearningProblem.COLLABORATIVE_FILTERING)) {
      @Override
      public IMetric<IVector, Double> createEvaluationMetric() {
         return new MeanAbsoluteErrorForVectors();
      }
   },
   /**
    * Mean squared error evaluation metric for vectors.
    */
   MEAN_SQUARED_ERROR_FOR_VECTORS("mean_squared_error_for_vector", Arrays.asList(ELearningProblem.COLLABORATIVE_FILTERING)) {
      @Override
      public IMetric<IVector, Double> createEvaluationMetric() {
         return new MeanSquaredErrorForVectors();
      }
   },
   /**
    * Mean squared error evaluation metric.
    */
   MEAN_SQUARED_ERROR("mean_squared_error", Arrays.asList(ELearningProblem.INSTANCE_RANKING, ELearningProblem.COLLABORATIVE_FILTERING, ELearningProblem.ORDINAL_CLASSIFICATION)) {
      @Override
      public IMetric<Double, Double> createEvaluationMetric() {
         return new MeanSquaredError();
      }
   },
   /**
    * Mean absolute error evaluation metric.
    */
   MEAN_ABSOLUTE_ERROR("mean_absolute_error", Arrays.asList(ELearningProblem.INSTANCE_RANKING, ELearningProblem.COLLABORATIVE_FILTERING, ELearningProblem.ORDINAL_CLASSIFICATION)) {
      @Override
      public IMetric<Double, Double> createEvaluationMetric() {
         return new MeanAbsoluteError();
      }
   },
   /**
    * Mean absolute error evaluation metric.
    */
   ROOT_MEAN_SQUARE_ERROR("root_mean_square_error", Arrays.asList(ELearningProblem.INSTANCE_RANKING, ELearningProblem.COLLABORATIVE_FILTERING, ELearningProblem.ORDINAL_CLASSIFICATION)) {
      @Override
      public IMetric<Double, Double> createEvaluationMetric() {
         return new RootMeanSquareError();
      }
   },
   /**
    * Kendall's Tau evaluation metric for RankAggregation and Object Ranking.
    */
   KENDALLS_TAU("kendalls_tau", Arrays.asList(ELearningProblem.OBJECT_RANKING, ELearningProblem.RANK_AGGREGATION, ELearningProblem.LABEL_RANKING)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new KendallsTau();
      }
   },
   /**
    * This entry is associated with the {@link Accuracy} measure for multilabel classification.
    */
   ACCURACY("accuracy", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new Accuracy();
      }
   },
   /**
    * This entry is associated with the {@link HammingLoss} for multilabel classification.
    */
   HAMMINGLOSS("hamming_loss", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new HammingLoss();
      }
   },
   /**
    * This entry is associated with the {@link ExampleBasedFMeasure} for multilabel classification.
    */
   EXAMPLE_BASED_F_MEASURE("example_based_f_measure", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new ExampleBasedFMeasure();
      }
   },
   /**
    * This entry is associated with the {@link MacroLabelBasedFMeasure} for multilabel
    * classification.
    */
   MACRO_LABEL_BASED_F_MEASURE("macro_label_based_f_measure", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new MacroLabelBasedFMeasure();
      }
   },
   /**
    * This entry is associated with the {@link ExampleBasedPrecision} for multilabel classification.
    */
   EXAMPLE_BASED_PRECISION("example_based_precision", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new ExampleBasedPrecision();
      }
   },
   /**
    * This examples is associated with the {@link ExampleBasedPrecision} for multilabel
    * classification.
    */
   EXAMPLE_BASED_RECALL("example_based_recall", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new ExampleBasedRecall();
      }
   },
   /**
    * This entry is associated with the {@link Subset01Loss} for multilabel classification.
    */
   SUBSET_01_LOSS("subset_01_loss", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new Subset01Loss();
      }
   },
   /**
    * This entry is associated with the {@link CrossEntropyError} for multilabel classification.
    */
   CROSS_ENTROPY_ERROR("cross_entropy_error", Arrays.asList(ELearningProblem.MULTILABEL_CLASSIFICATION)) {
      @Override
      public IMetric<?, ?> createEvaluationMetric() {
         return new CrossEntropyError();
      }
   };
   private static final String SELECTED_EVALUATION_METRIC_MESSAGE = "Selected evaluation metric:  '%s'  for learning problem: '%s'";
   private static final Logger logger = LoggerFactory.getLogger(EMetric.class);

   /**
    * The {@code evaluationMetricIdentifier} is initialized during the {@link Enum} are declared.
    * The {@link EvaluateAlgorithmsCommand} is executed it gets the appropriate {@link EEvaluation}
    * evaluationMetric to be used for current evaluation.
    * 
    */
   private String metricIdentifier;
   private List<ELearningProblem> learningProblems;


   /**
    * Private constructor for this enumeration.
    * 
    * @param evaluationMetricIdentifier an unambiguous identifier of the evaluation metric, which is
    *           used to determine which metric should be used depending on the user input
    * @param learningProblems the list of learning problems for which this metric can be used
    */
   private EMetric(String evaluationMetricIdentifier, List<ELearningProblem> learningProblems) {
      this.metricIdentifier = evaluationMetricIdentifier;
      this.learningProblems = learningProblems;
   }


   /**
    * Returns the according {@link EMetric} instance which has the identifier to search for and
    * which is applicable for the provided {@link ELearningProblem}.
    * 
    * @param learningProblem the learning problem associated with which we want the evaluation
    *           metric
    * @param metricIdentifier the identifier for which the {@link EMetric} is to be searched
    * @return {@link EMetric} with the correct identifier if found, null if not found
    */
   public static EMetric getEEvaluationMetricByProblemAndIdentifier(ELearningProblem learningProblem, String metricIdentifier) {
      EMetric[] metrics = EMetric.values();
      for (EMetric metric : metrics) {
         if (metric.metricIdentifier.equals(metricIdentifier) && metric.learningProblems.contains(learningProblem)) {
            logger.debug(
                  String.format(SELECTED_EVALUATION_METRIC_MESSAGE, metricIdentifier, learningProblem.getLearningProblemIdentifier()));
            return metric;
         }
      }
      return null;
   }


   /**
    * Returns the according list of {@link EMetric}s which are applicable for the provided
    * {@link ELearningProblem}.
    * 
    * @param learningProblem the learning problem associated with which we want the evaluation
    *           metric
    * @return list of {@link EMetric}
    */
   public static List<EMetric> getApplicableMetricsForALearningProblem(ELearningProblem learningProblem) {
      EMetric[] metrics = EMetric.values();
      List<EMetric> eMetrics = new ArrayList<>();
      for (EMetric metric : metrics) {
         if (metric.learningProblems.contains(learningProblem)) {

            eMetrics.add(metric);
         }
      }
      return eMetrics;
   }


   /**
    * Returns a list containing all {@link EMetric}.
    * 
    * @return list of all {@link EMetric}
    */
   public static List<EMetric> getEMetrics() {
      return Arrays.asList(EMetric.values());
   }


   /**
    * Returns a list containing all {@link EMetric} identifiers.
    * 
    * @return list of all {@link EMetric}
    */
   public static List<String> getMetricIdentifiers() {
      ArrayList<String> evalautionMetricEnum = new ArrayList<>();
      for (EMetric metricEnum : getEMetrics()) {
         evalautionMetricEnum.add(metricEnum.metricIdentifier);
      }
      return evalautionMetricEnum;
   }


   /**
    * Get the metric of type {@link String}.
    * 
    * @return the metric identifier
    */
   public String getMetricIdentifier() {
      return metricIdentifier;
   }


   /**
    * Create a new instance of the evaluation metric implementation which is linked to this
    * {@link Enum}.
    * 
    * @return new instance of the evaluation metric which is linked to this {@link Enum}.
    */
   public abstract IMetric<?, ?> createEvaluationMetric();
}
