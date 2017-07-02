package de.upb.cs.is.jpl.api.dataset.multilabelclassification;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class represents a simple and efficient dataset for multilabel classification problems with
 * sparse datasets (in terms of the label). In particular this implementation assumes that each
 * instance is only labeled with very few labels from the label set, as the labels of each instance
 * are stored in a sparse vector. Therefore the memory and possibly also the runtime efficiency of
 * this dataset decreases if the underlying dataset is not sparse (in terms of the labels).
 * 
 * The dataset is implemented in such a way to not work with
 * {@link MultilabelClassificationInstance}s directly in order to save resources.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationDataset extends ADataset<double[], NullType, SparseDoubleVector> {
   private int numberOfFeatures;

   private int numberOfLabels;
   private List<double[]> featureVectors;
   private List<SparseDoubleVector> correctResults;


   /**
    * Creates an empty multilabel classification dataset with an overall label set of the given
    * size, which can contain instances with the given number of features.
    * 
    * @param numberOfLabels the size of the overall label set
    * @param numberOfFeatures the number of features an instance in this dataset can have
    */
   public MultilabelClassificationDataset(int numberOfLabels, int numberOfFeatures) {
      super();
      this.numberOfFeatures = numberOfFeatures;
      this.numberOfLabels = numberOfLabels;
   }


   /**
    * Creates an empty multilabel classification dataset.
    * 
    */
   public MultilabelClassificationDataset() {
      super();
      this.numberOfFeatures = -1;
      this.numberOfLabels = -1;
   }


   @Override
   protected void init() {
      this.featureVectors = new ArrayList<>();
      this.correctResults = new ArrayList<>();
   }


   @Override
   public MultilabelClassificationInstance getInstance(int position) {
      assertInstanceIsInBounds(position);
      MultilabelClassificationInstance multiLabelClassificationInstance = new MultilabelClassificationInstance(getNumberOfFeatures());
      multiLabelClassificationInstance.setContextFeatureVector(featureVectors.get(position));
      multiLabelClassificationInstance.setRating(correctResults.get(position));
      return multiLabelClassificationInstance;
   }


   @Override
   public MultilabelClassificationDataset getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);
      MultilabelClassificationDataset partialDataset = new MultilabelClassificationDataset(getNumberOfLabels(), getNumberOfFeatures());
      for (int i = from; i < to; i++) {
         partialDataset.addFeatureVector(featureVectors.get(i));
         partialDataset.addCorrectResult(correctResults.get(i));
      }
      return partialDataset;
   }


   @Override
   public int getNumberOfInstances() {
      return featureVectors.size();
   }


   @Override
   public void addInstance(IInstance<double[], NullType, SparseDoubleVector> instance) {
      assertInstanceHasCorrectType(instance, MultilabelClassificationInstance.class);
      MultilabelClassificationInstance castedInstance = (MultilabelClassificationInstance) instance;
      featureVectors.add(castedInstance.getContextFeatureVector());
      correctResults.add(castedInstance.getRating());
   }


   /**
    * Adds the given feature vector to this dataset.
    * 
    * @param featureVector the feature vector to add
    */
   public void addFeatureVector(double[] featureVector) {
      featureVectors.add(featureVector);
   }


   /**
    * Adds the given correct result to this dataset.
    * 
    * @param correctResult the correct result to add
    */
   public void addCorrectResult(SparseDoubleVector correctResult) {
      correctResults.add(correctResult);
   }


   /**
    * Returns the number of features, this dataset is working with.
    * 
    * @return the number of features, this dataset is working with
    */
   public int getNumberOfFeatures() {
      if (numberOfFeatures == -1 && getNumberOfInstances() > 0) {
         numberOfFeatures = featureVectors.get(0).length;
      }
      return numberOfFeatures;
   }


   /**
    * Returns the number of labels this dataset is working with.
    * 
    * @return the number of labels this dataset is working with
    */
   public int getNumberOfLabels() {
      if (numberOfLabels == -1 && getNumberOfInstances() > 0) {
         numberOfLabels = correctResults.get(0).length();
      }
      return numberOfLabels;
   }


   /**
    * Returns the feature vectors of this dataset.
    * 
    * @return the feature vectors of this dataset
    */
   public List<double[]> getFeatureVectors() {
      return featureVectors;
   }


   /**
    * Returns the correct results of this dataset.
    * 
    * @return the correct results of this dataset
    */
   public List<SparseDoubleVector> getCorrectResults() {
      return correctResults;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((correctResults == null) ? 0 : correctResults.hashCode());
      result = prime * result + ((featureVectors == null) ? 0 : featureVectors.hashCode());
      result = prime * result + numberOfFeatures;
      result = prime * result + numberOfLabels;
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
      MultilabelClassificationDataset other = (MultilabelClassificationDataset) obj;
      if (correctResults == null) {
         if (other.correctResults != null)
            return false;
      } else if (!correctResults.equals(other.correctResults))
         return false;
      if (featureVectors == null) {
         if (other.featureVectors != null)
            return false;
      } else if (!featureVectors.equals(other.featureVectors))
         return false;
      if (numberOfFeatures != other.numberOfFeatures)
         return false;
      if (numberOfLabels != other.numberOfLabels)
         return false;
      return true;
   }

}
