package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.AOrdinalClassificationLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This learning model is used for the {@link OrdinalClassificationReductionFramework}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationReductionFrameworkLearningModel extends AOrdinalClassificationLearningModel {

   private ABaseLearningModel<Double> baselearnerModel;


   /**
    * Creates a new model for the ordinal classification reduction framework.
    * 
    * @param predictionClasses the classes used for prediction
    * @param learningModel the trained baselearner model
    * @param numberOfFeaturesTrainedOn the number of features this model is trained on
    */
   public OrdinalClassificationReductionFrameworkLearningModel(List<Double> predictionClasses, ABaseLearningModel<Double> learningModel,
         int numberOfFeaturesTrainedOn) {
      super(numberOfFeaturesTrainedOn);
      initializePredictionClasses(predictionClasses);
      this.baselearnerModel = learningModel;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      OrdinalClassificationInstance ordinalInstance = getOrdinalClassificationInstance(instance);

      int predictedClassIndex = 0;
      for (int i = 0; i < predictionClasses.size() - 1; i++) {
         BaselearnerInstance baselearnerInstance = new BaselearnerInstance(
               CollectionsUtils.extendArray(ordinalInstance.getContextFeatureVector(), predictionClasses.get(i)));
         if (baselearnerModel.predict(baselearnerInstance) > -1) {
            predictedClassIndex += 1;
         }
      }

      return predictionClasses.get(predictedClassIndex);
   }


   @Override
   public List<Double> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      OrdinalClassificationDataset ordinalDataset = getOrdinalClassificationDataset(dataset);
      List<BaselearnerDataset> baselearnerDatasets = createBaselearnerDatasets(ordinalDataset.getFeatureVectors());

      List<List<Double>> baselearnerResults = new ArrayList<>(ordinalDataset.getNumberOfInstances());
      for (int i = 0; i < baselearnerDatasets.size(); i++) {
         baselearnerResults.add(baselearnerModel.predict(baselearnerDatasets.get(i)));
      }

      List<Double> results = new ArrayList<>(ordinalDataset.getNumberOfInstances());
      for (int j = 0; j < baselearnerResults.size(); j++) {
         int predictedClassIndex = 0;
         for (int i = 0; i < predictionClasses.size() - 1; i++) {
            if (baselearnerResults.get(j).get(i) > -1) {
               predictedClassIndex += 1;
            }
         }
         results.add(j, predictionClasses.get(predictedClassIndex));
      }

      return results;
   }


   /**
    * Creates a list of {@link BaselearnerDataset}s, one for each given feature vector. Each dataset
    * contains instances with the original feature vector extended by an additional feature: the
    * prediction class which is tried to predict. Therefore each dataset contains as many instances
    * as number of prediction classes.
    * 
    * @param featureVectors the feature vectors to transform
    * 
    * @return a list of {@link BaselearnerDataset}, one for each given feature vector
    */
   private List<BaselearnerDataset> createBaselearnerDatasets(List<double[]> featureVectors) {
      List<BaselearnerDataset> baselearnerDatasets = new ArrayList<>(featureVectors.size());

      for (int i = 0; i < featureVectors.size(); i++) {
         baselearnerDatasets.add(new BaselearnerDataset(predictionClasses.size(), featureVectors.get(0).length + 1));
         for (int j = 0; j < predictionClasses.size(); j++) {
            baselearnerDatasets.get(i)
                  .addFeatureVectorWithoutResult(CollectionsUtils.extendArray(featureVectors.get(i), predictionClasses.get(j)));
         }
      }

      return baselearnerDatasets;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      OrdinalClassificationReductionFrameworkLearningModel other = (OrdinalClassificationReductionFrameworkLearningModel) obj;
      if (baselearnerModel == null) {
         if (other.baselearnerModel != null)
            return false;
      } else if (!baselearnerModel.equals(other.baselearnerModel))
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baselearnerModel == null) ? 0 : baselearnerModel.hashCode());
      return result;
   }

}
