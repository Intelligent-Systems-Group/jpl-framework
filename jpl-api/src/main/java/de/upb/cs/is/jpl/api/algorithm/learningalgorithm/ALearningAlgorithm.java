package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.ATrainableAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedDatasetTypeException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedInstanceTypeException;
import de.upb.cs.is.jpl.api.util.TypeCheckUtils;


/**
 * This abstract class represents a learning algorithm.
 * 
 * @param <CONFIG> the generic type extending AAlgorithmConfiguration, configuration associated with
 *           the ABaseLearnerAlgorithm class.
 * 
 * @author Sebastian Gottschalk
 * 
 */
public abstract class ALearningAlgorithm<CONFIG extends AAlgorithmConfiguration> extends ATrainableAlgorithm<CONFIG>
      implements ILearningAlgorithm {


   /**
    * Constructor which initializes the algorithm configuration to default algorithms configuration.
    * It also creates and initializes the member variables.
    *
    */
   public ALearningAlgorithm() {
      super();
      getAlgorithmConfiguration();
      init();
   }


   /**
    * Constructor which initializes the algorithm configuration to default algorithms configuration.
    * It also creates and initializes the member variables. The algorithm identifier is used for the
    * toString method.
    *
    * @param algorithmIdentifier the algorithm identifier for the toString method
    */
   public ALearningAlgorithm(String algorithmIdentifier) {
      super(algorithmIdentifier);
      getAlgorithmConfiguration();
      init();
   }


   /**
    * The abstract method to initialize the member variables. Generally called in constructor.
    */
   public abstract void init();


   @Override
   public ILearningModel<?> train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      init();
      checkDatasetForCompatibility(dataset);
      return performTraining(dataset);
   }


   /**
    * Checks if the given dataset is compatible with this algorithm. If this is not the case a
    * {@link TrainModelsFailedException} is thrown.
    * 
    * @param dataset dataset to check for compatibility
    * 
    * @throws TrainModelsFailedException is thrown if the dataset is not compatible
    */
   protected void checkDatasetForCompatibility(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      IDatasetParser customDatasetParser = getDatasetParser();
      Class<?> supportedDatasetClass = customDatasetParser.getDataset().getClass();
      if (!(supportedDatasetClass.isInstance(dataset))) {
         throw new TrainModelsFailedException(
               String.format(ERROR_UNSUPPORTED_DATASET_TYPE, dataset.getClass().getSimpleName(), supportedDatasetClass.getSimpleName()));
      }
   }


   /**
    * Asserts that the given dataset is of the given type. If this is not the case a
    * {@link PredictionFailedException} is thrown.
    * 
    * @param dataset the dataset to check the type of
    * @param expectedType the expected type of the dataset
    * 
    * @throws TrainModelsFailedException if the dataset is not compatible
    */
   protected void assertDatasetHasCorrectType(IDataset<?, ?, ?> dataset, Class<?> expectedType) throws TrainModelsFailedException {
      try {
         TypeCheckUtils.assertDatasetHasCorrectType(dataset, expectedType);
      } catch (UnsupportedDatasetTypeException unsupportedDatatsetTypeException) {
         throw new TrainModelsFailedException(unsupportedDatatsetTypeException);
      }
   }


   /**
    * Asserts that the given instance is of the given type. If this is not the case a
    * {@link TrainModelsFailedException} is thrown.
    * 
    * @param instance the instance to check the type of
    * @param expectedType the expected type of the instance
    * 
    * @throws TrainModelsFailedException if the instance is not compatible
    */
   protected void assertInstanceHasCorrectType(IInstance<?, ?, ?> instance, Class<?> expectedType) throws TrainModelsFailedException {
      try {
         TypeCheckUtils.assertInstanceHasCorrectType(instance, expectedType);
      } catch (UnsupportedInstanceTypeException unsupportedInstanceTypeException) {
         throw new TrainModelsFailedException(unsupportedInstanceTypeException);
      }
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ALearningAlgorithm) {
         return true;
      }
      return false;
   }

}
