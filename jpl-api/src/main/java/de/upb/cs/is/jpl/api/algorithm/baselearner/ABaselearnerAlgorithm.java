package de.upb.cs.is.jpl.api.algorithm.baselearner;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.ATrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedDatasetTypeException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedInstanceTypeException;
import de.upb.cs.is.jpl.api.util.TypeCheckUtils;


/**
 * This abstract class represents a base learner and should be extended by all base learners.
 *
 * @param <T> the generic type extending AAlgorithmConfiguration, configuration associated with the
 *           ABaseLearnerAlgorithm class.
 * 
 * @author Sebastian Gottschalk
 * 
 */
public abstract class ABaselearnerAlgorithm<T extends AAlgorithmConfiguration> extends ATrainableAlgorithm<T>
      implements IBaselearnerAlgorithm {


   /**
    * Constructor which initializes the base learner algorithm configuration to default algorithms
    * configuration.
    */
   public ABaselearnerAlgorithm() {
      super();
      getAlgorithmConfiguration();
   }


   /**
    * Constructor which initializes the base learner algorithm configuration to default algorithms
    * configuration.
    *
    * @param algorithmIdentifier the algorithm identifier
    */
   public ABaselearnerAlgorithm(String algorithmIdentifier) {
      super(algorithmIdentifier);
      getAlgorithmConfiguration();
   }


   @Override
   public ILearningModel<?> train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      assertDatasetIsBaselearnerDataset(dataset);
      return performTraining(dataset);
   }


   /**
    * Asserts that the given dataset is of the type {@link BaselearnerDataset}. If this is not the
    * case a {@link PredictionFailedException} is thrown.
    * 
    * @param dataset the dataset to check the type of
    * 
    * @throws TrainModelsFailedException if the dataset is not compatible
    */
   protected void assertDatasetIsBaselearnerDataset(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      try {
         TypeCheckUtils.assertDatasetHasCorrectType(dataset, BaselearnerDataset.class);
      } catch (UnsupportedDatasetTypeException unsupportedDatatsetTypeException) {
         throw new TrainModelsFailedException(unsupportedDatatsetTypeException);
      }
   }


   /**
    * Asserts that the given instance is of the type {@link BaselearnerInstance}. If this is not the
    * case a {@link TrainModelsFailedException} is thrown.
    * 
    * @param instance the instance to check the type of
    * 
    * @throws TrainModelsFailedException if the instance is not compatible
    */
   protected void assertInstanceHasCorrectType(IInstance<?, ?, ?> instance) throws TrainModelsFailedException {
      try {
         TypeCheckUtils.assertInstanceHasCorrectType(instance, BaselearnerInstance.class);
      } catch (UnsupportedInstanceTypeException unsupportedInstanceTypeException) {
         throw new TrainModelsFailedException(unsupportedInstanceTypeException);
      }
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ABaselearnerAlgorithm) {
         return true;
      }
      return false;
   }

}
