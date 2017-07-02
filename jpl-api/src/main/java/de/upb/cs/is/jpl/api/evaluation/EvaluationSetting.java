package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains all the required objects for running a single evaluation for the given
 * {@link ILearningAlgorithm} which is trained on some dataset to produce {@link ILearningModel},
 * which needs to be evaluated on the provided {@link IDataset} on the basis of given list of
 * {@link IMetric}s.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationSetting {
   private static final String AND = " and ";
   private static final String EVALUATION_SETTING = "The evaluation setting is for metric(s): ";
   private static final String FOR_LEARNING_ALGORITHM_MODEL = " for learning algorithm %s trained on some dataset to provide learning model %s ";
   private static final String DATASET_RESULT = "to be evaluated on the dataset %s";

   private IDataset<?, ?, ?> dataset;
   private ILearningAlgorithm learningAlgorithm;
   private ILearningModel<?> learningModel;
   private List<IMetric<?, ?>> metrics;


   /**
    * Creates a new {@link EvaluationSetting} to initialize the object and its member variables with
    * null values.
    */
   public EvaluationSetting() {
      this.dataset = null;
      this.learningAlgorithm = null;
      this.learningModel = null;
      this.metrics = new ArrayList<>();
   }


   /**
    * Creates a new {@link EvaluationSetting} and initializes its member variables with provided
    * values.
    * 
    * @param dataset the dataset on which the evaluation needs to be carried out
    * @param learningAlgorithm the algorithm for which the evaluation is being run
    * @param learningModel the learning model which is trained by given algorithm on the some
    *           dataset
    * @param metrics the list of {@link IMetric} on the basis of which evaluation
    */
   public EvaluationSetting(IDataset<?, ?, ?> dataset, ILearningAlgorithm learningAlgorithm, ILearningModel<?> learningModel,
         List<IMetric<?, ?>> metrics) {
      this.dataset = dataset;
      this.learningAlgorithm = learningAlgorithm;
      this.learningModel = learningModel;
      this.metrics = CollectionsUtils.getDeepCopyOf(metrics);
   }


   /**
    * Returns the dataset for which the evaluation has to be done. We need to predict the values for
    * this dataset using learning model for the given learning algorithm.
    * 
    * @return the dataset on which the evaluation needs to be done
    */
   public IDataset<?, ?, ?> getDataset() {
      return dataset;
   }


   /**
    * Returns the learning algorithm for which the evaluation has to be done. We can require to
    * train it on different parts of the dataset provided for evaluations like percentage split or
    * cross validation and do prediction on the other.
    * 
    * @return the learning algorithm for which evaluation is to be done
    */
   public ILearningAlgorithm getLearningAlgorithm() {
      return learningAlgorithm;
   }


   /**
    * Returns the learning model for which the evaluation has to be done, which is trained on some
    * dataset.
    * 
    * @return the learning model which is to be used
    */
   public ILearningModel<?> getLearningModel() {
      return learningModel;
   }


   /**
    * Returns list of {@link IMetric}s, the metrics on the basis of which the evaluation has to be
    * done.
    * 
    * @return the evaluation metric on the basis of which evaluation is to be done
    */
   public List<IMetric<?, ?>> getMetrics() {
      return metrics;
   }


   /**
    * Sets the dataset for which the evaluation has to be done.
    * 
    * @param dataset the dataset to set
    */
   public void setDataset(IDataset<?, ?, ?> dataset) {
      this.dataset = dataset;
   }


   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(StringUtils.LINE_BREAK);
      builder.append(EVALUATION_SETTING);
      for (IMetric<?, ?> metric : metrics) {
         if (metrics.indexOf(metric) == metrics.size() - 1)
            builder.append(AND);
         builder.append(metric.getClass().getSimpleName());
         if (metrics.indexOf(metric) < metrics.size() - 2)
            builder.append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND);
      }
      builder.append(String.format(FOR_LEARNING_ALGORITHM_MODEL, learningAlgorithm, learningModel));
      builder.append(String.format(DATASET_RESULT, dataset.toString()));
      return builder.toString();
   }
}
