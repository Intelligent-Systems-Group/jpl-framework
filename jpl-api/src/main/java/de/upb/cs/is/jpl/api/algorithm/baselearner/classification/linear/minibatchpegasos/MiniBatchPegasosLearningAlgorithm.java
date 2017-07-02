package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.minibatchpegasos;


import java.util.HashSet;
import java.util.Set;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.ALinearClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.util.DoubleVectorUtils;


/**
 * Linear Classification implementation using the Mini-Batch Pegasos Algorithm.
 * 
 * @author Tanja Tornede
 *
 */
public class MiniBatchPegasosLearningAlgorithm extends ALinearClassification<MiniBatchPegasosConfiguration> {


   private static final String ERROR_SUBSETSIZE_GREATER_DATASET_SIZE = "The chosen size of the subset used while training is greater than the number of instances in the given dataset.";


   /**
    * Creates a {@link MiniBatchPegasosLearningAlgorithm} with the default parameter values.
    */
   public MiniBatchPegasosLearningAlgorithm() {
      super(EBaseLearner.MINI_BATCH_PEGASOS.getBaseLearnerIdentifier());
   }


   /**
    * Creates a {@link MiniBatchPegasosLearningAlgorithm} with the given parameters.
    * 
    * @param learningRate the learning rate used during the training
    * @param numberOfIterations the number of iterations used during the training
    * @param subsetSize the size of the subset used during the training
    * @param regularizationParameter the regularization parameter used during training
    */
   public MiniBatchPegasosLearningAlgorithm(double learningRate, int numberOfIterations, int subsetSize, double regularizationParameter) {
      super(EBaseLearner.MINI_BATCH_PEGASOS.getBaseLearnerIdentifier());
      this.configuration.setLearningRate(learningRate);
      this.configuration.setNumberOfIterations(numberOfIterations);
      this.configuration.setSubsetSize(subsetSize);
      this.configuration.setRegularizationParameter(regularizationParameter);
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      BaselearnerDataset baselearnerDataset = (BaselearnerDataset) dataset;
      assertSubsetSizeIsSmallerThanNumberOfInstances(baselearnerDataset);
      LinearClassificationLearningModel learningModel = new LinearClassificationLearningModel(baselearnerDataset.getNumberOfFeatures(),
            configuration.getLearningRate());
      double stepSize;
      for (int t = 1; t <= this.configuration.getNumberOfIterations(); t++) {
         Set<BaselearnerInstance> subset = chooseSubsetUniformlyAtRandom(baselearnerDataset, configuration.getSubsetSize());
         Set<BaselearnerInstance> subsetWithRestrictions = getMisclassifiedInstances(learningModel, subset);
         stepSize = 1.0 / (configuration.getRegularizationParameter() * t);
         recomputeWeightVectorAndBias(learningModel, stepSize, subsetWithRestrictions);
      }
      return learningModel;
   }


   private void assertSubsetSizeIsSmallerThanNumberOfInstances(BaselearnerDataset dataset) throws TrainModelsFailedException {
      if (configuration.getSubsetSize() > dataset.getNumberOfInstances()) {
         throw new TrainModelsFailedException(ERROR_SUBSETSIZE_GREATER_DATASET_SIZE);
      }
   }


   /**
    * Recomputes the weight vector and the bias of the learning model according to the update rule
    * of this algorithm.
    * 
    * @param learningModel the learning model to update the weight vector and bias for
    * @param stepSize the size of the step to use
    * @param misclassifiedInstances a set of misclassified instances
    */
   private void recomputeWeightVectorAndBias(LinearClassificationLearningModel learningModel, double stepSize,
         Set<BaselearnerInstance> misclassifiedInstances) {
      IVector vector = new DenseDoubleVector(learningModel.getWeightVector().length());
      double sumForBias = 0;
      for (BaselearnerInstance instance : misclassifiedInstances) {
         vector.addVector(DoubleVectorUtils.scaleVector(instance.getRating(), instance.getContextFeatureVector()));
         sumForBias += instance.getRating();
      }

      double oneMinusStepsizeTimesRegulatizationParameter = 1 - (stepSize * configuration.getRegularizationParameter());

      IVector newWeightVector = new DenseDoubleVector(learningModel.getWeightVector().length());
      newWeightVector.addConstant(oneMinusStepsizeTimesRegulatizationParameter);
      newWeightVector.multiplyByVectorPairwise(learningModel.getWeightVector());

      IVector secondProduct = new DenseDoubleVector(learningModel.getWeightVector().length());
      secondProduct.addConstant(stepSize / configuration.getSubsetSize());
      secondProduct.multiplyByVectorPairwise(vector);

      newWeightVector.addVector(secondProduct);
      learningModel.setWeightVector(newWeightVector);

      double newBias = oneMinusStepsizeTimesRegulatizationParameter * learningModel.getBias();
      newBias += (stepSize / configuration.getSubsetSize()) * sumForBias;
      learningModel.setBias(newBias);
   }


   /**
    * Returns a set of all misclassified instances of the given set of instances.
    * 
    * @param learningModel the learning model to use
    * @param setOfInstances the set of instances
    * 
    * @return the set of misclassified instances according to a pre prediction
    */
   protected Set<BaselearnerInstance> getMisclassifiedInstances(LinearClassificationLearningModel learningModel,
         Set<BaselearnerInstance> setOfInstances) {
      Set<BaselearnerInstance> misclassifiedSubset = new HashSet<>();
      for (BaselearnerInstance instance : setOfInstances) {
         if (prePredict(learningModel, instance) < 1) {
            misclassifiedSubset.add(instance);
         }
      }
      return misclassifiedSubset;
   }


   /**
    * Returns a pre prediction of the given instance.
    * 
    * @param learningModel the learning model to use
    * @param instance the instance to pre predict
    * 
    * @return the result of the pre prediction
    */
   private double prePredict(LinearClassificationLearningModel learningModel, BaselearnerInstance instance) {
      return instance.getRating()
            * (learningModel.getWeightVector().dotProduct(instance.getContextFeatureVector()) + learningModel.getBias());
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      MiniBatchPegasosConfiguration defaultConfiguration = new MiniBatchPegasosConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof MiniBatchPegasosLearningAlgorithm) {
         return true;
      }
      return false;
   }

}
