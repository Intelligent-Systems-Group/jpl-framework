package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.AOrdinalClassification;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.OrdinalClassificationUtils;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This algorithm is a very simple approach for ordinal classification. The idea is to divide the
 * original ordinal problem with a class set of {@code m} classes up into {@code m-1} binary
 * classification problems. For each subproblem {@code i} the negative case is the meta-class
 * containing all classes {@code 1} ... {@code i} and the positive case is the meta-class containing
 * all classes {@code i+1} ... {@code m}. Therefore each subproblem will be trained by a training
 * set containing the instances having a correct result contained by the positive meta-class.
 * 
 * @author Tanja Tornede
 *
 */
public class SimpleOrdinalClassification extends AOrdinalClassification<SimpleOrdinalClassificationConfiguration> {

   /**
    * Creates a {@link SimpleOrdinalClassification} with the default configuration.
    */
   public SimpleOrdinalClassification() {
      super(ELearningAlgorithm.SIMPLE_ORDINAL_CLASSIFICATION.getIdentifier());
   }


   @Override
   public void init() {
      // nothing to do here
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new SimpleOrdinalClassificationConfiguration();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      OrdinalClassificationDataset ordinalDataset = (OrdinalClassificationDataset) dataset;
      List<Double> allPredictionClasses = getPredictionClasses(ordinalDataset);
      List<BaselearnerDataset> allBaselearnerDatasets = transformToBaselearnerDatasets(ordinalDataset, allPredictionClasses);

      List<ILearningModel<Double>> baselearnerModels = new ArrayList<>(allPredictionClasses.size() - 1);
      for (int predictionClass = 0; predictionClass < allPredictionClasses.size() - 1; predictionClass++) {
         BaselearnerDataset baselearnerDataset = allBaselearnerDatasets.get(predictionClass);

         @SuppressWarnings("unchecked")
         ILearningModel<Double> baselearnerModel = (ILearningModel<Double>) configuration.getBaseLearnerAlgorithm()
               .train(baselearnerDataset);
         baselearnerModels.add(baselearnerModel);

      }
      return new SimpleOrdinalClassificationLearningModel(allPredictionClasses, baselearnerModels, ordinalDataset.getNumberOfFeatures());
   }


   @Override
   public SimpleOrdinalClassificationLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (SimpleOrdinalClassificationLearningModel) super.train(dataset);
   }


   /**
    * Transforms the given {@link OrdinalClassificationDataset} to {@link BaselearnerDataset}'s, one
    * for each given prediction class.
    * 
    * @param dataset the dataset to transform
    * @param predictionClasses the prediction classes for which the dataset will be transformed
    * 
    * @return the transformed dataset to baselearner datasets, one for each prediction class
    */
   private List<BaselearnerDataset> transformToBaselearnerDatasets(OrdinalClassificationDataset dataset, List<Double> predictionClasses) {
      List<BaselearnerDataset> allBaselearnerDatasets = initializeBaselearnerDatasets(dataset.getNumberOfInstances(),
            dataset.getNumberOfFeatures(), predictionClasses.size());

      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         OrdinalClassificationInstance instance = dataset.getInstance(i);
         List<BaselearnerInstance> transformedInstances = OrdinalClassificationUtils
               .transformToAllBaselearnerInstancesWithCorrectResult(instance, predictionClasses);
         for (int j = 0; j < predictionClasses.size() - 1; j++) {
            allBaselearnerDatasets.get(j).addInstance(transformedInstances.get(j));
         }
      }
      return allBaselearnerDatasets;
   }


   /**
    * Initializes the list of base learner datasets for the given amount of prediction classes.
    * 
    * @param numberOfInstances the number of instances to store in the datasets
    * @param numberOfFeatures the number of features of the instances to store
    * @param numberOfPredictionClasses the amount of prediction classes, which is the amount of base
    *           learner datasets that will be returned
    * 
    * @return a list of {@code numberOfPredictionClasses} base learner datasets for
    *         {@code numberOfInstances} instances with {@code numberOfFeatures} features
    */
   private List<BaselearnerDataset> initializeBaselearnerDatasets(int numberOfInstances, int numberOfFeatures,
         int numberOfPredictionClasses) {
      List<BaselearnerDataset> allBaselearnerDatasets = new ArrayList<>(numberOfPredictionClasses);
      for (int j = 0; j < numberOfPredictionClasses - 1; j++) {
         BaselearnerDataset baselearnerDataset = new BaselearnerDataset(numberOfInstances, numberOfFeatures);
         allBaselearnerDatasets.add(baselearnerDataset);
      }
      return allBaselearnerDatasets;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof SimpleOrdinalClassification) {
         return true;
      }
      return false;
   }

}
