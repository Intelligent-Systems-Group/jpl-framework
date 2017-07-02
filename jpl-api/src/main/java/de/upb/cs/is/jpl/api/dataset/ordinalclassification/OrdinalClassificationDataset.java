package de.upb.cs.is.jpl.api.dataset.ordinalclassification;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.dataset.WrongDatasetInputException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This dataset is used to store a dataset for ordinal classification.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationDataset extends ADataset<double[], NullType, Double> {

   private static final String ERROR_GIVEN_INDEX_IS_ALREADY_FILLED_WITH_AN_INSTANCE = "This given index %s is already filled with an instance!";
   private static final String ERROR_GIVEN_FEATURE_VECTOR_LIST_AND_RATING_LIST_HAVE_NOT_SAME_LENGTH = "The given feature vector list and rating list have not the same length!";
   private static final String ERROR_GIVEN_INSTANCE_NUMBER_OUT_OF_BOUNDS = "The given instance number is out of the bounds of the dataset: %s";
   private static final String ERROR_WRONG_AMOUNT_OF_FEATURES = "The given instance has a wrong amount of context features.";
   private static final String ERROR_WRONG_INSTANCE_TYPE = "The given instance is not of type "
         + OrdinalClassificationInstance.class.getSimpleName() + ".";
   private static final String ERROR_WRONG_RATING_VALUE = "The given rating was not defined as valid: %s (allowed ratings: %s).";

   private boolean arePredictionClassesDefined;
   private List<Double> validRatings;

   private int numberOfFeatures;

   private List<double[]> featureVectors;
   private List<Double> ratings;


   /**
    * Creates an {@link OrdinalClassificationDataset} with the given number of features.
    * 
    * @param numberOfFeatures the number of features to store per instance in this dataset
    */
   public OrdinalClassificationDataset(int numberOfFeatures) {
      this(Collections.<Double> emptyList(), numberOfFeatures);
   }


   /**
    * Creates an {@link OrdinalClassificationDataset} with the given ordinal classes used for
    * prediction and the given number of features.
    * 
    * @param predictionClasses the ordinal classes used for prediction
    * @param numberOfFeatures the number of features to store per instance in this dataset
    */
   public OrdinalClassificationDataset(List<Double> predictionClasses, int numberOfFeatures) {
      super();
      if (!predictionClasses.isEmpty()) {
         this.validRatings = CollectionsUtils.getDeepCopyOf(predictionClasses);
         this.arePredictionClassesDefined = true;
      }
      this.numberOfFeatures = numberOfFeatures;
   }


   /**
    * Creates a {@link OrdinalClassificationDataset} with the given ordinal classes used for
    * prediction, the given number of features, the feature vectors and the rating of the instances.
    * This constructor is usually used to create a subset of a dataset.
    * 
    * @param predictionClasses the ordinal classes used for prediction
    * @param numberOfFeatures the number of features to store per instance in this dataset
    * @param featureVectors the feature vectors of the instances
    * @param ratings the ratings of the instances
    * @param datasetFile the according {@link DatasetFile} to this dataset
    */
   public OrdinalClassificationDataset(List<Double> predictionClasses, int numberOfFeatures, List<double[]> featureVectors,
         List<Double> ratings, DatasetFile datasetFile) {
      this(predictionClasses, numberOfFeatures);

      if (featureVectors.size() != ratings.size()) {
         throw new WrongDatasetInputException(ERROR_GIVEN_FEATURE_VECTOR_LIST_AND_RATING_LIST_HAVE_NOT_SAME_LENGTH);
      }
      this.featureVectors = CollectionsUtils.getDeepCopyOf(featureVectors);
      this.ratings = CollectionsUtils.getDeepCopyOf(ratings);
      this.datasetFile = datasetFile;
   }


   @Override
   protected void init() {
      validRatings = new ArrayList<>();
      arePredictionClassesDefined = false;
      featureVectors = new ArrayList<>();
      ratings = new ArrayList<>();
   }


   @Override
   public OrdinalClassificationInstance getInstance(int position) {
      assertInstancePositionIsInBounds(position);
      return new OrdinalClassificationInstance(featureVectors.get(position), ratings.get(position));
   }


   /**
    * Checks if the given instance position is part of the dataset.
    * 
    * @param position the instance position to be checked
    */
   private void assertInstancePositionIsInBounds(int position) {
      if (position < 0 || position >= featureVectors.size()) {
         throw new IndexOutOfBoundsException(String.format(ERROR_GIVEN_INSTANCE_NUMBER_OUT_OF_BOUNDS, position));
      }
   }


   @Override
   public void addInstance(IInstance<double[], NullType, Double> instance) {
      if (instance instanceof OrdinalClassificationInstance) {
         addFeatureVectorWithResult(instance.getContextFeatureVector(), instance.getRating());
      } else {
         throw new IllegalArgumentException(ERROR_WRONG_INSTANCE_TYPE);
      }
   }


   /**
    * Adds the given feature vector with its result to this dataset, if the context feature vector
    * given has the correct size.
    * 
    * @param featureVector the feature vector to add
    * @param rating the rating of the feature vector to add
    */
   private void addFeatureVectorWithResult(double[] featureVector, Double rating) {
      int nextFreeSlot = findNextFreeSlot();
      setFeatureVectorForInstance(nextFreeSlot, featureVector);
      setRatingForInstance(nextFreeSlot, rating);
   }


   /**
    * Returns the next free slot for an instance.
    * 
    * @return the index of the next free slot for an instance
    */
   private int findNextFreeSlot() {
      for (int i = 0; i < ratings.size(); i++) {
         if (ratings.get(i) == null) {
            return i;
         }
      }
      return ratings.size();
   }


   /**
    * Checks whether the given rating is valid for this dataset.
    * 
    * @param rating the rating to check
    */
   private boolean isRatingValid(Double rating) {
      if (validRatings.contains(rating)) {
         return true;
      } else if (!arePredictionClassesDefined) {
         validRatings.add(rating);
         return true;
      }
      return false;
   }


   @Override
   public OrdinalClassificationDataset getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);
      return createSubset(from, to);
   }


   /**
    * Returns a subset of this dataset containing the instance within the given range (exclusive
    * {@code to}).
    * 
    * @param from the index of the first instance to return
    * @param to the index of the first instance which will not be part of the subset anymore
    * 
    * @return a subset of this dataset
    */
   private OrdinalClassificationDataset createSubset(int from, int to) {
      List<double[]> subsetOfFeatureVectors = CollectionsUtils.getDeepCopyOf(featureVectors.subList(from, to));
      List<Double> subsetOfRatings = CollectionsUtils.getDeepCopyOf(ratings.subList(from, to));
      return new OrdinalClassificationDataset(validRatings, numberOfFeatures, subsetOfFeatureVectors, subsetOfRatings, getDatasetFile());
   }


   /**
    * Returns the ordinal classes used for prediction of this dataset.
    * 
    * @return the ordinal prediction classes
    */
   public List<Double> getValidRatings() {
      return validRatings;
   }


   /**
    * Sets the valid ordinal classes used for rating of this dataset.
    * 
    * @param newValidRatings the new valid ordinal rating classes to set
    */
   public void setValidRatings(List<Double> newValidRatings) {
      validRatings = CollectionsUtils.getDeepCopyOf(newValidRatings);
   }


   /**
    * Return the number of context features of this dataset.
    * 
    * @return the number of context features
    */
   public int getNumberOfFeatures() {
      return numberOfFeatures;
   }


   @Override
   public int getNumberOfInstances() {
      return featureVectors.size();
   }


   /**
    * Returns the {@link List} of feature vectors.
    * 
    * @return the feature vectors
    */
   public List<double[]> getFeatureVectors() {
      return featureVectors;
   }


   /**
    * Adds the given feature vector at the given index to the {@link List} of feature vectors.
    * 
    * @param index the index to store the given feature vector in the list of feature vectors
    * @param featureVector the feature vector to store
    */
   public void setFeatureVectorForInstance(int index, double[] featureVector) {
      if (featureVector.length == numberOfFeatures) {
         if (index > featureVectors.size() - 1) {
            while (featureVectors.size() - 1 < index) {
               featureVectors.add(null);
            }
         }
         if (featureVectors.get(index) == null) {
            featureVectors.set(index, featureVector);
         } else {
            throw new IllegalArgumentException(String.format(ERROR_GIVEN_INDEX_IS_ALREADY_FILLED_WITH_AN_INSTANCE, index));
         }
      } else {
         throw new IllegalArgumentException(ERROR_WRONG_AMOUNT_OF_FEATURES);
      }
   }


   /**
    * Returns the {@link List} of ratings.
    * 
    * @return the ratings
    */
   public List<Double> getRatings() {
      return ratings;
   }


   /**
    * Adds the given rating for the instance with the given index.
    * 
    * @param index the index to store the given rating in the list of ratings
    * @param rating the rating to store
    */
   public void setRatingForInstance(int index, double rating) {
      if (!isRatingValid(rating)) {
         throw new IllegalArgumentException(String.format(ERROR_WRONG_RATING_VALUE, rating, validRatings));
      }
      if (index > ratings.size() - 1) {
         while (ratings.size() - 1 < index) {
            ratings.add(null);
         }
      }
      if (ratings.get(index) == null) {
         ratings.set(index, rating);
      } else {
         throw new IllegalArgumentException(String.format(ERROR_GIVEN_INDEX_IS_ALREADY_FILLED_WITH_AN_INSTANCE, index));
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (arePredictionClassesDefined ? 1231 : 1237);
      result = prime * result + ((featureVectors == null) ? 0 : featureVectors.hashCode());
      result = prime * result + numberOfFeatures;
      result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
      result = prime * result + ((validRatings == null) ? 0 : validRatings.hashCode());
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
      OrdinalClassificationDataset other = (OrdinalClassificationDataset) obj;
      if (arePredictionClassesDefined != other.arePredictionClassesDefined)
         return false;
      if (featureVectors == null) {
         if (other.featureVectors != null)
            return false;
      } else if (!featureVectors.equals(other.featureVectors))
         return false;
      if (numberOfFeatures != other.numberOfFeatures)
         return false;
      if (ratings == null) {
         if (other.ratings != null)
            return false;
      } else if (!ratings.equals(other.ratings))
         return false;
      if (validRatings == null) {
         if (other.validRatings != null)
            return false;
      } else if (!validRatings.equals(other.validRatings))
         return false;
      return true;
   }

}
