package de.upb.cs.is.jpl.api.dataset.instanceranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.exception.dataset.WrongDatasetInputException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * A dataset specified for problems of instance ranking. The dataset can only handle features and
 * ratings in absolute form. The dataset is designed to work efficiently with tasks where a single
 * instance is directly connected to a rating. The context features can be accessed with the list
 * {@link InstanceRankingDataset#getContextFeatureList()} and the ratings with
 * {@link InstanceRankingDataset#getRatingList()}.
 *
 * @author Sebastian Gottschalk
 */
public class InstanceRankingDataset extends ADataset<double[], NullType, Integer> {
   private static final String ERROR_NO_RATING_FOUND = "The instance could not be added because it has no rating.";
   private static final String ERROR_WRONG_NUMBER_OF_FEATURES = "The added instance has not the same number of features than the dataset.";
   private static final String ERROR_FEATURE_OR_RATING_LIST_NULL = "The context feature list and the rating list should not be null.";

   private List<double[]> contextFeatureList;
   private List<Integer> ratingList;
   private int numberOfFeatures;


   @Override
   protected void init() {
      contextFeatureList = new ArrayList<>();
      ratingList = new ArrayList<>();
      numberOfFeatures = 0;
   }


   /**
    * Remove all elements from the dataset and fill up the dataset with instances in form of
    * features and ratings. For performance reasons the input lists are not copied so be aware of
    * side effects.
    * 
    * @param contextFeatureList a list of instances where each list is described as a list of double
    *           values
    * @param ratingList the ratings of the different instances
    */
   public void fillDataset(List<double[]> contextFeatureList, List<Integer> ratingList) {
      init();
      checkTheDatasetForNonEmptyFields(contextFeatureList, ratingList);

      this.contextFeatureList.addAll(contextFeatureList);
      this.ratingList.addAll(ratingList);
      if (!this.contextFeatureList.isEmpty()) {
         numberOfFeatures = contextFeatureList.get(0).length;
      }
   }


   /**
    * Check the dataset for non empty fields.
    * 
    * @param contextFeatureList a list of instances where each list is described as a list of double
    *           values
    * @param ratingList the ratings of the different instances
    */
   private void checkTheDatasetForNonEmptyFields(List<double[]> contextFeatureList, List<Integer> ratingList) {
      if (contextFeatureList == null || ratingList == null) {
         throw new WrongDatasetInputException(ERROR_FEATURE_OR_RATING_LIST_NULL);
      }

      if (contextFeatureList.size() != ratingList.size()) {
         throw new WrongDatasetInputException(ERROR_WRONG_NUMBER_OF_FEATURES);
      }
   }


   /**
    * Returns a list of all context features. The list provides context feature items in form of
    * doubles. The corresponding rating values are accessible with
    * {@link InstanceRankingDataset#getRatingList()}.
    * 
    * @return a list of all context features
    */
   public List<double[]> getContextFeatureList() {
      return this.contextFeatureList;
   }


   /**
    * Returns a list of all rating values in form of integer. The corresponding context feature
    * values are accessible with {@link InstanceRankingDataset#getContextFeatureList()}.
    * 
    * @return a list of all rating values
    */
   public List<Integer> getRatingList() {
      return this.ratingList;
   }


   @Override
   public int getNumberOfInstances() {
      return contextFeatureList.size();
   }


   /**
    * Returns the number of features of a single instance.
    * 
    * @return the number of features
    * 
    */
   public int getNumberOfFeatures() {
      return numberOfFeatures;
   }


   @Override
   public IInstance<double[], NullType, Integer> getInstance(int position) {
      assertInstanceIsInBounds(position);

      return new InstanceRankingInstance(position, contextFeatureList.get(position), ratingList.get(position));
   }


   @Override
   public IDataset<double[], NullType, Integer> getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);

      InstanceRankingDataset dataset = new InstanceRankingDataset();
      dataset.fillDataset(CollectionsUtils.getDeepCopyOf(contextFeatureList.subList(from, to)),
            CollectionsUtils.getDeepCopyOf(ratingList.subList(from, to)));
      return dataset;
   }


   /**
    * Checks if the {@link InstancRankingInstance} can be added to the dataset.
    * 
    * @param instance the instance which should be added to the dataset
    * @throws InvalidInstanceException if the instance can be not added to
    */
   private void checkInstanceForInvalidInput(InstanceRankingInstance instance) throws InvalidInstanceException {
      // No instance available in dataset
      if (contextFeatureList.isEmpty()) {
         numberOfFeatures = instance.getContextFeatureVector().length;
      }

      if (instance.getContextFeatureVector().length != numberOfFeatures) {
         throw new InvalidInstanceException(ERROR_WRONG_NUMBER_OF_FEATURES);
      }

      if (instance.getRating() == null) {
         throw new InvalidInstanceException(ERROR_NO_RATING_FOUND);
      }
   }


   @Override
   public void addInstance(IInstance<double[], NullType, Integer> instance) throws InvalidInstanceException {
      assertInstanceHasCorrectType(instance, InstanceRankingInstance.class);

      InstanceRankingInstance rankingInstance = (InstanceRankingInstance) instance;
      checkInstanceForInvalidInput(rankingInstance);

      contextFeatureList.add(rankingInstance.getContextFeatureVector());
      ratingList.add(rankingInstance.getRating());
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + contextFeatureList.hashCode();
      result = prime * result + ratingList.hashCode();
      result = prime * result + Integer.hashCode(numberOfFeatures);

      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof InstanceRankingDataset) {
         InstanceRankingDataset instanceRankingDataset = (InstanceRankingDataset) secondObject;
         if (!instanceRankingDataset.contextFeatureList.equals(contextFeatureList)
               || Integer.compare(instanceRankingDataset.numberOfFeatures, numberOfFeatures) != 0
               || !instanceRankingDataset.ratingList.equals(ratingList)) {
            return false;
         }

         return true;
      }
      return false;
   }


}