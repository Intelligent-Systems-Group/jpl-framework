package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.pocket;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.ALinearClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.perceptron.PerceptronLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;


/**
 * Linear Classification implementation using the Pocket Learning Algorithm to obtain the optimal
 * weight vector for a given dataset.
 * 
 * @author Tanja Tornede
 *
 */
public class PocketLearningAlgorithm extends ALinearClassification<PocketConfiguration> {


   /**
    * Creates a pocket algorithm with the default parameter values.
    */
   public PocketLearningAlgorithm() {
      super(EBaseLearner.POCKET.getBaseLearnerIdentifier());
   }


   /**
    * Creates a pocket algorithm with the given parameters.
    * 
    * @param learningRate the learning rate used during the training
    * @param numberOfIterations the number of iterations used during the training
    */
   public PocketLearningAlgorithm(double learningRate, int numberOfIterations) {
      super(EBaseLearner.POCKET.getBaseLearnerIdentifier());
      this.configuration.setLearningRate(learningRate);
      this.configuration.setNumberOfIterations(numberOfIterations);
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) {
      BaselearnerDataset baselearnerDataset = (BaselearnerDataset) dataset;
      LinearClassificationLearningModel bestLearningModel = new LinearClassificationLearningModel(baselearnerDataset.getNumberOfFeatures(),
            configuration.getLearningRate());
      LinearClassificationLearningModel currentLearningModel = new LinearClassificationLearningModel(bestLearningModel.getWeightVector(),
            bestLearningModel.getBias(), configuration.getLearningRate());
      double bestInsampleError = currentLearningModel.evaluateInSampleError(baselearnerDataset);
      PerceptronLearningAlgorithm perceptron = new PerceptronLearningAlgorithm();

      for (int i = 0; i < this.configuration.getNumberOfIterations(); i++) {
         perceptron.setLearningModel(currentLearningModel);
         perceptron.executeOneTurn(baselearnerDataset);
         double currentInsampleError = currentLearningModel.evaluateInSampleError(baselearnerDataset);
         if (currentInsampleError < bestInsampleError) {
            bestLearningModel.setWeightVector(currentLearningModel.getWeightVector());
            bestLearningModel.setBias(currentLearningModel.getBias());
            bestInsampleError = currentInsampleError;
         }
      }
      return bestLearningModel;
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      LinearClassificationConfiguration defaultConfiguration = new PocketConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof PocketLearningAlgorithm) {
         return true;
      }
      return false;
   }

}
