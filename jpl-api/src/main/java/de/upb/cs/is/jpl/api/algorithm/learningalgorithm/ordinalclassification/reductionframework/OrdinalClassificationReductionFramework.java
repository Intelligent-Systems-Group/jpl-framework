package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.AOrdinalClassification;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This is a reduction framework for ordinal classification. The idea is to train one baselearner
 * with modified instances: Each instance which is given to this framework will be transformed to
 * {@code m-1} {@link BaselearnerInstance}s, where {@code m} is the number of prediction classes.
 * Therefore the feature vector of every {@link BaselearnerInstance} is extended by an additional
 * feature representing the prediction class, one {@link BaselearnerInstance} for every prediction
 * class for which will be trained. As result every instance gets {@code 1} if the additional
 * feature (prediction class) is greater than the rating of the original
 * {@link OrdinalClassificationInstance}, otherwise {@code -1} if the additional feature is smaller
 * than the original rating.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationReductionFramework extends AOrdinalClassification<OrdinalClassificationReductionFrameworkConfiguration> {


   /**
    * Creates an {@link OrdinalClassificationReductionFramework} with the default configuration.
    */
   public OrdinalClassificationReductionFramework() {
      super(ELearningAlgorithm.ORDINAL_CLASSIFICATION_REDUCTION_FRAMEWORK.getIdentifier());
   }


   @Override
   public void init() {
      // nothing to do here
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      OrdinalClassificationReductionFrameworkConfiguration configuration = new OrdinalClassificationReductionFrameworkConfiguration();
      configuration.initializeDefaultConfiguration();
      return configuration;
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      OrdinalClassificationDataset ordinalDataset = (OrdinalClassificationDataset) dataset;
      List<Double> allPredictionClasses = getPredictionClasses(ordinalDataset);
      BaselearnerDataset baselearnerDataset = transformToBaselearnerDataset(ordinalDataset, allPredictionClasses.size());

      IBaselearnerAlgorithm baselearner = configuration.getBaseLearnerAlgorithm();
      @SuppressWarnings("unchecked")
      ABaseLearningModel<Double> baselearnerModel = (ABaseLearningModel<Double>) baselearner.train(baselearnerDataset);

      return new OrdinalClassificationReductionFrameworkLearningModel(allPredictionClasses, baselearnerModel,
            ordinalDataset.getNumberOfFeatures());
   }


   @Override
   public OrdinalClassificationReductionFrameworkLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (OrdinalClassificationReductionFrameworkLearningModel) super.train(dataset);
   }


   /**
    * Returns a baselearner dataset transformed from the given {@link OrdinalClassificationDataset}
    * for the given amount of prediction classes.
    * 
    * @param ordinalDataset the dataset to transform
    * @param numberOfPredictionClasses the number of prediction classes
    * 
    * @return the transformed baselearner dataset from the given ordinal dataset
    * 
    * @throws TrainModelsFailedException if the cost matrix type, set in the configuration, is
    *            unknown
    */
   private BaselearnerDataset transformToBaselearnerDataset(OrdinalClassificationDataset ordinalDataset, int numberOfPredictionClasses)
         throws TrainModelsFailedException {
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(
            ordinalDataset.getNumberOfInstances() * (numberOfPredictionClasses - 1), ordinalDataset.getNumberOfFeatures() + 1);

      for (int i = 0; i < ordinalDataset.getNumberOfInstances(); i++) {
         OrdinalClassificationInstance instance = ordinalDataset.getInstance(i);

         for (int predictionClassIndex = 0; predictionClassIndex < numberOfPredictionClasses - 1; predictionClassIndex++) {

            double weight = computeWeightFor((int) (instance.getRating().doubleValue()), predictionClassIndex);

            baselearnerDataset.addFeatureVectorWithResultAndWeight(
                  CollectionsUtils.extendArray(instance.getContextFeatureVector(), predictionClassIndex),
                  getNewRating(predictionClassIndex, instance.getRating()), weight);
         }
      }
      return baselearnerDataset;
   }


   /**
    * Computes the weight for the cost matrix entry at position (i,j).
    * 
    * @param i the x-coordinate of the matrix
    * @param j the y-coordinate of the matrix
    * 
    * @return the weight for the entry (i,j) of the cost matrix
    * 
    * @throws TrainModelsFailedException if the cost matrix type, set in the configuration, is
    *            unknown
    */
   private double computeWeightFor(int i, int j) throws TrainModelsFailedException {
      return Math.abs(configuration.getCostMatrixEntry(i, j) - configuration.getCostMatrixEntry(i, j + 1));
   }


   /**
    * Returns the new rating for a {@link BaselearnerInstance} of the given rating of an
    * {@link OrdinalClassificationInstance} for the given prediction class.
    * 
    * @param predictionClassIndex the index of the prediction class to get the rating for
    * @param rating the rating of the original {@link OrdinalClassificationInstance}
    * 
    * @return the rating for a {@link BaselearnerInstance}
    */
   private double getNewRating(int predictionClassIndex, double rating) {
      return predictionClassIndex < rating ? 1 : -1;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof OrdinalClassificationReductionFramework) {
         return true;
      }
      return false;
   }

}
