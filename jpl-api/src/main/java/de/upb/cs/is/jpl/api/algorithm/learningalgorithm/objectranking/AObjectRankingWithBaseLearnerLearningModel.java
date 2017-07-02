package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingInstance;
import de.upb.cs.is.jpl.api.exception.UnsupportedOperationException;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.kendallstau.KendallsTau;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelation;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This abstract learning model is used for all learning models of object ranking algorithms which
 * extends {@link AObjectRankingWithBaseLearner}.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class AObjectRankingWithBaseLearnerLearningModel extends ALearningModel<Ranking> {
   private static final String SIZE_OF_THE_WEIGHT_VECTOR_IS_NOT_EQUAL_INSTANCE_FEATURE_SIZE = "Size of the weight vector is not equal to the feature size of the item vector plus the context vector";


   private static final Logger logger = LoggerFactory.getLogger(AObjectRankingWithBaseLearnerLearningModel.class);

   private static final String ERROR_NUMBER_ITEM_FEATURES_NOT_SAME = "The number of items features is %s while it should be %s, which is number of item features of dataset on which the model is trained";
   private static final String ERROR_NUMBER_CONTEXT_FEATURES_NOT_SAME = "The number of context features is %s while it should be %s, which is number of context features of dataset on which the model is trained";
   private static final String PRINT_FINAL_AGGREGATED_RANK = "Predicted Rank: %s";
   private static final String PRINT_ACTUAL_RANK = "Actual Rank: %s";
   private static final String PRINT_KENDALLS_AND_SPEARMAN_FOR_PREDICTED_AND_ACTUAL_RANKING = "Kendalls Tau and Spearman rho between predicted and actual ranking are: %f and %f";
   private static final String WEIGHTS_NULL_FOR_BASE_LEARNER = "Base learner %s could not return the weights, they are emtpy ";
   private static final String WEIGHTS_NOT_SUPPORTED = "Base learner %s could not return the weights, it does not support weight vector evaluaiton ";

   protected static final String PREDICTION_ERROR_MESSAGE = "Error in predicting the scores for the given ranking %s by the linear model due to error %s";
   protected static final String BASE_LEARNER_NOTSET = "BaseLearner for the algorithm is not set.";
   protected static final String DATASET_NOT_COMAPTIBLE = "Dataset is not compatible on which the prediction has to be done as one of the instances have %s problem..";
   private static final String SORTED_SCORE_MAP_MESSAGE = "Objects Score Map: %s";

   protected ABaseLearningModel<Double> baseLearnerModel;
   protected int numberOfItemFeatures;
   protected int numberOfContextFeatures;
   protected boolean sortingInIncreasingOrderOfScores;
   private String exceptionString;
   protected KendallsTau tau;
   protected SpearmansCorrelation rho;
   protected IVector weights;
   protected double bias;


   /**
    * Creates a new {@link AObjectRankingWithBaseLearnerLearningModel} with the
    * {@link ABaseLearningModel} which is trained on the transformed dataset.
    * 
    * @param baseLearnerModel the base learner model to set
    * @param numberOfItemFeatures the number of item features for the object on which the model was
    *           trained
    * @param numberOfContextFeatures the number of context features for the object on which the
    *           model was trained
    * @param sortingInIncreasingOrderOfScores is {@code false} is the sorting order for scores is
    *           decreasing, else {@code true}
    */
   public AObjectRankingWithBaseLearnerLearningModel(ABaseLearningModel<Double> baseLearnerModel, int numberOfItemFeatures,
         int numberOfContextFeatures, boolean sortingInIncreasingOrderOfScores) {
      this.baseLearnerModel = baseLearnerModel;
      this.numberOfItemFeatures = numberOfItemFeatures;
      this.numberOfContextFeatures = numberOfContextFeatures;
      this.sortingInIncreasingOrderOfScores = sortingInIncreasingOrderOfScores;
      exceptionString = StringUtils.EMPTY_STRING;
      tau = new KendallsTau();
      rho = new SpearmansCorrelation();
      weights = new DenseDoubleVector(new double[0]);
      bias = 0.0;
   }


   @Override
   public List<Ranking> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, ObjectRankingDataset.class);
      List<Ranking> result = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         result.add(predict(dataset.getInstance(i)));
      }
      return result;
   }


   @Override
   public Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, ObjectRankingInstance.class);
      assertInstanceCompatibility(instance);
      assertCompatibilityOfBaseLearnerAndSetWeightAndBiasForModel();
      Ranking predictedRanking = null;
      ObjectRankingInstance objectRankingInstance = (ObjectRankingInstance) instance;
      Ranking actualRanking = objectRankingInstance.getRating();

      try {
         Map<Integer, Double> objectScores = getPredictedScoreMap(objectRankingInstance);
         predictedRanking = createRankingFromScoresMap(objectScores, actualRanking);
      } catch (PredictionFailedException exception) {
         String errorMessage = String.format(PREDICTION_ERROR_MESSAGE, actualRanking.toString(), exception.getMessage());
         throw new PredictionFailedException(errorMessage, exception);
      } catch (LossException exception) {
         logger.warn(exception.getMessage(), exception);
      }
      return predictedRanking;
   }


   /**
    * This method asserts weather the learned {@link ABaseLearningModel} if it is compatible for
    * this {@link AObjectRankingWithBaseLearnerLearningModel} and it is able to create correct
    * weight vector or not.
    * 
    * @throws PredictionFailedException if the weights and bias are not obtained from the
    *            {@link ABaseLearningModel}
    */
   public void assertCompatibilityOfBaseLearnerAndSetWeightAndBiasForModel() throws PredictionFailedException {
      if (baseLearnerModel == null) {
         throw new PredictionFailedException(BASE_LEARNER_NOTSET);
      }
      try {
         bias = baseLearnerModel.getBias();
         weights = createCorrectWeightVectorFromBaseLearner();
         if (weights.asArray().length == 0) {
            throw new PredictionFailedException(String.format(WEIGHTS_NULL_FOR_BASE_LEARNER, baseLearnerModel.getClass().getSimpleName()));
         }
      } catch (UnsupportedOperationException exception) {
         throw new PredictionFailedException(String.format(WEIGHTS_NOT_SUPPORTED, baseLearnerModel.getClass().getSimpleName()), exception);

      }

   }


   /**
    * This method gets the score map and create the raking according to the sorted scores in
    * increasing or decreasing order according to the scores.
    * 
    * @param objectScores the map contain the object and the corresponding score in the sorted order
    * @param actualRanking the actual {@link Ranking} for the instance to be predicted
    * @return the ranking created from the sorted map with the objects and the scores
    * @throws LossException if the the kendalls tau cannot be evaluated
    */
   protected Ranking createRankingFromScoresMap(Map<Integer, Double> objectScores, Ranking actualRanking) throws LossException {
      int[] rankedObjects;
      if (sortingInIncreasingOrderOfScores) {
         rankedObjects = CollectionsUtils.getSortedKeyValuesInIncreasingOrder(objectScores);
      } else {
         rankedObjects = CollectionsUtils.getSortedKeyValuesInDecreasingValueOrder(objectScores);
      }
      Ranking predictedRanking = new Ranking(rankedObjects, Ranking.createCompareOperatorArrayForLabels(rankedObjects));
      logger.debug(String.format(SORTED_SCORE_MAP_MESSAGE, objectScores.toString()));

      logger.debug(String.format(PRINT_FINAL_AGGREGATED_RANK, predictedRanking.toString()));
      logger.debug(String.format(PRINT_ACTUAL_RANK, actualRanking.toString()));
      logger.debug(String.format(PRINT_KENDALLS_AND_SPEARMAN_FOR_PREDICTED_AND_ACTUAL_RANKING,
            tau.getLossForSingleRating(actualRanking, predictedRanking), rho.getLossForSingleRating(actualRanking, predictedRanking)));
      return predictedRanking;

   }


   /**
    * This method takes the {@link ObjectRankingInstance} as instance and take the objects in the
    * ranking and evaluate the score for each object by taking a dot product with the weight vector
    * with the context feature of the instance and the features of each object concatenated in one
    * vector.
    * 
    * @param objectRankingInstance the {@link ObjectRankingInstance}
    * @return the {@link Map} score map containing the object as key and the score as the value, in
    *         the sorted order w.r.t scores.
    * 
    * @throws PredictionFailedException if the prediction cannot be done on the provided instance
    *            and the weight vector .
    */
   protected Map<Integer, Double> getPredictedScoreMap(ObjectRankingInstance objectRankingInstance) throws PredictionFailedException {
      List<Double> prediction = new ArrayList<>();
      Map<Integer, Double> objectScores = new HashMap<>();
      int[] rankedObjects = objectRankingInstance.getRating().getObjectList();

      for (int object : rankedObjects) {
         double[] features = ArrayUtils.addAll(objectRankingInstance.getContextFeatureVector(),
               objectRankingInstance.getFeaturesForItem(object));
         if (features.length != weights.length()) {
            throw new PredictionFailedException(SIZE_OF_THE_WEIGHT_VECTOR_IS_NOT_EQUAL_INSTANCE_FEATURE_SIZE);
         }
         double score = weights.dotProduct(features) + bias;
         prediction.add(score);
      }
      for (int i = 0; i < rankedObjects.length; i++) {
         objectScores.put(rankedObjects[i], prediction.get(i));
      }
      return objectScores;
   }


   /**
    * This method can be used if the weight vector for the learned baseLearner model have extra
    * weights.
    * 
    * @return the correct weight vector from the base leaner model
    * @throws UnsupportedOperationException if the weight cannot be obtained from the base learner
    */
   protected IVector createCorrectWeightVectorFromBaseLearner() throws UnsupportedOperationException {
      return baseLearnerModel.getWeightVector();
   }


   /**
    * This method can be used in all predict methods for all rank aggregation learning model to
    * assert the compatibility of the {@link ObjectRankingDataset}.
    * 
    * @param dataset the object ranking dataset
    * @throws PredictionFailedException if it is not applicable for this learning model
    */
   public void assetDatasetCompatibility(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException(exceptionString);
      }
   }


   /**
    * This method can be used in all predict methods for all object ranking model to assert the
    * compatibility of the {@link ObjectRankingDataset}..
    * 
    * @param instance the object ranking dataset
    * @throws PredictionFailedException if it is not applicable for this learning model
    */
   public void assertInstanceCompatibility(final IInstance<?, ?, ?> instance) throws PredictionFailedException {
      if (!isInstanceCompatible(instance)) {
         throw new PredictionFailedException(exceptionString);
      }
   }


   /**
    * {@inheritDoc} Returns true because the instance compatibility is enough.
    */
   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      boolean result = false;
      if (dataset != null && dataset instanceof ObjectRankingDataset) {
         result = true;
         for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
            result = result && isInstanceCompatible(dataset.getInstance(i));
         }
      }
      return result;
   }


   /**
    * {@inheritDoc} The method that checks weather the number of context features and number of item
    * features for the instance on which the object ranking model was trained on is same as the one
    * for which we are predicting the {@link Ranking}.
    */
   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance != null && instance instanceof ObjectRankingInstance) {
         int numberOfFeatures = ((ObjectRankingInstance) instance).getNumberOfContextsFeatures();
         if (numberOfFeatures != numberOfContextFeatures) {
            exceptionString = String.format(ERROR_NUMBER_CONTEXT_FEATURES_NOT_SAME, String.valueOf(numberOfFeatures),
                  String.valueOf(numberOfContextFeatures));
            return false;
         }
         numberOfFeatures = ((ObjectRankingInstance) instance).getNumofItemFeatures();
         if (numberOfFeatures != numberOfItemFeatures) {
            exceptionString = String.format(ERROR_NUMBER_ITEM_FEATURES_NOT_SAME, String.valueOf(numberOfFeatures),
                  String.valueOf(numberOfItemFeatures));
            return false;
         }
         return true;
      }
      return false;

   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((baseLearnerModel == null) ? 0 : baseLearnerModel.hashCode());
      result = prime * result + numberOfContextFeatures;
      result = prime * result + numberOfItemFeatures;
      result = prime * result + (sortingInIncreasingOrderOfScores ? 1231 : 1237);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      AObjectRankingWithBaseLearnerLearningModel other = (AObjectRankingWithBaseLearnerLearningModel) obj;
      if (baseLearnerModel == null) {
         if (other.baseLearnerModel != null)
            return false;
      } else if (!baseLearnerModel.equals(other.baseLearnerModel))
         return false;
      if (numberOfContextFeatures != other.numberOfContextFeatures)
         return false;
      if (numberOfItemFeatures != other.numberOfItemFeatures)
         return false;
      if (sortingInIncreasingOrderOfScores != other.sortingInIncreasingOrderOfScores)
         return false;
      return true;
   }


}

