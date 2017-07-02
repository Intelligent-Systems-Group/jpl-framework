package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression;


import java.util.Arrays;
import java.util.Random;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.util.DoubleVectorUtils;


/**
 * Combined ranking and regression combines regression and ranking to predict the rating of an
 * instance. It based on the assumption that an algorithm should be trained using ranking and
 * regression and the same time and makes usage of gradient descent. After initializing CRR by with
 * a probability of alpha the algorithms chooses a single example for learning, otherwise it chooses
 * the combination of two examples. As gradient functions they provided the squared loss and the
 * logistic loss.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class CombinedRankingAndRegressionLearningAlgorithm extends ALearningAlgorithm<CombinedRankingAndRegressionConfiguration> {
   private static final String ERROR_DATASET_HAS_NO_INPUT = "The algorithms need at least one context feature in the dataset";
   private static final String LOGISTIC_PARAMETER = "logistic";
   private static final String SQUARED_PARAMETER = "squared";

   private Random randomGenerator;
   private double[] weightingVector;
   private double[] contextVectorCache;
   private double ratingCache;


   /**
    * Creates a new CRR learning algorithm with the enum identifier.
    */
   public CombinedRankingAndRegressionLearningAlgorithm() {
      super(ELearningAlgorithm.COMBINED_RANKING_AND_REGRESSION.getIdentifier());
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new DefaultAbsoluteDatasetParser();
   }


   @Override
   public void init() {
      randomGenerator = RandomGenerator.getRNG();
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      CombinedRankingAndRegressionConfiguration configuration = new CombinedRankingAndRegressionConfiguration();
      configuration.initializeDefaultConfiguration();
      return configuration;
   }


   @Override
   protected CombinedRankingAndRegressionLearningModel performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      DefaultAbsoluteDataset defaultDataset = (DefaultAbsoluteDataset) dataset;

      initAlgorithm(defaultDataset);

      for (int i = 1; i <= configuration.getNumberOfIterations(); i++) {
         createSample(defaultDataset, configuration);
         updateWeightings(configuration, i);
      }

      return new CombinedRankingAndRegressionLearningModel(weightingVector, configuration.getLossFunctionIdentifier());
   }


   @Override
   public CombinedRankingAndRegressionLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (CombinedRankingAndRegressionLearningModel) super.train(dataset);
   }


   /**
    * Checks if the dataset is not empty and initialize the number of given weightings with zero.
    * 
    * @param dataset the dataset which should be used for initialization
    * @throws TrainModelsFailedException if the dataset has no instances
    */
   private void initAlgorithm(DefaultAbsoluteDataset dataset) throws TrainModelsFailedException {
      if (dataset.getNumberOfContexts() == 0) {
         throw new TrainModelsFailedException(ERROR_DATASET_HAS_NO_INPUT);
      }
      weightingVector = new double[dataset.getInstance(0).getContextFeatureVector().length];
      contextVectorCache = new double[dataset.getInstance(0).getContextFeatureVector().length];

   }


   /**
    * Return a random instance from the dataset.
    * 
    * @param dataset the dataset which should be used for a given distance
    * @return a random instance
    */
   private DefaultInstance<IVector> getRandomSample(DefaultAbsoluteDataset dataset) {
      int index = randomGenerator.nextInt(dataset.getNumberOfInstances());
      return dataset.getInstance(index);
   }


   /**
    * Create an internal sample with help of ranking and regression and saves it in
    * {@code itemVectorCache} and {@code ratingCache}.
    * 
    * @param dataset the dataset which should be used for creating the sample
    * @param configuration the current configuration of the algorithm
    */
   private void createSample(DefaultAbsoluteDataset dataset, CombinedRankingAndRegressionConfiguration configuration) {
      DefaultInstance<IVector> randomInstance = getRandomSample(dataset);
      if (randomGenerator.nextDouble() < configuration.getTradeoffParameter()) {
         for (int i = 0; i < contextVectorCache.length; i++) {
            contextVectorCache[i] = randomInstance.getContextFeatureVector()[i];
         }
         ratingCache = randomInstance.getRating().getValue(0);

      } else {
         DefaultInstance<IVector> secondRandomInstance = getRandomSample(dataset);

         for (int i = 0; i < contextVectorCache.length; i++) {
            contextVectorCache[i] = randomInstance.getContextFeatureVector()[i] - secondRandomInstance.getContextFeatureVector()[i];
         }
         if (configuration.getLossFunctionIdentifier().equals(LOGISTIC_PARAMETER)) {
            ratingCache = randomInstance.getRating().getValue(0) - secondRandomInstance.getRating().getValue(0);
         } else {
            ratingCache = (randomInstance.getRating().getValue(0) - secondRandomInstance.getRating().getValue(0)) / 2;
         }
      }
   }


   /**
    * Updates the internal {@code weightingVector} of the algorithm.
    * 
    * @param configuration the current configuration of the algorithm
    * @param iterationStep the current step number of the algorithm
    */
   private void updateWeightings(CombinedRankingAndRegressionConfiguration configuration, int iterationStep) {
      double eta = 1.0 / (iterationStep * configuration.getRegularizationParameter());
      double[] changedWeightingVector = new double[weightingVector.length];

      for (int i = 0; i < weightingVector.length; i++) {

         if (configuration.getLossFunctionIdentifier().equals(SQUARED_PARAMETER)) {
            changedWeightingVector[i] = (1 - eta * configuration.getRegularizationParameter()) * weightingVector[i]
                  + eta * contextVectorCache[i] * (ratingCache - DoubleVectorUtils.multiplyVectors(weightingVector, contextVectorCache));
         } else {
            changedWeightingVector[i] = (1 - eta * configuration.getRegularizationParameter()) * weightingVector[i]
                  + eta * contextVectorCache[i]
                        * (ratingCache - 1 / (1 + Math.exp(-DoubleVectorUtils.multiplyVectors(weightingVector, contextVectorCache))));
         }
      }

      weightingVector = changedWeightingVector;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof CombinedRankingAndRegressionLearningAlgorithm) {
         CombinedRankingAndRegressionLearningAlgorithm crrAlgorithm = (CombinedRankingAndRegressionLearningAlgorithm) secondObject;

         return crrAlgorithm.configuration.equals(this.configuration);
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * this.configuration.hashCode();
      hashCode += 31 * Arrays.hashCode(weightingVector);
      hashCode += 31 * Arrays.hashCode(contextVectorCache);
      hashCode += 31 * Double.hashCode(ratingCache);
      return hashCode;
   }
}
