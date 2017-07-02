package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.datastructure.WeightedObject;


/**
 * The model created by the {@link UserBasedFilteringLearningAlgorithm} algorithm.
 * 
 * @author Sebastian Osterbrink
 */
public class UserBasedFilteringLearningModel extends ALearningModel<Double> {


   private static final Logger logger = LoggerFactory.getLogger(UserBasedFilteringLearningModel.class);

   private static final String USER_S_ITEM_S_S_FROM_S_NEIGHBORS = "User %s/Item %s : %s , from %s neighbors";
   private static final String COULD_NOT_PREDICT_A_VALUE_FOR_ITEM_S = "Could not predict a value for item %s.";
   private static final String THE_COMBINATION_OF_CONTEXT_ID_I_AND_ITEM_ID_I_IS_NOT_VALID = "The combination of Context ID %i and Item ID %i is not valid. "
         + "You can only predict for context / item combinations which are in the training dataset.";

   private CollaborativeFilteringDataset trainingData;
   private double minDoubleValue = 0.0;
   private int numberOfNeighbors;

   private IMetric<IVector, Double> similarityFunction;

   private TreeSet<WeightedObject<Integer>>[] neighborDistances;


   /**
    * Creates a {@link UserBasedFilteringLearningModel} where a prediction for item <i>i</i> is
    * computed by averaging the values of the <i>k</i> nearest neighbors who have rated <i>i</i>.
    * 
    * @param dataset the data upon which the training is performed
    * @param numberOfNeighbors the number of neighbors which are queried
    * @param minDoubleValue the minimal double value which is considered != 0
    * @param similarityFunction a similarity function which determines how similar two rating
    *           vectors are
    */
   @SuppressWarnings("unchecked")
   public UserBasedFilteringLearningModel(CollaborativeFilteringDataset dataset, int numberOfNeighbors, double minDoubleValue,
         IMetric<IVector, Double> similarityFunction) {
      this.trainingData = dataset;
      this.numberOfNeighbors = numberOfNeighbors;
      this.minDoubleValue = minDoubleValue;
      this.similarityFunction = similarityFunction;
      neighborDistances = new TreeSet[dataset.getNumberOfInstances()];
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      CollaborativeFilteringInstance castedInstance = (CollaborativeFilteringInstance) instance;
      if (!isInstanceCompatible(castedInstance)) {
         throw new PredictionFailedException(String.format(THE_COMBINATION_OF_CONTEXT_ID_I_AND_ITEM_ID_I_IS_NOT_VALID,
               castedInstance.getContextId(), castedInstance.getItemId()));
      }

      return predictSingleItem(castedInstance.getContextId(), castedInstance.getItemId());
   }


   @Override
   public List<Double> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {

      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException("The dataset does not contain valid instances.");
      }
      List<Double> results = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         CollaborativeFilteringInstance instance = (CollaborativeFilteringInstance) dataset.getInstance(i);
         results.add(predictSingleItem(instance.getContextId(), instance.getItemId()));
      }
      return results;
   }


   /**
    * Computes the prediction value for a single item.
    * 
    * @param user the row of the vector which contains the ratings for the selected user
    * @param item the column of the vector which contains the ratings for the selected item
    * @return the computed rating prediction
    * @throws PredictionFailedException if the distance could not be computed
    */
   private double predictSingleItem(int user, int item) throws PredictionFailedException {

      if (neighborDistances[user] == null) {
         neighborDistances[user] = fillNeighborDistances(user);
      }

      double result = 0.0;
      int foundValues = 0;

      for (Iterator<WeightedObject<Integer>> iterator = neighborDistances[user].descendingIterator(); iterator.hasNext();) {
         WeightedObject<Integer> ratings = iterator.next();

         if (trainingData.getRating(ratings.getValue(), item) > minDoubleValue) {
            result += trainingData.getRating(ratings.getValue(), item);
            foundValues++;
         }
         if (foundValues >= numberOfNeighbors) {
            break;
         }
      }

      if (foundValues == 0)
         return 0;

      logger.debug(String.format(USER_S_ITEM_S_S_FROM_S_NEIGHBORS, user, item, result / foundValues, foundValues));
      return result / foundValues;

   }


   /**
    * Fill the {@link TreeSet} which contains the distances / similarities with the other users.
    * 
    * @param user the user id of the selected user
    * @return the distances relative to the selected user
    * @throws PredictionFailedException if the distance could not be computed
    */
   private TreeSet<WeightedObject<Integer>> fillNeighborDistances(int user) throws PredictionFailedException {
      IVector predictionVector = trainingData.getRowVector(user);
      TreeSet<WeightedObject<Integer>> distances = new TreeSet<>();
      for (int i = 0; i < trainingData.getNumberOfContexts(); i++) {
         IVector compareVector = trainingData.getRowVector(i);

         double similarity = Double.MAX_VALUE;
         try {
            similarity = similarityFunction.getLossForSingleRating(predictionVector, compareVector);
         } catch (LossException e) {
            throw new PredictionFailedException(String.format(COULD_NOT_PREDICT_A_VALUE_FOR_ITEM_S, user), e);
         }
         distances.add(new WeightedObject<Integer>(i, similarity));
      }
      return distances;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (!(dataset instanceof CollaborativeFilteringDataset)) {
         return false;
      }
      boolean compatible = true;
      for (int i = 0; i < dataset.getNumberOfInstances() && compatible; i++) {
         compatible = isInstanceCompatible(dataset.getInstance(i));
      }
      return compatible;

   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (!(instance instanceof CollaborativeFilteringInstance)) {
         return false;
      }
      CollaborativeFilteringInstance castedInstance = (CollaborativeFilteringInstance) instance;
      return castedInstance.getItemId() < trainingData.getNumberOfItems()
            && castedInstance.getContextId() < trainingData.getNumberOfContexts();

   }


   @Override
   public boolean equals(Object object) {
      if (this == object)
         return true;
      if (!super.equals(object))
         return false;
      if (object instanceof UserBasedFilteringLearningModel) {
         UserBasedFilteringLearningModel comparedObject = (UserBasedFilteringLearningModel) object;
         return trainingData.equals(comparedObject.trainingData) && similarityFunction.equals(comparedObject.similarityFunction)
               && numberOfNeighbors == comparedObject.numberOfNeighbors
               && Double.compare(minDoubleValue, comparedObject.minDoubleValue) == 0;
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * trainingData.hashCode();
      hashCode += 31 * similarityFunction.hashCode();
      hashCode += 31 * numberOfNeighbors;
      hashCode += 31 * Double.hashCode(minDoubleValue);
      return hashCode;
   }


}
