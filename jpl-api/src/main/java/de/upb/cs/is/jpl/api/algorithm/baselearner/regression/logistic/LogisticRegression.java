package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.IGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.batch.BatchGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.stochastic.StochasticGradientDescent;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This logistic regression implementation uses gradient descent approaches in order to find the
 * optimal weight vector with respect to the cross-entropy error. It can be configured via the
 * {@link LogisticRegressionConfiguration} to work with different {@link IGradientDescent}
 * implementations such as {@link StochasticGradientDescent} or {@link BatchGradientDescent}, which
 * compute the gradient differently.
 * 
 * During the whole implementation the bias is treated as the 0th coordinate of the weight vector.
 * Accordingly all instances are modified to contain a 1.0 as the 0th coordinate such that the dot
 * products still produce correct results.
 * 
 * @author Alexander Hetzer
 *
 */
public class LogisticRegression extends ABaselearnerAlgorithm<LogisticRegressionConfiguration> {

   private List<Triple<IVector, Double, Double>> transformedDataset;
   private BaselearnerDataset dataset;
   private int numberOfFeaturesIncludingBias;


   /**
    * Creates a logistic regression instance with the given gradient descent technique and the given
    * learning rate.
    * 
    * @param gradientDescent the gradient descent procedure to use
    * @param learningRate the learning rate to use
    * 
    */
   public LogisticRegression(IGradientDescent gradientDescent, double learningRate) {
      this();
      this.configuration.setGradientDescent(gradientDescent);
      this.configuration.getGradientDescent().setLearningRate(learningRate);
      this.configuration.setLearningRate(learningRate);
   }


   /**
    * Creates a logistic regression instance with default parameter values.
    */
   public LogisticRegression() {
      super(EBaseLearner.LOGISTIC_REGRESSION.getBaseLearnerIdentifier());
   }


   @Override
   protected LogisticRegressionLearningModel performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      transformInputDataset((BaselearnerDataset) dataset);

      configuration.setupGradientDescent(transformedDataset);
      configuration.getGradientDescent().initialize();
      configuration.getGradientDescent().optimize();

      return new LogisticRegressionLearningModel(configuration.getGradientDescent().getFinalWeightVector());
   }


   @Override
   public LogisticRegressionLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (LogisticRegressionLearningModel) super.train(dataset);
   }


   /**
    * Transforms the given dataset into a list of vectors in order to make the algorithm work more
    * efficiently.
    * 
    * @param dataset the dataset to be transformed
    */
   private void transformInputDataset(BaselearnerDataset dataset) {
      this.dataset = dataset;
      numberOfFeaturesIncludingBias = this.dataset.getNumberOfFeatures() + 1;
      this.transformedDataset = new ArrayList<>();
      double[] correctResults = this.dataset.getCorrectResults();
      double[][] featureVectorsOfDataset = this.dataset.getFeatureVectors();
      double[] instanceWeights = this.dataset.getInstanceWeights();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         IVector instanceVector = convertFeatureArrayToVector(numberOfFeaturesIncludingBias, featureVectorsOfDataset[i]);
         transformedDataset.add(Triple.of(instanceVector, correctResults[i], instanceWeights[i]));
      }
   }


   /**
    * Converts the given feature array into a vector where the 0th component is 1.0. This 0th
    * component is needed since the bias is implicitly contained inside the weight vector as the 0th
    * component.
    * 
    * @param numberOfDimensionsIncludingBias the number of dimensions including the bias
    * @param featureVectorOfDataset the feature vector as array to be converted into a vector
    * @return a vector representation of the given feature array
    */
   private IVector convertFeatureArrayToVector(int numberOfDimensionsIncludingBias, double[] featureVectorOfDataset) {
      IVector instanceVector = new DenseDoubleVector(numberOfDimensionsIncludingBias);
      instanceVector.setValue(0, 1);
      for (int j = 0; j < numberOfDimensionsIncludingBias - 1; j++) {
         instanceVector.setValue(j + 1, featureVectorOfDataset[j]);
      }
      return instanceVector;
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      LogisticRegressionConfiguration configuration = new LogisticRegressionConfiguration();
      configuration.initializeDefaultConfiguration();
      return configuration;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
      result = prime * result + numberOfFeaturesIncludingBias;
      result = prime * result + ((transformedDataset == null) ? 0 : transformedDataset.hashCode());
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
      LogisticRegression other = (LogisticRegression) obj;
      if (dataset == null) {
         if (other.dataset != null)
            return false;
      } else if (!dataset.equals(other.dataset))
         return false;
      if (numberOfFeaturesIncludingBias != other.numberOfFeaturesIncludingBias)
         return false;
      if (transformedDataset == null) {
         if (other.transformedDataset != null)
            return false;
      } else if (!transformedDataset.equals(other.transformedDataset))
         return false;
      return true;
   }

}