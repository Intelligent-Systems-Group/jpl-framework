package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This abstract learning model is used for all learning models of rank aggregation algorithms,
 * which contains the same dataset and instance compatibility assertions.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class ARankAggregationLearningModel extends ALearningModel<Ranking> {
   protected List<Integer> listOfLabels;
   private static final String DATASET_CONTAINS_EXTRA_LABEL_ERROR_MESSAGE = "The provided dataset contains the label %s which does not exist in the dataset on which the model is trained";
   private static final String INSTANCE_CONTAINS_EXTRA_LABEL_ERROR_MESSAGE = "The provided instance %s ranking contains the label %s which does not exist in the dataset on which the model is trained";
   protected String exceptionString;


   /**
    * Creates a new {@link ARankAggregationLearningModel} with the list of labels on which the model
    * was trained on.
    * 
    * @param listOfLabels the list of labels on which the model is trained on
    */
   public ARankAggregationLearningModel(List<Integer> listOfLabels) {
      this.listOfLabels = CollectionsUtils.getDeepCopyOf(listOfLabels);
   }


   /**
    * This method can be used in all predict methods for all rank aggregation learning model to
    * assert the compatibility of the {@link RankAggregationDataset}.
    * 
    * @param dataset the rank aggregation dataset
    * @throws PredictionFailedException if it is not applicable for this learning model
    */
   public void assetDatasetCompatibility(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException(exceptionString);
      }
   }


   /**
    * This method can be used in all predict methods for all rank aggregation learning model to
    * assert the compatibility of the {@link RankAggregationInstance}..
    * 
    * @param instance the rank aggregation instance
    * @throws PredictionFailedException if it is not applicable for this learning model
    */
   public void assertInstanceCompatibility(final IInstance<?, ?, ?> instance) throws PredictionFailedException {
      if (!isInstanceCompatible(instance)) {
         throw new PredictionFailedException(exceptionString);
      }
   }


   /**
    * {@inheritDoc} The method that checks weather the ranked labels in the
    * {@link RankAggregationDataset} are present in the list of labels on which the rank aggregation
    * {@link ILearningModel} is trained on.
    *
    * @param dataset the rank aggregation dataset
    * @return true, if the dataset compatibility check was successful
    */
   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (dataset instanceof RankAggregationDataset) {
         for (int label : ((RankAggregationDataset) dataset).getLabels()) {
            if (!listOfLabels.contains(label)) {
               exceptionString = String.format(DATASET_CONTAINS_EXTRA_LABEL_ERROR_MESSAGE, String.valueOf(label));
               return false;
            }
         }
         boolean result = true;
         for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
            result = result && isInstanceCompatible(dataset.getInstance(i));
         }
         return result;
      }
      return false;
   }


   /**
    * {@inheritDoc} The method that checks whether the objects in the {@link Ranking} of the
    * {@link RankAggregationInstance} are present in the list of labels on which the rank
    * aggregation {@link ILearningModel} is trained on.
    *
    * @param instance the rank aggregation instance
    * @return {@code true}, if instance compatibility check was successful
    */
   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (instance != null && instance instanceof RankAggregationInstance) {
         Ranking ranking = ((RankAggregationInstance) instance).getRating();
         for (int label : ranking.getObjectList()) {
            if (!listOfLabels.contains(label)) {
               exceptionString = String.format(INSTANCE_CONTAINS_EXTRA_LABEL_ERROR_MESSAGE, ranking.toString(), String.valueOf(label));
               return false;
            }
         }
      } else {
         return false;
      }
      return true;
   }


   /**
    * Returns the list of labels for which aggregated ranking has to be predicted.
    * 
    * @return the list of labels
    */
   public List<Integer> getListOfLabels() {
      return listOfLabels;
   }


   /**
    * Sets the list of labels for which aggregated ranking has to be predicted.
    * 
    * @param listOfLabels the list of labels on which model is trained on
    */
   public void setListOfLabels(List<Integer> listOfLabels) {
      this.listOfLabels = CollectionsUtils.getDeepCopyOf(listOfLabels);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((exceptionString == null) ? 0 : exceptionString.hashCode());
      result = prime * result + ((listOfLabels == null) ? 0 : listOfLabels.hashCode());
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
      ARankAggregationLearningModel other = (ARankAggregationLearningModel) obj;
      if (exceptionString == null) {
         if (other.exceptionString != null)
            return false;
      } else if (!exceptionString.equals(other.exceptionString))
         return false;
      if (listOfLabels == null) {
         if (other.listOfLabels != null)
            return false;
      } else if (!listOfLabels.equals(other.listOfLabels))
         return false;
      return true;
   }
}
