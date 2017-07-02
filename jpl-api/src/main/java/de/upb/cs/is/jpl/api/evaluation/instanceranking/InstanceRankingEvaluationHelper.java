package de.upb.cs.is.jpl.api.evaluation.instanceranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * Helper class to reduce the duplicated code inside the evaluations
 * 
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingEvaluationHelper {

   /**
    * Hides the public constructor.
    */
   private InstanceRankingEvaluationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * This method wraps the single evaluation for all instance ranking evaluation cases.
    * 
    * @param evaluationSetting the evaluation setting containing all objects required for evaluation
    *           which needs to be done.
    * 
    * @return the {@link EvaluationResult} for the given evaluation metric, by learning algorithm on
    *         given dataset
    * @throws LossException if the the loss could not be calculated for provided setting
    * @throws PredictionFailedException if the prediction is not working for
    */
   @SuppressWarnings("unchecked")
   public static EvaluationResult evaluateSingleCombinationHelper(EvaluationSetting evaluationSetting)
         throws LossException,
            PredictionFailedException {
      EvaluationResult evaluationResult = new EvaluationResult();

      List<Double> predictionList;
      List<Double> expectationList;
      if (evaluationSetting.getDataset() instanceof DefaultAbsoluteDataset) {
         predictionList = (List<Double>) evaluationSetting.getLearningModel().predict(evaluationSetting.getDataset());
         expectationList = createRatingListOutOfDoubleValues((DefaultAbsoluteDataset) evaluationSetting.getDataset());
      } else {
         predictionList = castIntegerToDoubleList(
               (List<Integer>) evaluationSetting.getLearningModel().predict(evaluationSetting.getDataset()));
         expectationList = createRatingList((IDataset<double[], NullType, Integer>) evaluationSetting.getDataset());
      }
      evaluationResult.setLearningAlgorithm(evaluationSetting.getLearningAlgorithm());
      evaluationResult.setDataset(evaluationSetting.getDataset());
      for (IMetric<?, ?> evaluationMetric : evaluationSetting.getMetrics()) {
         IMetric<Double, Double> castedEvaluationMetric = (IMetric<Double, Double>) evaluationMetric;
         double loss = castedEvaluationMetric.getAggregatedLossForRatings(expectationList, predictionList);
         evaluationResult.addLossWithMetric(loss, castedEvaluationMetric);
      }
      evaluationResult.setExtraEvaluationInformation(StringUtils.EMPTY_STRING);

      return evaluationResult;
   }


   /**
    * Create a rating list out of an {@link IDataset}.
    * 
    * @param dataset the dataset which should be used for creating a ranking list
    * @return the ratings of the dataset
    */
   public static List<Double> createRatingList(IDataset<double[], NullType, Integer> dataset) {
      List<Double> ratingList = new ArrayList<>();

      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         ratingList.add((double) dataset.getInstance(i).getRating());
      }

      return ratingList;

   }


   /**
    * Create a rating list out of an {@link DefaultAbsoluteDataset}.
    * 
    * @param dataset the dataset which should be used for creating a ranking list
    * @return the ratings of the dataset
    */
   public static List<Double> createRatingListOutOfDoubleValues(DefaultAbsoluteDataset dataset) {
      List<Double> ratingList = new ArrayList<>();

      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         ratingList.add(dataset.getInstance(i).getRating().getValue(0));
      }

      return ratingList;

   }


   /**
    * Create a integer list to a double list.
    * 
    * @param list the dataset which should be used for creating a ranking list
    * @return the ratings of the dataset
    */
   public static List<Double> castIntegerToDoubleList(List<Integer> list) {
      List<Double> doubleList = new ArrayList<>();
      for (int i = 0; i < list.size(); i++) {
         doubleList.add((double) list.get(i));
      }
      return doubleList;
   }

}
