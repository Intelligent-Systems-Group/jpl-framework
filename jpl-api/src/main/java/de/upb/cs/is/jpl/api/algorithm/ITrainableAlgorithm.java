package de.upb.cs.is.jpl.api.algorithm;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This interface defines behavior which needs to be supported by any kind of trainable algorithm in
 * this tool.
 * 
 * @author Sebastian Gottschalk
 * @author Tanja Tornede
 *
 */
public interface ITrainableAlgorithm extends IAlgorithm {

   /**
    * Trains this algorithm using the given dataset and returns the resulting learning model.
    * 
    * @param dataset the dataset to be used for training
    * @return the learning model induced from the given dataset
    * @throws TrainModelsFailedException if something went wrong during the training
    */
   public ILearningModel<?> train(final IDataset<?, ?, ?> dataset) throws TrainModelsFailedException;

}
