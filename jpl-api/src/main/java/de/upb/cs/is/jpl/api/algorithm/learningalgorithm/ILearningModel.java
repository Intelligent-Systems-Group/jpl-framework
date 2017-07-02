package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;


/**
 * Interface for any learning model. A learning model should support both prediction on a complete
 * dataset and on a single instance.
 * 
 * @author Pritha Gupta
 * @author Alexander Hetzer
 * 
 * @param <RATING> the return type of the prediction
 */
public interface ILearningModel<RATING> {

   /**
    * Predicts the target for the complete dataset given. The resulting predictions are returned in
    * the form of a {@link List}. The exact content depends on the implementation of the learning
    * algorithm underlying this model and the model itself.
    * 
    * @param dataset the dataset to use for prediction
    * 
    * @return resulting the predictions obtained in the form of a list
    * 
    * @throws PredictionFailedException if any problem during the prediction occurred
    */
   public List<RATING> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException;


   /**
    * Checks if the given dataset is compatible to this learning model. This method is used to check
    * if the prediction on a given dataset can be done or not. Returns {@code true}, if this is the
    * case, otherwise {@code false}.
    * 
    * @param dataset the dataset to check
    * 
    * @return {@code true}, if the dataset is compatible, otherwise {@code false}
    */
   public boolean isDatasetCompatible(final IDataset<?, ?, ?> dataset);


   /**
    * Predicts the target for the given instance. The exact content depends on the implementation of
    * the learning algorithm underlying this model and the model itself.
    * 
    * @param instance the instance to predict on
    * 
    * @return the result of the prediction with respect to the given instance
    * 
    * @throws PredictionFailedException if the prediction failed
    * 
    */
   public RATING predict(final IInstance<?, ?, ?> instance) throws PredictionFailedException;


   /**
    * Checks if the given instance is compatible to this learning model. This method is used to
    * check if the prediction on a given instance can be done or not. Returns {@code true}, if this
    * is the case, otherwise {@code false}.
    * 
    * @param instance the instance to check
    * 
    * @return {@code true}, if the instance is compatible, otherwise {@code false}
    */
   public boolean isInstanceCompatible(final IInstance<?, ?, ?> instance);


}
