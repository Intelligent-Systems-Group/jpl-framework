package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class stores different parts of the results obtained from current evaluation. It stored the
 * {@link ILearningAlgorithm} and test dataset object {@link IDataset} for which the evaluation was
 * done.A map which stored the {@link IMetric}as the key and a loss object, which is evaluated
 * according to the metric for the algorithm on the dataset.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationResult {
   private static final Logger logger = LoggerFactory.getLogger(EvaluationResult.class);

   private static final String EVALUATION_METRIC = "The loss on metric \"%s\" is: %s. ";
   private static final String FOR_LEARNING_PROBLEM = "For learning algorithm %s ";
   private static final String DATASET_RESULT = "on dataset %s:";
   private static final String OUTPUT_EXTRALOSS = "The evaluation output is \"%s\".";
   private static final String EVALUATION_METRIC_NULL = "Evaluation Metric for which loss need to be aquired is null.";
   private static final String EVALUATION_METRIC_ADDED_NULL = "Evaluation metric for which loss is to be added is null";
   private static final String LOSS_ADDED_NULL = "Loss to be added is null";
   private IDataset<?, ?, ?> dataset;
   private ILearningAlgorithm learningAlgorithm;
   private Map<IMetric<?, ?>, Object> metricWithOutput;
   private String extraEvaluationInformation;


   /**
    * Constructor to instantiate the member variables.
    */
   public EvaluationResult() {
      learningAlgorithm = null;
      dataset = null;
      metricWithOutput = new HashMap<>();
      extraEvaluationInformation = StringUtils.EMPTY_STRING;
   }


   /**
    * Returns the learning algorithm for which the evaluation has been done.
    * 
    * @return the {@link ILearningAlgorithm} for which the evaluation has been done
    */
   public ILearningAlgorithm getLearningAlgorithm() {
      return learningAlgorithm;
   }


   /**
    * Set the the learning algorithm for which the evaluation has been done.
    * 
    * @param learningAlgorithm the {@link ILearningAlgorithm} to set
    */
   public void setLearningAlgorithm(ILearningAlgorithm learningAlgorithm) {
      this.learningAlgorithm = learningAlgorithm;
   }


   /**
    * Returns the {@link IDataset} on which evaluation was done.
    * 
    * @return the dataset
    */
   public IDataset<?, ?, ?> getDataset() {
      return dataset;
   }


   /**
    * Set the {@link IDataset} Object on which the evaluation was done.
    * 
    * @param dataset the dataset to set
    */
   public void setDataset(IDataset<?, ?, ?> dataset) {
      this.dataset = dataset;
   }


   /**
    * Returns the extra information in the form of the string for the evaluation done on the basis
    * of metrics.
    * 
    * @return the extra evaluation information stored in form of the string
    */
   public String getExtraEvaluationInformation() {
      return extraEvaluationInformation;
   }


   /**
    * Sets the the extra information in the form of the string for the evaluation done on the basis
    * of metrics.
    * 
    * @param extraEvaluationInformation the extra evaluation information stored in form of the
    *           string
    */
   public void setExtraEvaluationInformation(String extraEvaluationInformation) {
      this.extraEvaluationInformation = extraEvaluationInformation;
   }


   /**
    * Sets the {@link IMetric} for which the evaluation result is evaluated with the new object as
    * loss result.
    * 
    * @param metric the {@link IMetric} to set
    */
   public void addMetricWithEmptyOuputInMap(IMetric<?, ?> metric) {
      if (metric != null) {
         metricWithOutput.put(metric, new Object());
      } else {
         logger.warn(EVALUATION_METRIC_ADDED_NULL);

      }
   }


   /**
    * Checks validity of the evaluation result.
    * 
    * @return the {@code true} if result is valid , else {@code false}
    */
   public boolean checkValidityOfTheResult() {
      boolean invalid = false;
      if (metricWithOutput.isEmpty()) {
         invalid = true;
      }
      for (Map.Entry<IMetric<?, ?>, Object> entry : metricWithOutput.entrySet()) {
         if (entry.getValue() == null) {
            invalid = true;
         }
      }
      return invalid;
   }


   /**
    * Adds the pair of the loss associated with the {@link IMetric} in the {@link HashMap}.
    * 
    * @param loss the loss to set
    * @param metric the metric for which the loss needs to set
    */
   public void addLossWithMetric(Object loss, IMetric<?, ?> metric) {
      if (metric != null && loss != null) {
         metricWithOutput.put(metric, loss);
      } else {
         if (metric == null) {
            logger.warn(EVALUATION_METRIC_ADDED_NULL);
         }
         if (loss == null) {
            logger.warn(LOSS_ADDED_NULL);
         }
      }
   }


   /**
    * Returns the loss associated with the {@link IMetric} in the map, where metric is the key and
    * output is the value.
    * 
    * @param metric the evaluation metric for which loss is required
    * @return the loss associated with the {@link IMetric}
    * @throws NullPointerException if the evaluation metric provided is {@code null}
    */
   public Object getLossForMetric(IMetric<?, ?> metric) {
      if (metric != null) {
         return metricWithOutput.get(metric);
      } else {
         logger.warn(EVALUATION_METRIC_NULL);
         throw new NullPointerException(EVALUATION_METRIC_NULL);
      }
   }


   /**
    * @return the list of evaluation metrics with loss
    */
   public Map<IMetric<?, ?>, Object> getEvaluationMetricWithLoss() {
      return metricWithOutput;
   }


   /**
    * The list of evaluation metrics.
    * 
    * @return the list of evaluation metrics
    */
   public List<IMetric<?, ?>> getEvaluationMetrics() {
      List<IMetric<?, ?>> evaluationMetrics = new ArrayList<>();
      for (IMetric<?, ?> evaluationMetric : metricWithOutput.keySet()) {
         evaluationMetrics.add(evaluationMetric);
      }
      return evaluationMetrics;
   }


   /**
    * The list of evaluation metrics for the evaluation result.
    * 
    * @param evaluationMetrics list of evaluation metrics
    */
   public void setEvaluationMetrics(List<IMetric<?, ?>> evaluationMetrics) {
      for (IMetric<?, ?> evaluationMetric : evaluationMetrics) {
         addMetricWithEmptyOuputInMap(evaluationMetric);
      }
   }


   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(String.format(FOR_LEARNING_PROBLEM, learningAlgorithm));
      builder.append(String.format(DATASET_RESULT, dataset.toString()));
      builder.append(StringUtils.LINE_BREAK);
      for (Map.Entry<IMetric<?, ?>, Object> entry : metricWithOutput.entrySet()) {
         builder.append(String.format(EVALUATION_METRIC, entry.getKey().toString(), String.valueOf(entry.getValue())));
         builder.append(StringUtils.LINE_BREAK);
      }
      if (extraEvaluationInformation != null && !extraEvaluationInformation.isEmpty()) {
         builder.append(String.format(OUTPUT_EXTRALOSS, extraEvaluationInformation));
      }
      return builder.toString();

   }


}
