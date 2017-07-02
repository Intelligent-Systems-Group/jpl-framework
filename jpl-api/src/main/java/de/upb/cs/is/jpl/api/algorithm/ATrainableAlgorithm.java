package de.upb.cs.is.jpl.api.algorithm;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This abstract class defines functions which needs to be supported by any kind of trainable
 * algorithm in this tool.
 * 
 * @param <CONFIG> the generic type extending AAlgorithmConfiguration, configuration associated with
 *           the ABaseLearnerAlgorithm class.
 * 
 * @author Sebastian Gottschalk
 * @author Tanja Tornede
 *
 */
public abstract class ATrainableAlgorithm<CONFIG extends AAlgorithmConfiguration> extends AAlgorithm<CONFIG>
      implements ITrainableAlgorithm {


   /**
    * Creates a new a algorithm with the simple name of the class as identifier.
    */
   public ATrainableAlgorithm() {
      super();
   }


   /**
    * Creates a new a algorithm with the algorithm identifier.
    *
    * @param algorithmIdentifier the algorithm identifier
    */
   public ATrainableAlgorithm(String algorithmIdentifier) {
      super(algorithmIdentifier);
   }


   /**
    * Performs the actual training process of this learning algorithm and returns the constructed
    * learning model. This method will be called after the dataset compatibility has been checked,
    * therefore it can be assumed that the dataset is of the correct type. Each learning algorithm
    * should overwrite this method to perform the training process accordingly.
    * 
    * @param dataset the dataset to train the learning model on
    * @return the learned model
    * @throws TrainModelsFailedException if the training process cannot be continued due to an error
    */
   protected abstract ILearningModel<?> performTraining(final IDataset<?, ?, ?> dataset) throws TrainModelsFailedException;


   /**
    * Converts the given prediction (0 or 1) to a -1/1 prediction. If the given prediction is 1, it
    * will stay 1. If it is 0 it will become -1.
    * 
    * @param value the prediction to convert (0 or 1)
    * @return -1, if the given prediction is 0, 1, otherwise
    */
   protected double convertPositiveBinaryPredictionToNegativePrediction(double value) {
      if (Double.compare(value, 0.0) == 0) {
         return -1;
      }
      return 1.0;
   }

}
