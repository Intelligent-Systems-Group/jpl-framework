package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.perceptron;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.ALinearClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;


/**
 * Linear Classification implementation using the Perceptron Learning Algorithm.
 * 
 * @author Tanja Tornede
 *
 */
public class PerceptronLearningAlgorithm extends ALinearClassification<PerceptronConfiguration> {

   private LinearClassificationLearningModel learningModel;


   /**
    * Creates a {@link PerceptronLearningAlgorithm} with the default parameter values.
    */
   public PerceptronLearningAlgorithm() {
      super(EBaseLearner.PERCEPTRON.getBaseLearnerIdentifier());
   }


   /**
    * Creates a {@link PerceptronLearningAlgorithm} with the given parameters.
    * 
    * @param learningRate the learning rate used during the training
    * @param numberOfIterations the number of iterations used during the training
    */
   public PerceptronLearningAlgorithm(double learningRate, int numberOfIterations) {
      super(EBaseLearner.PERCEPTRON.getBaseLearnerIdentifier());
      this.configuration.setLearningRate(learningRate);
      this.configuration.setNumberOfIterations(numberOfIterations);
   }


   /**
    * Sets the learning model of this algorithm.
    * 
    * @param learningModel the learning model to set
    */
   public void setLearningModel(LinearClassificationLearningModel learningModel) {
      this.learningModel = learningModel;
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) {
      BaselearnerDataset baselearnerDataset = (BaselearnerDataset) dataset;
      learningModel = new LinearClassificationLearningModel(baselearnerDataset.getNumberOfFeatures(), configuration.getLearningRate());
      for (int i = 0; i < this.configuration.getNumberOfIterations(); i++) {
         executeOneTurn(baselearnerDataset);
      }
      return learningModel;
   }


   /**
    * Executes the this algorithm for one turn for a randomly chosen misclassified instance of the
    * given dataset. This includes to update the weight vector according to the prediction of the
    * learning model of the specific instance.
    * 
    * @param baselearnerDataset the {@link BaselearnerDataset} on which the instance will be
    *           randomly chosen
    */
   public void executeOneTurn(final BaselearnerDataset baselearnerDataset) {
      BaselearnerInstance misclassifiedInstance = pickMisclassifiedInstance(learningModel, baselearnerDataset);
      if (misclassifiedInstance != null) {
         learningModel.updateWeightVector(misclassifiedInstance);
      }
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      LinearClassificationConfiguration defaultConfiguration = new PerceptronConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      PerceptronLearningAlgorithm other = (PerceptronLearningAlgorithm) obj;
      if (learningModel == null) {
         if (other.learningModel != null)
            return false;
      } else if (!learningModel.equals(other.learningModel))
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((learningModel == null) ? 0 : learningModel.hashCode());
      return result;
   }

}
