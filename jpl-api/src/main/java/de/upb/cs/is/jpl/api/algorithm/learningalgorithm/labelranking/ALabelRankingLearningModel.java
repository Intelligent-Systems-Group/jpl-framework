package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;


/**
 * This abstract class is designed to be the base class for all learning models used for label
 * ranking learning algorithms, which predict a {@link Ranking}. It offers convenience methods such
 * as checking if an instance has the correct number of features.
 *
 * @author Andreas Kornelsen
 */
public abstract class ALabelRankingLearningModel extends ALearningModel<Ranking> {

   protected int numberOfContextFeaturesTrainedOn;


   /**
    * Creates a new {@link ALabelRankingLearningModel} with the expected number of context features.
    * 
    * @param numberOfContextFeaturesTrainedOn the number of context features this model was trained
    *           on
    */
   public ALabelRankingLearningModel(int numberOfContextFeaturesTrainedOn) {
      super();
      this.numberOfContextFeaturesTrainedOn = numberOfContextFeaturesTrainedOn;
   }


   @Override
   public abstract Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException;


   /**
    * Returns {@code true} if the given features vector has as many entries as this model has
    * training features.
    * 
    * @param featureVector the feature vector to check
    * @return {@code true} if the given features vector has as many entries as this model has
    *         training features, otherwise {@code false}
    */
   private boolean isFeatureVectorSizedCorrectly(double[] featureVector) {
      return featureVector.length == numberOfContextFeaturesTrainedOn;
   }


   /**
    * {@inheritDoc} Note that calling this method is extremely expensive, as all instances of the
    * dataset will be checked.
    * 
    * @param dataset the dataset to check
    * @return {@code true} if the dataset is compatible, otherwise {@code false}
    */
   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (dataset != null && dataset instanceof LabelRankingDataset) {
         LabelRankingDataset labelRankingDataset = (LabelRankingDataset) dataset;
         int numberOfInstances = labelRankingDataset.getNumberOfInstances();
         for (int i = 0; i < numberOfInstances; i++) {
            double[] featureValuesOfAnInstance = labelRankingDataset.getFeatureValuesOfAnInstance(i);
            if (!isFeatureVectorSizedCorrectly(featureValuesOfAnInstance)) {
               return false;
            }
         }
         return true;
      }
      return false;
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance != null && instance instanceof LabelRankingInstance) {
         LabelRankingInstance labelRankingInstance = (LabelRankingInstance) instance;
         return isFeatureVectorSizedCorrectly(labelRankingInstance.getContextFeatureVector());
      }
      return false;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + numberOfContextFeaturesTrainedOn;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof ALabelRankingLearningModel))
         return false;
      ALabelRankingLearningModel other = (ALabelRankingLearningModel) obj;
      if (numberOfContextFeaturesTrainedOn != other.numberOfContextFeaturesTrainedOn)
         return false;
      return true;
   }

}
