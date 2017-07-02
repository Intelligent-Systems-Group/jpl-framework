package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization;


import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringInstance;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.IMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The basic Matrix factorization algorithm based on SVD. The algorithm will perform Stochastic
 * gradient descent to minimize the prediction error. Here the algorithm will randomly select one
 * instance and perform a gradient step based on the prediction and error for this instance.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class MatrixFactorizationLearningAlgorithm extends ALearningAlgorithm<MatrixFactorizationConfiguration> {


   private static final Logger logger = LoggerFactory.getLogger(MatrixFactorizationLearningAlgorithm.class);

   private static final String COULD_NOT_CALCULATE_ERROR = "Could not calculate error";
   private static final String GRADIENT_DESCENT_STOPPED = "Gradient descent stopped after %s steps.";
   private static final String STARTING_GRADIENT_DESCENT = "Starting gradient descent of %s steps.";
   private static final String ESTIMATED_TIME_UNTIL_GRADIENT_DESCENT_IS_COMPLETED = "Estimated time until Gradient Descent is completed: {} seconds.";


   private IMatrix hiddenItemFeatures;
   private IMatrix hiddenContextFeatures;

   private int numberOfHiddenFeatures;
   private double stepSize;
   private double regFactor;


   private int stepNumber;
   private Random random;


   /**
    * Creates a new matrix factorization algorithm with the enum identifier.
    */
   public MatrixFactorizationLearningAlgorithm() {
      super(ELearningAlgorithm.MATRIX_FACTORIZATION.getIdentifier());
   }


   @Override
   public boolean equals(Object object) {
      if (this == object)
         return true;
      if (!super.equals(object))
         return false;
      if (object instanceof MatrixFactorizationLearningAlgorithm) {
         MatrixFactorizationLearningAlgorithm comparedObject = (MatrixFactorizationLearningAlgorithm) object;
         return this.configuration.equals(comparedObject.getAlgorithmConfiguration());
      }
      return false;
   }


   @Override
   public int hashCode() {
      return super.hashCode() + 31 * configuration.hashCode();
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new CollaborativeFilteringParser();
   }


   @Override
   public void init() {
      random = new Random(configuration.getRandomSeed());
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      AAlgorithmConfiguration config = new MatrixFactorizationConfiguration();
      config.initializeDefaultConfiguration();
      return config;
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      assertDatasetHasCorrectType(dataset, CollaborativeFilteringDataset.class);
      CollaborativeFilteringDataset cfDataset = (CollaborativeFilteringDataset) dataset;

      initializeHiddenFeatures(cfDataset.getNumberOfItems(), cfDataset.getNumberOfContexts(), configuration.getNumberOfHiddenFeatures());

      long start = System.currentTimeMillis();
      stepNumber = 0;

      numberOfHiddenFeatures = configuration.getNumberOfHiddenFeatures();
      regFactor = configuration.getRegularizationFactor();
      stepSize = configuration.getStepSize();

      int numberOfInstances = cfDataset.getNumberOfInstances();
      logger.info(String.format(STARTING_GRADIENT_DESCENT, numberOfInstances * configuration.getNumberOfIterations()));
      while (stepNumber < configuration.getNumberOfIterations() * numberOfInstances) {
         CollaborativeFilteringInstance instance = cfDataset.getInstance(stepNumber % numberOfInstances);
         Pair<double[], double[]> newVectors = calculateGradientStep(instance);

         for (int i = 0; i < numberOfHiddenFeatures; i++) {
            hiddenContextFeatures.setValue(instance.getContextId(), i, newVectors.getFirst()[i]);
         }
         for (int i = 0; i < numberOfHiddenFeatures; i++) {
            hiddenItemFeatures.setValue(i, instance.getItemId(), newVectors.getSecond()[i]);
         }
         stepNumber++;
         if (stepNumber == cfDataset.getNumberOfInstances()) {
            logger.info(ESTIMATED_TIME_UNTIL_GRADIENT_DESCENT_IS_COMPLETED,
                  configuration.getNumberOfIterations() * (System.currentTimeMillis() - start) / 1000);
         }
      }

      logger.info(String.format(GRADIENT_DESCENT_STOPPED, stepNumber));

      return new MatrixFactorizationLearningModel(hiddenItemFeatures, hiddenContextFeatures);

   }


   @Override
   public MatrixFactorizationLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (MatrixFactorizationLearningModel) super.train(dataset);
   }


   /**
    * Calculate the result of the gradient descent descent step.
    * 
    * @param instance the training instance of this step
    * @return the new context and item vectors
    * @throws TrainModelsFailedException if the training operation failed
    */
   private Pair<double[], double[]> calculateGradientStep(CollaborativeFilteringInstance instance) throws TrainModelsFailedException {
      IVector itemVector = hiddenItemFeatures.getColumnVector(instance.getItemId());
      IVector contextVector = hiddenContextFeatures.getRowVector(instance.getContextId());
      double[] newItemVector = new double[numberOfHiddenFeatures];
      double[] newContextVector = new double[numberOfHiddenFeatures];

      double prediction = itemVector.dotProduct(contextVector);
      double error = calculateError(instance.getRating(), prediction);
      for (int i = 0; i < numberOfHiddenFeatures; i++) {
         double oldItemValue = itemVector.getValue(i);
         double oldContextValue = contextVector.getValue(i);
         newItemVector[i] = oldItemValue + stepSize * (2 * error * oldContextValue - regFactor * oldItemValue);
         newContextVector[i] = oldContextValue + stepSize * (2 * error * oldItemValue - regFactor * oldContextValue);
      }
      return Pair.of(newContextVector, newItemVector);
   }


   /**
    * Calculate the effective error. Here the error is the absolute difference between the real
    * rating and the prediction for all cases where there is a real rating and 0 otherwise.
    * 
    * @param rating the real rating
    * @param prediction the prediction from the algorithm
    * @return the calculated error
    * @throws TrainModelsFailedException if the prediction is an invalid value
    */
   private double calculateError(double rating, double prediction) throws TrainModelsFailedException {
      double error;
      error = rating < configuration.getMinRating() ? 0 : (rating - prediction);
      if (Double.isInfinite(error) || Double.isNaN(error)) {
         throw new TrainModelsFailedException(COULD_NOT_CALCULATE_ERROR);
      }
      return error;
   }


   /**
    * Initialize the hidden feature matrices with random values.
    * 
    * @param numberOfItems the number of different items
    * @param numberOfContexts the number of different contexts
    * @param k the number of hidden features
    */
   protected void initializeHiddenFeatures(int numberOfItems, int numberOfContexts, int k) {
      hiddenItemFeatures = new DenseDoubleMatrix(k, numberOfItems);
      hiddenContextFeatures = new DenseDoubleMatrix(numberOfContexts, k);
      random.setSeed(configuration.getRandomSeed());

      for (int i = 0; i < k; i++) {
         for (int j = 0; j < numberOfItems; j++) {
            hiddenItemFeatures.setValue(i, j, random.nextDouble());
         }
         for (int j = 0; j < numberOfContexts; j++) {
            hiddenContextFeatures.setValue(j, i, random.nextDouble());
         }
      }
   }


}
