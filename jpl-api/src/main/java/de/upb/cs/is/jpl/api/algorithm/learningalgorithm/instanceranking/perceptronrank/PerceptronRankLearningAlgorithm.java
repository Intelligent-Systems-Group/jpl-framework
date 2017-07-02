package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank;


import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDataset;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * The Perceptron Rank is one of the first instance ranking algorithms based on the perceptron
 * algorithm. The perceptron algorithm trains a weighting vector w, which distinguishes between two
 * classes. If the algorithm makes a mistake the weighting vector will be modified so that the
 * current mistake is corrected. Like perceptron, Perceptron Rank is also an online learning
 * algorithm but the division is between k classes.
 *
 * @author Sebastian Gottschalk
 */
public class PerceptronRankLearningAlgorithm extends ALearningAlgorithm<PerceptronRankConfiguration> {
   private static final Logger logger = LoggerFactory.getLogger(PerceptronRankLearningAlgorithm.class);
   private static final String CREATED_MODEL_WITH_WEIGHTINGS = "Created model with weightings %s and thresholds %s ";
   private static final String ERROR_ARRAYS_WITH_DIFFERENT_LENGTH = "Both parameters have the same length";


   // State saving of the algorithm
   private double[] weightings;
   private double[] thresholds;

   // Prediction variables
   int[] predictedYVector;
   int[] tauVector;


   /**
    * Creates a new PRankAlgorithm with the enum identifier.
    */
   public PerceptronRankLearningAlgorithm() {
      super(ELearningAlgorithm.PERCEPTRON_RANK.getIdentifier());
   }


   @Override
   public void init() {
      // Initialize method is not needed
   }


   @Override
   public ADatasetParser getDatasetParser() {
      return new InstanceRankingDatasetParser();
   }


   /**
    * Initialize the algorithm with empty values. The {@code numberOfFeatures} is used to create the
    * first weightings vector of the algorithm. It also sets initial values for the thresholds.
    * 
    * @param numberOfFeatures number of the features the algorithm should consider
    */
   private void setFirstStates(int numberOfFeatures) {
      // Set default threshold (always set index zero to 0)
      thresholds = new double[configuration.getK()];
      for (int i = 0; i < thresholds.length; i++) {
         thresholds[i] = 0;
      }
      thresholds[configuration.getK() - 1] = Integer.MAX_VALUE;

      // Set default weightings
      weightings = new double[numberOfFeatures];
      for (int i = 0; i < weightings.length; i++) {
         weightings[i] = 0;
      }

      // Set prediction variables
      predictedYVector = new int[configuration.getK() - 1];
      predictedYVector[0] = 0;
      tauVector = new int[configuration.getK() - 1];
      tauVector[0] = 0;

   }


   /**
    * Build a scalar product of two double arrays.
    * 
    * @param firstArray first array
    * @param secondArray second array
    * @return scalar product of both arrays
    */
   public static double buildScalarProduct(double[] firstArray, double[] secondArray) {
      if (firstArray.length != secondArray.length) {
         throw new IllegalArgumentException(ERROR_ARRAYS_WITH_DIFFERENT_LENGTH);
      }

      double scalarProduct = 0.0;

      for (int i = 0; i < firstArray.length; i++) {
         scalarProduct += firstArray[i] * secondArray[i];
      }

      return scalarProduct;
   }


   /**
    * Sums up a double array to an integer vector.
    * 
    * @param array the vector which should be summed
    * @return the summed vector as integer
    */
   public static int sumIntVector(int[] array) {
      int resultSum = 0;

      for (int i = 0; i < array.length; i++) {
         resultSum += array[i];
      }

      return resultSum;
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new PerceptronRankConfiguration();
   }


   @Override
   protected PerceptronRankLearningModel performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {

      // Initialize algorithm
      InstanceRankingDataset instancedataset = (InstanceRankingDataset) dataset;
      setFirstStates(instancedataset.getNumberOfFeatures());

      // Run single steps
      for (int i = 0; i < instancedataset.getNumberOfInstances(); i++) {
         double[] curInstanceFeatures = instancedataset.getContextFeatureList().get(i);
         Integer curInstanceRatings = instancedataset.getRatingList().get(i);
         performSingleStep(curInstanceFeatures, curInstanceRatings);
      }

      logger.debug(String.format(CREATED_MODEL_WITH_WEIGHTINGS, Arrays.toString(weightings), Arrays.toString(thresholds)));
      return new PerceptronRankLearningModel(weightings, thresholds);
   }


   @Override
   public PerceptronRankLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (PerceptronRankLearningModel) super.train(dataset);
   }


   /**
    * Start performing of a training step with help of a single instance
    * 
    * @param curInstanceFeatures the features of the current instance
    * @param curInstanceRating the rating of the current instance
    */
   private void performSingleStep(double[] curInstanceFeatures, Integer curInstanceRating) {
      int predictedY = predictY(curInstanceFeatures);
      if (predictedY != curInstanceRating) {
         updateYAndTauVector(curInstanceFeatures, curInstanceRating);
         updateWeightingsAndTreshholds(curInstanceFeatures);
      }
   }


   /**
    * Predict y^t for the current instance features.
    * 
    * @param curInstanceFeatures the features of the current instance
    * @return the predicted y
    */
   private int predictY(double[] curInstanceFeatures) {
      for (int j = 0; j < thresholds.length; j++) {
         if ((buildScalarProduct(weightings, curInstanceFeatures) - thresholds[j]) < 0) {
            return j + 1;
         }
      }
      return -1;
   }


   /**
    * Update the prediction and tau vector with help of an the current feature vector
    * {@code curInstanceFeatures} and rating {@code curInstanceRating}.
    * 
    * @param curInstanceFeatures the features of the current instance
    * @param curInstanceRating the rating of the current instance
    */
   private void updateYAndTauVector(double[] curInstanceFeatures, int curInstanceRating) {
      // Update prediction vector
      for (int j = 0; j < configuration.getK() - 1; j++) {
         if (curInstanceRating <= j + 1) {
            predictedYVector[j] = -1;
         } else {
            predictedYVector[j] = 1;
         }
      }
      // Update tau vector
      for (int j = 0; j < configuration.getK() - 1; j++) {
         if ((buildScalarProduct(weightings, curInstanceFeatures) - thresholds[j]) * predictedYVector[j] <= 0) {
            tauVector[j] = predictedYVector[j];
         } else {
            tauVector[j] = 0;
         }
      }
   }


   /**
    * Update the {@code weightings} and {@code thresholds} for the learning model.
    * 
    * @param curInstanceFeatures the features of the current instance
    */
   private void updateWeightingsAndTreshholds(double[] curInstanceFeatures) {
      for (int j = 0; j < weightings.length; j++) {
         weightings[j] = weightings[j] + sumIntVector(tauVector) * curInstanceFeatures[j];
      }
      for (int j = 0; j < configuration.getK() - 1; j++) {
         thresholds[j] = thresholds[j] - tauVector[j];
      }
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof PerceptronRankLearningAlgorithm) {
         PerceptronRankLearningAlgorithm prankAlgorithm = (PerceptronRankLearningAlgorithm) secondObject;

         return prankAlgorithm.configuration.equals(this.configuration);
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * this.configuration.hashCode();
      return hashCode;
   }
}
